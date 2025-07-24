package tech.stl.hcm.consumers;

import org.springframework.stereotype.Component;
import tech.stl.hcm.application.service.ApplicationService;
import tech.stl.hcm.common.dto.ApplicationDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${application.kafka.topic.update}",
        groupId = "${application.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "application.kafka.enable"
)
public class UpdateApplicationConsumer extends BaseTransactionConsumer<ApplicationDTO> {
    private final ApplicationService applicationService;

    public UpdateApplicationConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, ApplicationService applicationService) {
        super(objectMapper, transactionUpdateService);
        this.applicationService = applicationService;
    }

    @Override
    protected Class<ApplicationDTO> getEntityType() {
        return ApplicationDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Application Update";
    }

    @Override
    protected Object processEntity(ApplicationDTO application, UUID transactionId) {
        applicationService.updateApplication(application.getApplicationId(), application);
        return application;
    }

    @Override
    protected boolean isSuccess(Object result) {
        return result instanceof ApplicationDTO;
    }

    @Override
    protected UUID getEntityId(Object result) {
        // Application ID is Integer, so we return null for UUID
        return null;
    }
} 