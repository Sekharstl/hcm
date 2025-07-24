package tech.stl.hcm.candidate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.stl.hcm.candidate.service.CandidateCertificationService;
import tech.stl.hcm.common.dto.CandidateCertificationDTO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateCertificationControllerTest {

    @Mock
    private CandidateCertificationService certificationService;

    @InjectMocks
    private CandidateCertificationController certificationController;

    @Test
    void getCertificationsForCandidate_whenCertificationsExist_returnsCertifications() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateCertificationDTO> expectedCertifications = Arrays.asList(
            CandidateCertificationDTO.builder()
                .candidateId(candidateId)
                .certificationId(1)
                .certificationName("AWS Certified Solutions Architect")
                .issuingOrganization("Amazon Web Services")
                .issueDate(LocalDate.of(2022, 3, 15))
                .expiryDate(LocalDate.of(2025, 3, 15))
                .build(),
            CandidateCertificationDTO.builder()
                .candidateId(candidateId)
                .certificationId(2)
                .certificationName("Oracle Certified Professional")
                .issuingOrganization("Oracle")
                .issueDate(LocalDate.of(2021, 6, 20))
                .expiryDate(LocalDate.of(2024, 6, 20))
                .build()
        );
        
        when(certificationService.findAll(any(Specification.class)))
            .thenReturn(expectedCertifications);

        // When
        ResponseEntity<List<CandidateCertificationDTO>> response = 
            certificationController.getCertificationsForCandidate(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCertifications, response.getBody());
        verify(certificationService).findAll(any(Specification.class));
    }

    @Test
    void getCertificationsForCandidate_whenNoCertifications_returnsEmptyList() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateCertificationDTO> emptyCertifications = Collections.emptyList();
        
        when(certificationService.findAll(any(Specification.class)))
            .thenReturn(emptyCertifications);

        // When
        ResponseEntity<List<CandidateCertificationDTO>> response = 
            certificationController.getCertificationsForCandidate(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyCertifications, response.getBody());
        verify(certificationService).findAll(any(Specification.class));
    }

    @Test
    void getCertification_whenCertificationExists_returnsCertification() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer certificationId = 1;
        
        CandidateCertificationDTO expectedCertification = CandidateCertificationDTO.builder()
            .candidateId(candidateId)
            .certificationId(certificationId)
            .certificationName("AWS Certified Solutions Architect")
            .issuingOrganization("Amazon Web Services")
            .issueDate(LocalDate.of(2022, 3, 15))
            .expiryDate(LocalDate.of(2025, 3, 15))
            .build();
        
        when(certificationService.findById(certificationId))
            .thenReturn(Optional.of(expectedCertification));

        // When
        ResponseEntity<CandidateCertificationDTO> response = 
            certificationController.getCertification(candidateId, certificationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCertification, response.getBody());
        verify(certificationService).findById(certificationId);
    }

    @Test
    void getCertification_whenCertificationNotFound_returnsNotFound() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer certificationId = 1;
        
        when(certificationService.findById(certificationId))
            .thenReturn(Optional.empty());

        // When
        ResponseEntity<CandidateCertificationDTO> response = 
            certificationController.getCertification(candidateId, certificationId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(certificationService).findById(certificationId);
    }

    @Test
    void getCertificationsForCandidate_whenServiceThrowsException_shouldPropagate() {
        // Given
        UUID candidateId = UUID.randomUUID();
        when(certificationService.findAll(any(Specification.class)))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            certificationController.getCertificationsForCandidate(candidateId));
    }

    @Test
    void getCertification_whenServiceThrowsException_shouldPropagate() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer certificationId = 1;
        
        when(certificationService.findById(certificationId))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            certificationController.getCertification(candidateId, certificationId));
    }
} 