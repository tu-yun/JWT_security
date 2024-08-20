package com.xi.fmcs.domain.moveReserve.model;

import lombok.Data;

@Data
public class MoveReserveDetailResponseDto {
    private int aptMoveTimeSetSeq;
    private int orderNum;
    private String adminView;
    private String startTime;
    private String endTime;
    private int elvtr;
    private int sadari;
    private String regName;
    private String regTel;
    private int aptMoveReserveSeq;

}
