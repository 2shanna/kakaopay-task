package com.kakaopay.greentour.service;

import com.kakaopay.greentour.domain.GreenTour;
import com.kakaopay.greentour.domain.Program;
import com.kakaopay.greentour.domain.Region;
import com.kakaopay.greentour.dto.DetailKeywordResponse;
import com.kakaopay.greentour.dto.EcoInformation;
import com.kakaopay.greentour.dto.OutlineKeywordResponse;
import com.kakaopay.greentour.dto.RegionCountProgram;
import com.kakaopay.greentour.dto.SearchRegionProgram;
import com.kakaopay.greentour.dto.SearchRegionResponse;
import com.kakaopay.greentour.repository.GreenTourRepository;
import com.kakaopay.greentour.repository.ProgramRepository;
import com.kakaopay.greentour.repository.RegionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                    GreenTour tour;
                    Optional<GreenTour> greenTourOpt = greenTourRepository.findByProgramAndRegion(program.getProgramId(), region.getRegionCd());
                    if (greenTourOpt.isEmpty()) {
                        tour = greenTourRepository.saveAndFlush(new GreenTour(program, region));
                    } else {
                        tour = greenTourOpt.get();
                    }
                    greenTourList.add(tour);
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

    private EcoInformation mapProgramToDto(Program program) {
        return new EcoInformation(program.getProgramId(), program.getProgramName(),
                program.getTheme(), program.getOriginalRegion(), program.getOutline(), program.getDetail());
    }

    public EcoInformation registerEcoInfo(EcoInformation ecoInfo) {
        // program data existence check
        if (programRepository.existsById(ecoInfo.getProgramId())) {
            return null;
        }

        // create program data
        Program program = programService.save(ecoInfo);

        // create region data
        regionService.save(ecoInfo);

        // create greenTour data
        saveAll(List.of(program));

        return mapProgramToDto(program);
    }

    public EcoInformation updateEcoInfo(EcoInformation ecoInfo) {
        if (!programRepository.existsById(ecoInfo.getProgramId())) {
            return null;
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

        return mapProgramToDto(program);
    }

    public SearchRegionResponse findByRegionName(String regionName) {
        SearchRegionResponse response = new SearchRegionResponse();

        Optional<Region> regionOpt = regionRepository.findByRegionName(regionName);
        if (regionOpt.isPresent()) {
            Region region = regionOpt.get();
            response.setRegionCd(region.getRegionCd());

            List<GreenTour> tourList = greenTourRepository.findAllByRegion(region.getRegionCd());
            List<SearchRegionProgram> programList = new ArrayList<>();
            for (GreenTour tour : tourList) {
                Program program = tour.getProgram();
                programList.add(new SearchRegionProgram(program.getProgramName(), program.getTheme()));
            }
            response.setProgramList(programList);
        }
        return response;
    }

    public OutlineKeywordResponse findProgramByOutlineKeyword(String keyword) {
        OutlineKeywordResponse response = new OutlineKeywordResponse();

        List<RegionCountProgram> regionCountProgramList = new ArrayList<>();
        List<Program> programList = programRepository.findByOutlineContaining(keyword);
        Map<String, Long> programMap = new HashMap<>();
        for (Program program : programList) {
            Long cnt = programMap.getOrDefault(program.getOriginalRegion(), 0L);
            programMap.put(program.getOriginalRegion(), cnt + 1);
        }
        programMap.forEach((key, value) ->
                regionCountProgramList.add(new RegionCountProgram(key, value)));

        response.setKeyword(keyword);
        response.setProgramList(regionCountProgramList);

        return response;
    }

    public DetailKeywordResponse findProgramByDetailKeyword(String keyword) {
        DetailKeywordResponse response = new DetailKeywordResponse();

        long count = 0;
        List<Program> programList = programRepository.findByDetailContaining(keyword);
        for (Program program : programList) {
            count += StringUtils.countMatches(program.getDetail(), keyword);
        }

        response.setKeyword(keyword);
        response.setCount(count);

        return response;
    }
}
