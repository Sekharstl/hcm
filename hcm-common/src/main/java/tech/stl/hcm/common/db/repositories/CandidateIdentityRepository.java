package tech.stl.hcm.common.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.stl.hcm.common.db.entities.CandidateIdentity;

import java.util.List;
import java.util.UUID;

@Repository
public interface CandidateIdentityRepository extends JpaRepository<CandidateIdentity, Integer> {
    
    @Query("SELECT ci FROM CandidateIdentity ci WHERE ci.candidateId = :candidateId ORDER BY ci.createdAt DESC")
    List<CandidateIdentity> findByCandidateId(@Param("candidateId") UUID candidateId);
    
    @Query("SELECT ci FROM CandidateIdentity ci WHERE ci.candidateId = :candidateId AND ci.idTypeId = :idTypeId")
    List<CandidateIdentity> findByCandidateIdAndIdTypeId(@Param("candidateId") UUID candidateId, @Param("idTypeId") Integer idTypeId);
    
    @Query("SELECT ci FROM CandidateIdentity ci WHERE ci.candidateId = :candidateId AND ci.isVerified = true ORDER BY ci.createdAt DESC")
    List<CandidateIdentity> findVerifiedByCandidateId(@Param("candidateId") UUID candidateId);
} 