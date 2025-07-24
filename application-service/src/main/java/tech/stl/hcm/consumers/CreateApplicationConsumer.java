package tech.stl.hcm.consumers;

import org.springframework.stereotype.Component;
import tech.stl.hcm.application.service.ApplicationService;
import tech.stl.hcm.common.dto.helpers.ApplicationCreateDTO;
import tech.stl.hcm.common.dto.ApplicationDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${application.kafka.topic}",
        groupId = "${application.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "application.kafka.enable"
)
public class CreateApplicationConsumer extends BaseTransactionConsumer<ApplicationCreateDTO> {
    
    private final ApplicationService applicationService;

    public CreateApplicationConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, ApplicationService applicationService) {
        super(objectMapper, transactionUpdateService);
        this.applicationService = applicationService;
    }

    @Override
    protected Class<ApplicationCreateDTO> getEntityType() {
        return ApplicationCreateDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Application";
    }

    @Override
    protected Object processEntity(ApplicationCreateDTO application, UUID transactionId) {
        return applicationService.createApplication(application);
    }

    @Override
    protected boolean isSuccess(Object result) {
        return result instanceof ApplicationDTO;
    }

    @Override
    protected UUID getEntityId(Object result) {
        if (result instanceof ApplicationDTO) {
            ApplicationDTO application = (ApplicationDTO) result;
            // Application ID is Integer, so we return null for UUID
            // The transaction will still be tracked but without entity ID
            return null;
        }
        return null;
    }
} 