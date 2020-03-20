package com.kakaopay.greentour.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class Documents {

    private String addressName;

    private String addressType;

    private String x;

    private String y;

    private HashMap<String, Object> address;

    private HashMap<String, Object> roadAddress;

}
