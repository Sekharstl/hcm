package tech.stl.hcm.candidate.service;

import tech.stl.hcm.common.dto.CandidateDocumentDTO;
import tech.stl.hcm.common.dto.helpers.CandidateDocumentCreateDTO;
import java.util.List;
import java.util.UUID;

public interface CandidateDocumentService {
    
    UUID createCandidateDocument(CandidateDocumentCreateDTO createDTO);
    
    CandidateDocumentDTO getCandidateDocumentById(Integer documentId);
    
    List<CandidateDocumentDTO> getCandidateDocumentsByCandidateId(UUID candidateId);
    
    List<CandidateDocumentDTO> getCandidateDocumentsByType(UUID candidateId, Integer documentTypeId);
    
    UUID updateCandidateDocument(Integer documentId, CandidateDocumentDTO updateDTO);
    
    UUID deleteCandidateDocument(Integer documentId);
    
    void verifyDocument(Integer documentId, UUID verifiedBy);
    
    List<CandidateDocumentDTO> getVerifiedDocumentsByCandidateId(UUID candidateId);
} 