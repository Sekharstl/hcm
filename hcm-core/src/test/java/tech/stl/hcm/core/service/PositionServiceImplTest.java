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
import tech.stl.hcm.common.dto.PositionDTO;
import tech.stl.hcm.common.dto.helpers.PositionCreateDTO;
import tech.stl.hcm.core.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionServiceImplTest {

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
    private PositionServiceImpl positionService;

    @BeforeEach
    void setUp() {
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void createPosition_shouldPublishEvent() throws Exception {
        // Given
        PositionCreateDTO dto = PositionCreateDTO.builder()
            .title("Software Engineer")
            .description("Develop software applications")
            .location("San Francisco, CA")
            .employmentType("Full-time")
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        positionService.createPosition(dto);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("create-position"), eq("Software Engineer"), any(Map.class));
    }

    @Test
    void updatePosition_shouldPublishEvent() throws Exception {
        // Given
        Integer positionId = 1;
        PositionDTO dto = PositionDTO.builder()
            .positionId(positionId)
            .title("Senior Software Engineer")
            .description("Develop advanced software applications")
            .location("San Francisco, CA")
            .employmentType("Full-time")
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        positionService.updatePosition(positionId, dto);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("update-position"), eq(positionId.toString()), any(Map.class));
    }

    @Test
    void deletePosition_shouldPublishEvent() throws Exception {
        // Given
        Integer positionId = 1;
        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        positionService.deletePosition(positionId);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("delete-position"), eq(positionId.toString()), any(Map.class));
    }
} 