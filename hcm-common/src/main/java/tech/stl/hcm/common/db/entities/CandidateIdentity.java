package tech.stl.hcm.common.db.entities;

import lombok.*;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "candidate_identity", schema = "hcm")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identity_id")
    private Integer identityId;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Column(name = "id_type_id", nullable = false)
    private Integer idTypeId;

    @Column(name = "id_number", nullable = false, length = 100)
    private String idNumber;

    @Column(name = "issuing_country", length = 100)
    private String issuingCountry;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "verification_date")
    private Instant verificationDate;

    @Column(name = "verified_by")
    private UUID verifiedBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "updated_by", nullable = false)
    private UUID updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_id", insertable = false, updatable = false)
    private IdType idType;
} 