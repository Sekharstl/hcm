package tech.stl.hcm.vendor.consumer;

import org.springframework.stereotype.Component;
import tech.stl.hcm.vendor.service.VendorStatusService;
import tech.stl.hcm.common.dto.VendorStatusDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${vendorstatus.kafka.topic}",
        groupId = "${vendorstatus.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "vendorstatus.kafka.enable"
)
public class DeleteVendorStatusConsumer extends BaseTransactionConsumer<VendorStatusDTO> {
    private final VendorStatusService vendorStatusService;

    public DeleteVendorStatusConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, 
                                     VendorStatusService vendorStatusService) {
        super(objectMapper, transactionUpdateService);
        this.vendorStatusService = vendorStatusService;
    }

    @Override
    protected Class<VendorStatusDTO> getEntityType() {
        return VendorStatusDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Vendor Status Delete";
    }

    @Override
    protected Object processEntity(VendorStatusDTO status, UUID transactionId) {
        vendorStatusService.deleteVendorStatus(status.getStatusId());
        return status;
    }

    @Override
    protected boolean isSuccess(Object result) {
        if (result instanceof VendorStatusDTO) {
            VendorStatusDTO status = (VendorStatusDTO) result;
            return status != null && status.getStatusId() != null;
        }
        return false;
    }

    @Override
    protected UUID getEntityId(Object result) {
        // Vendor Status ID is Integer, so we return null for UUID
        return null;
    }
} 