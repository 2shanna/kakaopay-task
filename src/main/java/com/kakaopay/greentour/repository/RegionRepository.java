package com.kakaopay.greentour.repository;

import com.kakaopay.greentour.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByRegionName(String regionName);

    List<Region> findByRegionNameAndRegion1DepthNameAndAndRegion2DepthNameAndRegion3DepthName(
            @Param("region_name") String regionName, @Param("region_1depth_name") String region1DepthName,
            @Param("region_2depth_name") String region2DepthName, @Param("region_3depth_name") String region3DepthName);

}
