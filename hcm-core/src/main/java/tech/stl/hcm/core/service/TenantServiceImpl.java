package tech.stl.hcm.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.stl.hcm.common.dto.TenantDTO;
import tech.stl.hcm.common.dto.helpers.TenantCreateDTO;
import tech.stl.hcm.common.dto.helpers.TransactionDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.core.config.ServiceProperties;
import tech.stl.hcm.message.broker.producer.ProducerService;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class TenantServiceImpl implements TenantService {
    private final WebClient webClient;
    private final ProducerService producerService;
    private final ServiceProperties serviceProperties;
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    public TenantServiceImpl(WebClient.Builder webClientBuilder, ProducerService producerService, ServiceProperties serviceProperties, TransactionService transactionService, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.producerService = producerService;
        this.serviceProperties = serviceProperties;
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<TenantDTO> getAllTenants() {
        // The tenant service now returns a Page, so we need to extract the content
        Map<String, Object> pageResponse = webClient.get()
                .uri(serviceProperties.getTenantUrl())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        
        if (pageResponse != null && pageResponse.containsKey("content")) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> contentMaps = (List<Map<String, Object>>) pageResponse.get("content");
                return contentMaps.stream()
                        .map(contentMap -> objectMapper.convertValue(contentMap, TenantDTO.class))
                        .toList();
            } catch (Exception e) {
                // Log the error and return empty list
                return List.of();
            }
        }
        return List.of();
    }

    @Override
    public PaginatedResponseDTO<TenantDTO> getAllTenantsPaginated(int page, int size, String sortBy, String sortDirection) {
        String url = serviceProperties.getTenantUrl() + "/paginated?page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection;
        
        // For now, we'll call the non-paginated endpoint and manually create pagination
        // In a production environment, you would need to implement proper Page deserialization
        List<TenantDTO> allTenants = getAllTenants();
        
        // Calculate pagination manually
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allTenants.size());
        List<TenantDTO> pageContent = allTenants.subList(startIndex, endIndex);
        
        int totalPages = (int) Math.ceil((double) allTenants.size() / size);
        
        return new PaginatedResponseDTO<>(
            pageContent,
            page,
            size,
            allTenants.size(),
            totalPages,
            page < totalPages - 1,
            page > 0,
            page == 0,
            page == totalPages - 1
        );
    }

    @Override
    public TenantDTO getTenantById(String tenantId) {
        return webClient.get()
                .uri(serviceProperties.getTenantUrl() + "/{tenantId}", tenantId)
                .retrieve()
                .bodyToMono(TenantDTO.class)
                .block();
    }

    @Override
    public void createTenant(TenantCreateDTO tenant) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("tenant-service")
                    .operationType("CREATE")
                    .topicName("create-tenant")
                    .status("QUEUED")
                    .entityType("TENANT")
                    .correlationKey(tenant.getName())
                    .updatedBy(UUID.randomUUID())
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("create-tenant", tenant.getName(), tenant, transactionId);
            
            log.info("Created tenant transaction: {} for tenant: {}", transactionId, tenant.getName());
        } catch (Exception e) {
            log.error("Error creating tenant transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create tenant transaction", e);
        }
    }

    @Override
    public void updateTenant(String tenantId, TenantDTO tenant) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("tenant-service")
                    .operationType("UPDATE")
                    .topicName("update-tenant")
                    .status("QUEUED")
                    .entityType("TENANT")
                    .correlationKey(tenant.getName())
                    .createdBy(UUID.randomUUID())
                    .updatedBy(UUID.randomUUID())
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("update-tenant", tenantId, tenant, transactionId);
            
            log.info("Created tenant update transaction: {} for tenant ID: {}", transactionId, tenantId);
        } catch (Exception e) {
            log.error("Error creating tenant update transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create tenant update transaction", e);
        }
    }

    @Override
    public void deleteTenant(String tenantId) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("tenant-service")
                    .operationType("DELETE")
                    .topicName("delete-tenant")
                    .status("QUEUED")
                    .entityType("TENANT")
                    .correlationKey(tenantId)
                    .createdBy(UUID.randomUUID())
                    .updatedBy(UUID.randomUUID())
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("delete-tenant", tenantId, null, transactionId);
            
            log.info("Created tenant delete transaction: {} for tenant ID: {}", transactionId, tenantId);
        } catch (Exception e) {
            log.error("Error creating tenant delete transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create tenant delete transaction", e);
        }
    }

    private <T> void publishEvent(String topic, String key, T payload) {
        producerService.publishMessage(topic, key, payload);
    }
    
    private <T> void publishEvent(String topic, String key, T payload, UUID transactionId) {
        // Create a wrapper object that includes the transaction ID
        Map<String, Object> wrapper = new java.util.HashMap<>();
        wrapper.put("payload", payload);
        wrapper.put("transactionId", transactionId);
        producerService.publishMessage(topic, key, wrapper);
    }
} 