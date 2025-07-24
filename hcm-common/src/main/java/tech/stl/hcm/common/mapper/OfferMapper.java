package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.Offer;
import tech.stl.hcm.common.dto.OfferDTO;
import tech.stl.hcm.common.dto.helpers.OfferCreateDTO;

import java.time.Instant;
import java.time.LocalDate;

public class OfferMapper {

    public static OfferDTO toDTO(Offer entity) {
        if (entity == null) return null;
        return OfferDTO.builder()
                .offerId(entity.getOfferId())
                .applicationId(entity.getApplicationId())
                .candidateId(entity.getCandidateId())
                .requisitionId(entity.getRequisitionId())
                .statusId(entity.getStatusId())
                .salary(entity.getSalary())
                .currency(entity.getCurrency())
                .offerDate(entity.getOfferDate())
                .acceptanceDeadline(entity.getAcceptanceDeadline())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static Offer toEntity(OfferCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        LocalDate offerDate = dto.getOfferDate() != null ? dto.getOfferDate() : LocalDate.now();
        return Offer.builder()
                .applicationId(dto.getApplicationId())
                .candidateId(dto.getCandidateId())
                .requisitionId(dto.getRequisitionId())
                .statusId(dto.getStatusId())
                .salary(dto.getSalary())
                .currency(dto.getCurrency())
                .offerDate(offerDate)
                .acceptanceDeadline(dto.getAcceptanceDeadline())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }

    public static OfferCreateDTO toCreateDTO(OfferDTO dto) {
        if (dto == null) return null;
        return OfferCreateDTO.builder()
                .applicationId(dto.getApplicationId())
                .candidateId(dto.getCandidateId())
                .requisitionId(dto.getRequisitionId())
                .statusId(dto.getStatusId())
                .salary(dto.getSalary())
                .currency(dto.getCurrency())
                .offerDate(dto.getOfferDate())
                .acceptanceDeadline(dto.getAcceptanceDeadline())
                .createdBy(dto.getCreatedBy())
                .build();
    }
} 