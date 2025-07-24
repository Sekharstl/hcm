package tech.stl.hcm.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import tech.stl.hcm.core.config.ServiceProperties;
import tech.stl.hcm.message.broker.producer.ProducerService;
import tech.stl.hcm.common.dto.CandidateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateCreateDTO;
import tech.stl.hcm.core.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import tech.stl.hcm.common.dto.helpers.CandidateSkillCreateDTO;

@ExtendWith(MockitoExtension.class)
class CandidateServiceImplTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private ProducerService producerService;

    @Mock
    private ServiceProperties serviceProperties;

    @Mock
    private TransactionService transactionService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CandidateServiceImpl candidateService;

    @BeforeEach
    void setUp() {
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void createCandidate_shouldPublishEventWithTransaction() throws Exception {
        // Given
        CandidateCreateDTO dto = CandidateCreateDTO.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .phone("+1-555-0123")
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        candidateService.createCandidate(dto);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("create-candidate"), eq(dto.getEmail()), any(Map.class));
    }

    @Test
    void updateCandidate_shouldPublishEvent() {
        // Given
        String candidateId = "550e8400-e29b-41d4-a716-446655440000"; // Valid UUID
        CandidateDTO dto = CandidateDTO.builder()
            .candidateId(UUID.randomUUID())
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .phone("+1-555-0123")
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        candidateService.updateCandidate(candidateId, dto);

        // Then
        verify(producerService).publishMessage(eq("update-candidate"), eq(candidateId), argThat(wrapper -> {
            if (wrapper instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) wrapper;
                return map.containsKey("payload") && map.containsKey("transactionId") && 
                       map.get("payload").equals(dto) && map.get("transactionId").equals(transactionId);
            }
            return false;
        }));
    }

    @Test
    void deleteCandidate_shouldPublishEvent() {
        // Given
        String candidateId = "550e8400-e29b-41d4-a716-446655440000"; // Valid UUID
        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        candidateService.deleteCandidate(candidateId);

        // Then
        verify(producerService).publishMessage(eq("delete-candidate"), eq(candidateId), argThat(wrapper -> {
            if (wrapper instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) wrapper;
                return map.containsKey("payload") && map.containsKey("transactionId") && 
                       map.get("payload") == null && map.get("transactionId").equals(transactionId);
            }
            return false;
        }));
    }

    @Test
    void addCandidateSkill_shouldPublishEvent() {
        // Given
        String candidateId = "550e8400-e29b-41d4-a716-446655440000"; // Valid UUID
        String skillId = "skill-456";
        
        // Create a mock skill object
        CandidateSkillCreateDTO skill = CandidateSkillCreateDTO.builder()
            .skillId(1)
            .proficiencyLevel("Expert")
            .yearsOfExperience(5)
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        candidateService.addCandidateSkill(candidateId, skill);

        // Then
        verify(producerService).publishMessage(eq("create-candidate-skill"), eq(candidateId), argThat(wrapper -> {
            if (wrapper instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) wrapper;
                return map.containsKey("payload") && map.containsKey("transactionId") && 
                       map.get("payload").equals(skill) && map.get("transactionId").equals(transactionId);
            }
            return false;
        }));
    }

    @Test
    void updateCandidateSkill_shouldPublishEvent() {
        // Given
        String candidateId = "550e8400-e29b-41d4-a716-446655440000"; // Valid UUID
        String skillId = "skill-456";
        CandidateDTO skill = CandidateDTO.builder().build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        candidateService.updateCandidateSkill(candidateId, skillId, null);

        // Then
        verify(producerService).publishMessage(eq("update-candidate-skill"), eq(candidateId + ":" + skillId), argThat(wrapper -> {
            if (wrapper instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) wrapper;
                return map.containsKey("payload") && map.containsKey("transactionId") && 
                       map.get("payload") == null && map.get("transactionId").equals(transactionId);
            }
            return false;
        }));
    }

    @Test
    void deleteCandidateSkill_shouldPublishEvent() {
        // Given
        String candidateId = "550e8400-e29b-41d4-a716-446655440000"; // Valid UUID
        String skillId = "skill-456";

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        candidateService.deleteCandidateSkill(candidateId, skillId);

        // Then
        verify(producerService).publishMessage(eq("delete-candidate-skill"), eq(candidateId + ":" + skillId), argThat(wrapper -> {
            if (wrapper instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) wrapper;
                return map.containsKey("payload") && map.containsKey("transactionId") && 
                       map.get("payload") == null && map.get("transactionId").equals(transactionId);
            }
            return false;
        }));
    }

    @Test
    void createCandidate_whenTransactionServiceThrowsException_shouldPropagateException() throws Exception {
        // Given
        CandidateCreateDTO dto = CandidateCreateDTO.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .phone("+1-555-0123")
            .build();

        when(transactionService.generateTransactionId()).thenThrow(new RuntimeException("Transaction service error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> candidateService.createCandidate(dto));
        verify(producerService, never()).publishMessage(any(), any(), any());
    }

    @Test
    void createCandidate_whenProducerServiceThrowsException_shouldPropagateException() throws Exception {
        // Given
        CandidateCreateDTO dto = CandidateCreateDTO.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .phone("+1-555-0123")
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);
        doThrow(new RuntimeException("Producer service error")).when(producerService).publishMessage(any(), any(), any());

        // When & Then
        assertThrows(RuntimeException.class, () -> candidateService.createCandidate(dto));
    }
} 