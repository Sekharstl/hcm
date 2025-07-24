package tech.stl.hcm.vendor.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.stl.hcm.vendor.service.VendorService;
import tech.stl.hcm.common.dto.VendorDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${vendor.kafka.topic.update}",
        groupId = "${vendor.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "vendor.kafka.enable"
)
public class UpdateVendorConsumer extends BaseTransactionConsumer<VendorDTO> {
    private final VendorService vendorService;

    public UpdateVendorConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, VendorService vendorService) {
        super(objectMapper, transactionUpdateService);
        this.vendorService = vendorService;
    }

    @Override
    protected Class<VendorDTO> getEntityType() {
        return VendorDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Vendor Update";
    }

    @Override
    protected Object processEntity(VendorDTO vendor, UUID transactionId) {
        vendorService.updateVendor(vendor);
        return vendor;
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