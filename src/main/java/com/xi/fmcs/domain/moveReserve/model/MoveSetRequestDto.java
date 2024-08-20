package com.xi.fmcs.domain.moveReserve.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MoveSetRequestDto {
    private int aptSeq;

    private int moveSetSeq;

    @Schema(description = "입주기간 사전예약 시작일", example = "2023-03-01")
    private String preStartDate;

    @Schema(description = "입주기간 사전예약 시작 시간(시)", example = "10")
    private String preStartHour;

    @Schema(description = "입주기간 사전예약 시작 시간(분)", example = "00")
    private String preStartMin;

    @Schema(description = "입주기간 사전예약 종료일", example = "2023-06-01")
    private String preEndDate;

    @Schema(description = "입주기간 사전예약 종료 시간", example = "17")
    private String preEndHour;

    @Schema(description = "입주기간 사전예약 종료 시간(분)", example = "30")
    private String preEndMin;

    @Schema(description = "입주기간 시작일", example ="2023-03-15")
    private String startDate;

    @Schema(description = "입주기간 종료일", example ="2023-07-15")
    private String endDate;

    @Schema(description = "이사예약(1 전화, 2 예약가능)", example = "2")
    private int actionType;

    @Schema(description = "이사예약타입 1일시 보여줄 전화번호", example = "02-1234-1234")
    private String moveTel;

    @Schema(description = "앱 안내문")
    private String appContents;
}
