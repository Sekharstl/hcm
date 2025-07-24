package tech.stl.hcm.common.mapper;

import org.springframework.stereotype.Component;
import tech.stl.hcm.common.db.entities.State;
import tech.stl.hcm.common.dto.StateDTO;

@Component
public class StateMapper {

    public StateDTO toDTO(State entity) {
        if (entity == null) {
            return null;
        }

        return StateDTO.builder()
                .stateId(entity.getStateId())
                .countryId(entity.getCountryId())
                .stateName(entity.getStateName())
                .stateCode(entity.getStateCode())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public State toEntity(StateDTO dto) {
        if (dto == null) {
            return null;
        }

        return State.builder()
                .stateId(dto.getStateId())
                .countryId(dto.getCountryId())
                .stateName(dto.getStateName())
                .stateCode(dto.getStateCode())
                .isActive(dto.getIsActive())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .updatedAt(dto.getUpdatedAt())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }
} 