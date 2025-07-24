package tech.stl.hcm.common.service;

import java.util.UUID;

public interface TransactionUpdateService {
    void updateTransactionToPending(UUID transactionId);
    void updateTransactionToProcessing(UUID transactionId);
    void updateTransactionToSuccess(UUID transactionId, UUID entityId, String responsePayload);
    void updateTransactionToFailed(UUID transactionId, String errorMessage);
    void updateTransactionRequestPayload(UUID transactionId, String requestPayload);
    // Optionally, add getTransactionById, transactionExists, etc. if needed
} 