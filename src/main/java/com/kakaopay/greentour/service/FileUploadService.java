package com.kakaopay.greentour.service;

import com.kakaopay.greentour.domain.Program;
import com.kakaopay.greentour.domain.Region;
import com.kakaopay.greentour.dto.EcoInformation;
import com.kakaopay.greentour.dto.EcoInformationResponse;
import com.kakaopay.greentour.repository.GreenTourRepository;
import com.kakaopay.greentour.repository.ProgramRepository;
import com.kakaopay.greentour.repository.RegionRepository;
import com.kakaopay.greentour.util.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileUploadService {

    @Autowired
    private RegionService regionService;

    @Autowired
    private ProgramService programService;

    @Autowired
    private GreenTourService greenTourService;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private GreenTourRepository greenTourRepository;

    public EcoInformationResponse save(MultipartFile file) throws Exception {
        EcoInformationResponse response = new EcoInformationResponse();

        // first, delete whole data from table
        programRepository.deleteAll();
        programRepository.flush();
        regionRepository.deleteAll();
        regionRepository.flush();
        greenTourRepository.deleteAll();
        greenTourRepository.flush();

        // file read
        List<EcoInformation> ecoInformationList = new CsvUtil().readAndParse(file);

        // create program data
        List<Program> programList = programService.saveAll(ecoInformationList);
        response.setProgramList(programList);

        // create region data
        List<Region> regionList = regionService.saveAll(ecoInformationList);
        response.setRegionList(regionList);

        // create greenTour data
        greenTourService.saveAll(programList);

        return response;
    }
}