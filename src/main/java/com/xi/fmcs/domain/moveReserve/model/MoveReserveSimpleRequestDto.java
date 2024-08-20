package com.xi.fmcs.domain.moveReserve.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MoveReserveSimpleRequestDto {

    @Schema(required = true)
    private int aptSeq;

    @Schema(required = true)
    private String dong;

    @Schema(required = true)
    private String ho;
}
