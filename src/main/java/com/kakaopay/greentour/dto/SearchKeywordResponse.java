package com.kakaopay.greentour.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchKeywordResponse {

    private String keyword;

    private SearchKeywordProgram program;
}