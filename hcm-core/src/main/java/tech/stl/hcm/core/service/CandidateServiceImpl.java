package tech.stl.hcm.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.stl.hcm.common.dto.*;
import tech.stl.hcm.common.dto.helpers.*;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.common.mapper.CandidateMapper;
import tech.stl.hcm.core.config.ServiceProperties;
import tech.stl.hcm.message.broker.producer.ProducerService;
import tech.stl.hcm.common.dto.helpers.TransactionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class CandidateServiceImpl implements CandidateService {
    private final WebClient webClient;
    private final ProducerService producerService;
    private final ServiceProperties serviceProperties;
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    public CandidateServiceImpl(WebClient.Builder webClientBuilder, ProducerService producerService, ServiceProperties serviceProperties, TransactionService transactionService, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.producerService = producerService;
        this.serviceProperties = serviceProperties;
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<CandidateDTO> getAllCandidates() {
        // The candidate service now returns a Page, so we need to extract the content
        Map<String, Object> pageResponse = webClient.get()
                .uri(serviceProperties.getCandidateUrl())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        
        if (pageResponse != null && pageResponse.containsKey("content")) {
            try {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> contentMaps = (List<Map<String, Object>>) pageResponse.get("content");
                return contentMaps.stream()
                        .map(CandidateMapper::fromMap)
                        .toList();
            } catch (Exception e) {
                // Log the error and return empty list
                return List.of();
            }
        }
        return List.of();
    }

    @Override
    public PaginatedResponseDTO<CandidateDTO> getAllCandidatesPaginated(int page, int size, String sortBy, String sortDirection) {
        String url = serviceProperties.getCandidateUrl() + "/paginated?page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection;
        
        // For now, we'll call the non-paginated endpoint and manually create pagination
        // In a production environment, you would need to implement proper Page deserialization
        List<CandidateDTO> allCandidates = getAllCandidates();
        
        // Calculate pagination manually
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allCandidates.size());
        List<CandidateDTO> pageContent = allCandidates.subList(startIndex, endIndex);
        
        int totalPages = (int) Math.ceil((double) allCandidates.size() / size);
        
        return new PaginatedResponseDTO<>(
            pageContent,
            page,
            size,
            allCandidates.size(),
            totalPages,
            page < totalPages - 1,
            page > 0,
            page == 0,
            page == totalPages - 1
        );
    }

    @Override
    public CandidateDTO getCandidateById(String candidateId) {
        return webClient.get()
                .uri(serviceProperties.getCandidateUrl() + "/{candidateId}", candidateId)
                .retrieve()
                .bodyToMono(CandidateDTO.class)
                .block();
    }

    @Override
    public List<CandidateSkillDTO> getSkillsForCandidate(String candidateId) {
        return webClient.get()
                .uri(serviceProperties.getCandidateUrl() + "/{candidateId}/skills", candidateId)
                .retrieve()
                .bodyToFlux(CandidateSkillDTO.class)
                .collectList()
                .block();
    }

    @Override
    public List<CandidateEducationDTO> getEducationsForCandidate(String candidateId) {
        return webClient.get()
                .uri(serviceProperties.getCandidateUrl() + "/{candidateId}/educations", candidateId)
                .retrieve()
                .bodyToFlux(CandidateEducationDTO.class)
                .collectList()
                .block();
    }

    @Override
    public List<CandidateWorkHistoryDTO> getWorkHistoriesForCandidate(String candidateId) {
        return webClient.get()
                .uri(serviceProperties.getCandidateUrl() + "/{candidateId}/work-histories", candidateId)
                .retrieve()
                .bodyToFlux(CandidateWorkHistoryDTO.class)
                .collectList()
                .block();
    }

    @Override
    public List<CandidateCertificationDTO> getCertificationsForCandidate(String candidateId) {
        return webClient.get()
                .uri(serviceProperties.getCandidateUrl() + "/{candidateId}/certifications", candidateId)
                .retrieve()
                .bodyToFlux(CandidateCertificationDTO.class)
                .collectList()
                .block();
    }

    @Override
    public UUID createCandidate(CandidateCreateDTO candidate) {
        try {
            UUID transactionId = transactionService.generateTransactionId();
            String correlationKey = candidate != null ? candidate.getEmail() : null;
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionId(transactionId)
                    .serviceName("candidate-service")
                    .operationType("CREATE")
                    .topicName("create-candidate")
                    .status("QUEUED")
                    .entityType("CANDIDATE")
                    .correlationKey(correlationKey)
                    .createdBy(UUID.randomUUID())
                    .updatedBy(UUID.randomUUID())
                    .build();
            transactionService.createTransaction(transactionDTO);
            publishEvent("create-candidate", correlationKey, candidate, transactionId);
            log.info("Created candidate transaction: {} for email: {}", transactionId, correlationKey);
            return transactionId;
        } catch (Exception e) {
            log.error("Error creating candidate transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create candidate transaction", e);
        }
    }

    @Override
    public UUID updateCandidate(String candidateId, CandidateDTO candidate) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("UPDATE")
                .topicName("update-candidate")
                .status("QUEUED")
                .entityType("CANDIDATE")
                .correlationKey(candidateId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("update-candidate", candidateId, candidate, transactionId);
        return transactionId;
    }

    @Override
    public UUID deleteCandidate(String candidateId) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("DELETE")
                .topicName("delete-candidate")
                .status("QUEUED")
                .entityType("CANDIDATE")
                .correlationKey(candidateId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("delete-candidate", candidateId, null, transactionId);
        return transactionId;
    }

    @Override
    public UUID addCandidateSkill(String candidateId, CandidateSkillCreateDTO skill) {
        skill.setCandidateId(UUID.fromString(candidateId));
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("CREATE")
                .topicName("create-candidate-skill")
                .status("QUEUED")
                .entityType("CANDIDATE_SKILL")
                .correlationKey(candidateId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("create-candidate-skill", candidateId, skill, transactionId);
        return transactionId;
    }

    @Override
    public UUID updateCandidateSkill(String candidateId, String skillId, CandidateSkillDTO skill) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("UPDATE")
                .topicName("update-candidate-skill")
                .status("QUEUED")
                .entityType("CANDIDATE_SKILL")
                .correlationKey(candidateId + ":" + skillId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("update-candidate-skill", candidateId + ":" + skillId, skill, transactionId);
        return transactionId;
    }

    @Override
    public UUID deleteCandidateSkill(String candidateId, String skillId) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("DELETE")
                .topicName("delete-candidate-skill")
                .status("QUEUED")
                .entityType("CANDIDATE_SKILL")
                .correlationKey(candidateId + ":" + skillId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("delete-candidate-skill", candidateId + ":" + skillId, null, transactionId);
        return transactionId;
    }

    @Override
    public UUID addCandidateEducation(String candidateId, CandidateEducationCreateDTO education) {
        education.setCandidateId(UUID.fromString(candidateId));
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("CREATE")
                .topicName("create-candidate-education")
                .status("QUEUED")
                .entityType("CANDIDATE_EDUCATION")
                .correlationKey(candidateId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("create-candidate-education", candidateId, education, transactionId);
        return transactionId;
    }

    @Override
    public UUID updateCandidateEducation(String candidateId, String educationId, CandidateEducationDTO education) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("UPDATE")
                .topicName("update-candidate-education")
                .status("QUEUED")
                .entityType("CANDIDATE_EDUCATION")
                .correlationKey(candidateId + ":" + educationId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("update-candidate-education", candidateId + ":" + educationId, education, transactionId);
        return transactionId;
    }

    @Override
    public UUID deleteCandidateEducation(String candidateId, String educationId) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("DELETE")
                .topicName("delete-candidate-education")
                .status("QUEUED")
                .entityType("CANDIDATE_EDUCATION")
                .correlationKey(candidateId + ":" + educationId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("delete-candidate-education", candidateId + ":" + educationId, null, transactionId);
        return transactionId;
    }

    @Override
    public UUID addCandidateWorkHistory(String candidateId, CandidateWorkHistoryCreateDTO workHistory) {
        workHistory.setCandidateId(UUID.fromString(candidateId));
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("CREATE")
                .topicName("create-candidate-work-history")
                .status("QUEUED")
                .entityType("CANDIDATE_WORK_HISTORY")
                .correlationKey(candidateId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("create-candidate-work-history", candidateId, workHistory, transactionId);
        return transactionId;
    }

    @Override
    public UUID updateCandidateWorkHistory(String candidateId, String workHistoryId, CandidateWorkHistoryDTO workHistory) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("UPDATE")
                .topicName("update-candidate-work-history")
                .status("QUEUED")
                .entityType("CANDIDATE_WORK_HISTORY")
                .correlationKey(candidateId + ":" + workHistoryId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("update-candidate-work-history", candidateId + ":" + workHistoryId, workHistory, transactionId);
        return transactionId;
    }

    @Override
    public UUID deleteCandidateWorkHistory(String candidateId, String workHistoryId) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("DELETE")
                .topicName("delete-candidate-work-history")
                .status("QUEUED")
                .entityType("CANDIDATE_WORK_HISTORY")
                .correlationKey(candidateId + ":" + workHistoryId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("delete-candidate-work-history", candidateId + ":" + workHistoryId, null, transactionId);
        return transactionId;
    }

    @Override
    public UUID addCandidateCertification(String candidateId, CandidateCertificationCreateDTO certification) {
        certification.setCandidateId(UUID.fromString(candidateId));
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("CREATE")
                .topicName("create-candidate-certification")
                .status("QUEUED")
                .entityType("CANDIDATE_CERTIFICATION")
                .correlationKey(candidateId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("create-candidate-certification", candidateId, certification, transactionId);
        return transactionId;
    }

    @Override
    public UUID updateCandidateCertification(String candidateId, String certificationId, CandidateCertificationDTO certification) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("UPDATE")
                .topicName("update-candidate-certification")
                .status("QUEUED")
                .entityType("CANDIDATE_CERTIFICATION")
                .correlationKey(candidateId + ":" + certificationId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("update-candidate-certification", candidateId + ":" + certificationId, certification, transactionId);
        return transactionId;
    }

    @Override
    public UUID deleteCandidateCertification(String candidateId, String certificationId) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("DELETE")
                .topicName("delete-candidate-certification")
                .status("QUEUED")
                .entityType("CANDIDATE_CERTIFICATION")
                .correlationKey(candidateId + ":" + certificationId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("delete-candidate-certification", candidateId + ":" + certificationId, null, transactionId);
        return transactionId;
    }

    @Override
    public List<CandidateReferenceDTO> getReferencesForCandidate(String candidateId) {
        return webClient.get()
                .uri(serviceProperties.getCandidateUrl() + "/{candidateId}/references", candidateId)
                .retrieve()
                .bodyToFlux(CandidateReferenceDTO.class)
                .collectList()
                .block();
    }

    @Override
    public UUID addCandidateReference(String candidateId, CandidateReferenceCreateDTO reference) {
        reference.setCandidateId(UUID.fromString(candidateId));
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("CREATE")
                .topicName("create-candidate-reference")
                .status("QUEUED")
                .entityType("CANDIDATE_REFERENCE")
                .correlationKey(candidateId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("create-candidate-reference", candidateId, reference, transactionId);
        return transactionId;
    }

    @Override
    public UUID updateCandidateReference(String candidateId, String referenceId, CandidateReferenceDTO reference) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("UPDATE")
                .topicName("update-candidate-reference")
                .status("QUEUED")
                .entityType("CANDIDATE_REFERENCE")
                .correlationKey(candidateId + ":" + referenceId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("update-candidate-reference", candidateId + ":" + referenceId, reference, transactionId);
        return transactionId;
    }

    @Override
    public UUID deleteCandidateReference(String candidateId, String referenceId) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("DELETE")
                .topicName("delete-candidate-reference")
                .status("QUEUED")
                .entityType("CANDIDATE_REFERENCE")
                .correlationKey(candidateId + ":" + referenceId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("delete-candidate-reference", candidateId + ":" + referenceId, null, transactionId);
        return transactionId;
    }

    @Override
    public List<CandidateIdentityDTO> getIdentitiesForCandidate(String candidateId) {
        return webClient.get()
                .uri(serviceProperties.getCandidateUrl() + "/{candidateId}/identities", candidateId)
                .retrieve()
                .bodyToFlux(CandidateIdentityDTO.class)
                .collectList()
                .block();
    }

    @Override
    public UUID addCandidateIdentity(String candidateId, CandidateIdentityCreateDTO identity) {
        identity.setCandidateId(UUID.fromString(candidateId));
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("CREATE")
                .topicName("create-candidate-identity")
                .status("QUEUED")
                .entityType("CANDIDATE_IDENTITY")
                .correlationKey(candidateId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("create-candidate-identity", candidateId, identity, transactionId);
        return transactionId;
    }

    @Override
    public UUID updateCandidateIdentity(String candidateId, String identityId, CandidateIdentityDTO identity) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("UPDATE")
                .topicName("update-candidate-identity")
                .status("QUEUED")
                .entityType("CANDIDATE_IDENTITY")
                .correlationKey(candidateId + ":" + identityId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("update-candidate-identity", candidateId + ":" + identityId, identity, transactionId);
        return transactionId;
    }

    @Override
    public UUID deleteCandidateIdentity(String candidateId, String identityId) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("DELETE")
                .topicName("delete-candidate-identity")
                .status("QUEUED")
                .entityType("CANDIDATE_IDENTITY")
                .correlationKey(candidateId + ":" + identityId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("delete-candidate-identity", candidateId + ":" + identityId, null, transactionId);
        return transactionId;
    }

    @Override
    public List<CandidateDocumentDTO> getDocumentsForCandidate(String candidateId) {
        return webClient.get()
                .uri(serviceProperties.getCandidateUrl() + "/{candidateId}/documents", candidateId)
                .retrieve()
                .bodyToFlux(CandidateDocumentDTO.class)
                .collectList()
                .block();
    }

    @Override
    public UUID addCandidateDocument(String candidateId, CandidateDocumentCreateDTO document) {
        document.setCandidateId(UUID.fromString(candidateId));
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("CREATE")
                .topicName("create-candidate-document")
                .status("QUEUED")
                .entityType("CANDIDATE_DOCUMENT")
                .correlationKey(candidateId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("create-candidate-document", candidateId, document, transactionId);
        return transactionId;
    }

    @Override
    public UUID updateCandidateDocument(String candidateId, String documentId, CandidateDocumentDTO document) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("UPDATE")
                .topicName("update-candidate-document")
                .status("QUEUED")
                .entityType("CANDIDATE_DOCUMENT")
                .correlationKey(candidateId + ":" + documentId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("update-candidate-document", candidateId + ":" + documentId, document, transactionId);
        return transactionId;
    }

    @Override
    public UUID deleteCandidateDocument(String candidateId, String documentId) {
        UUID transactionId = transactionService.generateTransactionId();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(transactionId)
                .serviceName("candidate-service")
                .operationType("DELETE")
                .topicName("delete-candidate-document")
                .status("QUEUED")
                .entityType("CANDIDATE_DOCUMENT")
                .correlationKey(candidateId + ":" + documentId)
                .createdBy(UUID.randomUUID())
                .updatedBy(UUID.randomUUID())
                .build();
        transactionService.createTransaction(transactionDTO);
        publishEvent("delete-candidate-document", candidateId + ":" + documentId, null, transactionId);
        return transactionId;
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

    // Note: Response status methods have been replaced by TransactionService
    // Use TransactionController endpoints for transaction status queries
    @Override
    public TransactionDTO getCandidateResponseStatus(String candidateId) {
        // Dummy implementation for now
        return TransactionDTO.builder().correlationKey(candidateId).build();
    }

    @Override
    public TransactionDTO getCandidateResponseStatusByEmail(String email) {
        // Dummy implementation for now
        return TransactionDTO.builder().correlationKey(email).build();
    }

    @Override
    public List<TransactionDTO> getAllCandidateResponseStatuses() {
        // Dummy implementation for now
        return List.of();
    }

    @Override
    public void deleteCandidateResponseStatus(String candidateId) {
        // This method is deprecated - use TransactionService instead
        log.warn("deleteCandidateResponseStatus is deprecated. Use TransactionService.deleteTransaction() instead.");
    }

    // Helper method for serialization
    private String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
    }
} 