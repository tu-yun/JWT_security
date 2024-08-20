package com.xi.fmcs.domain.member.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class MngCancelRequestDto {

    @Positive(message = "")
    @Schema(description = "아파트 고유번호", required = true)
    private int aptSeq;

    @Positive(message = "")
    @Schema(description = "대상 고유번호", required = true)
    private int memSeq;
}
