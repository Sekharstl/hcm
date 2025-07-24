package tech.stl.hcm;

import tech.stl.hcm.common.dto.CandidateEducationDTO;
import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.time.LocalDate;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class UpdateCandidateEducationConsumerTest {

    private static final String topicName = "update-candidate-education";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        CandidateEducationDTO candidateEducationDTO = new CandidateEducationDTO();
        candidateEducationDTO.setEducationId(1);
        candidateEducationDTO.setCandidateId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"));
        candidateEducationDTO.setInstitution("Updated University");
        candidateEducationDTO.setDegree("Master of Science");
        candidateEducationDTO.setFieldOfStudy("Software Engineering");
        candidateEducationDTO.setStartDate(LocalDate.of(2022, 9, 1));
        candidateEducationDTO.setEndDate(LocalDate.of(2024, 5, 15));
        candidateEducationDTO.setGrade("3.9 GPA");
        candidateEducationDTO.setNotes("Updated graduation notes");
        candidateEducationDTO.setDescription("Updated degree description");
        candidateEducationDTO.setInstitutionName("Updated University");
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", candidateEducationDTO);
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 