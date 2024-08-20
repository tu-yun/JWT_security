package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VoteMemberDto {

    @Schema(description = "번호")
    private int rowNum;

    @Schema(description = "동")
    private String dong;

    @Schema(description = "호")
    private String ho;

    @Schema(description = "회원명")
    private String name;

    @Schema(description = "거주형태")
    private String residenceTypeNm;

    @Schema(description = "구분")
    private String householderTypeNm;

    private int totalCnt;
}
