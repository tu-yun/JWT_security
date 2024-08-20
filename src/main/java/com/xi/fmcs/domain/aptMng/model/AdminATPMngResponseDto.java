package com.xi.fmcs.domain.aptMng.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminATPMngResponseDto {
    private int rownum;

    @Schema(description = "아파트 고유번호")
    private int seq;

    private String xiCode;

    @Schema(description = "지역코드")
    private int addrDoType;

    private String aptName;

    private String addr;

    @Schema(description = "세대수")
    private int householdCnt;

    @Schema(description = "회사명")
    private String company;

    @Schema(description = "대표관리자 고유번호")
    private int mngSeq;

    @Schema(description = "대표관리자 이름")
    private String name;

    @Schema(description = "대표관리자 휴대폰번호")
    private String mobileNo;

    @Schema(description = "내단지 사용여부")
    private String myAptYn;

    private int totalcnt;

}
