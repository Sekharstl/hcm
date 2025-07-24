package tech.stl.hcm.candidate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.stl.hcm.candidate.service.CandidateDocumentService;
import tech.stl.hcm.candidate.service.FileStorageService;
import tech.stl.hcm.common.dto.CandidateDocumentDTO;
import tech.stl.hcm.common.dto.helpers.CandidateDocumentCreateDTO;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/candidates/documents")
@RequiredArgsConstructor
public class CandidateDocumentController {

    private final CandidateDocumentService candidateDocumentService;
    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<TransactionResponse> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("candidateId") UUID candidateId,
            @RequestParam("documentTypeId") Integer documentTypeId,
            @RequestParam(value = "expiryDate", required = false) String expiryDate,
            @RequestParam("createdBy") UUID createdBy) {
        
        try {
            // Store file
            String fileName = fileStorageService.storeFile(file, candidateId.toString(), "document");
            
            // Create document record
            CandidateDocumentCreateDTO createDTO = CandidateDocumentCreateDTO.builder()
                    .candidateId(candidateId)
                    .documentTypeId(documentTypeId)
                    .fileName(fileName)
                    .originalFileName(file.getOriginalFilename())
                    .filePath(fileName)
                    .fileSize(file.getSize())
                    .mimeType(file.getContentType())
                    .createdBy(createdBy)
                    .build();
            
            UUID transactionId = candidateDocumentService.createCandidateDocument(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new TransactionResponse(transactionId, "Document uploaded successfully"));
            
        } catch (IOException e) {
            log.error("Error uploading document for candidate: {}", candidateId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<CandidateDocumentDTO> getDocument(@PathVariable Integer documentId) {
        try {
            CandidateDocumentDTO document = candidateDocumentService.getCandidateDocumentById(documentId);
            return ResponseEntity.ok(document);
        } catch (Exception e) {
            log.error("Error fetching document: {}", documentId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<CandidateDocumentDTO>> getCandidateDocuments(@PathVariable UUID candidateId) {
        try {
            List<CandidateDocumentDTO> documents = candidateDocumentService.getCandidateDocumentsByCandidateId(candidateId);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("Error fetching documents for candidate: {}", candidateId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/candidate/{candidateId}/type/{documentTypeId}")
    public ResponseEntity<List<CandidateDocumentDTO>> getCandidateDocumentsByType(
            @PathVariable UUID candidateId,
            @PathVariable Integer documentTypeId) {
        try {
            List<CandidateDocumentDTO> documents = candidateDocumentService.getCandidateDocumentsByType(candidateId, documentTypeId);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("Error fetching documents by type for candidate: {}", candidateId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<TransactionResponse> updateDocument(
            @PathVariable Integer documentId,
            @RequestBody CandidateDocumentDTO updateDTO) {
        try {
            UUID transactionId = candidateDocumentService.updateCandidateDocument(documentId, updateDTO);
            return ResponseEntity.ok(new TransactionResponse(transactionId, "Document updated successfully"));
        } catch (Exception e) {
            log.error("Error updating document: {}", documentId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<TransactionResponse> deleteDocument(@PathVariable Integer documentId) {
        try {
            UUID transactionId = candidateDocumentService.deleteCandidateDocument(documentId);
            return ResponseEntity.ok(new TransactionResponse(transactionId, "Document deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting document: {}", documentId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{documentId}/verify")
    public ResponseEntity<Void> verifyDocument(
            @PathVariable Integer documentId,
            @RequestParam("verifiedBy") UUID verifiedBy) {
        try {
            candidateDocumentService.verifyDocument(documentId, verifiedBy);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error verifying document: {}", documentId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/candidate/{candidateId}/verified")
    public ResponseEntity<List<CandidateDocumentDTO>> getVerifiedDocuments(@PathVariable UUID candidateId) {
        try {
            List<CandidateDocumentDTO> documents = candidateDocumentService.getVerifiedDocumentsByCandidateId(candidateId);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("Error fetching verified documents for candidate: {}", candidateId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<Path> downloadDocument(@PathVariable Integer documentId) {
        try {
            CandidateDocumentDTO document = candidateDocumentService.getCandidateDocumentById(documentId);
            Path filePath = fileStorageService.loadFileAsPath(document.getFilePath());
            return ResponseEntity.ok(filePath);
        } catch (Exception e) {
            log.error("Error downloading document: {}", documentId, e);
            return ResponseEntity.notFound().build();
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