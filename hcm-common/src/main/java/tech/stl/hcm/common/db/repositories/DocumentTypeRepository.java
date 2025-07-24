package tech.stl.hcm.common.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.stl.hcm.common.db.entities.DocumentType;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer> {
    
    @Query("SELECT dt FROM DocumentType dt WHERE dt.tenantId = :tenantId ORDER BY dt.typeName")
    List<DocumentType> findByTenantId(@Param("tenantId") UUID tenantId);
    
    @Query("SELECT dt FROM DocumentType dt WHERE dt.tenantId = :tenantId AND dt.isRequired = true ORDER BY dt.typeName")
    List<DocumentType> findRequiredByTenantId(@Param("tenantId") UUID tenantId);
    
    @Query("SELECT dt FROM DocumentType dt WHERE dt.tenantId = :tenantId AND dt.typeName = :typeName")
    DocumentType findByTenantIdAndTypeName(@Param("tenantId") UUID tenantId, @Param("typeName") String typeName);
} 