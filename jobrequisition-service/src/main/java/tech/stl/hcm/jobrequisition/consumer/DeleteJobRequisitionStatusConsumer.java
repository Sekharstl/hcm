package tech.stl.hcm.jobrequisition.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.stl.hcm.jobrequisition.service.JobRequisitionStatusService;
import tech.stl.hcm.common.dto.JobRequisitionStatusDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@TopicListener(
        topic = "${jobrequisitionstatus.kafka.topic.delete}",
        groupId = "${jobrequisitionstatus.kafka.group-id}",
        valueType = Map.class,
        enableProperty = "jobrequisitionstatus.kafka.enable"
)
public class DeleteJobRequisitionStatusConsumer extends BaseTransactionConsumer<JobRequisitionStatusDTO> {
    private final JobRequisitionStatusService jobRequisitionStatusService;
    
    public DeleteJobRequisitionStatusConsumer(JobRequisitionStatusService jobRequisitionStatusService, 
                                           ObjectMapper objectMapper, 
                                           TransactionUpdateService transactionUpdateService) {
        super(objectMapper, transactionUpdateService);
        this.jobRequisitionStatusService = jobRequisitionStatusService;
    }

    @Override
    protected Class<JobRequisitionStatusDTO> getEntityType() {
        return JobRequisitionStatusDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Job Requisition Status";
    }

    @Override
    protected Object processEntity(JobRequisitionStatusDTO status, UUID transactionId) {
        jobRequisitionStatusService.deleteJobRequisitionStatus(status.getStatusId());
        return status;
    }

    @Override
    protected boolean isSuccess(Object result) {
        return result != null;
    }

    @Override
    protected UUID getEntityId(Object result) {
        // For delete operations, we don't have an entity ID to return
        return null;
    }
} 