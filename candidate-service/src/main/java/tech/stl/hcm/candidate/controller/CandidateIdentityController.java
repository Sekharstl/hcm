package tech.stl.hcm.candidate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.candidate.service.CandidateIdentityService;
import tech.stl.hcm.common.dto.CandidateIdentityDTO;
import tech.stl.hcm.common.dto.helpers.CandidateIdentityCreateDTO;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/candidates/identities")
@RequiredArgsConstructor
public class CandidateIdentityController {

    private final CandidateIdentityService candidateIdentityService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createIdentity(@RequestBody CandidateIdentityCreateDTO createDTO) {
        try {
            UUID transactionId = candidateIdentityService.createCandidateIdentity(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new TransactionResponse(transactionId, "Identity created successfully"));
        } catch (Exception e) {
            log.error("Error creating identity for candidate: {}", createDTO.getCandidateId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{identityId}")
    public ResponseEntity<CandidateIdentityDTO> getIdentity(@PathVariable Integer identityId) {
        try {
            CandidateIdentityDTO identity = candidateIdentityService.getCandidateIdentityById(identityId);
            return ResponseEntity.ok(identity);
        } catch (Exception e) {
            log.error("Error fetching identity: {}", identityId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<CandidateIdentityDTO>> getCandidateIdentities(@PathVariable UUID candidateId) {
        try {
            List<CandidateIdentityDTO> identities = candidateIdentityService.getCandidateIdentitiesByCandidateId(candidateId);
            return ResponseEntity.ok(identities);
        } catch (Exception e) {
            log.error("Error fetching identities for candidate: {}", candidateId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/candidate/{candidateId}/type/{idTypeId}")
    public ResponseEntity<List<CandidateIdentityDTO>> getCandidateIdentitiesByType(
            @PathVariable UUID candidateId,
            @PathVariable Integer idTypeId) {
        try {
            List<CandidateIdentityDTO> identities = candidateIdentityService.getCandidateIdentitiesByType(candidateId, idTypeId);
            return ResponseEntity.ok(identities);
        } catch (Exception e) {
            log.error("Error fetching identities by type for candidate: {}", candidateId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{identityId}")
    public ResponseEntity<TransactionResponse> updateIdentity(
            @PathVariable Integer identityId,
            @RequestBody CandidateIdentityDTO updateDTO) {
        try {
            UUID transactionId = candidateIdentityService.updateCandidateIdentity(identityId, updateDTO);
            return ResponseEntity.ok(new TransactionResponse(transactionId, "Identity updated successfully"));
        } catch (Exception e) {
            log.error("Error updating identity: {}", identityId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{identityId}")
    public ResponseEntity<TransactionResponse> deleteIdentity(@PathVariable Integer identityId) {
        try {
            UUID transactionId = candidateIdentityService.deleteCandidateIdentity(identityId);
            return ResponseEntity.ok(new TransactionResponse(transactionId, "Identity deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting identity: {}", identityId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{identityId}/verify")
    public ResponseEntity<Void> verifyIdentity(
            @PathVariable Integer identityId,
            @RequestParam("verifiedBy") UUID verifiedBy) {
        try {
            candidateIdentityService.verifyIdentity(identityId, verifiedBy);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error verifying identity: {}", identityId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/candidate/{candidateId}/verified")
    public ResponseEntity<List<CandidateIdentityDTO>> getVerifiedIdentities(@PathVariable UUID candidateId) {
        try {
            List<CandidateIdentityDTO> identities = candidateIdentityService.getVerifiedIdentitiesByCandidateId(candidateId);
            return ResponseEntity.ok(identities);
        } catch (Exception e) {
            log.error("Error fetching verified identities for candidate: {}", candidateId, e);
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