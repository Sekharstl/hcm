package tech.stl.hcm.core.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.stl.hcm.common.dto.VendorDTO;
import tech.stl.hcm.common.dto.helpers.VendorCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.common.dto.helpers.TransactionDTO;
import tech.stl.hcm.common.mapper.VendorMapper;
import tech.stl.hcm.core.config.ServiceProperties;
import tech.stl.hcm.message.broker.producer.ProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class VendorServiceImpl implements VendorService {
    private final WebClient webClient;
    private final ProducerService producerService;
    private final ServiceProperties serviceProperties;
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    public VendorServiceImpl(WebClient.Builder webClientBuilder, ProducerService producerService, ServiceProperties serviceProperties, TransactionService transactionService, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.producerService = producerService;
        this.serviceProperties = serviceProperties;
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<VendorDTO> getAllVendors() {
        // The vendor service now returns a Page, so we need to extract the content
        Map<String, Object> pageResponse = webClient.get()
                .uri(serviceProperties.getVendorUrl())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        
        if (pageResponse != null && pageResponse.containsKey("content")) {
            try {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> contentMaps = (List<Map<String, Object>>) pageResponse.get("content");
                return contentMaps.stream()
                        .map(VendorMapper::fromMap)
                        .toList();
            } catch (Exception e) {
                // Log the error and return empty list
                return List.of();
            }
        }
        return List.of();
    }

    @Override
    public PaginatedResponseDTO<VendorDTO> getAllVendorsPaginated(int page, int size, String sortBy, String sortDirection) {
        String url = serviceProperties.getVendorUrl() + "/paginated?page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection;
        
        // For now, we'll call the non-paginated endpoint and manually create pagination
        // In a production environment, you would need to implement proper Page deserialization
        List<VendorDTO> allVendors = getAllVendors();
        
        // Calculate pagination manually
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allVendors.size());
        List<VendorDTO> pageContent = allVendors.subList(startIndex, endIndex);
        
        int totalPages = (int) Math.ceil((double) allVendors.size() / size);
        
        return new PaginatedResponseDTO<>(
            pageContent,
            page,
            size,
            allVendors.size(),
            totalPages,
            page < totalPages - 1,
            page > 0,
            page == 0,
            page == totalPages - 1
        );
    }

    @Override
    public VendorDTO getVendorById(UUID vendorId) {
        return webClient.get()
                .uri(serviceProperties.getVendorUrl() + "/" + vendorId)
                .retrieve()
                .bodyToMono(VendorDTO.class)
                .block();
    }

    @Override
    public void createVendor(VendorCreateDTO vendor) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("vendor-service")
                    .operationType("CREATE")
                    .topicName("create-vendor")
                    .status("QUEUED")
                    .entityType("VENDOR")
                    .correlationKey(vendor.getVendorName())
                    .createdBy(UUID.randomUUID())
                    .updatedBy(UUID.randomUUID())
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("create-vendor", vendor.getVendorName(), vendor, transactionId);
            
            log.info("Created vendor transaction: {} for vendor: {}", transactionId, vendor.getVendorName());
        } catch (Exception e) {
            log.error("Error creating vendor transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create vendor transaction", e);
        }
    }

    @Override
    public void updateVendor(UUID vendorId, VendorDTO vendor) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("vendor-service")
                    .operationType("UPDATE")
                    .topicName("update-vendor")
                    .status("QUEUED")
                    .entityType("VENDOR")
                    .entityId(vendorId)
                    .correlationKey(vendor.getVendorName())
                    .createdBy(UUID.randomUUID())
                    .updatedBy(UUID.randomUUID())
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("update-vendor", vendorId.toString(), vendor, transactionId);
            
            log.info("Created vendor update transaction: {} for vendor ID: {}", transactionId, vendorId);
        } catch (Exception e) {
            log.error("Error creating vendor update transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create vendor update transaction", e);
        }
    }

    @Override
    public void deleteVendor(UUID vendorId) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("vendor-service")
                    .operationType("DELETE")
                    .topicName("delete-vendor")
                    .status("QUEUED")
                    .entityType("VENDOR")
                    .entityId(vendorId)
                    .correlationKey(vendorId.toString())
                    .createdBy(UUID.randomUUID())
                    .updatedBy(UUID.randomUUID())
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("delete-vendor", vendorId.toString(), vendorId, transactionId);
            
            log.info("Created vendor delete transaction: {} for vendor ID: {}", transactionId, vendorId);
        } catch (Exception e) {
            log.error("Error creating vendor delete transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create vendor delete transaction", e);
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