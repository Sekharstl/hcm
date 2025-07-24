package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionStatusCreateDTO {
    private String name;
    private String description;
    private UUID createdBy;
} 