package tech.stl.hcm;

import tech.stl.hcm.common.dto.CandidateIdentityDTO;
import tech.stl.hcm.common.dto.helpers.CandidateIdentityCreateDTO;
import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.time.LocalDate;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateCandidateIdentityConsumerTest {

    private static final String topicName = "create-candidate-identity";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        // Create the identity data
        CandidateIdentityCreateDTO candidateIdentityCreateDTO = CandidateIdentityCreateDTO.builder()
                .candidateId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"))
                .idTypeId(1) // PASSPORT
                .idNumber("A12345678")
                .issuingCountry("India")
                .issueDate(LocalDate.of(2020, 1, 15))
                .expiryDate(LocalDate.of(2030, 1, 15))
                .createdBy(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .build();
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", candidateIdentityCreateDTO);
        
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 