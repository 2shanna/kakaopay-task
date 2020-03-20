package com.kakaopay.greentour.repository;

import com.kakaopay.greentour.domain.GreenTour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GreenTourRepository extends JpaRepository<GreenTour, Long> {

    @Query(value = "SELECT * FROM GREEN_TOUR g WHERE g.region_cd = :region_cd", nativeQuery = true)
    List<GreenTour> findAllByRegion(@Param("region_cd") String regionCd);

    @Query(value = "SELECT * FROM GREEN_TOUR g WHERE g.program_id = :program_id", nativeQuery = true)
    List<GreenTour> findAllByProgram(@Param("program_id") long programId);

    @Query(value = "SELECT * FROM GREEN_TOUR g WHERE g.program_id = :program_id AND g.region_cd = :region_cd", nativeQuery = true)
    Optional<GreenTour> findByProgramAndRegion(@Param("program_id") long programId, @Param("region_cd") String regionCd);
}
