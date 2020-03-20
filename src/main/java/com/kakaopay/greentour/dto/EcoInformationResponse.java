package com.kakaopay.greentour.dto;

import com.kakaopay.greentour.domain.GreenTour;
import com.kakaopay.greentour.domain.Program;
import com.kakaopay.greentour.domain.Region;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EcoInformationResponse {

    private List<Region> regionList;

    private List<Program> programList;

    private List<GreenTour> greenTourList;
}
