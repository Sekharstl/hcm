package tech.stl.hcm;

import tech.stl.hcm.common.dto.CandidateCertificationDTO;
import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.time.LocalDate;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class UpdateCandidateCertificationConsumerTest {

    private static final String topicName = "update-candidate-certificate";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        CandidateCertificationDTO candidateCertificationDTO = new CandidateCertificationDTO();
        candidateCertificationDTO.setCertificationId(1);
        candidateCertificationDTO.setCandidateId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"));
        candidateCertificationDTO.setCertificateName("AWS Certified Solutions Architect - Professional");
        candidateCertificationDTO.setCertificationName("AWS Certified Solutions Architect - Professional");
        candidateCertificationDTO.setIssuedBy("Amazon Web Services");
        candidateCertificationDTO.setIssuingOrganization("Amazon Web Services");
        candidateCertificationDTO.setIssueDate(LocalDate.of(2024, 1, 15));
        candidateCertificationDTO.setExpiryDate(LocalDate.of(2027, 1, 15));
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", candidateCertificationDTO);
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 