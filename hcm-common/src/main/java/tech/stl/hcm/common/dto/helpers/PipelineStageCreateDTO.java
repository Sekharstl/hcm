package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineStageCreateDTO {
    private Integer requisitionId;
    private String name;
    private Integer sequence;
    private UUID createdBy;
} 