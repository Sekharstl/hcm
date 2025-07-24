package tech.stl.hcm;

import tech.stl.hcm.common.dto.CandidateWorkHistoryDTO;
import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.time.LocalDate;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class UpdateCandidateWorkHistoryConsumerTest {

    private static final String topicName = "update-candidate-work-history";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        CandidateWorkHistoryDTO candidateWorkHistoryDTO = new CandidateWorkHistoryDTO();
        candidateWorkHistoryDTO.setWorkHistoryId(1);
        candidateWorkHistoryDTO.setCandidateId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"));
        candidateWorkHistoryDTO.setCompanyName("Updated Tech Solutions Inc.");
        candidateWorkHistoryDTO.setPositionTitle("Lead Software Engineer");
        candidateWorkHistoryDTO.setJobTitle("Lead Software Engineer");
        candidateWorkHistoryDTO.setLocation("New York, NY");
        candidateWorkHistoryDTO.setStartDate(LocalDate.of(2021, 3, 1));
        candidateWorkHistoryDTO.setEndDate(LocalDate.of(2024, 12, 31));
        candidateWorkHistoryDTO.setResponsibilities("Updated responsibilities: Led development team and architected solutions");
        candidateWorkHistoryDTO.setDescription("Updated description: Led team of 8 engineers and delivered 5 major releases");
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", candidateWorkHistoryDTO);
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 