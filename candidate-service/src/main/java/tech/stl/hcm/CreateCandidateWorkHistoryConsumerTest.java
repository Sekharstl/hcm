package tech.stl.hcm;

import tech.stl.hcm.common.dto.CandidateWorkHistoryDTO;
import tech.stl.hcm.common.dto.helpers.CandidateWorkHistoryCreateDTO;
import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateCandidateWorkHistoryConsumerTest {

    private static final String topicName = "create-candidate-work-history";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        // Create the work history data
        CandidateWorkHistoryCreateDTO candidateWorkHistoryCreateDTO = CandidateWorkHistoryCreateDTO.builder()
                .candidateId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"))
                .companyName("Tech Solutions Inc.")
                .positionTitle("Senior Software Engineer")
                .jobTitle("Senior Software Engineer")
                .location("San Francisco, CA")
                .startDate(LocalDate.of(2020, 3, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .responsibilities("Developed and maintained web applications using Java and Spring Boot")
                .description("Led development team of 5 engineers and delivered 3 major product releases")
                .build();
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", candidateWorkHistoryCreateDTO);
        
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 