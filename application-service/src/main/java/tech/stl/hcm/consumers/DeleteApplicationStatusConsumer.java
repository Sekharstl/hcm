package tech.stl.hcm.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.stl.hcm.application.service.ApplicationStatusService;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@TopicListener(
        topic = "${applicationstatus.kafka.topic.delete}",
        groupId = "${applicationstatus.kafka.group-id}",
        valueType = Map.class,
        enableProperty = "applicationstatus.kafka.enable"
)
public class DeleteApplicationStatusConsumer extends BaseTransactionConsumer<Integer> {
    
    private final ApplicationStatusService applicationStatusService;
    
    public DeleteApplicationStatusConsumer(ApplicationStatusService applicationStatusService, 
                                       ObjectMapper objectMapper, 
                                       TransactionUpdateService transactionUpdateService) {
        super(objectMapper, transactionUpdateService);
        this.applicationStatusService = applicationStatusService;
    }

    @Override
    protected Class<Integer> getEntityType() {
        return Integer.class;
    }

    @Override
    protected String getEntityName() {
        return "Application Status";
    }

    @Override
    protected Object processEntity(Integer statusId, UUID transactionId) {
        applicationStatusService.deleteApplicationStatus(statusId);
        return statusId;
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