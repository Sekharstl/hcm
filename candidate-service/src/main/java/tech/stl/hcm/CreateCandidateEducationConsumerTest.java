package tech.stl.hcm;

import tech.stl.hcm.common.dto.CandidateEducationDTO;
import tech.stl.hcm.common.dto.helpers.CandidateEducationCreateDTO;
import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateCandidateEducationConsumerTest {

    private static final String topicName = "create-candidate-education";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        // Create the education data
        CandidateEducationCreateDTO candidateEducationCreateDTO = CandidateEducationCreateDTO.builder()
                .candidateId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"))
                .institution("Test University")
                .degree("Bachelor of Science")
                .fieldOfStudy("Computer Science")
                .startDate(LocalDate.of(2018, 9, 1))
                .endDate(LocalDate.of(2022, 5, 15))
                .grade("3.8 GPA")
                .notes("Graduated with honors")
                .description("Computer Science degree with focus on software engineering")
                .institutionName("Test University")
                .build();
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", candidateEducationCreateDTO);
        
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 