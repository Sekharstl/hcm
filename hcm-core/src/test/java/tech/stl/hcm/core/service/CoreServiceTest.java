package tech.stl.hcm.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import tech.stl.hcm.core.config.ServiceProperties;
import tech.stl.hcm.message.broker.producer.ProducerService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CoreServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private ProducerService producerService;
    @Mock
    private ServiceProperties serviceProperties;

    @InjectMocks
    private CoreService coreService;

    @Test
    void publishEvent_callsProducerService() {
        String topic = "topic";
        String key = "key";
        String payload = "payload";
        coreService.publishEvent(topic, key, payload);
        verify(producerService).publishMessage(topic, key, payload);
    }

    @Test
    void publishEvent_withNulls_shouldCallProducer() {
        coreService.publishEvent(null, null, null);
        verify(producerService).publishMessage(null, null, null);
    }

    @Test
    void publishEvent_whenProducerThrows_shouldPropagate() {
        doThrow(new RuntimeException("fail")).when(producerService).publishMessage(any(), any(), any());
        assertThrows(RuntimeException.class, () -> coreService.publishEvent("t", "k", "p"));
    }
} 