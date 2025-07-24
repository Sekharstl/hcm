package tech.stl.hcm.candidate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.stl.hcm.candidate.service.CandidateIdentityService;
import tech.stl.hcm.common.dto.CandidateIdentityDTO;
import tech.stl.hcm.common.dto.helpers.CandidateIdentityCreateDTO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateIdentityControllerTest {

    @Mock
    private CandidateIdentityService candidateIdentityService;

    @InjectMocks
    private CandidateIdentityController candidateIdentityController;

    @Test
    void createIdentity_whenSuccessful_returnsCreated() {
        // Given
        CandidateIdentityCreateDTO createDTO = CandidateIdentityCreateDTO.builder()
            .candidateId(UUID.randomUUID())
            .idTypeId(1)
            .idNumber("123456789")
            .issuingCountry("USA")
            .issueDate(LocalDate.of(2020, 1, 15))
            .expiryDate(LocalDate.of(2030, 1, 15))
            .build();
        
        UUID expectedTransactionId = UUID.randomUUID();
        
        when(candidateIdentityService.createCandidateIdentity(createDTO))
            .thenReturn(expectedTransactionId);

        // When
        ResponseEntity<CandidateIdentityController.TransactionResponse> response = 
            candidateIdentityController.createIdentity(createDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedTransactionId, response.getBody().getTransactionId());
        assertEquals("Identity created successfully", response.getBody().getMessage());
        verify(candidateIdentityService).createCandidateIdentity(createDTO);
    }

    @Test
    void createIdentity_whenServiceThrowsException_returnsInternalServerError() {
        // Given
        CandidateIdentityCreateDTO createDTO = CandidateIdentityCreateDTO.builder()
            .candidateId(UUID.randomUUID())
            .idNumber("123456789")
            .build();
        
        when(candidateIdentityService.createCandidateIdentity(createDTO))
            .thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<CandidateIdentityController.TransactionResponse> response = 
            candidateIdentityController.createIdentity(createDTO);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateIdentityService).createCandidateIdentity(createDTO);
    }

    @Test
    void getIdentity_whenFound_returnsIdentity() {
        // Given
        Integer identityId = 1;
        CandidateIdentityDTO expectedIdentity = CandidateIdentityDTO.builder()
            .identityId(identityId)
            .candidateId(UUID.randomUUID())
            .idTypeId(1)
            .idNumber("123456789")
            .issuingCountry("USA")
            .build();
        
        when(candidateIdentityService.getCandidateIdentityById(identityId))
            .thenReturn(expectedIdentity);

        // When
        ResponseEntity<CandidateIdentityDTO> response = 
            candidateIdentityController.getIdentity(identityId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedIdentity, response.getBody());
        verify(candidateIdentityService).getCandidateIdentityById(identityId);
    }

    @Test
    void getIdentity_whenNotFound_returnsNotFound() {
        // Given
        Integer identityId = 1;
        when(candidateIdentityService.getCandidateIdentityById(identityId))
            .thenThrow(new RuntimeException("Identity not found"));

        // When
        ResponseEntity<CandidateIdentityDTO> response = 
            candidateIdentityController.getIdentity(identityId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateIdentityService).getCandidateIdentityById(identityId);
    }

    @Test
    void getCandidateIdentities_whenFound_returnsIdentities() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateIdentityDTO> expectedIdentities = Arrays.asList(
            CandidateIdentityDTO.builder()
                .identityId(1)
                .candidateId(candidateId)
                .idTypeId(1)
                .idNumber("123456789")
                .issuingCountry("USA")
                .build(),
            CandidateIdentityDTO.builder()
                .identityId(2)
                .candidateId(candidateId)
                .idTypeId(2)
                .idNumber("987654321")
                .issuingCountry("Canada")
                .build()
        );
        
        when(candidateIdentityService.getCandidateIdentitiesByCandidateId(candidateId))
            .thenReturn(expectedIdentities);

        // When
        ResponseEntity<List<CandidateIdentityDTO>> response = 
            candidateIdentityController.getCandidateIdentities(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedIdentities, response.getBody());
        verify(candidateIdentityService).getCandidateIdentitiesByCandidateId(candidateId);
    }

    @Test
    void getCandidateIdentities_whenServiceThrowsException_returnsInternalServerError() {
        // Given
        UUID candidateId = UUID.randomUUID();
        when(candidateIdentityService.getCandidateIdentitiesByCandidateId(candidateId))
            .thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<List<CandidateIdentityDTO>> response = 
            candidateIdentityController.getCandidateIdentities(candidateId);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateIdentityService).getCandidateIdentitiesByCandidateId(candidateId);
    }

    @Test
    void getCandidateIdentitiesByType_whenFound_returnsIdentities() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer idTypeId = 1;
        List<CandidateIdentityDTO> expectedIdentities = Arrays.asList(
            CandidateIdentityDTO.builder()
                .identityId(1)
                .candidateId(candidateId)
                .idTypeId(idTypeId)
                .idNumber("123456789")
                .issuingCountry("USA")
                .build()
        );
        
        when(candidateIdentityService.getCandidateIdentitiesByType(candidateId, idTypeId))
            .thenReturn(expectedIdentities);

        // When
        ResponseEntity<List<CandidateIdentityDTO>> response = 
            candidateIdentityController.getCandidateIdentitiesByType(candidateId, idTypeId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedIdentities, response.getBody());
        verify(candidateIdentityService).getCandidateIdentitiesByType(candidateId, idTypeId);
    }

    @Test
    void getCandidateIdentitiesByType_whenServiceThrowsException_returnsInternalServerError() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer idTypeId = 1;
        when(candidateIdentityService.getCandidateIdentitiesByType(candidateId, idTypeId))
            .thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<List<CandidateIdentityDTO>> response = 
            candidateIdentityController.getCandidateIdentitiesByType(candidateId, idTypeId);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateIdentityService).getCandidateIdentitiesByType(candidateId, idTypeId);
    }

    @Test
    void updateIdentity_whenSuccessful_returnsUpdatedIdentity() {
        // Given
        Integer identityId = 1;
        CandidateIdentityDTO updateDTO = CandidateIdentityDTO.builder()
            .identityId(identityId)
            .candidateId(UUID.randomUUID())
            .idTypeId(1)
            .idNumber("123456789")
            .issuingCountry("USA")
            .build();
        
        UUID expectedTransactionId = UUID.randomUUID();
        
        when(candidateIdentityService.updateCandidateIdentity(identityId, updateDTO))
            .thenReturn(expectedTransactionId);

        // When
        ResponseEntity<CandidateIdentityController.TransactionResponse> response = 
            candidateIdentityController.updateIdentity(identityId, updateDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedTransactionId, response.getBody().getTransactionId());
        assertEquals("Identity updated successfully", response.getBody().getMessage());
        verify(candidateIdentityService).updateCandidateIdentity(identityId, updateDTO);
    }

    @Test
    void updateIdentity_whenNotFound_returnsNotFound() {
        // Given
        Integer identityId = 1;
        CandidateIdentityDTO updateDTO = CandidateIdentityDTO.builder().build();
        
        when(candidateIdentityService.updateCandidateIdentity(identityId, updateDTO))
            .thenThrow(new RuntimeException("Identity not found"));

        // When
        ResponseEntity<CandidateIdentityController.TransactionResponse> response = 
            candidateIdentityController.updateIdentity(identityId, updateDTO);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateIdentityService).updateCandidateIdentity(identityId, updateDTO);
    }

    @Test
    void deleteIdentity_whenSuccessful_returnsOk() {
        // Given
        Integer identityId = 1;
        UUID expectedTransactionId = UUID.randomUUID();
        
        when(candidateIdentityService.deleteCandidateIdentity(identityId))
            .thenReturn(expectedTransactionId);

        // When
        ResponseEntity<CandidateIdentityController.TransactionResponse> response = 
            candidateIdentityController.deleteIdentity(identityId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedTransactionId, response.getBody().getTransactionId());
        assertEquals("Identity deleted successfully", response.getBody().getMessage());
        verify(candidateIdentityService).deleteCandidateIdentity(identityId);
    }

    @Test
    void deleteIdentity_whenNotFound_returnsNotFound() {
        // Given
        Integer identityId = 1;
        when(candidateIdentityService.deleteCandidateIdentity(identityId))
            .thenThrow(new RuntimeException("Identity not found"));

        // When
        ResponseEntity<CandidateIdentityController.TransactionResponse> response = 
            candidateIdentityController.deleteIdentity(identityId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateIdentityService).deleteCandidateIdentity(identityId);
    }

    @Test
    void verifyIdentity_whenSuccessful_returnsOk() {
        // Given
        Integer identityId = 1;
        UUID verifiedBy = UUID.randomUUID();
        doNothing().when(candidateIdentityService).verifyIdentity(identityId, verifiedBy);

        // When
        ResponseEntity<Void> response = 
            candidateIdentityController.verifyIdentity(identityId, verifiedBy);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateIdentityService).verifyIdentity(identityId, verifiedBy);
    }

    @Test
    void verifyIdentity_whenNotFound_returnsNotFound() {
        // Given
        Integer identityId = 1;
        UUID verifiedBy = UUID.randomUUID();
        doThrow(new RuntimeException("Identity not found"))
            .when(candidateIdentityService).verifyIdentity(identityId, verifiedBy);

        // When
        ResponseEntity<Void> response = 
            candidateIdentityController.verifyIdentity(identityId, verifiedBy);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateIdentityService).verifyIdentity(identityId, verifiedBy);
    }

    @Test
    void getVerifiedIdentities_whenFound_returnsIdentities() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateIdentityDTO> expectedIdentities = Arrays.asList(
            CandidateIdentityDTO.builder()
                .identityId(1)
                .candidateId(candidateId)
                .idTypeId(1)
                .idNumber("123456789")
                .issuingCountry("USA")
                .isVerified(true)
                .build(),
            CandidateIdentityDTO.builder()
                .identityId(2)
                .candidateId(candidateId)
                .idTypeId(2)
                .idNumber("987654321")
                .issuingCountry("Canada")
                .isVerified(true)
                .build()
        );
        
        when(candidateIdentityService.getVerifiedIdentitiesByCandidateId(candidateId))
            .thenReturn(expectedIdentities);

        // When
        ResponseEntity<List<CandidateIdentityDTO>> response = 
            candidateIdentityController.getVerifiedIdentities(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedIdentities, response.getBody());
        verify(candidateIdentityService).getVerifiedIdentitiesByCandidateId(candidateId);
    }

    @Test
    void getVerifiedIdentities_whenServiceThrowsException_returnsInternalServerError() {
        // Given
        UUID candidateId = UUID.randomUUID();
        when(candidateIdentityService.getVerifiedIdentitiesByCandidateId(candidateId))
            .thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<List<CandidateIdentityDTO>> response = 
            candidateIdentityController.getVerifiedIdentities(candidateId);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateIdentityService).getVerifiedIdentitiesByCandidateId(candidateId);
    }
} 