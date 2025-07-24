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
import tech.stl.hcm.common.dto.TenantDTO;
import tech.stl.hcm.common.dto.helpers.TenantCreateDTO;
import tech.stl.hcm.core.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenantServiceImplTest {

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
    private TenantServiceImpl tenantService;

    @BeforeEach
    void setUp() {
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void createTenant_shouldPublishEvent() throws Exception {
        // Given
        TenantCreateDTO dto = TenantCreateDTO.builder()
            .name("Tech Solutions Inc")
            .domain("techsolutions.com")
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        tenantService.createTenant(dto);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("create-tenant"), eq(dto.getName()), any(Map.class));
    }

    @Test
    void updateTenant_shouldPublishEvent() throws Exception {
        // Given
        String tenantId = "tenant-123";
        TenantDTO dto = TenantDTO.builder()
            .tenantId(UUID.randomUUID())
            .name("Updated Tech Solutions Inc")
            .domain("updated-techsolutions.com")
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        tenantService.updateTenant(tenantId, dto);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("update-tenant"), eq(tenantId), any(Map.class));
    }

    @Test
    void deleteTenant_shouldPublishEvent() throws Exception {
        // Given
        String tenantId = "tenant-123";
        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        tenantService.deleteTenant(tenantId);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("delete-tenant"), eq(tenantId), any(Map.class));
    }
} 