package tech.stl.hcm;

import tech.stl.hcm.common.dto.CandidateDocumentDTO;
import tech.stl.hcm.common.dto.helpers.CandidateDocumentCreateDTO;
import tech.stl.hcm.message.broker.producer.ProducerFactory;
import tech.stl.hcm.message.broker.producer.ProducerService;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateCandidateDocumentConsumerTest {

    private static final String topicName = "create-candidate-document";

    public static void main(String[] args) {
        ProducerFactory producerFactory = new ProducerFactory();
        ProducerService producerService = new ProducerService(producerFactory);
        
        // Create the document data
        CandidateDocumentCreateDTO candidateDocumentCreateDTO = CandidateDocumentCreateDTO.builder()
                .candidateId(UUID.fromString("a15104c0-44b7-4512-b9b1-6122e7af7d41"))
                .documentTypeId(1) // RESUME
                .fileName("resume_john_doe_20241201.pdf")
                .originalFileName("John_Doe_Resume.pdf")
                .filePath("uploads/candidates/a15104c0-44b7-4512-b9b1-6122e7af7d41/resume_john_doe_20241201.pdf")
                .fileSize(1024000L) // 1MB
                .mimeType("application/pdf")
                .createdBy(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .build();
        
        // Create transaction wrapper
        Map<String, Object> transactionWrapper = new HashMap<>();
        transactionWrapper.put("transactionId", UUID.randomUUID().toString());
        transactionWrapper.put("payload", candidateDocumentCreateDTO);
        
        String key = UUID.randomUUID().toString();
        producerService.publishMessage(topicName, key, transactionWrapper);
    }
} 