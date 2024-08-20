package com.xi.fmcs.domain.aptMng.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AptSeqAndXiCodeDto {
    @Schema(description = "아파트 고유번호 ")
    private int aptSeq;

    @Schema(description = "자이코드")
    private String xiCode;
}
