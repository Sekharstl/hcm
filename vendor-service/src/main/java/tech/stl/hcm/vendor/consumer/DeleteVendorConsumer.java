package tech.stl.hcm.vendor.consumer;

import org.springframework.stereotype.Component;
import tech.stl.hcm.vendor.service.VendorService;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${vendor.kafka.topic.delete}",
        groupId = "${vendor.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "vendor.kafka.enable"
)
public class DeleteVendorConsumer extends BaseTransactionConsumer<UUID> {
    private final VendorService vendorService;

    public DeleteVendorConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, VendorService vendorService) {
        super(objectMapper, transactionUpdateService);
        this.vendorService = vendorService;
    }

    @Override
    protected Class<UUID> getEntityType() {
        return UUID.class;
    }

    @Override
    protected String getEntityName() {
        return "Vendor Delete";
    }

    @Override
    protected Object processEntity(UUID vendorId, UUID transactionId) {
        vendorService.deleteVendor(vendorId);
        return vendorId;
    }

    @Override
    protected boolean isSuccess(Object result) {
        return result instanceof UUID;
    }

    @Override
    protected UUID getEntityId(Object result) {
        if (result instanceof UUID) {
            return (UUID) result;
        }
        return null;
    }
} 