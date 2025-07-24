package tech.stl.hcm.common.mapper;

import org.springframework.stereotype.Component;
import tech.stl.hcm.common.db.entities.Country;
import tech.stl.hcm.common.dto.CountryDTO;

@Component
public class CountryMapper {

    public CountryDTO toDTO(Country entity) {
        if (entity == null) {
            return null;
        }

        return CountryDTO.builder()
                .countryId(entity.getCountryId())
                .countryName(entity.getCountryName())
                .countryCode(entity.getCountryCode())
                .phoneCode(entity.getPhoneCode())
                .currencyCode(entity.getCurrencyCode())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public Country toEntity(CountryDTO dto) {
        if (dto == null) {
            return null;
        }

        return Country.builder()
                .countryId(dto.getCountryId())
                .countryName(dto.getCountryName())
                .countryCode(dto.getCountryCode())
                .phoneCode(dto.getPhoneCode())
                .currencyCode(dto.getCurrencyCode())
                .isActive(dto.getIsActive())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .updatedAt(dto.getUpdatedAt())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }
} 