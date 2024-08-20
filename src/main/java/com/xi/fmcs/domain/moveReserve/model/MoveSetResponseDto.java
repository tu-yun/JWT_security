package com.xi.fmcs.domain.moveReserve.model;

import lombok.Data;

@Data
public class MoveSetResponseDto {
    private int moveSetSeq;
    private int aptSeq;
    private String startDate;
    private String endDate;
    private int actionType;
    private String moveTel;
    private String appContents;
    private String preStartDate;
    private String preStartHour;
    private String preStartMin;
    private String preEndDate;
    private String preEndHour;
    private String preEndMin;

}
