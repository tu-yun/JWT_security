package com.xi.fmcs.domain.member.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberInsertOrUpdateRequestDto {

    @Schema(required = true)
    private int aptSeq;

    @Schema(description = "단지코드", required = true)
    private String xiCode;

    @Schema(description = "회원고유번호")
    private int memberSeq;

    @Schema(description = "동", required = true)
    private String dong;

    @Schema(description = "호", required = true)
    private String ho;

    @Schema(description = "이름", required = true)
    private String name;

    @Schema(description = "전화번호", required = true)
    private String phone;

    @Schema(description = "생일", required = true)
    private String birthday;

    @Schema(description = "1: 남자 , 2: 여자", required = true)
    private int sex;

    @Schema(description = "거주형태 (1: 자가, 2:임대, 3:소유주)", required = true)
    private int residenceType;

    @Schema(description = "세대주타입 (0:없음, 1:세대원, 2:세대주)")
    private int householderType;

    private String rfCardNo;

    @Schema(description = "투표참여여부 Y/N", required = true)
    private String voteYn;

    @Schema(description = "닉네임", required = true)
    private String nickName;

    private String email;

    private String imageUrl;
}
