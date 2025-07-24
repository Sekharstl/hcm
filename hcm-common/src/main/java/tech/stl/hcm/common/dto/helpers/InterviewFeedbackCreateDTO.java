package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewFeedbackCreateDTO {
    private Integer interviewId;
    private UUID interviewerId;
    private UUID candidateId;
    private String feedback;
    private Integer rating;
    private UUID createdBy;
} 