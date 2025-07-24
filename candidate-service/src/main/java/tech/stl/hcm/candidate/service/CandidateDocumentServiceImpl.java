package tech.stl.hcm.candidate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.stl.hcm.common.db.entities.CandidateDocument;
import tech.stl.hcm.common.db.repositories.CandidateDocumentRepository;
import tech.stl.hcm.common.dto.CandidateDocumentDTO;
import tech.stl.hcm.common.dto.helpers.CandidateDocumentCreateDTO;
import tech.stl.hcm.common.mapper.CandidateDocumentMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidateDocumentServiceImpl implements CandidateDocumentService {

    private final CandidateDocumentRepository candidateDocumentRepository;
    private final CandidateDocumentMapper candidateDocumentMapper;

    @Override
    @Transactional
    public UUID createCandidateDocument(CandidateDocumentCreateDTO createDTO) {
        // Generate transaction ID
        UUID transactionId = UUID.randomUUID();
        
        try {
            CandidateDocument entity = CandidateDocument.builder()
                    .candidateId(createDTO.getCandidateId())
                    .documentTypeId(createDTO.getDocumentTypeId())
                    .fileName(createDTO.getFileName())
                    .originalFileName(createDTO.getOriginalFileName())
                    .filePath(createDTO.getFilePath())
                    .fileSize(createDTO.getFileSize())
                    .mimeType(createDTO.getMimeType())
                    .isVerified(false)
                    .createdAt(Instant.now())
                    .createdBy(UUID.randomUUID()) // TODO: Get from security context
                    .updatedAt(Instant.now())
                    .updatedBy(UUID.randomUUID()) // TODO: Get from security context
                    .build();
            
            CandidateDocument savedDocument = candidateDocumentRepository.save(entity);
            log.info("Created candidate document with transaction ID: {} and document ID: {}", transactionId, savedDocument.getDocumentId());
            return transactionId;
        } catch (Exception e) {
            log.error("Error creating candidate document with transaction ID: {}: {}", transactionId, e.getMessage(), e);
            throw new RuntimeException("Failed to create candidate document", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateDocumentDTO getCandidateDocumentById(Integer documentId) {
        return candidateDocumentRepository.findById(documentId)
                .map(candidateDocumentMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Candidate document not found with ID: " + documentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateDocumentDTO> getCandidateDocumentsByCandidateId(UUID candidateId) {
        return candidateDocumentRepository.findByCandidateId(candidateId).stream()
                .map(candidateDocumentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateDocumentDTO> getCandidateDocumentsByType(UUID candidateId, Integer documentTypeId) {
        return candidateDocumentRepository.findByCandidateIdAndDocumentTypeId(candidateId, documentTypeId).stream()
                .map(candidateDocumentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UUID updateCandidateDocument(Integer documentId, CandidateDocumentDTO updateDTO) {
        // Generate transaction ID
        UUID transactionId = UUID.randomUUID();
        
        try {
            CandidateDocument entity = candidateDocumentRepository.findById(documentId)
                    .orElseThrow(() -> new EntityNotFoundException("Candidate document not found with ID: " + documentId));
            
            // Update fields manually since there's no updateEntity method
            if (updateDTO.getDocumentTypeId() != null) {
                entity.setDocumentTypeId(updateDTO.getDocumentTypeId());
            }
            if (updateDTO.getFileName() != null) {
                entity.setFileName(updateDTO.getFileName());
            }
            if (updateDTO.getOriginalFileName() != null) {
                entity.setOriginalFileName(updateDTO.getOriginalFileName());
            }
            if (updateDTO.getFilePath() != null) {
                entity.setFilePath(updateDTO.getFilePath());
            }
            if (updateDTO.getFileSize() != null) {
                entity.setFileSize(updateDTO.getFileSize());
            }
            if (updateDTO.getMimeType() != null) {
                entity.setMimeType(updateDTO.getMimeType());
            }
            if (updateDTO.getIsVerified() != null) {
                entity.setIsVerified(updateDTO.getIsVerified());
            }
            if (updateDTO.getVerificationDate() != null) {
                entity.setVerificationDate(updateDTO.getVerificationDate());
            }
            if (updateDTO.getVerifiedBy() != null) {
                entity.setVerifiedBy(updateDTO.getVerifiedBy());
            }
            
            entity.setUpdatedAt(Instant.now());
            entity.setUpdatedBy(UUID.randomUUID()); // TODO: Get from security context
            
            candidateDocumentRepository.save(entity);
            log.info("Updated candidate document with transaction ID: {} and document ID: {}", transactionId, documentId);
            return transactionId;
        } catch (Exception e) {
            log.error("Error updating candidate document with transaction ID: {}: {}", transactionId, e.getMessage(), e);
            throw new RuntimeException("Failed to update candidate document", e);
        }
    }

    @Override
    @Transactional
    public UUID deleteCandidateDocument(Integer documentId) {
        // Generate transaction ID
        UUID transactionId = UUID.randomUUID();
        
        try {
            if (!candidateDocumentRepository.existsById(documentId)) {
                throw new EntityNotFoundException("Candidate document not found with ID: " + documentId);
            }
            
            candidateDocumentRepository.deleteById(documentId);
            log.info("Deleted candidate document with transaction ID: {} and document ID: {}", transactionId, documentId);
            return transactionId;
        } catch (Exception e) {
            log.error("Error deleting candidate document with transaction ID: {}: {}", transactionId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete candidate document", e);
        }
    }

    @Override
    @Transactional
    public void verifyDocument(Integer documentId, UUID verifiedBy) {
        CandidateDocument entity = candidateDocumentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate document not found with ID: " + documentId));
        
        entity.setIsVerified(true);
        entity.setVerifiedBy(verifiedBy);
        entity.setVerificationDate(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(verifiedBy);
        
        candidateDocumentRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateDocumentDTO> getVerifiedDocumentsByCandidateId(UUID candidateId) {
        return candidateDocumentRepository.findVerifiedByCandidateId(candidateId).stream()
                .map(candidateDocumentMapper::toDTO)
                .collect(Collectors.toList());
    }
} 