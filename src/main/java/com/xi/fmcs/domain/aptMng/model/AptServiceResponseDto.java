package com.xi.fmcs.domain.aptMng.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AptServiceResponseDto {

    @Schema(description = "아파트 고유번호")
    private Integer seq;

    @Schema(description = "승인형식(Y 자동 N 현장승인)")
    private String authYn;

    @Schema(description = "운영관리(단지서비스 Y 사용 N 미사용)")
    private String operationYn;

    @Schema(description = "시설관리(자이안클럽 Y 사용 N 미사용)")
    private String facilityYn;

    @Schema(description = "컨시어지/멤버쉽 (사용:Y ,미사용:N)")
    private String conciergeYn;

    @Schema(description = "자이앱 내단지 사용유무")
    private String myaptYn;

    public AptServiceResponseDto(){}

}
