package tech.stl.hcm.consumers;

import org.springframework.stereotype.Component;
import tech.stl.hcm.application.service.ApplicationService;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${application.kafka.topic.delete}",
        groupId = "${application.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "application.kafka.enable"
)
public class DeleteApplicationConsumer extends BaseTransactionConsumer<Integer> {
    
    private final ApplicationService applicationService;

    public DeleteApplicationConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, ApplicationService applicationService) {
        super(objectMapper, transactionUpdateService);
        this.applicationService = applicationService;
    }

    @Override
    protected Class<Integer> getEntityType() {
        return Integer.class;
    }

    @Override
    protected String getEntityName() {
        return "Application Delete";
    }

    @Override
    protected Object processEntity(Integer applicationId, UUID transactionId) {
        applicationService.deleteApplication(applicationId);
        return applicationId;
    }

    @Override
    protected boolean isSuccess(Object result) {
        return result instanceof Integer;
    }

    @Override
    protected UUID getEntityId(Object result) {
        // Application ID is Integer, so we return null for UUID
        return null;
    }
} 