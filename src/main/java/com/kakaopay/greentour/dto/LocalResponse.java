package com.kakaopay.greentour.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class LocalResponse {

    private HashMap<String, Object> meta;

    private List<Documents> documents;
}

