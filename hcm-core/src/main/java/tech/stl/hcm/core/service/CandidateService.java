package tech.stl.hcm.core.service;

import tech.stl.hcm.common.dto.*;
import tech.stl.hcm.common.dto.helpers.CandidateCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateSkillCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateEducationCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateWorkHistoryCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateCertificationCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateReferenceCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateIdentityCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateDocumentCreateDTO;
import tech.stl.hcm.common.dto.helpers.TransactionDTO;
import java.util.List;
import java.util.UUID;

public interface CandidateService {
    List<CandidateDTO> getAllCandidates();
    PaginatedResponseDTO<CandidateDTO> getAllCandidatesPaginated(int page, int size, String sortBy, String sortDirection);
    CandidateDTO getCandidateById(String candidateId);
    List<CandidateSkillDTO> getSkillsForCandidate(String candidateId);
    List<CandidateEducationDTO> getEducationsForCandidate(String candidateId);
    List<CandidateWorkHistoryDTO> getWorkHistoriesForCandidate(String candidateId);
    List<CandidateCertificationDTO> getCertificationsForCandidate(String candidateId);
    List<CandidateReferenceDTO> getReferencesForCandidate(String candidateId);
    List<CandidateIdentityDTO> getIdentitiesForCandidate(String candidateId);
    List<CandidateDocumentDTO> getDocumentsForCandidate(String candidateId);
    
    UUID createCandidate(CandidateCreateDTO candidate);
    UUID updateCandidate(String candidateId, CandidateDTO candidate);
    UUID deleteCandidate(String candidateId);
    
    UUID addCandidateSkill(String candidateId, CandidateSkillCreateDTO skill);
    UUID updateCandidateSkill(String candidateId, String skillId, CandidateSkillDTO skill);
    UUID deleteCandidateSkill(String candidateId, String skillId);
    
    UUID addCandidateEducation(String candidateId, CandidateEducationCreateDTO education);
    UUID updateCandidateEducation(String candidateId, String educationId, CandidateEducationDTO education);
    UUID deleteCandidateEducation(String candidateId, String educationId);
    
    UUID addCandidateWorkHistory(String candidateId, CandidateWorkHistoryCreateDTO workHistory);
    UUID updateCandidateWorkHistory(String candidateId, String workHistoryId, CandidateWorkHistoryDTO workHistory);
    UUID deleteCandidateWorkHistory(String candidateId, String workHistoryId);
    
    UUID addCandidateCertification(String candidateId, CandidateCertificationCreateDTO certification);
    UUID updateCandidateCertification(String candidateId, String certificationId, CandidateCertificationDTO certification);
    UUID deleteCandidateCertification(String candidateId, String certificationId);
    
    UUID addCandidateReference(String candidateId, CandidateReferenceCreateDTO reference);
    UUID updateCandidateReference(String candidateId, String referenceId, CandidateReferenceDTO reference);
    UUID deleteCandidateReference(String candidateId, String referenceId);
    
    UUID addCandidateIdentity(String candidateId, CandidateIdentityCreateDTO identity);
    UUID updateCandidateIdentity(String candidateId, String identityId, CandidateIdentityDTO identity);
    UUID deleteCandidateIdentity(String candidateId, String identityId);
    
    UUID addCandidateDocument(String candidateId, CandidateDocumentCreateDTO document);
    UUID updateCandidateDocument(String candidateId, String documentId, CandidateDocumentDTO document);
    UUID deleteCandidateDocument(String candidateId, String documentId);
    
    // Note: Response status methods are deprecated
    // Use TransactionService for transaction status queries instead
    @Deprecated
    TransactionDTO getCandidateResponseStatus(String candidateId);
    @Deprecated
    TransactionDTO getCandidateResponseStatusByEmail(String email);
    @Deprecated
    List<TransactionDTO> getAllCandidateResponseStatuses();
    @Deprecated
    void deleteCandidateResponseStatus(String candidateId);
} 