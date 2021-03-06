package com.kakaopay.greentour.service;

import com.kakaopay.greentour.domain.Region;
import com.kakaopay.greentour.dto.EcoInformation;
import com.kakaopay.greentour.repository.RegionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class RegionServiceTest {

    @InjectMocks
    private RegionService regionService;

    @Mock
    private RegionRepository regionRepository;

    @Test
    @DisplayName("edit region string")
    void parseRegionName() throws Exception {
        EcoInformation ecoInfo = new EcoInformation(200, "테스트 프로그램", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");

        String result = regionService.parseRegionName(ecoInfo);
        assertEquals("강원도 속초 양양 고성", result);
    }

    @Test
    @DisplayName("parse local data which received from external api")
    void getLocalAddress() throws Exception {
        String regionName1 = "풍기읍";
        String regionName2 = "법주사로";

        Region result1 = regionService.getLocalAddress(regionName1);
        assertEquals("풍기읍", result1.getRegionName());
        assertEquals("경북", result1.getRegion1DepthName());
        assertEquals("영주시", result1.getRegion2DepthName());
        assertEquals("풍기읍", result1.getRegion3DepthName());

        Region result2 = regionService.getLocalAddress(regionName2);
        assertEquals("법주사로", result2.getRegionName());
        assertEquals("충북", result2.getRegion1DepthName());
        assertEquals("보은군", result2.getRegion2DepthName());
        assertEquals("속리산면", result2.getRegion3DepthName());
    }

    @Test
    @DisplayName("save all region data")
    void saveAll() throws Exception {
        EcoInformation ecoInfo1 = new EcoInformation(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");
        EcoInformation ecoInfo2 = new EcoInformation(201, "테스트 프로그램222", "문화생태체험, 국립공원",
                "강원도", "두번째 테스트 프로그램입니다", "두번째 테스트 프로그램입니다. 디테일입니다22222.");
        List<EcoInformation> ecoInformationList = List.of(ecoInfo1, ecoInfo2);

        Region region1 = new Region("강원도");
        Region region2 = new Region("속초");
        Region region3 = new Region("양양");
        Region region4 = new Region("고성");
        List<Region> regionList = List.of(region1, region2, region3, region4);

        when(regionRepository.findAll()).thenReturn(regionList);

        List<Region> result = regionService.saveAll(ecoInformationList);
        assertEquals(regionList.size(), result.size());
        verify(regionRepository, times(regionList.size())).saveAndFlush(any());
    }

    @Test
    @DisplayName("save one region data")
    void save() throws Exception {
        EcoInformation ecoInfo = new EcoInformation(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");

        Region region1 = new Region("강원도");
        Region region2 = new Region("속초");
        Region region3 = new Region("양양");
        Region region4 = new Region("고성");
        List<Region> regionList = List.of(region1, region2, region3, region4);

        when(regionRepository.findAll()).thenReturn(regionList);

        List<Region> result = regionService.save(ecoInfo);
        assertEquals(regionList.size(), result.size());
        verify(regionRepository, times(regionList.size())).saveAndFlush(any());
    }
}