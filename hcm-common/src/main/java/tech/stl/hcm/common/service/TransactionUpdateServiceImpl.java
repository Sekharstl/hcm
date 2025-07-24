package tech.stl.hcm.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionUpdateServiceImpl implements TransactionUpdateService {

    @Value("${hcm.core.url:http://localhost:9112}")
    private String hcmCoreUrl;

    private final WebClient.Builder webClientBuilder;

    @Override
    public void updateTransactionToPending(UUID transactionId) {
        try {
            WebClient webClient = webClientBuilder.build();
            webClient.put()
                    .uri(hcmCoreUrl + "/api/v1/transactions/{transactionId}/status", transactionId)
                    .bodyValue(new TransactionStatusUpdate("PENDING", null, null, null))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            log.info("Updated transaction {} to PENDING", transactionId);
        } catch (Exception e) {
            log.error("Failed to update transaction {} to PENDING: {}", transactionId, e.getMessage(), e);
        }
    }

    @Override
    public void updateTransactionToProcessing(UUID transactionId) {
        try {
            WebClient webClient = webClientBuilder.build();
            webClient.put()
                    .uri(hcmCoreUrl + "/api/v1/transactions/{transactionId}/status", transactionId)
                    .bodyValue(new TransactionStatusUpdate("PROCESSING", null, null, null))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            log.info("Updated transaction {} to PROCESSING", transactionId);
        } catch (Exception e) {
            log.error("Failed to update transaction {} to PROCESSING: {}", transactionId, e.getMessage(), e);
        }
    }

    @Override
    public void updateTransactionToSuccess(UUID transactionId, UUID entityId, String responsePayload) {
        try {
            WebClient webClient = webClientBuilder.build();
            webClient.put()
                    .uri(hcmCoreUrl + "/api/v1/transactions/{transactionId}/status", transactionId)
                    .bodyValue(new TransactionStatusUpdate("SUCCESS", entityId, responsePayload, null))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            log.info("Updated transaction {} to SUCCESS with entity ID: {}", transactionId, entityId);
        } catch (Exception e) {
            log.error("Failed to update transaction {} to SUCCESS: {}", transactionId, e.getMessage(), e);
        }
    }

    @Override
    public void updateTransactionToFailed(UUID transactionId, String errorMessage) {
        try {
            WebClient webClient = webClientBuilder.build();
            webClient.put()
                    .uri(hcmCoreUrl + "/api/v1/transactions/{transactionId}/status", transactionId)
                    .bodyValue(new TransactionStatusUpdate("FAILED", null, null, errorMessage))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            log.info("Updated transaction {} to FAILED: {}", transactionId, errorMessage);
        } catch (Exception e) {
            log.error("Failed to update transaction {} to FAILED: {}", transactionId, e.getMessage(), e);
        }
    }

    @Override
    public void updateTransactionRequestPayload(UUID transactionId, String requestPayload) {
        try {
            WebClient webClient = webClientBuilder.build();
            webClient.put()
                    .uri(hcmCoreUrl + "/api/v1/transactions/{transactionId}/request-payload", transactionId)
                    .bodyValue(new TransactionRequestPayloadUpdate(requestPayload))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            log.info("Updated transaction {} request payload", transactionId);
        } catch (Exception e) {
            log.error("Failed to update transaction {} request payload: {}", transactionId, e.getMessage(), e);
        }
    }

    // Inner class for transaction status update
    public static class TransactionStatusUpdate {
        private String status;
        private UUID entityId;
        private String responsePayload;
        private String errorMessage;

        public TransactionStatusUpdate(String status, UUID entityId, String responsePayload, String errorMessage) {
            this.status = status;
            this.entityId = entityId;
            this.responsePayload = responsePayload;
            this.errorMessage = errorMessage;
        }

        // Getters and setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public UUID getEntityId() { return entityId; }
        public void setEntityId(UUID entityId) { this.entityId = entityId; }
        public String getResponsePayload() { return responsePayload; }
        public void setResponsePayload(String responsePayload) { this.responsePayload = responsePayload; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    // Inner class for transaction request payload update
    public static class TransactionRequestPayloadUpdate {
        private String requestPayload;

        public TransactionRequestPayloadUpdate(String requestPayload) {
            this.requestPayload = requestPayload;
        }

        // Getters and setters
        public String getRequestPayload() { return requestPayload; }
        public void setRequestPayload(String requestPayload) { this.requestPayload = requestPayload; }
    }
} 