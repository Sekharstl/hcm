package tech.stl.hcm;

import tech.stl.hcm.common.dto.helpers.VendorCreateDTO;
import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateVendorConsumerTest {

    private static final String topicName = "create-vendor";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        VendorCreateDTO vendorCreateDTO = VendorCreateDTO.builder()
                .tenantId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"))
                .organizationId(UUID.fromString("08b06d14-4e03-11f0-bc56-325096b39f47"))
                .vendorName("Test Vendor")
                .contactName("John Doe")
                .contactEmail("john.doe@testvendor.com")
                .contactPhone("+1234567890")
                .address("123 Test Street, Test City, TC 12345")
                .statusId(1)
                .createdBy(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                .build();
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", vendorCreateDTO);
        
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 