package tech.stl.hcm.common.dto.helpers;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDocumentCreateDTO {
    private UUID candidateId;
    private Integer documentTypeId;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private Long fileSize;
    private String mimeType;
    private LocalDate expiryDate;
    private UUID createdBy;
} 