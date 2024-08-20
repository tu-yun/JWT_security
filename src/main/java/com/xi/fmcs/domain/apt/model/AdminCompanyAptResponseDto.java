package com.xi.fmcs.domain.apt.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminCompanyAptResponseDto {

    @Schema(description = "아파트고유번호")
    private int seq;

    @Schema(description = "아파트(단지)명")
    private String aptName;

    @Schema(description = "단지코드")
    private String xiCode;

    @Schema(description = "지역 코드 (도)")
    private int addrDoType;

    @Schema(description = "주소")
    private String addr;

    @Schema(description = "전화번호")
    private String tel;

    @Schema(description = "팩스번호")
    private String fax;

    @Schema(description = "세대수")
    private int householdCnt;

}