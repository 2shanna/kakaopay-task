package com.kakaopay.greentour.service;

import com.kakaopay.greentour.domain.GreenTour;
import com.kakaopay.greentour.domain.Program;
import com.kakaopay.greentour.domain.Region;
import com.kakaopay.greentour.dto.EcoInformation;
import com.kakaopay.greentour.dto.SearchRegionNameResponse;
import com.kakaopay.greentour.repository.GreenTourRepository;
import com.kakaopay.greentour.repository.ProgramRepository;
import com.kakaopay.greentour.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GreenTourService {

    @Autowired
    private RegionService regionService;

    @Autowired
    private ProgramService programService;

    @Autowired
    private GreenTourRepository greenTourRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ProgramRepository programRepository;

    public List<GreenTour> saveAll(List<Program> programList) {
        List<GreenTour> greenTourList = new ArrayList<>();

        programList.forEach(program -> {
            for (String regionName : program.getEditedRegion().split(" ")) {
                Region region;
                Optional<Region> regionOpt = regionRepository.findByRegionName(regionName);
                if (regionOpt.isPresent()) {
                    region = regionOpt.get();
                    Optional<GreenTour> greenTourOpt = greenTourRepository.findByProgramAndRegion(program.getProgramId(), region.getRegionCd());
                    if (greenTourOpt.isEmpty()) {
                        greenTourRepository.saveAndFlush(new GreenTour(program, region));
                    }
                    greenTourList.add(greenTourRepository.findByProgramAndRegion(program.getProgramId(), region.getRegionCd()).get());
                }
            }
        });
        return greenTourList;
    }

    public List<EcoInformation> findEcoInfoByRegionCd(String regionCd) {
        List<EcoInformation> ecoInformationList = new ArrayList<>();
        List<GreenTour> greenTourList = greenTourRepository.findAllByRegion(regionCd);
        for (GreenTour tour : greenTourList) {
            Program program = tour.getProgram();
            EcoInformation programDto = new EcoInformation(program.getProgramId(), program.getProgramName(),
                    program.getTheme(), program.getOriginalRegion(), program.getOutline(), program.getDetail());
            ecoInformationList.add(programDto);
        }
        return ecoInformationList;
    }

    private List<EcoInformation> findEcoInfoByProgramId(long programId) {
        List<EcoInformation> ecoInformationList = new ArrayList<>();
        List<GreenTour> greenTourList = greenTourRepository.findAllByProgram(programId);
        for (GreenTour tour : greenTourList) {
            Program program = tour.getProgram();
            if (ecoInformationList.stream().noneMatch(info -> info.getProgramId() == program.getProgramId())) {
                EcoInformation programDto = new EcoInformation(program.getProgramId(), program.getProgramName(),
                        program.getTheme(), program.getOriginalRegion(), program.getOutline(), program.getDetail());
                ecoInformationList.add(programDto);
            }
        }
        return ecoInformationList;
    }

    public List<EcoInformation> registerEcoInfo(EcoInformation ecoInfo) {
        // program data existence check
        if (programRepository.existsById(ecoInfo.getProgramId())) {
            return null;                // duplication error
        }

        // create program data
        Program program = programService.save(ecoInfo);

        // create region data
        regionService.save(ecoInfo);

        // create greenTour data
        saveAll(List.of(program));

        return findEcoInfoByProgramId(ecoInfo.getProgramId());
    }

    public List<EcoInformation> updateEcoInfo(EcoInformation ecoInfo) {
        if (!programRepository.existsById(ecoInfo.getProgramId())) {
            return null;                // duplication error
        }

        // save program data
        Program program = programService.save(ecoInfo);

        // save region data
        regionService.save(ecoInfo);

        // save greenTour data (delete and save as new)
        List<Long> greenTourIdList = program.getGreenTours().stream().map(GreenTour::getGreenTourId)
                .collect(Collectors.toList());
        greenTourIdList.forEach(id -> greenTourRepository.deleteById(id));
        saveAll(List.of(program));

        return findEcoInfoByProgramId(ecoInfo.getProgramId());
    }

    public SearchRegionNameResponse findByRegionName(String regionName) {
        SearchRegionNameResponse response = new SearchRegionNameResponse();

        Optional<Region> regionOpt = regionRepository.findByRegionName(regionName);
        if (regionOpt.isPresent()) {
            Region region = regionOpt.get();
            region.setGreenTours(new ArrayList<>());
            response.setRegion(region);

            List<GreenTour> tourList = greenTourRepository.findAllByRegion(region.getRegionCd());
            List<Program> programList = new ArrayList<>();
            for (GreenTour tour : tourList) {
                Program program = tour.getProgram();
                program.setGreenTours(new ArrayList<>());
                programList.add(program);
            }
            response.setProgramList(programList);
        }
        return response;
    }
}
