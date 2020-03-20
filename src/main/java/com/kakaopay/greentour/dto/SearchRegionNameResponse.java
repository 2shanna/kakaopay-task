package com.kakaopay.greentour.dto;

import com.kakaopay.greentour.domain.Program;
import com.kakaopay.greentour.domain.Region;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchRegionNameResponse {

    private Region region;

    private List<Program> programList;
}
