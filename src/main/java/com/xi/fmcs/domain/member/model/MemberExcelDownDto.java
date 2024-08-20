package com.xi.fmcs.domain.member.model;

import lombok.Data;

@Data
public class MemberExcelDownDto {
    private String name;
    private String phone;
    private String aptDong;
    private String aptHo;
    private String textSex;
    private String nickName;
    private String birthday;
    private String textResidenceType;
    private String textHouseholderType;
    private String dongMngYn;
    private String dongMngTitle;
}
