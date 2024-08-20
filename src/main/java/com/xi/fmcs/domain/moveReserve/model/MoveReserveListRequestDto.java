package com.xi.fmcs.domain.moveReserve.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MoveReserveListRequestDto {
    @Schema(required = true)
    private int aptSeq;

    @Schema(description = "승인일자조회 - 시작", example = "2023-03-01")
    private String startDate;

    @Schema(description = "승인일자조회 - 끝", example = "2024-03-15")
    private String endDate;

    private String dong;

    private String ho;

    @Schema(description = "검색어")
    private String schVal;
}
