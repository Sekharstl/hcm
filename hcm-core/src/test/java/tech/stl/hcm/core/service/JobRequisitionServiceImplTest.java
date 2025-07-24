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
import tech.stl.hcm.common.dto.JobRequisitionDTO;
import tech.stl.hcm.common.dto.helpers.JobRequisitionCreateDTO;
import tech.stl.hcm.core.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobRequisitionServiceImplTest {

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
    private JobRequisitionServiceImpl jobRequisitionService;

    @BeforeEach
    void setUp() {
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void createJobRequisition_shouldPublishEvent() throws Exception {
        // Given
        JobRequisitionCreateDTO dto = JobRequisitionCreateDTO.builder()
            .title("Software Engineer")
            .location("San Francisco, CA")
            .employmentType("Full-time")
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        jobRequisitionService.createJobRequisition(dto);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("create-job-requisition"), eq(dto.getTitle()), any(Map.class));
    }

    @Test
    void updateJobRequisition_shouldPublishEvent() throws Exception {
        // Given
        JobRequisitionDTO dto = JobRequisitionDTO.builder()
            .jobRequisitionId(1)
            .title("Senior Software Engineer")
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        jobRequisitionService.updateJobRequisition(dto);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("update-job-requisition"), eq(dto.getJobRequisitionId().toString()), any(Map.class));
    }

    @Test
    void deleteJobRequisition_shouldPublishEvent() throws Exception {
        // Given
        Integer requisitionId = 1;
        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        jobRequisitionService.deleteJobRequisition(requisitionId);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("delete-job-requisition"), eq(requisitionId.toString()), any(Map.class));
    }
} 