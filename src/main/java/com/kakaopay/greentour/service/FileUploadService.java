package com.kakaopay.greentour.service;

import com.kakaopay.greentour.domain.Program;
import com.kakaopay.greentour.domain.Region;
import com.kakaopay.greentour.dto.CsvInfoDto;
import com.kakaopay.greentour.repository.ProgramRepository;
import com.kakaopay.greentour.repository.RegionRepository;
import com.kakaopay.greentour.util.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileUploadService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ProgramRepository programRepository;

    public List<CsvInfoDto> save(MultipartFile file) throws Exception {

        List<CsvInfoDto> csvInfoDtoList = new CsvUtil().readAndParseToDto(file);
        List<Region> regionList = new ArrayList<>();
        List<Program> programList = new ArrayList<>();

        csvInfoDtoList.forEach(info -> {
            regionList.add(new Region(info.getRegion()));
            programList.add(new Program(info.getProgramId(), info.getProgramName(), info.getTheme(),
                    info.getRegion(), info.getOutline(), info.getDetail()));
        });

        regionList.forEach(regionRepository::save);
        programList.forEach(programRepository::save);
        return csvInfoDtoList;
    }
}