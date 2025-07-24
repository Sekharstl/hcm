package tech.stl.hcm.core.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.stl.hcm.common.dto.PositionDTO;
import tech.stl.hcm.common.dto.PositionStatusDTO;
import tech.stl.hcm.common.dto.helpers.PositionCreateDTO;
import tech.stl.hcm.common.dto.helpers.TransactionDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.core.config.ServiceProperties;
import tech.stl.hcm.message.broker.producer.ProducerService;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class PositionServiceImpl implements PositionService {
    private final WebClient webClient;
    private final ProducerService producerService;
    private final ServiceProperties serviceProperties;
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    public PositionServiceImpl(WebClient.Builder webClientBuilder, ProducerService producerService, ServiceProperties serviceProperties, TransactionService transactionService, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.producerService = producerService;
        this.serviceProperties = serviceProperties;
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<PositionDTO> getAllPositions() {
        // The position service now returns a Page, so we need to extract the content
        Map<String, Object> pageResponse = webClient.get()
                .uri(serviceProperties.getPositionUrl())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        
        if (pageResponse != null && pageResponse.containsKey("content")) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> contentMaps = (List<Map<String, Object>>) pageResponse.get("content");
                return contentMaps.stream()
                        .map(contentMap -> objectMapper.convertValue(contentMap, PositionDTO.class))
                        .toList();
            } catch (Exception e) {
                // Log the error and return empty list
                return List.of();
            }
        }
        return List.of();
    }



    @Override
    public PaginatedResponseDTO<PositionDTO> getAllPositionsPaginated(int page, int size, String sortBy, String sortDirection) {
        String url = serviceProperties.getPositionUrl() + "/paginated?page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection;
        
        // For now, we'll call the non-paginated endpoint and manually create pagination
        // In a production environment, you would need to implement proper Page deserialization
        List<PositionDTO> allPositions = getAllPositions();
        
        // Calculate pagination manually
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allPositions.size());
        List<PositionDTO> pageContent = allPositions.subList(startIndex, endIndex);
        
        int totalPages = (int) Math.ceil((double) allPositions.size() / size);
        
        return new PaginatedResponseDTO<>(
            pageContent,
            page,
            size,
            allPositions.size(),
            totalPages,
            page < totalPages - 1,
            page > 0,
            page == 0,
            page == totalPages - 1
        );
    }

    @Override
    public PositionDTO getPositionById(Integer id) {
        return webClient.get()
                .uri(serviceProperties.getPositionUrl() + "/{id}", id)
                .retrieve()
                .bodyToMono(PositionDTO.class)
                .block();
    }

    @Override
    public void createPosition(PositionCreateDTO position) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("position-service")
                    .operationType("CREATE")
                    .topicName("create-position")
                    .status("QUEUED")
                    .entityType("POSITION")
                    .correlationKey(position.getTitle())
                    .createdBy(UUID.randomUUID())
                    .updatedBy(UUID.randomUUID())
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("create-position", position.getTitle(), position, transactionId);
            
            log.info("Created position transaction: {} for position: {}", transactionId, position.getTitle());
        } catch (Exception e) {
            log.error("Error creating position transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create position transaction", e);
        }
    }

    @Override
    public void updatePosition(Integer id, PositionDTO position) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("position-service")
                    .operationType("UPDATE")
                    .topicName("update-position")
                    .status("QUEUED")
                    .entityType("POSITION")
                    .correlationKey(id.toString())
                    .createdBy(UUID.randomUUID())
                    .updatedBy(UUID.randomUUID())
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("update-position", id.toString(), position, transactionId);
            
            log.info("Created position update transaction: {} for position ID: {}", transactionId, id);
        } catch (Exception e) {
            log.error("Error creating position update transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create position update transaction", e);
        }
    }

    @Override
    public void deletePosition(Integer id) {
        try {
            // Generate transaction ID
            UUID transactionId = transactionService.generateTransactionId();
            
            // Create transaction record
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("position-service")
                    .operationType("DELETE")
                    .topicName("delete-position")
                    .status("QUEUED")
                    .entityType("POSITION")
                    .correlationKey(id.toString())
                    .createdBy(UUID.randomUUID())
                    .updatedBy(UUID.randomUUID())
                    .build();
            
            transactionService.createTransaction(transactionDTO);
            
            // Publish event with transaction ID
            publishEvent("delete-position", id.toString(), null, transactionId);
            
            log.info("Created position delete transaction: {} for position ID: {}", transactionId, id);
        } catch (Exception e) {
            log.error("Error creating position delete transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create position delete transaction", e);
        }
    }

    @Override
    public List<PositionStatusDTO> getAllPositionStatuses() {
        return webClient.get()
                .uri(serviceProperties.getPositionStatusUrl())
                .retrieve()
                .bodyToFlux(PositionStatusDTO.class)
                .collectList()
                .block();
    }

    @Override
    public PositionStatusDTO getPositionStatusById(Integer id) {
        return webClient.get()
                .uri(serviceProperties.getPositionStatusUrl() + "/{id}", id)
                .retrieve()
                .bodyToMono(PositionStatusDTO.class)
                .block();
    }

    @Override
    public void createPositionStatus(PositionStatusDTO status) {
        publishEvent("create-position-status", status != null && status.getStatusId() != null ? status.getStatusId().toString() : null, status);
    }

    @Override
    public void updatePositionStatus(Integer id, PositionStatusDTO status) {
        publishEvent("update-position-status", id.toString(), status);
    }

    @Override
    public void deletePositionStatus(Integer id) {
        publishEvent("delete-position-status", id.toString(), null);
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