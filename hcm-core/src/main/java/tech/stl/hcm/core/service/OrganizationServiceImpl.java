package tech.stl.hcm.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.stl.hcm.common.dto.OrganizationDTO;
import tech.stl.hcm.common.dto.helpers.OrganizationCreateDTO;
import tech.stl.hcm.common.dto.helpers.TransactionDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.core.config.ServiceProperties;
import tech.stl.hcm.message.broker.producer.ProducerService;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final WebClient webClient;
    private final ProducerService producerService;
    private final ServiceProperties serviceProperties;
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    public OrganizationServiceImpl(WebClient.Builder webClientBuilder, ProducerService producerService, ServiceProperties serviceProperties, TransactionService transactionService, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.producerService = producerService;
        this.serviceProperties = serviceProperties;
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<OrganizationDTO> getAllOrganizations() {
        // The organization service now returns a Page, so we need to extract the content
        Map<String, Object> pageResponse = webClient.get()
                .uri(serviceProperties.getOrganizationUrl())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        
        if (pageResponse != null && pageResponse.containsKey("content")) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> contentMaps = (List<Map<String, Object>>) pageResponse.get("content");
                return contentMaps.stream()
                        .map(contentMap -> objectMapper.convertValue(contentMap, OrganizationDTO.class))
                        .toList();
            } catch (Exception e) {
                // Log the error and return empty list
                return List.of();
            }
        }
        return List.of();
    }

    @Override
    public PaginatedResponseDTO<OrganizationDTO> getAllOrganizationsPaginated(int page, int size, String sortBy, String sortDirection) {
        String url = serviceProperties.getOrganizationUrl() + "/paginated?page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection;
        
        // For now, we'll call the non-paginated endpoint and manually create pagination
        // In a production environment, you would need to implement proper Page deserialization
        List<OrganizationDTO> allOrganizations = getAllOrganizations();
        
        // Calculate pagination manually
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allOrganizations.size());
        List<OrganizationDTO> pageContent = allOrganizations.subList(startIndex, endIndex);
        
        int totalPages = (int) Math.ceil((double) allOrganizations.size() / size);
        
        return new PaginatedResponseDTO<>(
            pageContent,
            page,
            size,
            allOrganizations.size(),
            totalPages,
            page < totalPages - 1,
            page > 0,
            page == 0,
            page == totalPages - 1
        );
    }

    @Override
    public OrganizationDTO getOrganizationById(String organizationId) {
        return webClient.get()
                .uri(serviceProperties.getOrganizationUrl() + "/{organizationId}", organizationId)
                .retrieve()
                .bodyToMono(OrganizationDTO.class)
                .block();
    }

    @Override
    public void createOrganization(OrganizationCreateDTO organization) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("organization-service")
                    .operationType("CREATE")
                    .topicName("create-organization")
                    .status("QUEUED")
                    .entityType("ORGANIZATION")
                    .correlationKey(organization.getName())
                    .createdBy(UUID.randomUUID()) // TODO: Get from security context
                    .updatedBy(UUID.randomUUID()) // TODO: Get from security context
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("create-organization", organization.getName(), organization, transactionId);
            
            log.info("Created organization transaction: {} for organization: {}", transactionId, organization.getName());
        } catch (Exception e) {
            log.error("Error creating organization transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create organization transaction", e);
        }
    }

    @Override
    public void updateOrganization(String organizationId, OrganizationDTO organization) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("organization-service")
                    .operationType("UPDATE")
                    .topicName("update-organization")
                    .status("QUEUED")
                    .entityType("ORGANIZATION")
                    .correlationKey(organizationId)
                    .createdBy(UUID.randomUUID()) // TODO: Get from security context
                    .updatedBy(UUID.randomUUID()) // TODO: Get from security context
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("update-organization", organizationId, organization, transactionId);
            
            log.info("Created organization update transaction: {} for organization ID: {}", transactionId, organizationId);
        } catch (Exception e) {
            log.error("Error creating organization update transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create organization update transaction", e);
        }
    }

    @Override
    public void deleteOrganization(String organizationId) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("organization-service")
                    .operationType("DELETE")
                    .topicName("delete-organization")
                    .status("QUEUED")
                    .entityType("ORGANIZATION")
                    .correlationKey(organizationId)
                    .createdBy(UUID.randomUUID()) // TODO: Get from security context
                    .updatedBy(UUID.randomUUID()) // TODO: Get from security context
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("delete-organization", organizationId, null, transactionId);
            
            log.info("Created organization delete transaction: {} for organization ID: {}", transactionId, organizationId);
        } catch (Exception e) {
            log.error("Error creating organization delete transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create organization delete transaction", e);
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