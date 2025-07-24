package tech.stl.hcm.core.service;

import tech.stl.hcm.common.dto.helpers.TransactionDTO;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    
    /**
     * Create a new transaction
     */
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    
    /**
     * Update transaction status
     */
    TransactionDTO updateTransactionStatus(UUID transactionId, String status, String responsePayload, String errorMessage);
    
    /**
     * Update transaction request payload
     */
    TransactionDTO updateTransactionRequestPayload(UUID transactionId, String requestPayload);
    
    /**
     * Get transaction by ID
     */
    TransactionDTO getTransactionById(UUID transactionId);
    
    /**
     * Get transaction by correlation key
     */
    TransactionDTO getTransactionByCorrelationKey(String correlationKey);
    
    /**
     * Get transaction by entity ID
     */
    TransactionDTO getTransactionByEntityId(UUID entityId);
    
    /**
     * Get all transactions
     */
    List<TransactionDTO> getAllTransactions();
    
    /**
     * Get transactions by status
     */
    List<TransactionDTO> getTransactionsByStatus(String status);
    
    /**
     * Get transactions by service name
     */
    List<TransactionDTO> getTransactionsByServiceName(String serviceName);
    
    /**
     * Get transactions by entity type
     */
    List<TransactionDTO> getTransactionsByEntityType(String entityType);
    
    /**
     * Get pending transactions
     */
    List<TransactionDTO> getPendingTransactions();
    
    /**
     * Get failed transactions
     */
    List<TransactionDTO> getFailedTransactions();
    
    /**
     * Get successful transactions
     */
    List<TransactionDTO> getSuccessfulTransactions();
    
    /**
     * Get transactions for retry
     */
    List<TransactionDTO> getTransactionsForRetry();
    
    /**
     * Delete transaction
     */
    void deleteTransaction(UUID transactionId);
    
    /**
     * Generate a new transaction ID
     */
    UUID generateTransactionId();
    
    /**
     * Check if transaction exists
     */
    boolean transactionExists(UUID transactionId);
    
    /**
     * Get transaction count by status
     */
    long getTransactionCountByStatus(String status);
    
    /**
     * Get transaction count by service and status
     */
    long getTransactionCountByServiceAndStatus(String serviceName, String status);
} 