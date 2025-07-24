package tech.stl.hcm.common.db.entities;

import lombok.*;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "candidate_reference", schema = "hcm")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reference_id")
    private Integer referenceId;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Column(name = "reference_name", nullable = false, length = 200)
    private String referenceName;

    @Column(name = "relationship", length = 100)
    private String relationship;

    @Column(name = "company", length = 200)
    private String company;

    @Column(name = "position", length = 200)
    private String position;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

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
} 