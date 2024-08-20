package com.xi.fmcs.domain.moveReserve.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReserveCalendarDto {
    private int seq;

    private int aptSeq;

    private String dong;

    private String ho;

    @Schema(description = "엘레베이터 번호")
    private int elvtrNo;

    @Schema(description = "이사일시(날짜)")
    private String moveDate;

    @Schema(description = "이사일시(시간)")
    private String moveTime;

    @Schema(description = "시간대 고유 번호")
    private int aptMoveTimeSetSeq;

    @Schema(description = "이사방법 (1:엘리베이터 2:엘리베이터,사다리 교차 가능 3.엘리베이터,사다리 둘 중 하나)")
    private int moveType;

    private String regName;

    private String isAdmin;

    private String regTel;

}
