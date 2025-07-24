package tech.stl.hcm.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.stl.hcm.application.service.ApplicationService;
import tech.stl.hcm.common.dto.ApplicationDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationCreateDTO;
import tech.stl.hcm.consumers.CreateApplicationConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateApplicationConsumerTest {

    @Mock
    private ApplicationService applicationService;
    
    @Mock
    private TransactionUpdateService transactionUpdateService;
    
    @Mock
    private ObjectMapper objectMapper;

    private CreateApplicationConsumer createApplicationConsumer;

    @Test
    void testHandle_Success() throws JsonProcessingException {
        // Given
        ApplicationCreateDTO createDTO = ApplicationCreateDTO.builder()
                .candidateId(UUID.randomUUID())
                .requisitionId(1)
                .statusId(1)
                .appliedDate(LocalDate.now())
                .source("Website")
                .build();

        ApplicationDTO expectedDTO = ApplicationDTO.builder()
                .applicationId(1)
                .candidateId(createDTO.getCandidateId())
                .requisitionId(createDTO.getRequisitionId())
                .statusId(createDTO.getStatusId())
                .appliedDate(createDTO.getAppliedDate())
                .source(createDTO.getSource())
                .build();

        when(applicationService.createApplication(any(ApplicationCreateDTO.class)))
                .thenReturn(expectedDTO);
        when(objectMapper.convertValue(any(), eq(ApplicationCreateDTO.class)))
                .thenReturn(createDTO);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"status\":\"SUCCESS\"}");

        createApplicationConsumer = new CreateApplicationConsumer(objectMapper, transactionUpdateService, applicationService);

        // Create transaction wrapper with payload as Map
        Map<String, Object> message = new HashMap<>();
        message.put("transactionId", UUID.randomUUID().toString());
        
        // Convert DTO to Map for payload
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("candidateId", createDTO.getCandidateId().toString());
        payloadMap.put("requisitionId", createDTO.getRequisitionId());
        payloadMap.put("statusId", createDTO.getStatusId());
        payloadMap.put("appliedDate", createDTO.getAppliedDate().toString());
        payloadMap.put("source", createDTO.getSource());
        message.put("payload", payloadMap);

        // When
        createApplicationConsumer.handle(message, "test-key");

        // Then
        verify(applicationService, times(1)).createApplication(createDTO);
        verify(transactionUpdateService, times(1)).updateTransactionToProcessing(any(UUID.class));
        verify(transactionUpdateService, times(1)).updateTransactionToSuccess(any(UUID.class), eq(null), eq("{\"status\":\"SUCCESS\"}"));
    }

    @Test
    void testHandle_Exception() {
        // Given
        ApplicationCreateDTO createDTO = ApplicationCreateDTO.builder()
                .candidateId(UUID.randomUUID())
                .requisitionId(1)
                .statusId(1)
                .appliedDate(LocalDate.now())
                .source("Website")
                .build();

        when(applicationService.createApplication(any(ApplicationCreateDTO.class)))
                .thenThrow(new RuntimeException("Test exception"));
        when(objectMapper.convertValue(any(), eq(ApplicationCreateDTO.class)))
                .thenReturn(createDTO);

        createApplicationConsumer = new CreateApplicationConsumer(objectMapper, transactionUpdateService, applicationService);

        // Create transaction wrapper with payload as Map
        Map<String, Object> message = new HashMap<>();
        message.put("transactionId", UUID.randomUUID().toString());
        
        // Convert DTO to Map for payload
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("candidateId", createDTO.getCandidateId().toString());
        payloadMap.put("requisitionId", createDTO.getRequisitionId());
        payloadMap.put("statusId", createDTO.getStatusId());
        payloadMap.put("appliedDate", createDTO.getAppliedDate().toString());
        payloadMap.put("source", createDTO.getSource());
        message.put("payload", payloadMap);

        // When & Then
        try {
            createApplicationConsumer.handle(message, "test-key");
        } catch (RuntimeException e) {
            // Expected
        }

        verify(applicationService, times(1)).createApplication(createDTO);
        verify(transactionUpdateService, times(1)).updateTransactionToProcessing(any(UUID.class));
        verify(transactionUpdateService, times(1)).updateTransactionToFailed(any(UUID.class), anyString());
    }
} 