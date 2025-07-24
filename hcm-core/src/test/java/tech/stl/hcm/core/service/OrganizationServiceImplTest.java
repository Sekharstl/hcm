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
import tech.stl.hcm.common.dto.OrganizationDTO;
import tech.stl.hcm.common.dto.helpers.OrganizationCreateDTO;
import tech.stl.hcm.core.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {

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
    private OrganizationServiceImpl organizationService;

    @BeforeEach
    void setUp() {
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void createOrganization_shouldPublishEvent() throws Exception {
        // Given
        OrganizationCreateDTO dto = OrganizationCreateDTO.builder()
            .name("Tech Solutions Inc")
            .address("123 Tech Street, Silicon Valley, CA")
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        organizationService.createOrganization(dto);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("create-organization"), eq(dto.getName()), any(Map.class));
    }

    @Test
    void updateOrganization_shouldPublishEvent() throws Exception {
        // Given
        String orgId = "org-123";
        OrganizationDTO dto = OrganizationDTO.builder()
            .organizationId(UUID.randomUUID())
            .name("Updated Tech Solutions Inc")
            .address("456 Updated Street, Silicon Valley, CA")
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        organizationService.updateOrganization(orgId, dto);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("update-organization"), eq(orgId), any(Map.class));
    }

    @Test
    void deleteOrganization_shouldPublishEvent() throws Exception {
        // Given
        String orgId = "org-123";
        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        organizationService.deleteOrganization(orgId);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("delete-organization"), eq(orgId), any(Map.class));
    }
} 