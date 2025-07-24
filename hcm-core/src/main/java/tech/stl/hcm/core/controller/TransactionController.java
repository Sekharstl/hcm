package tech.stl.hcm.core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.common.dto.helpers.TransactionDTO;
import tech.stl.hcm.core.service.TransactionService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Get transaction by ID
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable UUID transactionId) {
        TransactionDTO transaction = transactionService.getTransactionById(transactionId);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get transaction by correlation key
     */
    @GetMapping("/correlation/{correlationKey}")
    public ResponseEntity<TransactionDTO> getTransactionByCorrelationKey(@PathVariable String correlationKey) {
        TransactionDTO transaction = transactionService.getTransactionByCorrelationKey(correlationKey);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get transaction by entity ID
     */
    @GetMapping("/entity/{entityId}")
    public ResponseEntity<TransactionDTO> getTransactionByEntityId(@PathVariable UUID entityId) {
        TransactionDTO transaction = transactionService.getTransactionByEntityId(entityId);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get all transactions
     */
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    /**
     * Get transactions by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByStatus(@PathVariable String status) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByStatus(status);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Get transactions by service name
     */
    @GetMapping("/service/{serviceName}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByServiceName(@PathVariable String serviceName) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByServiceName(serviceName);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Get transactions by entity type
     */
    @GetMapping("/entity-type/{entityType}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByEntityType(@PathVariable String entityType) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByEntityType(entityType);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Get pending transactions
     */
    @GetMapping("/pending")
    public ResponseEntity<List<TransactionDTO>> getPendingTransactions() {
        List<TransactionDTO> transactions = transactionService.getPendingTransactions();
        return ResponseEntity.ok(transactions);
    }

    /**
     * Get failed transactions
     */
    @GetMapping("/failed")
    public ResponseEntity<List<TransactionDTO>> getFailedTransactions() {
        List<TransactionDTO> transactions = transactionService.getFailedTransactions();
        return ResponseEntity.ok(transactions);
    }

    /**
     * Get successful transactions
     */
    @GetMapping("/successful")
    public ResponseEntity<List<TransactionDTO>> getSuccessfulTransactions() {
        List<TransactionDTO> transactions = transactionService.getSuccessfulTransactions();
        return ResponseEntity.ok(transactions);
    }

    /**
     * Get transactions for retry
     */
    @GetMapping("/retry")
    public ResponseEntity<List<TransactionDTO>> getTransactionsForRetry() {
        List<TransactionDTO> transactions = transactionService.getTransactionsForRetry();
        return ResponseEntity.ok(transactions);
    }

    /**
     * Check if transaction exists
     */
    @GetMapping("/{transactionId}/exists")
    public ResponseEntity<Boolean> transactionExists(@PathVariable UUID transactionId) {
        boolean exists = transactionService.transactionExists(transactionId);
        return ResponseEntity.ok(exists);
    }

    /**
     * Get transaction count by status
     */
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getTransactionCountByStatus(@PathVariable String status) {
        long count = transactionService.getTransactionCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    /**
     * Get transaction count by service and status
     */
    @GetMapping("/count/service/{serviceName}/status/{status}")
    public ResponseEntity<Long> getTransactionCountByServiceAndStatus(
            @PathVariable String serviceName, 
            @PathVariable String status) {
        long count = transactionService.getTransactionCountByServiceAndStatus(serviceName, status);
        return ResponseEntity.ok(count);
    }

    /**
     * Delete transaction
     */
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update transaction status
     */
    @PutMapping("/{transactionId}/status")
    public ResponseEntity<TransactionDTO> updateTransactionStatus(
            @PathVariable UUID transactionId,
            @RequestParam String status,
            @RequestParam(required = false) String responsePayload,
            @RequestParam(required = false) String errorMessage) {
        
        TransactionDTO updatedTransaction = transactionService.updateTransactionStatus(
                transactionId, status, responsePayload, errorMessage);
        return ResponseEntity.ok(updatedTransaction);
    }

    /**
     * Update transaction request payload
     */
    @PutMapping("/{transactionId}/request-payload")
    public ResponseEntity<TransactionDTO> updateTransactionRequestPayload(
            @PathVariable UUID transactionId,
            @RequestBody RequestPayloadUpdate request) {
        
        TransactionDTO updatedTransaction = transactionService.updateTransactionRequestPayload(
                transactionId, request.getRequestPayload());
        return ResponseEntity.ok(updatedTransaction);
    }

    /**
     * Request payload update DTO
     */
    public static class RequestPayloadUpdate {
        private String requestPayload;

        public RequestPayloadUpdate() {}

        public RequestPayloadUpdate(String requestPayload) {
            this.requestPayload = requestPayload;
        }

        public String getRequestPayload() {
            return requestPayload;
        }

        public void setRequestPayload(String requestPayload) {
            this.requestPayload = requestPayload;
        }
    }

    /**
     * Generate new transaction ID
     */
    @PostMapping("/generate-id")
    public ResponseEntity<UUID> generateTransactionId() {
        UUID transactionId = transactionService.generateTransactionId();
        return ResponseEntity.ok(transactionId);
    }
} 