package com.xi.fmcs.domain.adminCompany.model;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

@Data
public class AdminCompanyCURequestDto {

    @Hidden
    private int companySeq;
    private String company;
    private String companyPresident;
    private String companyTelNo;
    private String companyNo;
    private String companyFaxNo;
    private String name;
    private String position;
    private String department;
    private String telNo;
    private String mobileNo;
    private String email;

    @Hidden
    private int adminMemberSeq;
    @Hidden
    private String nickName;
}
