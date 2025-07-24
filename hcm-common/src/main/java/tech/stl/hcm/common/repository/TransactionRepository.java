package tech.stl.hcm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.stl.hcm.common.db.entities.Transaction;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /**
     * Find transaction by correlation key
     */
    Optional<Transaction> findByCorrelationKey(String correlationKey);

    /**
     * Find transaction by entity ID
     */
    Optional<Transaction> findByEntityId(UUID entityId);

    /**
     * Find transactions by status
     */
    List<Transaction> findByStatus(String status);

    /**
     * Find transactions by service name
     */
    List<Transaction> findByServiceName(String serviceName);

    /**
     * Find transactions by entity type
     */
    List<Transaction> findByEntityType(String entityType);

    /**
     * Find transactions by status and service name
     */
    List<Transaction> findByStatusAndServiceName(String status, String serviceName);

    /**
     * Find transactions by status and entity type
     */
    List<Transaction> findByStatusAndEntityType(String status, String entityType);

    /**
     * Find pending transactions (QUEUED, PENDING or PROCESSING status)
     */
    @Query("SELECT t FROM Transaction t WHERE t.status IN ('QUEUED', 'PENDING', 'PROCESSING') ORDER BY t.createdAt ASC")
    List<Transaction> findPendingTransactions();

    /**
     * Find failed transactions
     */
    @Query("SELECT t FROM Transaction t WHERE t.status = 'FAILED' ORDER BY t.createdAt DESC")
    List<Transaction> findFailedTransactions();

    /**
     * Find successful transactions
     */
    @Query("SELECT t FROM Transaction t WHERE t.status = 'SUCCESS' ORDER BY t.createdAt DESC")
    List<Transaction> findSuccessfulTransactions();

    /**
     * Find transactions created after a specific time
     */
    List<Transaction> findByCreatedAtAfter(Instant createdAt);

    /**
     * Find transactions by correlation key and status
     */
    Optional<Transaction> findByCorrelationKeyAndStatus(String correlationKey, String status);

    /**
     * Find transactions by entity ID and status
     */
    Optional<Transaction> findByEntityIdAndStatus(UUID entityId, String status);

    /**
     * Count transactions by status
     */
    long countByStatus(String status);

    /**
     * Count transactions by service name and status
     */
    long countByServiceNameAndStatus(String serviceName, String status);

    /**
     * Find transactions that need retry (FAILED status with retry count < max retries)
     */
    @Query("SELECT t FROM Transaction t WHERE t.status = 'FAILED' AND t.retryCount < t.maxRetries ORDER BY t.createdAt ASC")
    List<Transaction> findTransactionsForRetry();

    /**
     * Find transactions by correlation key and entity type
     */
    List<Transaction> findByCorrelationKeyAndEntityType(String correlationKey, String entityType);

    /**
     * Find the latest transaction for a correlation key
     */
    @Query("SELECT t FROM Transaction t WHERE t.correlationKey = :correlationKey ORDER BY t.createdAt DESC")
    List<Transaction> findLatestByCorrelationKey(@Param("correlationKey") String correlationKey);
} 