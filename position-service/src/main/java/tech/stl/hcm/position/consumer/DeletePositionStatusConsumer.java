package tech.stl.hcm.position.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.stl.hcm.position.service.PositionStatusService;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@TopicListener(
        topic = "${positionstatus.kafka.topic.delete}",
        groupId = "${positionstatus.kafka.group-id}",
        valueType = Map.class,
        enableProperty = "positionstatus.kafka.enable"
)
public class DeletePositionStatusConsumer extends BaseTransactionConsumer<Integer> {
    private final PositionStatusService positionStatusService;
    
    public DeletePositionStatusConsumer(PositionStatusService positionStatusService, 
                                     ObjectMapper objectMapper, 
                                     TransactionUpdateService transactionUpdateService) {
        super(objectMapper, transactionUpdateService);
        this.positionStatusService = positionStatusService;
    }

    @Override
    protected Class<Integer> getEntityType() {
        return Integer.class;
    }

    @Override
    protected String getEntityName() {
        return "Position Status";
    }

    @Override
    protected Object processEntity(Integer statusId, UUID transactionId) {
        positionStatusService.deletePositionStatus(statusId);
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
