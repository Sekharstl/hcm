package tech.stl.hcm;

import tech.stl.hcm.common.dto.CandidateCertificationDTO;
import tech.stl.hcm.common.dto.helpers.CandidateCertificationCreateDTO;
import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateCandidateCertificationConsumerTest {

    private static final String topicName = "create-candidate-certificate";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        // Create the certification data
        CandidateCertificationCreateDTO candidateCertificationCreateDTO = CandidateCertificationCreateDTO.builder()
                .candidateId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"))
                .certificateName("AWS Certified Solutions Architect")
                .certificationName("AWS Certified Solutions Architect")
                .issuedBy("Amazon Web Services")
                .issuingOrganization("Amazon Web Services")
                .issueDate(LocalDate.of(2023, 6, 15))
                .expiryDate(LocalDate.of(2026, 6, 15))
                .build();
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", candidateCertificationCreateDTO);
        
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 