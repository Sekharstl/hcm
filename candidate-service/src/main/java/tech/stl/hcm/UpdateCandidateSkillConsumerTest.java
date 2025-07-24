package tech.stl.hcm;

import tech.stl.hcm.common.dto.CandidateSkillDTO;
import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class UpdateCandidateSkillConsumerTest {

    private static final String topicName = "update-candidate-skill";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        CandidateSkillDTO candidateSkillDTO = new CandidateSkillDTO();
        candidateSkillDTO.setCandidateId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"));
        candidateSkillDTO.setSkillId(1);
        candidateSkillDTO.setProficiencyLevel("Expert");
        candidateSkillDTO.setYearsOfExperience(7);
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", candidateSkillDTO);
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 