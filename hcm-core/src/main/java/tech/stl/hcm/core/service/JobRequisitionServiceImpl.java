package tech.stl.hcm.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.stl.hcm.common.dto.JobRequisitionDTO;
import tech.stl.hcm.common.dto.helpers.JobRequisitionCreateDTO;
import tech.stl.hcm.common.dto.helpers.TransactionDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.core.config.ServiceProperties;
import tech.stl.hcm.message.broker.producer.ProducerService;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class JobRequisitionServiceImpl implements JobRequisitionService {
    private final WebClient webClient;
    private final ProducerService producerService;
    private final ServiceProperties serviceProperties;
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    public JobRequisitionServiceImpl(WebClient.Builder webClientBuilder, ProducerService producerService, ServiceProperties serviceProperties, TransactionService transactionService, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.producerService = producerService;
        this.serviceProperties = serviceProperties;
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<JobRequisitionDTO> getAllJobRequisitions() {
        // The job requisition service now returns a Page, so we need to extract the content
        Map<String, Object> pageResponse = webClient.get()
                .uri(serviceProperties.getJobRequisitionUrl())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        
        if (pageResponse != null && pageResponse.containsKey("content")) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> contentMaps = (List<Map<String, Object>>) pageResponse.get("content");
                return contentMaps.stream()
                        .map(contentMap -> objectMapper.convertValue(contentMap, JobRequisitionDTO.class))
                        .toList();
            } catch (Exception e) {
                // Log the error and return empty list
                return List.of();
            }
        }
        return List.of();
    }



    @Override
    public PaginatedResponseDTO<JobRequisitionDTO> getAllJobRequisitionsPaginated(int page, int size, String sortBy, String sortDirection) {
        String url = serviceProperties.getJobRequisitionUrl() + "/paginated?page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection;
        
        // For now, we'll call the non-paginated endpoint and manually create pagination
        // In a production environment, you would need to implement proper Page deserialization
        List<JobRequisitionDTO> allJobRequisitions = getAllJobRequisitions();
        
        // Calculate pagination manually
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allJobRequisitions.size());
        List<JobRequisitionDTO> pageContent = allJobRequisitions.subList(startIndex, endIndex);
        
        int totalPages = (int) Math.ceil((double) allJobRequisitions.size() / size);
        
        return new PaginatedResponseDTO<>(
            pageContent,
            page,
            size,
            allJobRequisitions.size(),
            totalPages,
            page < totalPages - 1,
            page > 0,
            page == 0,
            page == totalPages - 1
        );
    }

    @Override
    public JobRequisitionDTO getJobRequisitionById(Integer id) {
        return webClient.get()
                .uri(serviceProperties.getJobRequisitionUrl() + "/{id}", id)
                .retrieve()
                .bodyToMono(JobRequisitionDTO.class)
                .block();
    }

    @Override
    public void createJobRequisition(JobRequisitionCreateDTO createDTO) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("job-requisition-service")
                    .operationType("CREATE")
                    .topicName("create-job-requisition")
                    .status("QUEUED")
                    .entityType("JOB_REQUISITION")
                    .correlationKey(createDTO.getTitle())
                    .createdBy(UUID.randomUUID()) // TODO: Get from security context
                    .updatedBy(UUID.randomUUID()) // TODO: Get from security context
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("create-job-requisition", createDTO.getTitle(), createDTO, transactionId);
            
            log.info("Created job requisition transaction: {} for job requisition: {}", transactionId, createDTO.getTitle());
        } catch (Exception e) {
            log.error("Error creating job requisition transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create job requisition transaction", e);
        }
    }

    @Override
    public void updateJobRequisition(JobRequisitionDTO dto) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("job-requisition-service")
                    .operationType("UPDATE")
                    .topicName("update-job-requisition")
                    .status("QUEUED")
                    .entityType("JOB_REQUISITION")
                    .correlationKey(dto.getJobRequisitionId().toString())
                    .createdBy(UUID.randomUUID()) // TODO: Get from security context
                    .updatedBy(UUID.randomUUID()) // TODO: Get from security context
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("update-job-requisition", dto.getJobRequisitionId().toString(), dto, transactionId);
            
            log.info("Created job requisition update transaction: {} for job requisition ID: {}", transactionId, dto.getJobRequisitionId());
        } catch (Exception e) {
            log.error("Error creating job requisition update transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create job requisition update transaction", e);
        }
    }

    @Override
    public void deleteJobRequisition(Integer id) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("job-requisition-service")
                    .operationType("DELETE")
                    .topicName("delete-job-requisition")
                    .status("QUEUED")
                    .entityType("JOB_REQUISITION")
                    .correlationKey(id.toString())
                    .createdBy(UUID.randomUUID()) // TODO: Get from security context
                    .updatedBy(UUID.randomUUID()) // TODO: Get from security context
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("delete-job-requisition", id.toString(), null, transactionId);
            
            log.info("Created job requisition delete transaction: {} for job requisition ID: {}", transactionId, id);
        } catch (Exception e) {
            log.error("Error creating job requisition delete transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create job requisition delete transaction", e);
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