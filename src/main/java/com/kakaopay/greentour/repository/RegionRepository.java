package com.kakaopay.greentour.repository;

import com.kakaopay.greentour.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByRegionCd(String regionCd);

    //    @Transactional
//    long deleteAllByRegionNameIn(List<String> regionNames);
    Optional<Region> findByRegionName(String regionName);


    //    List<Region> findByRegionNameIn(List<String> regionNames);
    List<Region> findByRegionNameAndRegion1DepthNameAndAndRegion2DepthNameAndRegion3DepthName(
            @Param("region_name") String regionName, @Param("region_1depth_name") String region1DepthName,
            @Param("region_2depth_name") String region2DepthName, @Param("region_3depth_name") String region3DepthName);

//    @Query("SELECT e from Employee e where e.employeeName =:name AND e.employeeRole =:role")
//    List<Employee> findByNameAndRole(@Param("name") String name,@Param("role")String role);
//
//    @Query(value = "SELECT * from Employee e where e.employeeName =:name AND e.employeeRole = :role ", nativeQuery = true)
//    List<Employee> findByNameAndRoleNative(@Param("name") String name, @Param("role")String role);
}
