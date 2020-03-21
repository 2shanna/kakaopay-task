package com.kakaopay.greentour.dto;

import lombok.Data;

import java.util.HashMap;

@Data
public class Documents {

    private String addressName;

    private String addressType;

    private String x;

    private String y;

    private HashMap<String, Object> address;

    private HashMap<String, Object> roadAddress;

}
