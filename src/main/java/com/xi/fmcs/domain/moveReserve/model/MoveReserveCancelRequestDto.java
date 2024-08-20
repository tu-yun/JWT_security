package com.xi.fmcs.domain.moveReserve.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MoveReserveCancelRequestDto {
    @Schema(description = "아파트 고유번호", required = true)
    private int aptSeq;
    @Schema(description = "이사예약 고유번호", required = true)
    private int aptMoveReserveSeq;

}
