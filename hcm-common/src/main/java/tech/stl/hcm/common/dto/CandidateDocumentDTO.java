package tech.stl.hcm.common.dto;

import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDocumentDTO {
    private Integer documentId;
    private UUID candidateId;
    private Integer documentTypeId;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private Long fileSize;
    private String mimeType;
    private Instant uploadDate;
    private LocalDate expiryDate;
    private Boolean isVerified;
    private Instant verificationDate;
    private UUID verifiedBy;
    private Instant createdAt;
    private UUID createdBy;
    private Instant updatedAt;
    private UUID updatedBy;
    
    // Additional fields for UI
    private String documentTypeName;
    private String downloadUrl;
} 