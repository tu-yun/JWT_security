package com.xi.fmcs.domain.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminAPTManagerUpdateRequestDto {

    @Schema(description = "대표 내단지 관리자 고유번호")
    private int aptMngMemberSeq;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "휴대전화")
    private String mobileNo;

}
