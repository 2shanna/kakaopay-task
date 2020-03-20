package com.kakaopay.greentour.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class LocalResponse {

    private HashMap<String, Object> meta;

    private List<Documents> documents;
}

