package tech.stl.hcm.candidate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.stl.hcm.common.db.entities.CandidateReference;
import tech.stl.hcm.common.db.repositories.CandidateReferenceRepository;
import tech.stl.hcm.common.dto.CandidateReferenceDTO;
import tech.stl.hcm.common.dto.helpers.CandidateReferenceCreateDTO;
import tech.stl.hcm.common.mapper.CandidateReferenceMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidateReferenceServiceImpl implements CandidateReferenceService {

    private final CandidateReferenceRepository candidateReferenceRepository;
    private final CandidateReferenceMapper candidateReferenceMapper;

    @Override
    @Transactional
    public UUID createCandidateReference(CandidateReferenceCreateDTO createDTO) {
        // Generate transaction ID
        UUID transactionId = UUID.randomUUID();
        
        try {
            CandidateReference entity = CandidateReference.builder()
                    .candidateId(createDTO.getCandidateId())
                    .referenceName(createDTO.getReferenceName())
                    .relationship(createDTO.getRelationship())
                    .company(createDTO.getCompany())
                    .position(createDTO.getPosition())
                    .email(createDTO.getEmail())
                    .phone(createDTO.getPhone())
                    .isVerified(false)
                    .createdAt(Instant.now())
                    .createdBy(UUID.randomUUID()) // TODO: Get from security context
                    .updatedAt(Instant.now())
                    .updatedBy(UUID.randomUUID()) // TODO: Get from security context
                    .build();
            
            CandidateReference savedReference = candidateReferenceRepository.save(entity);
            log.info("Created candidate reference with transaction ID: {} and reference ID: {}", transactionId, savedReference.getReferenceId());
            return transactionId;
        } catch (Exception e) {
            log.error("Error creating candidate reference with transaction ID: {}: {}", transactionId, e.getMessage(), e);
            throw new RuntimeException("Failed to create candidate reference", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateReferenceDTO getCandidateReferenceById(Integer referenceId) {
        return candidateReferenceRepository.findById(referenceId)
                .map(candidateReferenceMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Candidate reference not found with ID: " + referenceId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateReferenceDTO> getCandidateReferencesByCandidateId(UUID candidateId) {
        return candidateReferenceRepository.findByCandidateId(candidateId).stream()
                .map(candidateReferenceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UUID updateCandidateReference(Integer referenceId, CandidateReferenceDTO updateDTO) {
        // Generate transaction ID
        UUID transactionId = UUID.randomUUID();
        
        try {
            CandidateReference entity = candidateReferenceRepository.findById(referenceId)
                    .orElseThrow(() -> new EntityNotFoundException("Candidate reference not found with ID: " + referenceId));
            
            // Update fields manually since there's no updateEntity method
            if (updateDTO.getReferenceName() != null) {
                entity.setReferenceName(updateDTO.getReferenceName());
            }
            if (updateDTO.getRelationship() != null) {
                entity.setRelationship(updateDTO.getRelationship());
            }
            if (updateDTO.getCompany() != null) {
                entity.setCompany(updateDTO.getCompany());
            }
            if (updateDTO.getPosition() != null) {
                entity.setPosition(updateDTO.getPosition());
            }
            if (updateDTO.getEmail() != null) {
                entity.setEmail(updateDTO.getEmail());
            }
            if (updateDTO.getPhone() != null) {
                entity.setPhone(updateDTO.getPhone());
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
            
            candidateReferenceRepository.save(entity);
            log.info("Updated candidate reference with transaction ID: {} and reference ID: {}", transactionId, referenceId);
            return transactionId;
        } catch (Exception e) {
            log.error("Error updating candidate reference with transaction ID: {}: {}", transactionId, e.getMessage(), e);
            throw new RuntimeException("Failed to update candidate reference", e);
        }
    }

    @Override
    @Transactional
    public UUID deleteCandidateReference(Integer referenceId) {
        // Generate transaction ID
        UUID transactionId = UUID.randomUUID();
        
        try {
            if (!candidateReferenceRepository.existsById(referenceId)) {
                throw new EntityNotFoundException("Candidate reference not found with ID: " + referenceId);
            }
            
            candidateReferenceRepository.deleteById(referenceId);
            log.info("Deleted candidate reference with transaction ID: {} and reference ID: {}", transactionId, referenceId);
            return transactionId;
        } catch (Exception e) {
            log.error("Error deleting candidate reference with transaction ID: {}: {}", transactionId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete candidate reference", e);
        }
    }

    @Override
    @Transactional
    public void verifyReference(Integer referenceId, UUID verifiedBy) {
        CandidateReference entity = candidateReferenceRepository.findById(referenceId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate reference not found with ID: " + referenceId));
        
        entity.setIsVerified(true);
        entity.setVerifiedBy(verifiedBy);
        entity.setVerificationDate(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(verifiedBy);
        
        candidateReferenceRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateReferenceDTO> getVerifiedReferencesByCandidateId(UUID candidateId) {
        return candidateReferenceRepository.findVerifiedByCandidateId(candidateId).stream()
                .map(candidateReferenceMapper::toDTO)
                .collect(Collectors.toList());
    }
} 