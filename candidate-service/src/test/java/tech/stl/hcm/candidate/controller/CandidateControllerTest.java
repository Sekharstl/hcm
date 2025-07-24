package tech.stl.hcm.candidate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.stl.hcm.candidate.service.CandidateService;
import tech.stl.hcm.common.dto.CandidateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateCreateDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CandidateControllerTest {

    @Mock
    private CandidateService candidateService;

    @InjectMocks
    private CandidateController candidateController;

    @Test
    void getAllCandidates_withPagination_delegatesToService() {
        // Given
        int page = 0;
        int size = 10;
        String sortBy = "firstName";
        String sortDirection = "ASC";
        
        Pageable pageable = PageRequest.of(page, size);
        List<CandidateDTO> candidates = Arrays.asList(
            CandidateDTO.builder()
                .candidateId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build(),
            CandidateDTO.builder()
                .candidateId(UUID.randomUUID())
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build()
        );
        Page<CandidateDTO> pageResponse = new PageImpl<>(candidates, pageable, 2);
        
        when(candidateService.findAllPaginated(eq(null), any(Pageable.class)))
            .thenReturn(pageResponse);

        // When
        ResponseEntity<Page<CandidateDTO>> response = 
            candidateController.getAllCandidates(page, size, sortBy, sortDirection);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageResponse, response.getBody());
        verify(candidateService).findAllPaginated(eq(null), any(Pageable.class));
    }

    @Test
    void getAllCandidates_withDefaultPagination_delegatesToService() {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        List<CandidateDTO> candidates = Collections.emptyList();
        Page<CandidateDTO> pageResponse = new PageImpl<>(candidates, pageable, 0);
        
        when(candidateService.findAllPaginated(eq(null), any(Pageable.class)))
            .thenReturn(pageResponse);

        // When
        ResponseEntity<Page<CandidateDTO>> response = 
            candidateController.getAllCandidates(0, 20, "candidateId", "ASC");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageResponse, response.getBody());
        verify(candidateService).findAllPaginated(eq(null), any(Pageable.class));
    }

    @Test
    void getCandidateById_whenFound_returnsCandidate() {
        // Given
        UUID candidateId = UUID.randomUUID();
        CandidateDTO expectedCandidate = CandidateDTO.builder()
            .candidateId(candidateId)
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .build();
        
        when(candidateService.findByCandidateId(candidateId))
            .thenReturn(Optional.of(expectedCandidate));

        // When
        ResponseEntity<CandidateDTO> response = candidateController.getCandidateById(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCandidate, response.getBody());
        verify(candidateService).findByCandidateId(candidateId);
    }

    @Test
    void getCandidateById_whenNotFound_returnsNotFound() {
        // Given
        UUID candidateId = UUID.randomUUID();
        when(candidateService.findByCandidateId(candidateId))
            .thenReturn(Optional.empty());

        // When
        ResponseEntity<CandidateDTO> response = candidateController.getCandidateById(candidateId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(candidateService).findByCandidateId(candidateId);
    }

    @Test
    void getCandidateById_whenServiceThrowsException_shouldPropagate() {
        // Given
        UUID candidateId = UUID.randomUUID();
        when(candidateService.findByCandidateId(candidateId))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            candidateController.getCandidateById(candidateId));
    }

    @Test
    void getAllCandidates_whenServiceThrowsException_shouldPropagate() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        when(candidateService.findAllPaginated(eq(null), any(Pageable.class)))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            candidateController.getAllCandidates(0, 10, "firstName", "ASC"));
    }
} 