package com.kakaopay.greentour.dto;

import lombok.Data;

import java.util.List;

@Data
public class OutlineKeywordResponse {

    private String keyword;

    private List<RegionCountProgram> programList;
}