package tech.stl.hcm.consumers;

import org.springframework.stereotype.Component;
import tech.stl.hcm.application.service.ApplicationStatusService;
import tech.stl.hcm.common.dto.helpers.ApplicationStatusCreateDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${applicationstatus.kafka.topic}",
        groupId = "${applicationstatus.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "applicationstatus.kafka.enable"
)
public class CreateApplicationStatusConsumer extends BaseTransactionConsumer<ApplicationStatusCreateDTO> {
    
    private final ApplicationStatusService applicationStatusService;

    public CreateApplicationStatusConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, 
                                        ApplicationStatusService applicationStatusService) {
        super(objectMapper, transactionUpdateService);
        this.applicationStatusService = applicationStatusService;
    }

    @Override
    protected Class<ApplicationStatusCreateDTO> getEntityType() {
        return ApplicationStatusCreateDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Application Status";
    }

    @Override
    protected Object processEntity(ApplicationStatusCreateDTO applicationStatus, UUID transactionId) {
        return applicationStatusService.createApplicationStatus(applicationStatus);
    }

    @Override
    protected boolean isSuccess(Object result) {
        return result != null;
    }

    @Override
    protected UUID getEntityId(Object result) {
        // Application Status ID is Integer, so we return null for UUID
        return null;
    }
} 