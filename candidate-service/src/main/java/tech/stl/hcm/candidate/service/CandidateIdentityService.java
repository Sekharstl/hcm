package tech.stl.hcm.candidate.service;

import tech.stl.hcm.common.dto.CandidateIdentityDTO;
import tech.stl.hcm.common.dto.helpers.CandidateIdentityCreateDTO;
import java.util.List;
import java.util.UUID;

public interface CandidateIdentityService {
    
    UUID createCandidateIdentity(CandidateIdentityCreateDTO createDTO);
    
    CandidateIdentityDTO getCandidateIdentityById(Integer identityId);
    
    List<CandidateIdentityDTO> getCandidateIdentitiesByCandidateId(UUID candidateId);
    
    List<CandidateIdentityDTO> getCandidateIdentitiesByType(UUID candidateId, Integer idTypeId);
    
    UUID updateCandidateIdentity(Integer identityId, CandidateIdentityDTO updateDTO);
    
    UUID deleteCandidateIdentity(Integer identityId);
    
    void verifyIdentity(Integer identityId, UUID verifiedBy);
    
    List<CandidateIdentityDTO> getVerifiedIdentitiesByCandidateId(UUID candidateId);
} 