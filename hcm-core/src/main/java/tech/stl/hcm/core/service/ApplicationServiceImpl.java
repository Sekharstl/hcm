package tech.stl.hcm.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.stl.hcm.common.dto.ApplicationDTO;
import tech.stl.hcm.common.dto.ApplicationStatusDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationCreateDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationStatusCreateDTO;
import tech.stl.hcm.common.dto.helpers.TransactionDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.core.config.ServiceProperties;
import tech.stl.hcm.message.broker.producer.ProducerService;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final WebClient webClient;
    private final ProducerService producerService;
    private final ServiceProperties serviceProperties;
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    public ApplicationServiceImpl(WebClient.Builder webClientBuilder, ProducerService producerService, ServiceProperties serviceProperties, TransactionService transactionService, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.producerService = producerService;
        this.serviceProperties = serviceProperties;
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ApplicationDTO> getApplicationsForCandidate(String candidateId) {
        return webClient.get()
                .uri(serviceProperties.getApplicationUrl() + "/candidate/{candidateId}", candidateId)
                .retrieve()
                .bodyToFlux(ApplicationDTO.class)
                .collectList()
                .block();
    }

    @Override
    public ApplicationStatusDTO getApplicationStatus(String candidateId, String applicationId) {
        return webClient.get()
                .uri(serviceProperties.getApplicationUrl() + "/candidate/{candidateId}/application/{applicationId}/status", candidateId, applicationId)
                .retrieve()
                .bodyToMono(ApplicationStatusDTO.class)
                .block();
    }

    @Override
    public void addApplication(ApplicationCreateDTO application) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("application-service")
                    .operationType("CREATE")
                    .topicName("create-application")
                    .status("QUEUED")
                    .entityType("APPLICATION")
                    .correlationKey(application.getCandidateId().toString())
                    .createdBy(UUID.randomUUID()) // TODO: Get from security context
                    .updatedBy(UUID.randomUUID()) // TODO: Get from security context
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("create-application", application.getCandidateId().toString(), application, transactionId);
            
            log.info("Created application transaction: {} for candidate ID: {}", transactionId, application.getCandidateId());
        } catch (Exception e) {
            log.error("Error creating application transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create application transaction", e);
        }
    }

    @Override
    public void updateApplication(String candidateId, String applicationId, ApplicationDTO application) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("application-service")
                    .operationType("UPDATE")
                    .topicName("update-application")
                    .status("QUEUED")
                    .entityType("APPLICATION")
                    .correlationKey(candidateId + ":" + applicationId)
                    .createdBy(UUID.randomUUID()) // TODO: Get from security context
                    .updatedBy(UUID.randomUUID()) // TODO: Get from security context
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("update-application", candidateId + ":" + applicationId, application, transactionId);
            
            log.info("Created application update transaction: {} for candidate ID: {} and application ID: {}", transactionId, candidateId, applicationId);
        } catch (Exception e) {
            log.error("Error creating application update transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create application update transaction", e);
        }
    }

    @Override
    public void deleteApplication(String candidateId, String applicationId) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("application-service")
                    .operationType("DELETE")
                    .topicName("delete-application")
                    .status("QUEUED")
                    .entityType("APPLICATION")
                    .correlationKey(candidateId + ":" + applicationId)
                    .createdBy(UUID.randomUUID()) // TODO: Get from security context
                    .updatedBy(UUID.randomUUID()) // TODO: Get from security context
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("delete-application", candidateId + ":" + applicationId, null, transactionId);
            
            log.info("Created application delete transaction: {} for candidate ID: {} and application ID: {}", transactionId, candidateId, applicationId);
        } catch (Exception e) {
            log.error("Error creating application delete transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create application delete transaction", e);
        }
    }

    @Override
    public void addApplicationStatus(ApplicationStatusCreateDTO applicationStatus) {
        publishEvent("create-application-status", "new-status", applicationStatus);
    }

    @Override
    public void updateApplicationStatus(String candidateId, String applicationId, ApplicationStatusDTO applicationStatus) {
        publishEvent("update-application-status", candidateId + ":" + applicationId, applicationStatus);
    }

    @Override
    public void deleteApplicationStatus(String candidateId, String applicationId) {
        publishEvent("delete-application-status", candidateId + ":" + applicationId, null);
    }

    @Override
    public List<ApplicationDTO> getAllApplications() {
        // The application service now returns a Page, so we need to extract the content
        Map<String, Object> pageResponse = webClient.get()
                .uri(serviceProperties.getApplicationUrl())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        
        if (pageResponse != null && pageResponse.containsKey("content")) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> contentMaps = (List<Map<String, Object>>) pageResponse.get("content");
                return contentMaps.stream()
                        .map(contentMap -> objectMapper.convertValue(contentMap, ApplicationDTO.class))
                        .toList();
            } catch (Exception e) {
                // Log the error and return empty list
                return List.of();
            }
        }
        return List.of();
    }

    @Override
    public PaginatedResponseDTO<ApplicationDTO> getAllApplicationsPaginated(int page, int size, String sortBy, String sortDirection) {
        String url = serviceProperties.getApplicationUrl() + "/paginated?page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection;
        
        // For now, we'll call the non-paginated endpoint and manually create pagination
        // In a production environment, you would need to implement proper Page deserialization
        List<ApplicationDTO> allApplications = getAllApplications();
        
        // Calculate pagination manually
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allApplications.size());
        List<ApplicationDTO> pageContent = allApplications.subList(startIndex, endIndex);
        
        int totalPages = (int) Math.ceil((double) allApplications.size() / size);
        
        return new PaginatedResponseDTO<>(
            pageContent,
            page,
            size,
            allApplications.size(),
            totalPages,
            page < totalPages - 1,
            page > 0,
            page == 0,
            page == totalPages - 1
        );
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