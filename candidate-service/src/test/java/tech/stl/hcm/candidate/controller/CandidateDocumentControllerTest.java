package tech.stl.hcm.candidate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import tech.stl.hcm.candidate.service.CandidateDocumentService;
import tech.stl.hcm.candidate.service.FileStorageService;
import tech.stl.hcm.common.dto.CandidateDocumentDTO;
import tech.stl.hcm.common.dto.helpers.CandidateDocumentCreateDTO;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateDocumentControllerTest {

    @Mock
    private CandidateDocumentService candidateDocumentService;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private CandidateDocumentController candidateDocumentController;

    @Test
    void uploadDocument_whenSuccessful_returnsCreated() throws IOException {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer documentTypeId = 1;
        UUID createdBy = UUID.randomUUID();
        
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "resume.pdf", 
            "application/pdf", 
            "test content".getBytes()
        );
        
        String storedFileName = "candidate_" + candidateId + "_document_resume.pdf";
        
        when(fileStorageService.storeFile(file, candidateId.toString(), "document"))
            .thenReturn(storedFileName);
        
        UUID expectedTransactionId = UUID.randomUUID();
        
        when(candidateDocumentService.createCandidateDocument(any(CandidateDocumentCreateDTO.class)))
            .thenReturn(expectedTransactionId);

        // When
        ResponseEntity<CandidateDocumentController.TransactionResponse> response = 
            candidateDocumentController.uploadDocument(file, candidateId, documentTypeId, null, createdBy);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedTransactionId, response.getBody().getTransactionId());
        assertEquals("Document uploaded successfully", response.getBody().getMessage());
        verify(fileStorageService).storeFile(file, candidateId.toString(), "document");
        verify(candidateDocumentService).createCandidateDocument(any(CandidateDocumentCreateDTO.class));
    }

    @Test
    void uploadDocument_whenFileStorageFails_returnsInternalServerError() throws IOException {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer documentTypeId = 1;
        UUID createdBy = UUID.randomUUID();
        
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "resume.pdf", 
            "application/pdf", 
            "test content".getBytes()
        );
        
        when(fileStorageService.storeFile(file, candidateId.toString(), "document"))
            .thenThrow(new IOException("Storage error"));

        // When
        ResponseEntity<CandidateDocumentController.TransactionResponse> response = 
            candidateDocumentController.uploadDocument(file, candidateId, documentTypeId, null, createdBy);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(fileStorageService).storeFile(file, candidateId.toString(), "document");
        verify(candidateDocumentService, never()).createCandidateDocument(any());
    }

    @Test
    void getDocument_whenFound_returnsDocument() {
        // Given
        Integer documentId = 1;
        CandidateDocumentDTO expectedDocument = CandidateDocumentDTO.builder()
            .documentId(documentId)
            .candidateId(UUID.randomUUID())
            .documentTypeId(1)
            .fileName("resume.pdf")
            .originalFileName("resume.pdf")
            .filePath("uploads/candidate/resume.pdf")
            .fileSize(1024L)
            .mimeType("application/pdf")
            .build();
        
        when(candidateDocumentService.getCandidateDocumentById(documentId))
            .thenReturn(expectedDocument);

        // When
        ResponseEntity<CandidateDocumentDTO> response = 
            candidateDocumentController.getDocument(documentId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDocument, response.getBody());
        verify(candidateDocumentService).getCandidateDocumentById(documentId);
    }

    @Test
    void getDocument_whenNotFound_returnsNotFound() {
        // Given
        Integer documentId = 1;
        when(candidateDocumentService.getCandidateDocumentById(documentId))
            .thenThrow(new RuntimeException("Document not found"));

        // When
        ResponseEntity<CandidateDocumentDTO> response = 
            candidateDocumentController.getDocument(documentId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateDocumentService).getCandidateDocumentById(documentId);
    }

    @Test
    void getCandidateDocuments_whenFound_returnsDocuments() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateDocumentDTO> expectedDocuments = Arrays.asList(
            CandidateDocumentDTO.builder()
                .documentId(1)
                .candidateId(candidateId)
                .documentTypeId(1)
                .fileName("resume.pdf")
                .originalFileName("resume.pdf")
                .build(),
            CandidateDocumentDTO.builder()
                .documentId(2)
                .candidateId(candidateId)
                .documentTypeId(2)
                .fileName("cover_letter.pdf")
                .originalFileName("cover_letter.pdf")
                .build()
        );
        
        when(candidateDocumentService.getCandidateDocumentsByCandidateId(candidateId))
            .thenReturn(expectedDocuments);

        // When
        ResponseEntity<List<CandidateDocumentDTO>> response = 
            candidateDocumentController.getCandidateDocuments(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDocuments, response.getBody());
        verify(candidateDocumentService).getCandidateDocumentsByCandidateId(candidateId);
    }

    @Test
    void getCandidateDocuments_whenServiceThrowsException_returnsInternalServerError() {
        // Given
        UUID candidateId = UUID.randomUUID();
        when(candidateDocumentService.getCandidateDocumentsByCandidateId(candidateId))
            .thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<List<CandidateDocumentDTO>> response = 
            candidateDocumentController.getCandidateDocuments(candidateId);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateDocumentService).getCandidateDocumentsByCandidateId(candidateId);
    }

    @Test
    void getCandidateDocumentsByType_whenFound_returnsDocuments() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer documentTypeId = 1;
        List<CandidateDocumentDTO> expectedDocuments = Arrays.asList(
            CandidateDocumentDTO.builder()
                .documentId(1)
                .candidateId(candidateId)
                .documentTypeId(documentTypeId)
                .fileName("resume.pdf")
                .originalFileName("resume.pdf")
                .build()
        );
        
        when(candidateDocumentService.getCandidateDocumentsByType(candidateId, documentTypeId))
            .thenReturn(expectedDocuments);

        // When
        ResponseEntity<List<CandidateDocumentDTO>> response = 
            candidateDocumentController.getCandidateDocumentsByType(candidateId, documentTypeId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDocuments, response.getBody());
        verify(candidateDocumentService).getCandidateDocumentsByType(candidateId, documentTypeId);
    }

    @Test
    void getCandidateDocumentsByType_whenServiceThrowsException_returnsInternalServerError() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer documentTypeId = 1;
        when(candidateDocumentService.getCandidateDocumentsByType(candidateId, documentTypeId))
            .thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<List<CandidateDocumentDTO>> response = 
            candidateDocumentController.getCandidateDocumentsByType(candidateId, documentTypeId);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateDocumentService).getCandidateDocumentsByType(candidateId, documentTypeId);
    }

    @Test
    void updateDocument_whenSuccessful_returnsUpdatedDocument() {
        // Given
        Integer documentId = 1;
        CandidateDocumentDTO updateDTO = CandidateDocumentDTO.builder()
            .documentId(documentId)
            .candidateId(UUID.randomUUID())
            .documentTypeId(1)
            .fileName("updated_resume.pdf")
            .originalFileName("updated_resume.pdf")
            .build();
        
        UUID expectedTransactionId = UUID.randomUUID();
        
        when(candidateDocumentService.updateCandidateDocument(documentId, updateDTO))
            .thenReturn(expectedTransactionId);

        // When
        ResponseEntity<CandidateDocumentController.TransactionResponse> response = 
            candidateDocumentController.updateDocument(documentId, updateDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedTransactionId, response.getBody().getTransactionId());
        assertEquals("Document updated successfully", response.getBody().getMessage());
        verify(candidateDocumentService).updateCandidateDocument(documentId, updateDTO);
    }

    @Test
    void updateDocument_whenNotFound_returnsNotFound() {
        // Given
        Integer documentId = 1;
        CandidateDocumentDTO updateDTO = CandidateDocumentDTO.builder().build();
        
        when(candidateDocumentService.updateCandidateDocument(documentId, updateDTO))
            .thenThrow(new RuntimeException("Document not found"));

        // When
        ResponseEntity<CandidateDocumentController.TransactionResponse> response = 
            candidateDocumentController.updateDocument(documentId, updateDTO);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateDocumentService).updateCandidateDocument(documentId, updateDTO);
    }

    @Test
    void deleteDocument_whenSuccessful_returnsOk() {
        // Given
        Integer documentId = 1;
        UUID expectedTransactionId = UUID.randomUUID();
        
        when(candidateDocumentService.deleteCandidateDocument(documentId))
            .thenReturn(expectedTransactionId);

        // When
        ResponseEntity<CandidateDocumentController.TransactionResponse> response = 
            candidateDocumentController.deleteDocument(documentId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedTransactionId, response.getBody().getTransactionId());
        assertEquals("Document deleted successfully", response.getBody().getMessage());
        verify(candidateDocumentService).deleteCandidateDocument(documentId);
    }

    @Test
    void deleteDocument_whenNotFound_returnsNotFound() {
        // Given
        Integer documentId = 1;
        when(candidateDocumentService.deleteCandidateDocument(documentId))
            .thenThrow(new RuntimeException("Document not found"));

        // When
        ResponseEntity<CandidateDocumentController.TransactionResponse> response = 
            candidateDocumentController.deleteDocument(documentId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateDocumentService).deleteCandidateDocument(documentId);
    }

    @Test
    void verifyDocument_whenSuccessful_returnsOk() {
        // Given
        Integer documentId = 1;
        UUID verifiedBy = UUID.randomUUID();
        doNothing().when(candidateDocumentService).verifyDocument(documentId, verifiedBy);

        // When
        ResponseEntity<Void> response = 
            candidateDocumentController.verifyDocument(documentId, verifiedBy);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateDocumentService).verifyDocument(documentId, verifiedBy);
    }

    @Test
    void verifyDocument_whenNotFound_returnsNotFound() {
        // Given
        Integer documentId = 1;
        UUID verifiedBy = UUID.randomUUID();
        doThrow(new RuntimeException("Document not found"))
            .when(candidateDocumentService).verifyDocument(documentId, verifiedBy);

        // When
        ResponseEntity<Void> response = 
            candidateDocumentController.verifyDocument(documentId, verifiedBy);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateDocumentService).verifyDocument(documentId, verifiedBy);
    }

    @Test
    void getVerifiedDocuments_whenFound_returnsDocuments() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateDocumentDTO> expectedDocuments = Arrays.asList(
            CandidateDocumentDTO.builder()
                .documentId(1)
                .candidateId(candidateId)
                .documentTypeId(1)
                .fileName("resume.pdf")
                .originalFileName("resume.pdf")
                .isVerified(true)
                .build(),
            CandidateDocumentDTO.builder()
                .documentId(2)
                .candidateId(candidateId)
                .documentTypeId(2)
                .fileName("cover_letter.pdf")
                .originalFileName("cover_letter.pdf")
                .isVerified(true)
                .build()
        );
        
        when(candidateDocumentService.getVerifiedDocumentsByCandidateId(candidateId))
            .thenReturn(expectedDocuments);

        // When
        ResponseEntity<List<CandidateDocumentDTO>> response = 
            candidateDocumentController.getVerifiedDocuments(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDocuments, response.getBody());
        verify(candidateDocumentService).getVerifiedDocumentsByCandidateId(candidateId);
    }

    @Test
    void getVerifiedDocuments_whenServiceThrowsException_returnsInternalServerError() {
        // Given
        UUID candidateId = UUID.randomUUID();
        when(candidateDocumentService.getVerifiedDocumentsByCandidateId(candidateId))
            .thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<List<CandidateDocumentDTO>> response = 
            candidateDocumentController.getVerifiedDocuments(candidateId);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateDocumentService).getVerifiedDocumentsByCandidateId(candidateId);
    }

    @Test
    void downloadDocument_whenSuccessful_returnsFilePath() {
        // Given
        Integer documentId = 1;
        CandidateDocumentDTO document = CandidateDocumentDTO.builder()
            .documentId(documentId)
            .candidateId(UUID.randomUUID())
            .fileName("resume.pdf")
            .filePath("uploads/candidate/resume.pdf")
            .build();
        
        Path expectedPath = Paths.get("uploads/candidate/resume.pdf");
        
        when(candidateDocumentService.getCandidateDocumentById(documentId))
            .thenReturn(document);
        when(fileStorageService.loadFileAsPath(document.getFilePath()))
            .thenReturn(expectedPath);

        // When
        ResponseEntity<Path> response = 
            candidateDocumentController.downloadDocument(documentId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPath, response.getBody());
        verify(candidateDocumentService).getCandidateDocumentById(documentId);
        verify(fileStorageService).loadFileAsPath(document.getFilePath());
    }

    @Test
    void downloadDocument_whenDocumentNotFound_returnsNotFound() {
        // Given
        Integer documentId = 1;
        when(candidateDocumentService.getCandidateDocumentById(documentId))
            .thenThrow(new RuntimeException("Document not found"));

        // When
        ResponseEntity<Path> response = 
            candidateDocumentController.downloadDocument(documentId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateDocumentService).getCandidateDocumentById(documentId);
        verify(fileStorageService, never()).loadFileAsPath(any());
    }
} 