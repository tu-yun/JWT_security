package com.xi.fmcs.domain.moveReserve.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MoveReserveDetailRequestDto {

    @Schema(required = true)
    private String dong;

    @Schema(description = "엘레베이터 번호", required = true)
    private int evtrNo;

    @Schema(description = "선택 날짜", required = true, example = "2023-03-20")
    private String moveDate;

    @Schema(required = true)
    private int aptSeq;
}
