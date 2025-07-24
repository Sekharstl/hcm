package tech.stl.hcm.message.broker.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseProducerService {

    private final ProducerService producerService;

    /**
     * Publish a response message to a response topic
     * @param responseTopic The topic to publish the response to
     * @param correlationKey The correlation key to match with original request
     * @param responsePayload The response payload
     */
    public <T> void publishResponse(String responseTopic, String correlationKey, T responsePayload) {
        try {
            producerService.publishMessage(responseTopic, correlationKey, responsePayload);
            log.info("Response published to topic: {}, key: {}", responseTopic, correlationKey);
        } catch (Exception e) {
            log.error("Failed to publish response to topic: {}, key: {}", responseTopic, correlationKey, e);
        }
    }
} 