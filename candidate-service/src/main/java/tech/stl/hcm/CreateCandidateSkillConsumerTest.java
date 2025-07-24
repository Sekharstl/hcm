package tech.stl.hcm;

import tech.stl.hcm.common.dto.CandidateSkillDTO;
import tech.stl.hcm.common.dto.helpers.CandidateSkillCreateDTO;
import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateCandidateSkillConsumerTest {

    private static final String topicName = "create-candidate-skill";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        // Create the skill data
        CandidateSkillCreateDTO candidateSkillCreateDTO = CandidateSkillCreateDTO.builder()
                .candidateId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"))
                .skillId(1)
                .proficiencyLevel("Advanced")
                .yearsOfExperience(5)
                .build();
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", candidateSkillCreateDTO);
        
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 