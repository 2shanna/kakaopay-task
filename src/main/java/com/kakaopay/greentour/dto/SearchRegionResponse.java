package com.kakaopay.greentour.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchRegionResponse {

    private String regionCd;

    private List<SearchRegionProgram> programList;
}