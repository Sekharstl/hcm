package tech.stl.hcm.position.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.stl.hcm.position.service.PositionStatusService;
import tech.stl.hcm.common.dto.PositionStatusDTO;
import tech.stl.hcm.common.dto.helpers.PositionStatusCreateDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@TopicListener(
        topic = "${positionstatus.kafka.topic}",
        groupId = "${positionstatus.kafka.group-id}",
        valueType = Map.class,
        enableProperty = "positionstatus.kafka.enable"
)
public class CreatePositionStatusConsumer extends BaseTransactionConsumer<PositionStatusDTO> {
    private final PositionStatusService positionStatusService;
    
    public CreatePositionStatusConsumer(PositionStatusService positionStatusService, 
                                     ObjectMapper objectMapper, 
                                     TransactionUpdateService transactionUpdateService) {
        super(objectMapper, transactionUpdateService);
        this.positionStatusService = positionStatusService;
    }

    @Override
    protected Class<PositionStatusDTO> getEntityType() {
        return PositionStatusDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Position Status";
    }

    @Override
    protected Object processEntity(PositionStatusDTO status, UUID transactionId) {
        PositionStatusCreateDTO createDTO = new PositionStatusCreateDTO();
        // Map fields from PositionStatusDTO to PositionStatusCreateDTO as needed
        return positionStatusService.createPositionStatus(createDTO);
    }

    @Override
    protected boolean isSuccess(Object result) {
        return result != null;
    }

    @Override
    protected UUID getEntityId(Object result) {
        // Return the created entity ID if available
        return null;
    }
}
