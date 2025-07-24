package tech.stl.hcm.position.consumer;

import org.springframework.stereotype.Component;
import tech.stl.hcm.position.service.PositionService;
import tech.stl.hcm.common.dto.PositionDTO;
import tech.stl.hcm.common.dto.helpers.PositionCreateDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import tech.stl.hcm.common.mapper.PositionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${position.kafka.topic}",
        groupId = "${position.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "position.kafka.enable"
)
public class CreatePositionConsumer extends BaseTransactionConsumer<PositionCreateDTO> {
    private final PositionService positionService;

    public CreatePositionConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, PositionService positionService) {
        super(objectMapper, transactionUpdateService);
        this.positionService = positionService;
    }

    @Override
    protected Class<PositionCreateDTO> getEntityType() {
        return PositionCreateDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Position";
    }

    @Override
    protected Object processEntity(PositionCreateDTO position, UUID transactionId) {
        return positionService.createPosition(position);
    }

    @Override
    protected boolean isSuccess(Object result) {
        if (result instanceof PositionDTO) {
            PositionDTO position = (PositionDTO) result;
            return position != null && position.getPositionId() != null;
        }
        return false;
    }

    @Override
    protected UUID getEntityId(Object result) {
        if (result instanceof PositionDTO) {
            PositionDTO position = (PositionDTO) result;
            // Position ID is Integer, so we return null for UUID
            // The transaction will still be tracked but without entity ID
            return null;
        }
        return null;
    }
}
