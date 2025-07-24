package tech.stl.hcm.candidate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.stl.hcm.common.db.entities.CandidateIdentity;
import tech.stl.hcm.common.db.repositories.CandidateIdentityRepository;
import tech.stl.hcm.common.dto.CandidateIdentityDTO;
import tech.stl.hcm.common.dto.helpers.CandidateIdentityCreateDTO;
import tech.stl.hcm.common.mapper.CandidateIdentityMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidateIdentityServiceImpl implements CandidateIdentityService {

    private final CandidateIdentityRepository candidateIdentityRepository;
    private final CandidateIdentityMapper candidateIdentityMapper;

    @Override
    @Transactional
    public UUID createCandidateIdentity(CandidateIdentityCreateDTO createDTO) {
        // Generate transaction ID
        UUID transactionId = UUID.randomUUID();
        
        try {
            CandidateIdentity entity = CandidateIdentity.builder()
                    .candidateId(createDTO.getCandidateId())
                    .idTypeId(createDTO.getIdTypeId())
                    .idNumber(createDTO.getIdNumber())
                    .issuingCountry(createDTO.getIssuingCountry())
                    .issueDate(createDTO.getIssueDate())
                    .expiryDate(createDTO.getExpiryDate())
                    .isVerified(false)
                    .createdAt(Instant.now())
                    .createdBy(UUID.randomUUID()) // TODO: Get from security context
                    .updatedAt(Instant.now())
                    .updatedBy(UUID.randomUUID()) // TODO: Get from security context
                    .build();
            
            CandidateIdentity savedIdentity = candidateIdentityRepository.save(entity);
            log.info("Created candidate identity with transaction ID: {} and identity ID: {}", transactionId, savedIdentity.getIdentityId());
            return transactionId;
        } catch (Exception e) {
            log.error("Error creating candidate identity with transaction ID: {}: {}", transactionId, e.getMessage(), e);
            throw new RuntimeException("Failed to create candidate identity", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateIdentityDTO getCandidateIdentityById(Integer identityId) {
        return candidateIdentityRepository.findById(identityId)
                .map(candidateIdentityMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Candidate identity not found with ID: " + identityId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateIdentityDTO> getCandidateIdentitiesByCandidateId(UUID candidateId) {
        return candidateIdentityRepository.findByCandidateId(candidateId).stream()
                .map(candidateIdentityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateIdentityDTO> getCandidateIdentitiesByType(UUID candidateId, Integer idTypeId) {
        return candidateIdentityRepository.findByCandidateIdAndIdTypeId(candidateId, idTypeId).stream()
                .map(candidateIdentityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UUID updateCandidateIdentity(Integer identityId, CandidateIdentityDTO updateDTO) {
        // Generate transaction ID
        UUID transactionId = UUID.randomUUID();
        
        try {
            CandidateIdentity entity = candidateIdentityRepository.findById(identityId)
                    .orElseThrow(() -> new EntityNotFoundException("Candidate identity not found with ID: " + identityId));
            
            // Update fields manually since there's no updateEntity method
            if (updateDTO.getIdTypeId() != null) {
                entity.setIdTypeId(updateDTO.getIdTypeId());
            }
            if (updateDTO.getIdNumber() != null) {
                entity.setIdNumber(updateDTO.getIdNumber());
            }
            if (updateDTO.getIssuingCountry() != null) {
                entity.setIssuingCountry(updateDTO.getIssuingCountry());
            }
            if (updateDTO.getIssueDate() != null) {
                entity.setIssueDate(updateDTO.getIssueDate());
            }
            if (updateDTO.getExpiryDate() != null) {
                entity.setExpiryDate(updateDTO.getExpiryDate());
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
            
            candidateIdentityRepository.save(entity);
            log.info("Updated candidate identity with transaction ID: {} and identity ID: {}", transactionId, identityId);
            return transactionId;
        } catch (Exception e) {
            log.error("Error updating candidate identity with transaction ID: {}: {}", transactionId, e.getMessage(), e);
            throw new RuntimeException("Failed to update candidate identity", e);
        }
    }

    @Override
    @Transactional
    public UUID deleteCandidateIdentity(Integer identityId) {
        // Generate transaction ID
        UUID transactionId = UUID.randomUUID();
        
        try {
            if (!candidateIdentityRepository.existsById(identityId)) {
                throw new EntityNotFoundException("Candidate identity not found with ID: " + identityId);
            }
            
            candidateIdentityRepository.deleteById(identityId);
            log.info("Deleted candidate identity with transaction ID: {} and identity ID: {}", transactionId, identityId);
            return transactionId;
        } catch (Exception e) {
            log.error("Error deleting candidate identity with transaction ID: {}: {}", transactionId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete candidate identity", e);
        }
    }

    @Override
    @Transactional
    public void verifyIdentity(Integer identityId, UUID verifiedBy) {
        CandidateIdentity entity = candidateIdentityRepository.findById(identityId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate identity not found with ID: " + identityId));
        
        entity.setIsVerified(true);
        entity.setVerifiedBy(verifiedBy);
        entity.setVerificationDate(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(verifiedBy);
        
        candidateIdentityRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateIdentityDTO> getVerifiedIdentitiesByCandidateId(UUID candidateId) {
        return candidateIdentityRepository.findVerifiedByCandidateId(candidateId).stream()
                .map(candidateIdentityMapper::toDTO)
                .collect(Collectors.toList());
    }
} 