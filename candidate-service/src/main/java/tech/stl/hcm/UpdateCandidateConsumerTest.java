package tech.stl.hcm;

import tech.stl.hcm.common.dto.CandidateDTO;
import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdateCandidateConsumerTest {

    private static final String topicName = "update-candidate";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        CandidateDTO candidateDTO = new CandidateDTO();
        candidateDTO.setCandidateId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"));
        candidateDTO.setTenantId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"));
        candidateDTO.setFirstName("John Updated");
        candidateDTO.setLastName("Doe Updated");
        candidateDTO.setEmail("john.updated@example.com");
        candidateDTO.setPhone("+919870345745");
        candidateDTO.setOrganizationId(UUID.fromString("08b06d14-4e03-11f0-bc56-325096b39f47"));
        candidateDTO.setVendorId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000")); // Example vendor ID
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", candidateDTO);
        
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 