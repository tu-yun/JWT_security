package com.xi.fmcs.domain.apt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AptMoveInfoDto {

    @Schema(description = "APT_INFO 고유번호")
    private int aptInfoSeq;
    @Schema(description = "엘레베이터 번호")
    private int elvtrNo;

    @Schema(description = "이사방법 (1:엘리베이터 2:엘리베이터,사다리 교차 가능 3.엘리베이터,사다리 둘 중 하나)")
    private int moveType;

}
