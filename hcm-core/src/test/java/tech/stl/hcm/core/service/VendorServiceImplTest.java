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
import tech.stl.hcm.common.dto.VendorDTO;
import tech.stl.hcm.common.dto.helpers.VendorCreateDTO;
import tech.stl.hcm.core.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendorServiceImplTest {

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
    private VendorServiceImpl vendorService;

    @BeforeEach
    void setUp() {
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void createVendor_shouldPublishEvent() throws Exception {
        // Given
        VendorCreateDTO dto = VendorCreateDTO.builder()
            .vendorName("Tech Solutions Inc")
            .contactEmail("contact@techsolutions.com")
            .contactPhone("+1-555-0123")
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        vendorService.createVendor(dto);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("create-vendor"), eq(dto.getVendorName()), any(Map.class));
    }

    @Test
    void updateVendor_shouldPublishEvent() throws Exception {
        // Given
        UUID vendorId = UUID.randomUUID();
        VendorDTO dto = VendorDTO.builder()
            .vendorId(vendorId)
            .vendorName("Updated Tech Solutions Inc")
            .contactEmail("updated@techsolutions.com")
            .contactPhone("+1-555-9999")
            .build();

        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        vendorService.updateVendor(vendorId, dto);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("update-vendor"), eq(vendorId.toString()), any(Map.class));
    }

    @Test
    void deleteVendor_shouldPublishEvent() throws Exception {
        // Given
        UUID vendorId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        when(transactionService.generateTransactionId()).thenReturn(transactionId);

        // When
        vendorService.deleteVendor(vendorId);

        // Then
        verify(transactionService).generateTransactionId();
        verify(transactionService).createTransaction(any());
        verify(producerService).publishMessage(eq("delete-vendor"), eq(vendorId.toString()), any(Map.class));
    }
} 