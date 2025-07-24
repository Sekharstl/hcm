package tech.stl.hcm.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.stl.hcm.application.service.ApplicationStatusService;
import tech.stl.hcm.common.dto.ApplicationStatusDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationStatusCreateDTO;
import tech.stl.hcm.consumers.CreateApplicationStatusConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateApplicationStatusConsumerTest {

    @Mock
    private ApplicationStatusService applicationStatusService;
    
    @Mock
    private TransactionUpdateService transactionUpdateService;
    
    @Mock
    private ObjectMapper objectMapper;

    private CreateApplicationStatusConsumer createApplicationStatusConsumer;

    @Test
    void testHandle_Success() throws JsonProcessingException {
        // Given
        ApplicationStatusCreateDTO createDTO = ApplicationStatusCreateDTO.builder()
                .name("Applied")
                .build();

        ApplicationStatusDTO expectedDTO = ApplicationStatusDTO.builder()
                .statusId(1)
                .name(createDTO.getName())
                .build();

        when(applicationStatusService.createApplicationStatus(any(ApplicationStatusCreateDTO.class)))
                .thenReturn(expectedDTO);
        when(objectMapper.convertValue(any(), eq(ApplicationStatusCreateDTO.class)))
                .thenReturn(createDTO);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"status\":\"SUCCESS\"}");

        createApplicationStatusConsumer = new CreateApplicationStatusConsumer(objectMapper, transactionUpdateService, applicationStatusService);

        // Create transaction wrapper with payload as Map
        Map<String, Object> message = new HashMap<>();
        message.put("transactionId", UUID.randomUUID().toString());
        
        // Convert DTO to Map for payload
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("name", createDTO.getName());
        message.put("payload", payloadMap);

        // When
        createApplicationStatusConsumer.handle(message, "test-key");

        // Then
        verify(applicationStatusService, times(1)).createApplicationStatus(createDTO);
        verify(transactionUpdateService, times(1)).updateTransactionToProcessing(any(UUID.class));
        verify(transactionUpdateService, times(1)).updateTransactionToSuccess(any(UUID.class), eq(null), eq("{\"status\":\"SUCCESS\"}"));
    }

    @Test
    void testHandle_Exception() {
        // Given
        ApplicationStatusCreateDTO createDTO = ApplicationStatusCreateDTO.builder()
                .name("Applied")
                .build();

        when(applicationStatusService.createApplicationStatus(any(ApplicationStatusCreateDTO.class)))
                .thenThrow(new RuntimeException("Test exception"));
        when(objectMapper.convertValue(any(), eq(ApplicationStatusCreateDTO.class)))
                .thenReturn(createDTO);

        createApplicationStatusConsumer = new CreateApplicationStatusConsumer(objectMapper, transactionUpdateService, applicationStatusService);

        // Create transaction wrapper with payload as Map
        Map<String, Object> message = new HashMap<>();
        message.put("transactionId", UUID.randomUUID().toString());
        
        // Convert DTO to Map for payload
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("name", createDTO.getName());
        message.put("payload", payloadMap);

        // When & Then
        try {
            createApplicationStatusConsumer.handle(message, "test-key");
        } catch (RuntimeException e) {
            // Expected
        }

        verify(applicationStatusService, times(1)).createApplicationStatus(createDTO);
        verify(transactionUpdateService, times(1)).updateTransactionToProcessing(any(UUID.class));
        verify(transactionUpdateService, times(1)).updateTransactionToFailed(any(UUID.class), anyString());
    }
} 