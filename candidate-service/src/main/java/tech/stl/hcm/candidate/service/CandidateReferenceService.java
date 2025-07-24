package tech.stl.hcm.candidate.service;

import tech.stl.hcm.common.dto.CandidateReferenceDTO;
import tech.stl.hcm.common.dto.helpers.CandidateReferenceCreateDTO;
import java.util.List;
import java.util.UUID;

public interface CandidateReferenceService {
    
    UUID createCandidateReference(CandidateReferenceCreateDTO createDTO);
    
    CandidateReferenceDTO getCandidateReferenceById(Integer referenceId);
    
    List<CandidateReferenceDTO> getCandidateReferencesByCandidateId(UUID candidateId);
    
    UUID updateCandidateReference(Integer referenceId, CandidateReferenceDTO updateDTO);
    
    UUID deleteCandidateReference(Integer referenceId);
    
    void verifyReference(Integer referenceId, UUID verifiedBy);
    
    List<CandidateReferenceDTO> getVerifiedReferencesByCandidateId(UUID candidateId);
} 