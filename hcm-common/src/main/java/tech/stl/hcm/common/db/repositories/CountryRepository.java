package tech.stl.hcm.common.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.stl.hcm.common.db.entities.Country;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    
    @Query("SELECT c FROM Country c WHERE c.isActive = true ORDER BY c.countryName")
    List<Country> findActiveCountries();
    
    @Query("SELECT c FROM Country c WHERE c.countryCode = :countryCode")
    Optional<Country> findByCountryCode(@Param("countryCode") String countryCode);
    
    @Query("SELECT c FROM Country c WHERE c.countryName ILIKE %:countryName% ORDER BY c.countryName")
    List<Country> findByCountryNameContainingIgnoreCase(@Param("countryName") String countryName);
} 