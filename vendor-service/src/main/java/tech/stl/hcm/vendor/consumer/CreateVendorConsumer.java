package tech.stl.hcm.vendor.consumer;

import org.springframework.stereotype.Component;
import tech.stl.hcm.vendor.service.VendorService;
import tech.stl.hcm.common.dto.helpers.VendorCreateDTO;
import tech.stl.hcm.common.dto.VendorDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${vendor.kafka.topic}",
        groupId = "${vendor.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "vendor.kafka.enable"
)
public class CreateVendorConsumer extends BaseTransactionConsumer<VendorCreateDTO> {
    private final VendorService vendorService;

    public CreateVendorConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, VendorService vendorService) {
        super(objectMapper, transactionUpdateService);
        this.vendorService = vendorService;
    }

    @Override
    protected Class<VendorCreateDTO> getEntityType() {
        return VendorCreateDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Vendor";
    }

    @Override
    protected Object processEntity(VendorCreateDTO vendor, UUID transactionId) {
        return vendorService.createVendor(vendor);
    }

    @Override
    protected boolean isSuccess(Object result) {
        if (result instanceof VendorDTO) {
            VendorDTO vendor = (VendorDTO) result;
            return vendor != null && vendor.getVendorId() != null;
        }
        return false;
    }

    @Override
    protected UUID getEntityId(Object result) {
        if (result instanceof VendorDTO) {
            VendorDTO vendor = (VendorDTO) result;
            return vendor.getVendorId();
        }
        return null;
    }
} 