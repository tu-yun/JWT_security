package com.xi.fmcs.domain.member.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberListRequestDto {

    @Schema(required = true)
    private int aptSeq;

    @Schema(description = "승인일자조회 - 시작")
    private String startDate;

    @Schema(description = "승인일자조회 - 끝")
    private String endDate;

    private String dong;

    private String ho;

    @Schema(description = "회원정보 검색어")
    private String schVal;

    @Schema(description = "거주형태 (1: 자가, 2:임대, 3:소유주)")
    private int residenceType;

    @Schema(description = "세대주타입 (-1: 전체, 0:없음, 1:세대원, 2:세대주)")
    private int houseType;
}
