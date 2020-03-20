package com.kakaopay.greentour.service;

import com.kakaopay.greentour.domain.Program;
import com.kakaopay.greentour.dto.EcoInformation;
import com.kakaopay.greentour.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgramService {

    @Autowired
    private
    RegionService regionService;

    @Autowired
    private
    ProgramRepository programRepository;

    public List<Program> saveAll(List<EcoInformation> ecoInformationList) {
        List<Program> programList = ecoInformationList.stream().map(this::parseEcoInfoIntoProgram)
                .collect(Collectors.toList());

        // save and return all program data
        return programRepository.saveAll(programList);
    }

    public Program save(EcoInformation ecoInfo) {

        Optional<Program> programOpt = programRepository.findById(ecoInfo.getProgramId());
        if (programOpt.isPresent()) {
            Program oldProgram = programOpt.get();
            oldProgram.setProgramName(ecoInfo.getProgramName());
            oldProgram.setTheme(ecoInfo.getTheme());
            oldProgram.setOriginalRegion(ecoInfo.getRegion());
            oldProgram.setEditedRegion(regionService.parseRegionName(ecoInfo));
            oldProgram.setOutline(ecoInfo.getOutline());
            oldProgram.setDetail(ecoInfo.getDetail());
            return programRepository.saveAndFlush(oldProgram);
        } else {
            return programRepository.saveAndFlush(parseEcoInfoIntoProgram(ecoInfo));
        }
    }

    private Program parseEcoInfoIntoProgram(EcoInformation ecoInfo) {
        return new Program(
                ecoInfo.getProgramId(), ecoInfo.getProgramName(), ecoInfo.getTheme(), ecoInfo.getRegion(),
                regionService.parseRegionName(ecoInfo), ecoInfo.getOutline(), ecoInfo.getDetail());
    }
}
