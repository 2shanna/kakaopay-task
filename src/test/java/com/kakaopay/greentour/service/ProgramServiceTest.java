package com.kakaopay.greentour.service;

import com.kakaopay.greentour.domain.Program;
import com.kakaopay.greentour.dto.EcoInformation;
import com.kakaopay.greentour.repository.ProgramRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProgramServiceTest {

    @InjectMocks
    private ProgramService programService;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private RegionService regionService;

    @Test
    @DisplayName("save all program data")
    void saveAll() throws Exception {
        EcoInformation ecoInfo1 = new EcoInformation(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");
        EcoInformation ecoInfo2 = new EcoInformation(201, "테스트 프로그램222", "문화생태체험, 국립공원",
                "강원도", "두번째 테스트 프로그램입니다", "두번째 테스트 프로그램입니다. 디테일입니다22222.");
        List<EcoInformation> ecoInformationList = List.of(ecoInfo1, ecoInfo2);

        Program program1 = new Program();
        program1.setProgramId(200);
        Program program2 = new Program();
        program2.setProgramId(201);
        List<Program> programList = List.of(program1, program2);

        when(regionService.parseRegionName(ecoInfo1)).thenReturn(program1.getEditedRegion());
        when(regionService.parseRegionName(ecoInfo2)).thenReturn(program2.getEditedRegion());
        when(programRepository.saveAll(any())).thenReturn(programList);

        List<Program> result = programService.saveAll(ecoInformationList);
        assertEquals(result.size(), programList.size());
        verify(programRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("save(update) one program data")
    void save_update() throws Exception {
        EcoInformation ecoInfo = new EcoInformation(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");

        Program program = new Program(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "강원도 속초 양양 고성",
                "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");

        when(regionService.parseRegionName(ecoInfo)).thenReturn(program.getEditedRegion());
        when(programRepository.findById(ecoInfo.getProgramId())).thenReturn(Optional.of(program));
        when(programRepository.saveAndFlush(program)).thenReturn(program);

        Program result = programService.save(ecoInfo);
        assertEquals(result.getProgramId(), program.getProgramId());
        verify(programRepository, times(1)).saveAndFlush(any());
    }


    @Test
    @DisplayName("save(register) one program data")
    void save_insert() throws Exception {
        EcoInformation ecoInfo = new EcoInformation(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");

        Program program = new Program(200, "테스트 프로그램111", "자연휴양림, 국립공원",
                "강원도 속초, 양양, 고성", "강원도 속초 양양 고성",
                "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");

        when(regionService.parseRegionName(ecoInfo)).thenReturn(program.getEditedRegion());
        when(programRepository.findById(ecoInfo.getProgramId())).thenReturn(Optional.empty());
        when(programRepository.saveAndFlush(any())).thenReturn(program);

        Program result = programService.save(ecoInfo);
        assertEquals(result.getProgramId(), program.getProgramId());
        verify(programRepository, times(1)).saveAndFlush(any());
    }
}