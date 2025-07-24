package tech.stl.hcm.core.controller;

import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.common.dto.*;
import tech.stl.hcm.common.dto.helpers.CandidateCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateSkillCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateEducationCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateWorkHistoryCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateCertificationCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateReferenceCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateIdentityCreateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateDocumentCreateDTO;
import tech.stl.hcm.core.service.CandidateService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    // Main CRUD GET/PUT/DELETE for candidate
    @GetMapping
    public PaginatedResponseDTO<CandidateDTO> getAllCandidates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "candidateId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return candidateService.getAllCandidatesPaginated(page, size, sortBy, sortDirection);
    }

    @GetMapping("/{candidateId}")
    public CandidateDTO getCandidateById(@PathVariable String candidateId) {
        return candidateService.getCandidateById(candidateId);
    }

    @GetMapping("/{candidateId}/skills")
    public List<CandidateSkillDTO> getSkillsForCandidate(@PathVariable String candidateId) {
        return candidateService.getSkillsForCandidate(candidateId);
    }

    @GetMapping("/{candidateId}/educations")
    public List<CandidateEducationDTO> getEducationsForCandidate(@PathVariable String candidateId) {
        return candidateService.getEducationsForCandidate(candidateId);
    }

    @GetMapping("/{candidateId}/work-histories")
    public List<CandidateWorkHistoryDTO> getWorkHistoriesForCandidate(@PathVariable String candidateId) {
        return candidateService.getWorkHistoriesForCandidate(candidateId);
    }

    @GetMapping("/{candidateId}/certifications")
    public List<CandidateCertificationDTO> getCertificationsForCandidate(@PathVariable String candidateId) {
        return candidateService.getCertificationsForCandidate(candidateId);
    }

    @GetMapping("/{candidateId}/references")
    public List<CandidateReferenceDTO> getReferencesForCandidate(@PathVariable String candidateId) {
        return candidateService.getReferencesForCandidate(candidateId);
    }

    @GetMapping("/{candidateId}/identities")
    public List<CandidateIdentityDTO> getIdentitiesForCandidate(@PathVariable String candidateId) {
        return candidateService.getIdentitiesForCandidate(candidateId);
    }

    @GetMapping("/{candidateId}/documents")
    public List<CandidateDocumentDTO> getDocumentsForCandidate(@PathVariable String candidateId) {
        return candidateService.getDocumentsForCandidate(candidateId);
    }

    @PutMapping("/{candidateId}")
    public UUID updateCandidate(@PathVariable String candidateId, @RequestBody CandidateDTO candidate) {
        return candidateService.updateCandidate(candidateId, candidate);
    }

    @DeleteMapping("/{candidateId}")
    public UUID deleteCandidate(@PathVariable String candidateId) {
        return candidateService.deleteCandidate(candidateId);
    }

    // All @PostMapping (create) endpoints grouped together
    @PostMapping
    public UUID createCandidate(@RequestBody CandidateCreateDTO candidate) {
        return candidateService.createCandidate(candidate);
    }

    @PostMapping("/{candidateId}/educations")
    public UUID addCandidateEducation(@PathVariable String candidateId, @RequestBody CandidateEducationCreateDTO education) {
        return candidateService.addCandidateEducation(candidateId, education);
    }

    @PostMapping("/{candidateId}/work-histories")
    public UUID addCandidateWorkHistory(@PathVariable String candidateId, @RequestBody CandidateWorkHistoryCreateDTO workHistory) {
        return candidateService.addCandidateWorkHistory(candidateId, workHistory);
    }

    @PostMapping("/{candidateId}/certifications")
    public UUID addCandidateCertification(@PathVariable String candidateId, @RequestBody CandidateCertificationCreateDTO certification) {
        return candidateService.addCandidateCertification(candidateId, certification);
    }

    @PostMapping("/{candidateId}/skills")
    public UUID addCandidateSkill(@PathVariable String candidateId, @RequestBody CandidateSkillCreateDTO skill) {
        return candidateService.addCandidateSkill(candidateId, skill);
    }

    @PostMapping("/{candidateId}/references")
    public UUID addCandidateReference(@PathVariable String candidateId, @RequestBody CandidateReferenceCreateDTO reference) {
        return candidateService.addCandidateReference(candidateId, reference);
    }

    @PostMapping("/{candidateId}/identities")
    public UUID addCandidateIdentity(@PathVariable String candidateId, @RequestBody CandidateIdentityCreateDTO identity) {
        return candidateService.addCandidateIdentity(candidateId, identity);
    }

    @PostMapping("/{candidateId}/documents")
    public UUID addCandidateDocument(@PathVariable String candidateId, @RequestBody CandidateDocumentCreateDTO document) {
        return candidateService.addCandidateDocument(candidateId, document);
    }

    // All @PutMapping (update) endpoints for sub-resources
    @PutMapping("/{candidateId}/educations/{educationId}")
    public UUID updateCandidateEducation(@PathVariable String candidateId, @PathVariable String educationId, @RequestBody CandidateEducationDTO education) {
        return candidateService.updateCandidateEducation(candidateId, educationId, education);
    }

    @PutMapping("/{candidateId}/work-histories/{workHistoryId}")
    public UUID updateCandidateWorkHistory(@PathVariable String candidateId, @PathVariable String workHistoryId, @RequestBody CandidateWorkHistoryDTO workHistory) {
        return candidateService.updateCandidateWorkHistory(candidateId, workHistoryId, workHistory);
    }

    @PutMapping("/{candidateId}/certifications/{certificationId}")
    public UUID updateCandidateCertification(@PathVariable String candidateId, @PathVariable String certificationId, @RequestBody CandidateCertificationDTO certification) {
        return candidateService.updateCandidateCertification(candidateId, certificationId, certification);
    }

    @PutMapping("/{candidateId}/skills/{skillId}")
    public UUID updateCandidateSkill(@PathVariable String candidateId, @PathVariable String skillId, @RequestBody CandidateSkillDTO skill) {
        return candidateService.updateCandidateSkill(candidateId, skillId, skill);
    }

    @PutMapping("/{candidateId}/references/{referenceId}")
    public UUID updateCandidateReference(@PathVariable String candidateId, @PathVariable String referenceId, @RequestBody CandidateReferenceDTO reference) {
        return candidateService.updateCandidateReference(candidateId, referenceId, reference);
    }

    @PutMapping("/{candidateId}/identities/{identityId}")
    public UUID updateCandidateIdentity(@PathVariable String candidateId, @PathVariable String identityId, @RequestBody CandidateIdentityDTO identity) {
        return candidateService.updateCandidateIdentity(candidateId, identityId, identity);
    }

    @PutMapping("/{candidateId}/documents/{documentId}")
    public UUID updateCandidateDocument(@PathVariable String candidateId, @PathVariable String documentId, @RequestBody CandidateDocumentDTO document) {
        return candidateService.updateCandidateDocument(candidateId, documentId, document);
    }

    // All @DeleteMapping (delete) endpoints for sub-resources
    @DeleteMapping("/{candidateId}/educations/{educationId}")
    public UUID deleteCandidateEducation(@PathVariable String candidateId, @PathVariable String educationId) {
        return candidateService.deleteCandidateEducation(candidateId, educationId);
    }

    @DeleteMapping("/{candidateId}/work-histories/{workHistoryId}")
    public UUID deleteCandidateWorkHistory(@PathVariable String candidateId, @PathVariable String workHistoryId) {
        return candidateService.deleteCandidateWorkHistory(candidateId, workHistoryId);
    }

    @DeleteMapping("/{candidateId}/certifications/{certificationId}")
    public UUID deleteCandidateCertification(@PathVariable String candidateId, @PathVariable String certificationId) {
        return candidateService.deleteCandidateCertification(candidateId, certificationId);
    }

    @DeleteMapping("/{candidateId}/skills/{skillId}")
    public UUID deleteCandidateSkill(@PathVariable String candidateId, @PathVariable String skillId) {
        return candidateService.deleteCandidateSkill(candidateId, skillId);
    }

    @DeleteMapping("/{candidateId}/references/{referenceId}")
    public UUID deleteCandidateReference(@PathVariable String candidateId, @PathVariable String referenceId) {
        return candidateService.deleteCandidateReference(candidateId, referenceId);
    }

    @DeleteMapping("/{candidateId}/identities/{identityId}")
    public UUID deleteCandidateIdentity(@PathVariable String candidateId, @PathVariable String identityId) {
        return candidateService.deleteCandidateIdentity(candidateId, identityId);
    }

    @DeleteMapping("/{candidateId}/documents/{documentId}")
    public UUID deleteCandidateDocument(@PathVariable String candidateId, @PathVariable String documentId) {
        return candidateService.deleteCandidateDocument(candidateId, documentId);
    }
} 