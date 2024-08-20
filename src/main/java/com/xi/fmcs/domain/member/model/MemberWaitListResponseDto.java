package com.xi.fmcs.domain.member.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberWaitListResponseDto {
    private int rowNum;
    private int seq;
    private String aptDong;
    private String aptHo;
    private String name;
    private String nickname;
    private String sex;
    private String phone;
    private int totalCnt;

    @Schema(description = "승인대기회원 : 입주민명부")
    private String residentYn;

    @Schema(description = "승인요청일시")
    private String authDate;
    @Schema(description = "미승인조회 : 미승인 처리일시")
    private String modDate;
}
