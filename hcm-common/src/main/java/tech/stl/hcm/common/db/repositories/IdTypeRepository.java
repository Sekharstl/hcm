package tech.stl.hcm.common.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.stl.hcm.common.db.entities.IdType;

import java.util.List;
import java.util.UUID;

@Repository
public interface IdTypeRepository extends JpaRepository<IdType, Integer> {
    
    @Query("SELECT it FROM IdType it WHERE it.tenantId = :tenantId ORDER BY it.typeName")
    List<IdType> findByTenantId(@Param("tenantId") UUID tenantId);
    
    @Query("SELECT it FROM IdType it WHERE it.tenantId = :tenantId AND it.isRequired = true ORDER BY it.typeName")
    List<IdType> findRequiredByTenantId(@Param("tenantId") UUID tenantId);
    
    @Query("SELECT it FROM IdType it WHERE it.tenantId = :tenantId AND it.typeName = :typeName")
    IdType findByTenantIdAndTypeName(@Param("tenantId") UUID tenantId, @Param("typeName") String typeName);
} 