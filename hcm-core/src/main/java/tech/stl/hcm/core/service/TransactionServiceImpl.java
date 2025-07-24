package tech.stl.hcm.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.stl.hcm.common.db.entities.Transaction;
import tech.stl.hcm.common.dto.helpers.TransactionDTO;
import tech.stl.hcm.common.repository.TransactionRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        try {
            Transaction transaction = Transaction.builder()
                    .transactionId(transactionDTO.getTransactionId())
                    .serviceName(transactionDTO.getServiceName())
                    .operationType(transactionDTO.getOperationType())
                    .topicName(transactionDTO.getTopicName())
                    .status(transactionDTO.getStatus())
                    .entityType(transactionDTO.getEntityType())
                    .entityId(transactionDTO.getEntityId())
                    .correlationKey(transactionDTO.getCorrelationKey())
                    .requestPayload(null) // Don't set request payload during creation
                    .responsePayload(transactionDTO.getResponsePayload())
                    .errorMessage(transactionDTO.getErrorMessage())
                    .retryCount(transactionDTO.getRetryCount() != null ? transactionDTO.getRetryCount() : 0)
                    .maxRetries(transactionDTO.getMaxRetries() != null ? transactionDTO.getMaxRetries() : 3)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .processedAt(transactionDTO.getProcessedAt())
                    .createdBy(transactionDTO.getCreatedBy())
                    .updatedBy(transactionDTO.getUpdatedBy())
                    .build();

            Transaction savedTransaction = transactionRepository.save(transaction);
            log.info("Created transaction: {}", savedTransaction.getTransactionId());
            return mapToDTO(savedTransaction);
        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create transaction", e);
        }
    }

    @Override
    @Transactional
    public TransactionDTO updateTransactionStatus(UUID transactionId, String status, String responsePayload, String errorMessage) {
        try {
            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new RuntimeException("Transaction not found: " + transactionId));

            transaction.setStatus(status);
            transaction.setResponsePayload(responsePayload);
            transaction.setErrorMessage(errorMessage);
            transaction.setUpdatedAt(Instant.now());

            if ("SUCCESS".equals(status) || "FAILED".equals(status)) {
                transaction.setProcessedAt(Instant.now());
            }

            Transaction updatedTransaction = transactionRepository.save(transaction);
            log.info("Updated transaction status: {} -> {}", transactionId, status);
            return mapToDTO(updatedTransaction);
        } catch (Exception e) {
            log.error("Error updating transaction status: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update transaction status", e);
        }
    }

    @Override
    @Transactional
    public TransactionDTO updateTransactionRequestPayload(UUID transactionId, String requestPayload) {
        try {
            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new RuntimeException("Transaction not found: " + transactionId));

            transaction.setRequestPayload(requestPayload);
            transaction.setUpdatedAt(Instant.now());

            Transaction updatedTransaction = transactionRepository.save(transaction);
            log.info("Updated transaction request payload: {}", transactionId);
            return mapToDTO(updatedTransaction);
        } catch (Exception e) {
            log.error("Error updating transaction request payload: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update transaction request payload", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDTO getTransactionByCorrelationKey(String correlationKey) {
        return transactionRepository.findByCorrelationKey(correlationKey)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDTO getTransactionByEntityId(UUID entityId) {
        return transactionRepository.findByEntityId(entityId)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByStatus(String status) {
        return transactionRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByServiceName(String serviceName) {
        return transactionRepository.findByServiceName(serviceName).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByEntityType(String entityType) {
        return transactionRepository.findByEntityType(entityType).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getPendingTransactions() {
        return transactionRepository.findPendingTransactions().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getFailedTransactions() {
        return transactionRepository.findFailedTransactions().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getSuccessfulTransactions() {
        return transactionRepository.findSuccessfulTransactions().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsForRetry() {
        return transactionRepository.findTransactionsForRetry().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTransaction(UUID transactionId) {
        try {
            transactionRepository.deleteById(transactionId);
            log.info("Deleted transaction: {}", transactionId);
        } catch (Exception e) {
            log.error("Error deleting transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete transaction", e);
        }
    }

    @Override
    public UUID generateTransactionId() {
        return UUID.randomUUID();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean transactionExists(UUID transactionId) {
        return transactionRepository.existsById(transactionId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTransactionCountByStatus(String status) {
        return transactionRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTransactionCountByServiceAndStatus(String serviceName, String status) {
        return transactionRepository.countByServiceNameAndStatus(serviceName, status);
    }

    private TransactionDTO mapToDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .transactionId(transaction.getTransactionId())
                .serviceName(transaction.getServiceName())
                .operationType(transaction.getOperationType())
                .topicName(transaction.getTopicName())
                .status(transaction.getStatus())
                .entityType(transaction.getEntityType())
                .entityId(transaction.getEntityId())
                .correlationKey(transaction.getCorrelationKey())
                .requestPayload(transaction.getRequestPayload())
                .responsePayload(transaction.getResponsePayload())
                .errorMessage(transaction.getErrorMessage())
                .retryCount(transaction.getRetryCount())
                .maxRetries(transaction.getMaxRetries())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .processedAt(transaction.getProcessedAt())
                .createdBy(transaction.getCreatedBy())
                .updatedBy(transaction.getUpdatedBy())
                .build();
    }
} 