package tech.stl.hcm.core.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.stl.hcm.common.dto.CandidateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.common.dto.helpers.CandidateCreateDTO;
import tech.stl.hcm.core.service.CandidateService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CandidateControllerTest {

    @Mock
    private CandidateService candidateService;

    @InjectMocks
    private CandidateController candidateController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCandidates() {
        PaginatedResponseDTO<CandidateDTO> expected = new PaginatedResponseDTO<>();
        when(candidateService.getAllCandidatesPaginated(0, 20, "candidateId", "ASC")).thenReturn(expected);
        PaginatedResponseDTO<CandidateDTO> result = candidateController.getAllCandidates(0, 20, "candidateId", "ASC");
        assertEquals(expected, result);
    }

    @Test
    void testGetCandidateById() {
        String candidateId = "123";
        CandidateDTO expected = new CandidateDTO();
        when(candidateService.getCandidateById(candidateId)).thenReturn(expected);
        CandidateDTO result = candidateController.getCandidateById(candidateId);
        assertEquals(expected, result);
    }

    @Test
    void testCreateCandidate() {
        CandidateCreateDTO createDTO = new CandidateCreateDTO();
        UUID expectedTransactionId = UUID.randomUUID();
        when(candidateService.createCandidate(createDTO)).thenReturn(expectedTransactionId);
        UUID result = candidateController.createCandidate(createDTO);
        assertEquals(expectedTransactionId, result);
        verify(candidateService, times(1)).createCandidate(createDTO);
    }

    @Test
    void testUpdateCandidate() {
        String candidateId = "123";
        CandidateDTO candidateDTO = new CandidateDTO();
        UUID expectedTransactionId = UUID.randomUUID();
        when(candidateService.updateCandidate(candidateId, candidateDTO)).thenReturn(expectedTransactionId);
        UUID result = candidateController.updateCandidate(candidateId, candidateDTO);
        assertEquals(expectedTransactionId, result);
        verify(candidateService, times(1)).updateCandidate(candidateId, candidateDTO);
    }

    @Test
    void testDeleteCandidate() {
        String candidateId = "123";
        UUID expectedTransactionId = UUID.randomUUID();
        when(candidateService.deleteCandidate(candidateId)).thenReturn(expectedTransactionId);
        UUID result = candidateController.deleteCandidate(candidateId);
        assertEquals(expectedTransactionId, result);
        verify(candidateService, times(1)).deleteCandidate(candidateId);
    }
} 