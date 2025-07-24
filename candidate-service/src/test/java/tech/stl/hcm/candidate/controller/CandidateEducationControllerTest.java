package tech.stl.hcm.candidate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.stl.hcm.candidate.service.CandidateEducationService;
import tech.stl.hcm.common.dto.CandidateEducationDTO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateEducationControllerTest {

    @Mock
    private CandidateEducationService educationService;

    @InjectMocks
    private CandidateEducationController educationController;

    @Test
    void getEducationsForCandidate_whenEducationsExist_returnsEducations() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateEducationDTO> expectedEducations = Arrays.asList(
            CandidateEducationDTO.builder()
                .candidateId(candidateId)
                .educationId(1)
                .institutionName("University of Technology")
                .degree("Bachelor of Science")
                .fieldOfStudy("Computer Science")
                .startDate(LocalDate.of(2018, 9, 1))
                .endDate(LocalDate.of(2022, 5, 15))
                .build(),
            CandidateEducationDTO.builder()
                .candidateId(candidateId)
                .educationId(2)
                .institutionName("Tech Institute")
                .degree("Master of Science")
                .fieldOfStudy("Software Engineering")
                .startDate(LocalDate.of(2022, 9, 1))
                .endDate(LocalDate.of(2024, 5, 15))
                .build()
        );
        
        when(educationService.findByCandidateId(candidateId))
            .thenReturn(expectedEducations);

        // When
        ResponseEntity<List<CandidateEducationDTO>> response = 
            educationController.getEducationsForCandidate(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedEducations, response.getBody());
        verify(educationService).findByCandidateId(candidateId);
    }

    @Test
    void getEducationsForCandidate_whenNoEducations_returnsEmptyList() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateEducationDTO> emptyEducations = Collections.emptyList();
        
        when(educationService.findByCandidateId(candidateId))
            .thenReturn(emptyEducations);

        // When
        ResponseEntity<List<CandidateEducationDTO>> response = 
            educationController.getEducationsForCandidate(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyEducations, response.getBody());
        verify(educationService).findByCandidateId(candidateId);
    }

    @Test
    void getEducation_whenEducationExists_returnsEducation() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer educationId = 1;
        
        CandidateEducationDTO expectedEducation = CandidateEducationDTO.builder()
            .candidateId(candidateId)
            .educationId(educationId)
            .institutionName("University of Technology")
            .degree("Bachelor of Science")
            .fieldOfStudy("Computer Science")
            .startDate(LocalDate.of(2018, 9, 1))
            .endDate(LocalDate.of(2022, 5, 15))
            .build();
        
        when(educationService.findById(educationId))
            .thenReturn(Optional.of(expectedEducation));

        // When
        ResponseEntity<CandidateEducationDTO> response = 
            educationController.getEducation(candidateId, educationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedEducation, response.getBody());
        verify(educationService).findById(educationId);
    }

    @Test
    void getEducation_whenEducationNotFound_returnsNotFound() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer educationId = 1;
        
        when(educationService.findById(educationId))
            .thenReturn(Optional.empty());

        // When
        ResponseEntity<CandidateEducationDTO> response = 
            educationController.getEducation(candidateId, educationId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(educationService).findById(educationId);
    }

    @Test
    void getEducationsForCandidate_whenServiceThrowsException_shouldPropagate() {
        // Given
        UUID candidateId = UUID.randomUUID();
        when(educationService.findByCandidateId(candidateId))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            educationController.getEducationsForCandidate(candidateId));
    }

    @Test
    void getEducation_whenServiceThrowsException_shouldPropagate() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer educationId = 1;
        
        when(educationService.findById(educationId))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            educationController.getEducation(candidateId, educationId));
    }
} 