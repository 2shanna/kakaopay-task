package com.kakaopay.greentour.dto;

import com.kakaopay.greentour.domain.Program;
import com.kakaopay.greentour.domain.Region;
import lombok.Data;

import java.util.List;

@Data
public class EcoInformationResponse {

    private List<Region> regionList;

    private List<Program> programList;
}
