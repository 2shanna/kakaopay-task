package com.kakaopay.greentour.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchRegionResponse {

    private String regionCd;

    private List<SearchRegionProgram> programList;
}