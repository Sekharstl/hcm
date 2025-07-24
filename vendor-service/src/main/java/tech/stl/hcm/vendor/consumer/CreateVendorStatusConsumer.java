package tech.stl.hcm.vendor.consumer;

import org.springframework.stereotype.Component;
import tech.stl.hcm.vendor.service.VendorStatusService;
import tech.stl.hcm.common.dto.VendorStatusDTO;
import tech.stl.hcm.common.dto.helpers.VendorStatusCreateDTO;
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
public class CreateVendorStatusConsumer extends BaseTransactionConsumer<VendorStatusCreateDTO> {
    private final VendorStatusService vendorStatusService;

    public CreateVendorStatusConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, 
                                     VendorStatusService vendorStatusService) {
        super(objectMapper, transactionUpdateService);
        this.vendorStatusService = vendorStatusService;
    }

    @Override
    protected Class<VendorStatusCreateDTO> getEntityType() {
        return VendorStatusCreateDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Vendor Status";
    }

    @Override
    protected Object processEntity(VendorStatusCreateDTO status, UUID transactionId) {
        return vendorStatusService.createVendorStatus(status);
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