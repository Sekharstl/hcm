package tech.stl.hcm.candidate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.stl.hcm.candidate.service.CandidateWorkHistoryService;
import tech.stl.hcm.common.dto.CandidateWorkHistoryDTO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateWorkHistoryControllerTest {

    @Mock
    private CandidateWorkHistoryService workHistoryService;

    @InjectMocks
    private CandidateWorkHistoryController workHistoryController;

    @Test
    void getWorkHistoriesForCandidate_whenWorkHistoriesExist_returnsWorkHistories() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateWorkHistoryDTO> expectedWorkHistories = Arrays.asList(
            CandidateWorkHistoryDTO.builder()
                .candidateId(candidateId)
                .workHistoryId(1)
                .companyName("Tech Corp")
                .jobTitle("Software Engineer")
                .startDate(LocalDate.of(2020, 1, 15))
                .endDate(LocalDate.of(2022, 6, 30))
                .build(),
            CandidateWorkHistoryDTO.builder()
                .candidateId(candidateId)
                .workHistoryId(2)
                .companyName("Innovation Inc")
                .jobTitle("Senior Developer")
                .startDate(LocalDate.of(2022, 7, 1))
                .endDate(null) // Current position
                .build()
        );
        
        when(workHistoryService.findByCandidateId(candidateId))
            .thenReturn(expectedWorkHistories);

        // When
        ResponseEntity<List<CandidateWorkHistoryDTO>> response = 
            workHistoryController.getWorkHistoriesForCandidate(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedWorkHistories, response.getBody());
        verify(workHistoryService).findByCandidateId(candidateId);
    }

    @Test
    void getWorkHistoriesForCandidate_whenNoWorkHistories_returnsEmptyList() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateWorkHistoryDTO> emptyWorkHistories = Collections.emptyList();
        
        when(workHistoryService.findByCandidateId(candidateId))
            .thenReturn(emptyWorkHistories);

        // When
        ResponseEntity<List<CandidateWorkHistoryDTO>> response = 
            workHistoryController.getWorkHistoriesForCandidate(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyWorkHistories, response.getBody());
        verify(workHistoryService).findByCandidateId(candidateId);
    }

    @Test
    void getWorkHistory_whenWorkHistoryExists_returnsWorkHistory() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer workHistoryId = 1;
        
        CandidateWorkHistoryDTO expectedWorkHistory = CandidateWorkHistoryDTO.builder()
            .candidateId(candidateId)
            .workHistoryId(workHistoryId)
            .companyName("Tech Corp")
            .jobTitle("Software Engineer")
            .startDate(LocalDate.of(2020, 1, 15))
            .endDate(LocalDate.of(2022, 6, 30))
            .build();
        
        when(workHistoryService.findById(workHistoryId))
            .thenReturn(Optional.of(expectedWorkHistory));

        // When
        ResponseEntity<CandidateWorkHistoryDTO> response = 
            workHistoryController.getWorkHistory(candidateId, workHistoryId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedWorkHistory, response.getBody());
        verify(workHistoryService).findById(workHistoryId);
    }

    @Test
    void getWorkHistory_whenWorkHistoryNotFound_returnsNotFound() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer workHistoryId = 1;
        
        when(workHistoryService.findById(workHistoryId))
            .thenReturn(Optional.empty());

        // When
        ResponseEntity<CandidateWorkHistoryDTO> response = 
            workHistoryController.getWorkHistory(candidateId, workHistoryId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(workHistoryService).findById(workHistoryId);
    }

    @Test
    void getWorkHistoriesForCandidate_whenServiceThrowsException_shouldPropagate() {
        // Given
        UUID candidateId = UUID.randomUUID();
        when(workHistoryService.findByCandidateId(candidateId))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            workHistoryController.getWorkHistoriesForCandidate(candidateId));
    }

    @Test
    void getWorkHistory_whenServiceThrowsException_shouldPropagate() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer workHistoryId = 1;
        
        when(workHistoryService.findById(workHistoryId))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            workHistoryController.getWorkHistory(candidateId, workHistoryId));
    }
} 