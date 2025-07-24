package tech.stl.hcm.consumers;

import org.springframework.stereotype.Component;
import tech.stl.hcm.candidate.service.CandidateDocumentService;
import tech.stl.hcm.common.dto.CandidateDocumentDTO;
import tech.stl.hcm.common.dto.helpers.CandidateDocumentCreateDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${candidatedocument.kafka.topic}",
        groupId = "${candidatedocument.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "candidatedocument.kafka.enable"
)
public class CreateCandidateDocumentConsumer extends BaseTransactionConsumer<CandidateDocumentCreateDTO> {

    private final CandidateDocumentService candidateDocumentService;

    public CreateCandidateDocumentConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, 
                                         CandidateDocumentService candidateDocumentService) {
        super(objectMapper, transactionUpdateService);
        this.candidateDocumentService = candidateDocumentService;
    }

    @Override
    protected Class<CandidateDocumentCreateDTO> getEntityType() {
        return CandidateDocumentCreateDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Candidate Document";
    }

    @Override
    protected Object processEntity(CandidateDocumentCreateDTO candidateDocument, UUID transactionId) {
        return candidateDocumentService.createCandidateDocument(candidateDocument);
    }

    @Override
    protected boolean isSuccess(Object result) {
        return result != null;
    }

    @Override
    protected UUID getEntityId(Object result) {
        // Document ID is not tracked in transaction
        return null;
    }
} 