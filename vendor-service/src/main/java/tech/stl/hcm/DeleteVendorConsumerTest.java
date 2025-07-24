package tech.stl.hcm;

import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeleteVendorConsumerTest {

    private static final String topicName = "delete-vendor";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        UUID vendorId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"); // Example UUID
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", vendorId);
        
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 