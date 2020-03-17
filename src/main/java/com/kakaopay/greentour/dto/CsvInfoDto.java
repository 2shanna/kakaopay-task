package com.kakaopay.greentour.dto;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CsvInfoDto {

    @CsvBindByPosition(position = 0)
    private long programId;

    @CsvBindByPosition(position = 1)
    private String prgmName;

    @CsvBindByPosition(position = 2)
    private String theme;

    @CsvBindByPosition(position = 3)
    private String region;

    @CsvBindByPosition(position = 4)
    private String outline;

    @CsvBindByPosition(position = 5)
    private String detail;
}
