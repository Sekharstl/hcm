package tech.stl.hcm;

import tech.stl.hcm.common.dto.CandidateReferenceDTO;
import tech.stl.hcm.common.dto.helpers.CandidateReferenceCreateDTO;
import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateCandidateReferenceConsumerTest {

    private static final String topicName = "create-candidate-reference";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        // Create the reference data
        CandidateReferenceCreateDTO candidateReferenceCreateDTO = CandidateReferenceCreateDTO.builder()
                .candidateId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"))
                .referenceName("Jane Smith")
                .relationship("Former Manager")
                .company("Tech Solutions Inc.")
                .position("Senior Software Engineer")
                .email("jane.smith@techsolutions.com")
                .phone("+1-555-0123")
                .createdBy(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .build();
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", candidateReferenceCreateDTO);
        
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 