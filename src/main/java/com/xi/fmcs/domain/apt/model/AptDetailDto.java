package com.xi.fmcs.domain.apt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AptDetailDto {
    @Schema(description = "아파트 고유번호")
    private int seq;

    @Schema(description = "단지코드")
    private String xiCode;

    @Schema(description = "지역 코드 (도)")
    private int addrDoType;

    @Schema(description = "주소")
    private String addr;

    @Schema(description = "아파트(단지)명")
    private String aptName;

    @Schema(description = "문의전화")
    private String tel;

    @Schema(description = "팩스번호")
    private String fax;

    @Schema(description = "커뮤니티 동호수 노출 여부(Y/N)")
    private String brdDonghoDisp;

    @Schema(description = "커뮤니티 사용자명 노출 여부 (0:노출안함, 1:이름, 2:별명)")
    private String brdUsernmDisp;

    @Schema(description = "세대수")
    private int householdCnt;

    @Schema(description = "담당 회사고유번호")
    private int companySeq;

    @Schema(description = "담당 회사명")
    private String company;

    @Schema(description = "자이앱사용유무")
    private String myAptYn;
}
