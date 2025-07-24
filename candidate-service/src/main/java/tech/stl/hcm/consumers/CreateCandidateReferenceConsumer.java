package tech.stl.hcm.consumers;

import org.springframework.stereotype.Component;
import tech.stl.hcm.candidate.service.CandidateReferenceService;
import tech.stl.hcm.common.dto.CandidateReferenceDTO;
import tech.stl.hcm.common.dto.helpers.CandidateReferenceCreateDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${candidatereference.kafka.topic}",
        groupId = "${candidatereference.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "candidatereference.kafka.enable"
)
public class CreateCandidateReferenceConsumer extends BaseTransactionConsumer<CandidateReferenceCreateDTO> {

    private final CandidateReferenceService candidateReferenceService;

    public CreateCandidateReferenceConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, 
                                          CandidateReferenceService candidateReferenceService) {
        super(objectMapper, transactionUpdateService);
        this.candidateReferenceService = candidateReferenceService;
    }

    @Override
    protected Class<CandidateReferenceCreateDTO> getEntityType() {
        return CandidateReferenceCreateDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Candidate Reference";
    }

    @Override
    protected Object processEntity(CandidateReferenceCreateDTO candidateReference, UUID transactionId) {
        return candidateReferenceService.createCandidateReference(candidateReference);
    }

    @Override
    protected boolean isSuccess(Object result) {
        return result != null;
    }

    @Override
    protected UUID getEntityId(Object result) {
        // Reference ID is not tracked in transaction
        return null;
    }
} 