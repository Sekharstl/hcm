package tech.stl.hcm.candidate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.candidate.service.CandidateReferenceService;
import tech.stl.hcm.common.dto.CandidateReferenceDTO;
import tech.stl.hcm.common.dto.helpers.CandidateReferenceCreateDTO;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/candidates/references")
@RequiredArgsConstructor
public class CandidateReferenceController {

    private final CandidateReferenceService candidateReferenceService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createReference(@RequestBody CandidateReferenceCreateDTO createDTO) {
        try {
            UUID transactionId = candidateReferenceService.createCandidateReference(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new TransactionResponse(transactionId, "Reference created successfully"));
        } catch (Exception e) {
            log.error("Error creating reference for candidate: {}", createDTO.getCandidateId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{referenceId}")
    public ResponseEntity<CandidateReferenceDTO> getReference(@PathVariable Integer referenceId) {
        try {
            CandidateReferenceDTO reference = candidateReferenceService.getCandidateReferenceById(referenceId);
            return ResponseEntity.ok(reference);
        } catch (Exception e) {
            log.error("Error fetching reference: {}", referenceId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<CandidateReferenceDTO>> getCandidateReferences(@PathVariable UUID candidateId) {
        try {
            List<CandidateReferenceDTO> references = candidateReferenceService.getCandidateReferencesByCandidateId(candidateId);
            return ResponseEntity.ok(references);
        } catch (Exception e) {
            log.error("Error fetching references for candidate: {}", candidateId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{referenceId}")
    public ResponseEntity<TransactionResponse> updateReference(
            @PathVariable Integer referenceId,
            @RequestBody CandidateReferenceDTO updateDTO) {
        try {
            UUID transactionId = candidateReferenceService.updateCandidateReference(referenceId, updateDTO);
            return ResponseEntity.ok(new TransactionResponse(transactionId, "Reference updated successfully"));
        } catch (Exception e) {
            log.error("Error updating reference: {}", referenceId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{referenceId}")
    public ResponseEntity<TransactionResponse> deleteReference(@PathVariable Integer referenceId) {
        try {
            UUID transactionId = candidateReferenceService.deleteCandidateReference(referenceId);
            return ResponseEntity.ok(new TransactionResponse(transactionId, "Reference deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting reference: {}", referenceId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{referenceId}/verify")
    public ResponseEntity<Void> verifyReference(
            @PathVariable Integer referenceId,
            @RequestParam("verifiedBy") UUID verifiedBy) {
        try {
            candidateReferenceService.verifyReference(referenceId, verifiedBy);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error verifying reference: {}", referenceId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/candidate/{candidateId}/verified")
    public ResponseEntity<List<CandidateReferenceDTO>> getVerifiedReferences(@PathVariable UUID candidateId) {
        try {
            List<CandidateReferenceDTO> references = candidateReferenceService.getVerifiedReferencesByCandidateId(candidateId);
            return ResponseEntity.ok(references);
        } catch (Exception e) {
            log.error("Error fetching verified references for candidate: {}", candidateId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Inner class for transaction response
    public static class TransactionResponse {
        private UUID transactionId;
        private String message;

        public TransactionResponse(UUID transactionId, String message) {
            this.transactionId = transactionId;
            this.message = message;
        }

        public UUID getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(UUID transactionId) {
            this.transactionId = transactionId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
} 