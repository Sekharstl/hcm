package tech.stl.hcm.candidate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.stl.hcm.candidate.service.CandidateSkillService;
import tech.stl.hcm.common.db.entities.CandidateSkillId;
import tech.stl.hcm.common.dto.CandidateSkillDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateSkillControllerTest {

    @Mock
    private CandidateSkillService skillService;

    @InjectMocks
    private CandidateSkillController skillController;

    @Test
    void getSkillsForCandidate_whenSkillsExist_returnsSkills() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateSkillDTO> expectedSkills = Arrays.asList(
            CandidateSkillDTO.builder()
                .candidateId(candidateId)
                .skillId(1)
                .proficiencyLevel("Expert")
                .build(),
            CandidateSkillDTO.builder()
                .candidateId(candidateId)
                .skillId(2)
                .proficiencyLevel("Advanced")
                .build()
        );
        
        when(skillService.findByCandidateId(candidateId))
            .thenReturn(expectedSkills);

        // When
        ResponseEntity<List<CandidateSkillDTO>> response = 
            skillController.getSkillsForCandidate(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSkills, response.getBody());
        verify(skillService).findByCandidateId(candidateId);
    }

    @Test
    void getSkillsForCandidate_whenNoSkills_returnsEmptyList() {
        // Given
        UUID candidateId = UUID.randomUUID();
        List<CandidateSkillDTO> emptySkills = Collections.emptyList();
        
        when(skillService.findByCandidateId(candidateId))
            .thenReturn(emptySkills);

        // When
        ResponseEntity<List<CandidateSkillDTO>> response = 
            skillController.getSkillsForCandidate(candidateId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptySkills, response.getBody());
        verify(skillService).findByCandidateId(candidateId);
    }

    @Test
    void getSkill_whenSkillExists_returnsSkill() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer skillId = 1;
        CandidateSkillId candidateSkillId = new CandidateSkillId();
        candidateSkillId.setCandidateId(candidateId);
        candidateSkillId.setSkillId(skillId);
        
        CandidateSkillDTO expectedSkill = CandidateSkillDTO.builder()
            .candidateId(candidateId)
            .skillId(skillId)
            .proficiencyLevel("Expert")
            .build();
        
        when(skillService.findById(any(CandidateSkillId.class)))
            .thenReturn(Optional.of(expectedSkill));

        // When
        ResponseEntity<CandidateSkillDTO> response = 
            skillController.getSkill(candidateId, skillId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSkill, response.getBody());
        verify(skillService).findById(any(CandidateSkillId.class));
    }

    @Test
    void getSkill_whenSkillNotFound_returnsNotFound() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer skillId = 1;
        CandidateSkillId candidateSkillId = new CandidateSkillId();
        candidateSkillId.setCandidateId(candidateId);
        candidateSkillId.setSkillId(skillId);
        
        when(skillService.findById(any(CandidateSkillId.class)))
            .thenReturn(Optional.empty());

        // When
        ResponseEntity<CandidateSkillDTO> response = 
            skillController.getSkill(candidateId, skillId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(skillService).findById(any(CandidateSkillId.class));
    }

    @Test
    void getSkillsForCandidate_whenServiceThrowsException_shouldPropagate() {
        // Given
        UUID candidateId = UUID.randomUUID();
        when(skillService.findByCandidateId(candidateId))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            skillController.getSkillsForCandidate(candidateId));
    }

    @Test
    void getSkill_whenServiceThrowsException_shouldPropagate() {
        // Given
        UUID candidateId = UUID.randomUUID();
        Integer skillId = 1;
        CandidateSkillId candidateSkillId = new CandidateSkillId();
        candidateSkillId.setCandidateId(candidateId);
        candidateSkillId.setSkillId(skillId);
        
        when(skillService.findById(any(CandidateSkillId.class)))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            skillController.getSkill(candidateId, skillId));
    }
} 