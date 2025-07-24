package tech.stl.hcm.common.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.stl.hcm.common.db.entities.CandidateReference;

import java.util.List;
import java.util.UUID;

@Repository
public interface CandidateReferenceRepository extends JpaRepository<CandidateReference, Integer> {
    
    @Query("SELECT cr FROM CandidateReference cr WHERE cr.candidateId = :candidateId ORDER BY cr.createdAt DESC")
    List<CandidateReference> findByCandidateId(@Param("candidateId") UUID candidateId);
    
    @Query("SELECT cr FROM CandidateReference cr WHERE cr.candidateId = :candidateId AND cr.isVerified = true ORDER BY cr.createdAt DESC")
    List<CandidateReference> findVerifiedByCandidateId(@Param("candidateId") UUID candidateId);
} 