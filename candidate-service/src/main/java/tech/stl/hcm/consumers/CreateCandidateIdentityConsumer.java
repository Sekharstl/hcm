package tech.stl.hcm.consumers;

import org.springframework.stereotype.Component;
import tech.stl.hcm.candidate.service.CandidateIdentityService;
import tech.stl.hcm.common.dto.CandidateIdentityDTO;
import tech.stl.hcm.common.dto.helpers.CandidateIdentityCreateDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${candidateidentity.kafka.topic}",
        groupId = "${candidateidentity.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "candidateidentity.kafka.enable"
)
public class CreateCandidateIdentityConsumer extends BaseTransactionConsumer<CandidateIdentityCreateDTO> {

    private final CandidateIdentityService candidateIdentityService;

    public CreateCandidateIdentityConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, 
                                         CandidateIdentityService candidateIdentityService) {
        super(objectMapper, transactionUpdateService);
        this.candidateIdentityService = candidateIdentityService;
    }

    @Override
    protected Class<CandidateIdentityCreateDTO> getEntityType() {
        return CandidateIdentityCreateDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Candidate Identity";
    }

    @Override
    protected Object processEntity(CandidateIdentityCreateDTO candidateIdentity, UUID transactionId) {
        return candidateIdentityService.createCandidateIdentity(candidateIdentity);
    }

    @Override
    protected boolean isSuccess(Object result) {
        return result != null;
    }

    @Override
    protected UUID getEntityId(Object result) {
        // Identity ID is not tracked in transaction
        return null;
    }
} 