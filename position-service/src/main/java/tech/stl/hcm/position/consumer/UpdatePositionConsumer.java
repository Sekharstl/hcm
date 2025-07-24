package tech.stl.hcm.position.consumer;

import org.springframework.stereotype.Component;
import tech.stl.hcm.position.service.PositionService;
import tech.stl.hcm.common.dto.PositionDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${position.kafka.topic.update}",
        groupId = "${position.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "position.kafka.enable"
)
public class UpdatePositionConsumer extends BaseTransactionConsumer<PositionDTO> {
    private final PositionService positionService;

    public UpdatePositionConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, PositionService positionService) {
        super(objectMapper, transactionUpdateService);
        this.positionService = positionService;
    }

    @Override
    protected Class<PositionDTO> getEntityType() {
        return PositionDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Position Update";
    }

    @Override
    protected Object processEntity(PositionDTO position, UUID transactionId) {
        positionService.updatePosition(position);
        return position;
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
        // Position ID is Integer, so we return null for UUID
        return null;
    }
}
