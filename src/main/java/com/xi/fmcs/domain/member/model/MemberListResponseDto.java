package com.xi.fmcs.domain.member.model;

import com.xi.fmcs.support.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberListResponseDto {
    @Schema(description = "회원 고유번호")
    private int seq;
    private String aptDong;
    private String aptHo;
    private String name;
    private String nickname;
    private int sex;
    private String phone;
    private int residenceType;
    private String authDate;
    private String modDate;
    private String voteYn;
    private int houseHolderType;
    private String dongMngYn;
    private String dongMngTitle;
    private int totalCnt;

    public void setName(String name) {
        this.name = StringUtil.hideName(name);
    }

    public void setPhone(String phone) {
        this.phone = StringUtil.hideNumber(phone);
    }
}
