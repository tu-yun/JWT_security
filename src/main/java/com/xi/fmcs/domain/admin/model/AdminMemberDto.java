package com.xi.fmcs.domain.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AdminMemberDto {

    @Schema(description = "관리자 고유번호")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int adminMemberSeq;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "휴대전화")
    private String mobileNo;

    @Schema(description = "어드민 회사번호")
    private int companySeq;

    public void setSeq(int seq) {
        adminMemberSeq = seq;
    }
    public int getSeq() {
        return adminMemberSeq;
    }

}
