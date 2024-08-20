package com.xi.fmcs.domain.moveReserve.model;

import lombok.Data;

@Data
public class ReserveCalendarRequestDto {

    private int aptSeq;

    private int year;

    private int month;

    private String dong;

    private int evNo;

}
