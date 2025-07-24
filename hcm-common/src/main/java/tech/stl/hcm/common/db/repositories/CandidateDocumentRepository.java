package tech.stl.hcm.common.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.stl.hcm.common.db.entities.CandidateDocument;

import java.util.List;
import java.util.UUID;

@Repository
public interface CandidateDocumentRepository extends JpaRepository<CandidateDocument, Integer> {
    
    @Query("SELECT cd FROM CandidateDocument cd WHERE cd.candidateId = :candidateId ORDER BY cd.uploadDate DESC")
    List<CandidateDocument> findByCandidateId(@Param("candidateId") UUID candidateId);
    
    @Query("SELECT cd FROM CandidateDocument cd WHERE cd.candidateId = :candidateId AND cd.documentTypeId = :documentTypeId")
    List<CandidateDocument> findByCandidateIdAndDocumentTypeId(@Param("candidateId") UUID candidateId, @Param("documentTypeId") Integer documentTypeId);
    
    @Query("SELECT cd FROM CandidateDocument cd WHERE cd.candidateId = :candidateId AND cd.isVerified = true ORDER BY cd.uploadDate DESC")
    List<CandidateDocument> findVerifiedByCandidateId(@Param("candidateId") UUID candidateId);
    
    @Query("SELECT COUNT(cd) FROM CandidateDocument cd WHERE cd.candidateId = :candidateId AND cd.documentTypeId = :documentTypeId")
    Long countByCandidateIdAndDocumentTypeId(@Param("candidateId") UUID candidateId, @Param("documentTypeId") Integer documentTypeId);
} 