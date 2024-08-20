package com.xi.fmcs.domain.moveReserve.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ReserveCalendarResponseDto {
    @Schema(description = "이사예약 리스트")
    private List<ReserveCalendarDto> reserveList;

    @Schema(description = "이사시간설정 리스트")
    private List<MoveTimeSetDto> moveTimeList;

    @Schema(description = "입주기간 시작일")
    private String startDate;

    @Schema(description = "입주기간 종료일")
    private String endDate;

    private int moveType;
    private int elvtrNo;
}
