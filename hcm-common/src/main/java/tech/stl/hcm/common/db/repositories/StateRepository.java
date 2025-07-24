package tech.stl.hcm.common.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.stl.hcm.common.db.entities.State;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {
    
    @Query("SELECT s FROM State s WHERE s.countryId = :countryId AND s.isActive = true ORDER BY s.stateName")
    List<State> findActiveStatesByCountryId(@Param("countryId") Integer countryId);
    
    @Query("SELECT s FROM State s WHERE s.countryId = :countryId ORDER BY s.stateName")
    List<State> findByCountryId(@Param("countryId") Integer countryId);
    
    @Query("SELECT s FROM State s WHERE s.stateCode = :stateCode AND s.countryId = :countryId")
    Optional<State> findByStateCodeAndCountryId(@Param("stateCode") String stateCode, @Param("countryId") Integer countryId);
    
    @Query("SELECT s FROM State s WHERE s.stateName ILIKE %:stateName% AND s.isActive = true ORDER BY s.stateName")
    List<State> findByStateNameContainingIgnoreCase(@Param("stateName") String stateName);
    
    @Query("SELECT s FROM State s JOIN Country c ON s.countryId = c.countryId WHERE c.countryCode = :countryCode ORDER BY s.stateName")
    List<State> findByCountryCountryCode(@Param("countryCode") String countryCode);
} 