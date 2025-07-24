package tech.stl.hcm.candidate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.stl.hcm.candidate.service.CandidateReferenceService;
import tech.stl.hcm.common.dto.CandidateReferenceDTO;
import tech.stl.hcm.common.dto.helpers.CandidateReferenceCreateDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateReferenceControllerTest {

    @Mock
    private CandidateReferenceService candidateReferenceService;

    @InjectMocks
    private CandidateReferenceController candidateReferenceController;

    @Test
    void createReference_whenSuccessful_returnsCreated() {
        // Given
        CandidateReferenceCreateDTO createDTO = CandidateReferenceCreateDTO.builder()
            .candidateId(UUID.randomUUID())
            .referenceName("John Smith")
            .email("john.smith@example.com")
            .phone("+1234567890")
            .relationship("Former Manager")
            .build();
        
        UUID expectedTransactionId = UUID.randomUUID();
        
        when(candidateReferenceService.createCandidateReference(createDTO))
            .thenReturn(expectedTransactionId);

        // When
        ResponseEntity<CandidateReferenceController.TransactionResponse> response = 
            candidateReferenceController.createReference(createDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedTransactionId, response.getBody().getTransactionId());
        assertEquals("Reference created successfully", response.getBody().getMessage());
        verify(candidateReferenceService).createCandidateReference(createDTO);
    }

    @Test
    void createReference_whenServiceThrowsException_returnsInternalServerError() {
        // Given
        CandidateReferenceCreateDTO createDTO = CandidateReferenceCreateDTO.builder()
            .candidateId(UUID.randomUUID())
            .referenceName("John Smith")
            .build();
        
        when(candidateReferenceService.createCandidateReference(createDTO))
            .thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<CandidateReferenceController.TransactionResponse> response = 
            candidateReferenceController.createReference(createDTO);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateReferenceService).createCandidateReference(createDTO);
    }

    @Test
    void getReference_whenFound_returnsReference() {
        // Given
        Integer referenceId = 1;
        CandidateReferenceDTO expectedReference = CandidateReferenceDTO.builder()
            .referenceId(referenceId)
            .candidateId(UUID.randomUUID())
            .referenceName("John Smith")
            .email("john.smith@example.com")
            .build();
        
        when(candidateReferenceService.getCandidateReferenceById(referenceId))
            .thenReturn(expectedReference);

        // When
        ResponseEntity<CandidateReferenceDTO> response = 
            candidateReferenceController.getReference(referenceId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedReference, response.getBody());
        verify(candidateReferenceService).getCandidateReferenceById(referenceId);
    }

    @Test
    void getReference_whenNotFound_returnsNotFound() {
        // Given
        Integer referenceId = 1;
        when(candidateReferenceService.getCandidateReferenceById(referenceId))
            .thenThrow(new RuntimeException("Reference not found"));

        // When
        ResponseEntity<CandidateReferenceDTO> response = 
            candidateReferenceController.getReference(referenceId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateReferenceService).getCandidateReferenceById(referenceId);
    }

    @Test
    void getCandidateReferences_whenFound_returnsReferences() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateReferenceDTO> expectedReferences = Arrays.asList(
            CandidateReferenceDTO.builder()
                .referenceId(1)
                .candidateId(candidateId)
                .referenceName("John Smith")
                .email("john.smith@example.com")
                .build(),
            CandidateReferenceDTO.builder()
                .referenceId(2)
                .candidateId(candidateId)
                .referenceName("Jane Doe")
                .email("jane.doe@example.com")
                .build()
        );
        
        when(candidateReferenceService.getCandidateReferencesByCandidateId(candidateId))
            .thenReturn(expectedReferences);

        // When
        ResponseEntity<List<CandidateReferenceDTO>> response = 
            candidateReferenceController.getCandidateReferences(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedReferences, response.getBody());
        verify(candidateReferenceService).getCandidateReferencesByCandidateId(candidateId);
    }

    @Test
    void getCandidateReferences_whenServiceThrowsException_returnsInternalServerError() {
        // Given
        UUID candidateId = UUID.randomUUID();
        when(candidateReferenceService.getCandidateReferencesByCandidateId(candidateId))
            .thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<List<CandidateReferenceDTO>> response = 
            candidateReferenceController.getCandidateReferences(candidateId);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateReferenceService).getCandidateReferencesByCandidateId(candidateId);
    }

    @Test
    void updateReference_whenSuccessful_returnsUpdatedReference() {
        // Given
        Integer referenceId = 1;
        CandidateReferenceDTO updateDTO = CandidateReferenceDTO.builder()
            .referenceId(referenceId)
            .candidateId(UUID.randomUUID())
            .referenceName("John Smith Updated")
            .email("john.updated@example.com")
            .build();
        
        UUID expectedTransactionId = UUID.randomUUID();
        
        when(candidateReferenceService.updateCandidateReference(referenceId, updateDTO))
            .thenReturn(expectedTransactionId);

        // When
        ResponseEntity<CandidateReferenceController.TransactionResponse> response = 
            candidateReferenceController.updateReference(referenceId, updateDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedTransactionId, response.getBody().getTransactionId());
        assertEquals("Reference updated successfully", response.getBody().getMessage());
        verify(candidateReferenceService).updateCandidateReference(referenceId, updateDTO);
    }

    @Test
    void updateReference_whenNotFound_returnsNotFound() {
        // Given
        Integer referenceId = 1;
        CandidateReferenceDTO updateDTO = CandidateReferenceDTO.builder().build();
        
        when(candidateReferenceService.updateCandidateReference(referenceId, updateDTO))
            .thenThrow(new RuntimeException("Reference not found"));

        // When
        ResponseEntity<CandidateReferenceController.TransactionResponse> response = 
            candidateReferenceController.updateReference(referenceId, updateDTO);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateReferenceService).updateCandidateReference(referenceId, updateDTO);
    }

    @Test
    void deleteReference_whenSuccessful_returnsOk() {
        // Given
        Integer referenceId = 1;
        UUID expectedTransactionId = UUID.randomUUID();
        
        when(candidateReferenceService.deleteCandidateReference(referenceId))
            .thenReturn(expectedTransactionId);

        // When
        ResponseEntity<CandidateReferenceController.TransactionResponse> response = 
            candidateReferenceController.deleteReference(referenceId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedTransactionId, response.getBody().getTransactionId());
        assertEquals("Reference deleted successfully", response.getBody().getMessage());
        verify(candidateReferenceService).deleteCandidateReference(referenceId);
    }

    @Test
    void deleteReference_whenNotFound_returnsNotFound() {
        // Given
        Integer referenceId = 1;
        when(candidateReferenceService.deleteCandidateReference(referenceId))
            .thenThrow(new RuntimeException("Reference not found"));

        // When
        ResponseEntity<CandidateReferenceController.TransactionResponse> response = 
            candidateReferenceController.deleteReference(referenceId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateReferenceService).deleteCandidateReference(referenceId);
    }

    @Test
    void verifyReference_whenSuccessful_returnsOk() {
        // Given
        Integer referenceId = 1;
        UUID verifiedBy = UUID.randomUUID();
        doNothing().when(candidateReferenceService).verifyReference(referenceId, verifiedBy);

        // When
        ResponseEntity<Void> response = 
            candidateReferenceController.verifyReference(referenceId, verifiedBy);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateReferenceService).verifyReference(referenceId, verifiedBy);
    }

    @Test
    void verifyReference_whenNotFound_returnsNotFound() {
        // Given
        Integer referenceId = 1;
        UUID verifiedBy = UUID.randomUUID();
        doThrow(new RuntimeException("Reference not found"))
            .when(candidateReferenceService).verifyReference(referenceId, verifiedBy);

        // When
        ResponseEntity<Void> response = 
            candidateReferenceController.verifyReference(referenceId, verifiedBy);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateReferenceService).verifyReference(referenceId, verifiedBy);
    }

    @Test
    void getVerifiedReferences_whenFound_returnsReferences() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateReferenceDTO> expectedReferences = Arrays.asList(
            CandidateReferenceDTO.builder()
                .referenceId(1)
                .candidateId(candidateId)
                .referenceName("John Smith")
                .email("john.smith@example.com")
                .isVerified(true)
                .build(),
            CandidateReferenceDTO.builder()
                .referenceId(2)
                .candidateId(candidateId)
                .referenceName("Jane Doe")
                .email("jane.doe@example.com")
                .isVerified(true)
                .build()
        );
        
        when(candidateReferenceService.getVerifiedReferencesByCandidateId(candidateId))
            .thenReturn(expectedReferences);

        // When
        ResponseEntity<List<CandidateReferenceDTO>> response = 
            candidateReferenceController.getVerifiedReferences(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedReferences, response.getBody());
        verify(candidateReferenceService).getVerifiedReferencesByCandidateId(candidateId);
    }

    @Test
    void getVerifiedReferences_whenServiceThrowsException_returnsInternalServerError() {
        // Given
        UUID candidateId = UUID.randomUUID();
        when(candidateReferenceService.getVerifiedReferencesByCandidateId(candidateId))
            .thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<List<CandidateReferenceDTO>> response = 
            candidateReferenceController.getVerifiedReferences(candidateId);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateReferenceService).getVerifiedReferencesByCandidateId(candidateId);
    }
} 