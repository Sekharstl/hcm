package tech.stl.hcm.position.consumer;

import org.springframework.stereotype.Component;
import tech.stl.hcm.position.service.PositionService;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${position.kafka.topic.delete}",
        groupId = "${position.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "position.kafka.enable"
)
public class DeletePositionConsumer extends BaseTransactionConsumer<Integer> {
    private final PositionService positionService;

    public DeletePositionConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, PositionService positionService) {
        super(objectMapper, transactionUpdateService);
        this.positionService = positionService;
    }

    @Override
    protected Class<Integer> getEntityType() {
        return Integer.class;
    }

    @Override
    protected String getEntityName() {
        return "Position Delete";
    }

    @Override
    protected Object processEntity(Integer positionId, UUID transactionId) {
        positionService.deletePosition(positionId);
        return positionId;
    }

    @Override
    protected boolean isSuccess(Object result) {
        return result instanceof Integer;
    }

    @Override
    protected UUID getEntityId(Object result) {
        // Position ID is Integer, so we return null for UUID
        return null;
    }
}
