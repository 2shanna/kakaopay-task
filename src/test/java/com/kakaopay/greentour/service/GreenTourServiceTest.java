package com.kakaopay.greentour.service;

import com.kakaopay.greentour.domain.GreenTour;
import com.kakaopay.greentour.domain.Program;
import com.kakaopay.greentour.domain.Region;
import com.kakaopay.greentour.dto.DetailKeywordResponse;
import com.kakaopay.greentour.dto.EcoInformation;
import com.kakaopay.greentour.dto.OutlineKeywordResponse;
import com.kakaopay.greentour.dto.SearchRegionResponse;
import com.kakaopay.greentour.repository.GreenTourRepository;
import com.kakaopay.greentour.repository.ProgramRepository;
import com.kakaopay.greentour.repository.RegionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class GreenTourServiceTest {

    @InjectMocks
    private GreenTourService greenTourService;

    @Mock
    private RegionService regionService;

    @Mock
    private ProgramService programService;

    @Mock
    private GreenTourRepository greenTourRepository;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private ProgramRepository programRepository;

    @Test
    @DisplayName("save all greentour data")
    void saveAll() throws Exception {
        Region region1 = new Region("reg00001", "강원도", "강원", "", "", new ArrayList<>());
        Region region2 = new Region("reg00002", "속초", "강원", "속초", "", new ArrayList<>());
        Region region3 = new Region("reg00003", "양양", "강원", "양양", "", new ArrayList<>());
        Region region4 = new Region("reg00004", "고성", "강원", "고성", "", new ArrayList<>());

        Program program1 = new Program(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "강원도 속초 양양 고성",
                "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");
        Program program2 = new Program(200, "테스트 프로그램222", "문화생태체험, 국립공원",
                "강원도", "강원도",
                "두번째 테스트 프로그램입니다", "두번째 테스트 프로그램입니다. 디테일입니다22222.");
        List<Program> programList = List.of(program1, program2);

        GreenTour greenTour1 = new GreenTour(1L, program1, region1);
        GreenTour greenTour2 = new GreenTour(2L, program2, region1);
        GreenTour greenTour3 = new GreenTour(3L, program1, region2);
        GreenTour greenTour4 = new GreenTour(4L, program1, region3);
        GreenTour greenTour5 = new GreenTour(5L, program1, region4);
        List<GreenTour> greenTourList = List.of(greenTour1, greenTour2, greenTour3, greenTour4, greenTour5);

        when(regionRepository.findByRegionName("강원도")).thenReturn(Optional.of(region1));
        when(regionRepository.findByRegionName("속초")).thenReturn(Optional.of(region2));
        when(regionRepository.findByRegionName("양양")).thenReturn(Optional.of(region3));
        when(regionRepository.findByRegionName("고성")).thenReturn(Optional.of(region4));
        when(greenTourRepository.findByProgramAndRegion(program1.getProgramId(), region1.getRegionCd())).thenReturn(Optional.empty());
        when(greenTourRepository.findByProgramAndRegion(program2.getProgramId(), region1.getRegionCd())).thenReturn(Optional.of(greenTour2));
        when(greenTourRepository.findByProgramAndRegion(program1.getProgramId(), region2.getRegionCd())).thenReturn(Optional.of(greenTour3));
        when(greenTourRepository.findByProgramAndRegion(program1.getProgramId(), region3.getRegionCd())).thenReturn(Optional.of(greenTour4));
        when(greenTourRepository.findByProgramAndRegion(program1.getProgramId(), region4.getRegionCd())).thenReturn(Optional.empty());
        when(greenTourRepository.saveAndFlush(greenTour5)).thenReturn(greenTour5);

        List<GreenTour> result = greenTourService.saveAll(programList);
        assertEquals(greenTourList.size(), result.size());
    }

    @Test
    @DisplayName("find eco information by region code")
    void findEcoInfoByRegionCd() throws Exception {
        String regionCd = "reg00001";
        Region region1 = new Region("reg00001", "강원도", "강원", "", "", new ArrayList<>());

        Program program1 = new Program(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "강원도 속초 양양 고성",
                "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");
        Program program2 = new Program(200, "테스트 프로그램222", "문화생태체험, 국립공원",
                "강원도", "강원도",
                "두번째 테스트 프로그램입니다", "두번째 테스트 프로그램입니다. 디테일입니다22222.");
        List<Program> programList = List.of(program1, program2);

        EcoInformation ecoInfo1 = new EcoInformation(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");
        EcoInformation ecoInfo2 = new EcoInformation(201, "테스트 프로그램222", "문화생태체험, 국립공원",
                "강원도", "두번째 테스트 프로그램입니다", "두번째 테스트 프로그램입니다. 디테일입니다22222.");
        List<EcoInformation> ecoInformationList = List.of(ecoInfo1, ecoInfo2);

        GreenTour greenTour1 = new GreenTour(1L, program1, region1);
        GreenTour greenTour2 = new GreenTour(2L, program2, region1);
        List<GreenTour> greenTourList = List.of(greenTour1, greenTour2);

        when(greenTourRepository.findAllByRegion(regionCd)).thenReturn(greenTourList);

        List<EcoInformation> result = greenTourService.findEcoInfoByRegionCd(regionCd);
        assertEquals(ecoInformationList.size(), result.size());
    }

    @Test
    @DisplayName("register eco information")
    void registerEcoInfo() throws Exception {
        EcoInformation ecoInfo = new EcoInformation(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");

        Program program = new Program(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "강원도 속초 양양 고성",
                "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");

        when(programRepository.existsById(ecoInfo.getProgramId())).thenReturn(false);
        when(programService.save(ecoInfo)).thenReturn(program);

        EcoInformation result = greenTourService.registerEcoInfo(ecoInfo);
        assertEquals(program.getProgramId(), result.getProgramId());
    }

    @Test
    @DisplayName("update eco information")
    void updateEcoInfo() throws Exception {
        EcoInformation ecoInfo = new EcoInformation(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");

        Program program = new Program(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "강원도 속초 양양 고성",
                "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");
        program.setGreenTours(List.of(new GreenTour(1L, program, new Region("강원도"))));

        when(programRepository.existsById(ecoInfo.getProgramId())).thenReturn(true);
        when(programService.save(ecoInfo)).thenReturn(program);

        EcoInformation result = greenTourService.updateEcoInfo(ecoInfo);
        assertEquals(program.getProgramId(), result.getProgramId());
        verify(greenTourRepository, atLeastOnce()).deleteById(any());
    }

    @Test
    @DisplayName("find region & program greentour by region name")
    void findByRegionName() throws Exception {
        String regionName = "강원도";
        Region region = new Region("reg00001", "강원도", "강원", "", "", new ArrayList<>());

        Program program1 = new Program(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "강원도 속초 양양 고성",
                "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");
        Program program2 = new Program(200, "테스트 프로그램222", "문화생태체험, 국립공원",
                "강원도", "강원도",
                "두번째 테스트 프로그램입니다", "두번째 테스트 프로그램입니다. 디테일입니다22222.");
        List<Program> programList = List.of(program1, program2);

        GreenTour greenTour1 = new GreenTour(1L, program1, region);
        GreenTour greenTour2 = new GreenTour(2L, program2, region);
        List<GreenTour> greenTourList = List.of(greenTour1, greenTour2);
        when(regionRepository.findByRegionName(regionName)).thenReturn(Optional.of(region));
        when(greenTourRepository.findAllByRegion(region.getRegionCd())).thenReturn(greenTourList);

        SearchRegionResponse result = greenTourService.findByRegionName(regionName);
        assertEquals(region.getRegionCd(), result.getRegionCd());
        assertEquals(programList.size(), result.getProgramList().size());
    }

    @Test
    @DisplayName("find program outline by keyword")
    void findProgramByOutlineKeyword() throws Exception {
        String keyword = "국립공원";

        Program program1 = new Program(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도", "강원도",
                "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");
        Program program2 = new Program(200, "테스트 프로그램222", "문화생태체험, 국립공원",
                "강원도", "강원도",
                "두번째 테스트 프로그램입니다", "두번째 테스트 프로그램입니다. 디테일입니다22222.");
        List<Program> programList = List.of(program1, program2);
        when(programRepository.findByOutlineContaining(keyword)).thenReturn(programList);

        OutlineKeywordResponse result = greenTourService.findProgramByOutlineKeyword(keyword);
        assertEquals(keyword, result.getKeyword());
        assertEquals("강원도", result.getProgramList().get(0).getRegion());
        assertEquals(2, result.getProgramList().get(0).getCount());
    }

    @Test
    @DisplayName("find program detail by keyword")
    void findProgramByDetailKeyword() throws Exception {
        String keyword = "디테일";

        Program program1 = new Program(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도", "강원도",
                "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");
        Program program2 = new Program(200, "테스트 프로그램222", "문화생태체험, 국립공원",
                "강원도", "강원도",
                "두번째 테스트 프로그램입니다", "두번째 테스트 프로그램입니다. 디테일입니다22222.");
        List<Program> programList = List.of(program1, program2);
        when(programRepository.findByDetailContaining(keyword)).thenReturn(programList);

        DetailKeywordResponse result = greenTourService.findProgramByDetailKeyword(keyword);
        assertEquals(keyword, result.getKeyword());
        assertEquals(2, result.getCount());
    }
}