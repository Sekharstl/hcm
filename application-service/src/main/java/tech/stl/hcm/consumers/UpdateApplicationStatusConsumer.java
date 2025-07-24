package tech.stl.hcm.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.stl.hcm.application.service.ApplicationStatusService;
import tech.stl.hcm.common.dto.ApplicationStatusDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@TopicListener(
        topic = "${applicationstatus.kafka.topic.update}",
        groupId = "${applicationstatus.kafka.group-id}",
        valueType = Map.class,
        enableProperty = "applicationstatus.kafka.enable"
)
public class UpdateApplicationStatusConsumer extends BaseTransactionConsumer<ApplicationStatusDTO> {
    private final ApplicationStatusService applicationStatusService;
    
    public UpdateApplicationStatusConsumer(ApplicationStatusService applicationStatusService, 
                                       ObjectMapper objectMapper, 
                                       TransactionUpdateService transactionUpdateService) {
        super(objectMapper, transactionUpdateService);
        this.applicationStatusService = applicationStatusService;
    }

    @Override
    protected Class<ApplicationStatusDTO> getEntityType() {
        return ApplicationStatusDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Application Status";
    }

    @Override
    protected Object processEntity(ApplicationStatusDTO dto, UUID transactionId) {
        return applicationStatusService.updateApplicationStatus(dto.getStatusId(), dto);
    }

    @Override
    protected boolean isSuccess(Object result) {
        return result != null;
    }

    @Override
    protected UUID getEntityId(Object result) {
        // Return the updated entity ID if available
        return null;
    }
} 