package tech.stl.hcm.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import tech.stl.hcm.message.broker.consumer.MessageHandler;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseTransactionConsumer<T> implements MessageHandler<Map<String, Object>> {
    
    protected final ObjectMapper objectMapper;
    protected final TransactionUpdateService transactionUpdateService;
    
    @Override
    public void handle(Map<String, Object> message, String key) {
        UUID transactionId = null;
        T entity = null;
        
        try {
            // Extract transaction ID and entity data from the message
            transactionId = UUID.fromString((String) message.get("transactionId"));
            @SuppressWarnings("unchecked")
            Map<String, Object> entityData = (Map<String, Object>) message.get("payload");
            
            // Convert to entity type
            entity = objectMapper.convertValue(entityData, getEntityType());
            
            MDC.put("Transaction ID", transactionId.toString());
            log.info("Consumed {} with transaction ID: {}", getEntityName(), transactionId);
            
            try {
                // Update transaction status from QUEUED to PENDING
                transactionUpdateService.updateTransactionToPending(transactionId);
                
                // Update transaction status to PROCESSING
                transactionUpdateService.updateTransactionToProcessing(transactionId);
                
                // Process the entity
                Object result = processEntity(entity, transactionId);
                
                if (isSuccess(result)) {
                    log.info("{} processed successfully", getEntityName());
                    
                    // Set the request payload after successful processing
                    String requestPayload = objectMapper.writeValueAsString(entity);
                    transactionUpdateService.updateTransactionRequestPayload(transactionId, requestPayload);
                    
                    // Update transaction status to SUCCESS
                    String responsePayload = objectMapper.writeValueAsString(result);
                    UUID entityId = getEntityId(result);
                    transactionUpdateService.updateTransactionToSuccess(transactionId, entityId, responsePayload);
                } else {
                    log.error("{} processing failed", getEntityName());
                    
                    // Update transaction status to FAILED
                    transactionUpdateService.updateTransactionToFailed(transactionId, getEntityName() + " processing failed");
                }
            } catch (Exception processingException) {
                log.error("Error during processing of {}: {}", getEntityName(), processingException.getMessage(), processingException);
                
                // Update transaction status to FAILED
                transactionUpdateService.updateTransactionToFailed(transactionId, "Error processing " + getEntityName() + ": " + processingException.getMessage());
            }
        } catch (Exception e) {
            log.error("Error processing {}: {}", getEntityName(), e.getMessage(), e);
            
            // Update transaction status to FAILED if we have a transaction ID
            if (transactionId != null) {
                transactionUpdateService.updateTransactionToFailed(transactionId, "Error processing " + getEntityName() + ": " + e.getMessage());
            }
        } finally {
            MDC.clear();
        }
    }
    
    /**
     * Get the entity type class
     */
    protected abstract Class<T> getEntityType();
    
    /**
     * Get the entity name for logging
     */
    protected abstract String getEntityName();
    
    /**
     * Process the entity and return the result
     */
    protected abstract Object processEntity(T entity, UUID transactionId);
    
    /**
     * Check if the processing was successful
     */
    protected abstract boolean isSuccess(Object result);
    
    /**
     * Extract entity ID from the result
     */
    protected abstract UUID getEntityId(Object result);
} 