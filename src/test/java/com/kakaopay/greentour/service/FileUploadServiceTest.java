package com.kakaopay.greentour.service;

import com.kakaopay.greentour.dto.EcoInformationResponse;
import com.kakaopay.greentour.repository.GreenTourRepository;
import com.kakaopay.greentour.repository.ProgramRepository;
import com.kakaopay.greentour.repository.RegionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class FileUploadServiceTest {

    @InjectMocks
    private FileUploadService fileUploadService;

    @Mock
    private RegionService regionService;

    @Mock
    private ProgramService programService;

    @Mock
    private GreenTourService greenTourService;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private GreenTourRepository greenTourRepository;

    @Test
    @DisplayName("read file and save the eco information to the database")
    void save() throws Exception {
        String fileDirectory = "./src/test/resources/";
        String fileName = "사전과제2.csv";
        String filePath = String.format("%s%s", fileDirectory, fileName);
        MultipartFile csvFile = new MockMultipartFile(fileName, new FileInputStream(new File(filePath)));

        EcoInformationResponse result = fileUploadService.save(csvFile);
        assertNotNull(result.getProgramList());
        assertNotNull(result.getRegionList());

        verify(programRepository, times(1)).deleteAll();
        verify(regionRepository, times(1)).deleteAll();
        verify(greenTourRepository, times(1)).deleteAll();

        verify(programService, times(1)).saveAll(any());
        verify(regionService, times(1)).saveAll(any());
        verify(greenTourService, times(1)).saveAll(any());

    }
}