package com.xi.fmcs.domain.adminCompany.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminCompanyListDto {

    @Schema(description = "순번(row_num)")
    private int rowNum;
    @Schema(description = "회사 고유번호")
    private int companySeq;
    @Schema(description = "회사명")
    private String company;
    @Schema(description = "대표자명")
    private String companyPresident;
    @Schema(description = "팩스 번호")
    private String companyFaxNo;
    @Schema(description = "전화번호")
    private String companyTelNo;
    @Schema(description = "사업자 번호")
    private String companyNo;

    @Schema(description = "등록일(자동설정)")
    private LocalDateTime regDate;
    @Schema(description = "등록자")
    private int regSeq;
    @Schema(description = "관리자 이름")
    private String name;
    @Schema(description = "관리자 이메일")
    private String email;
    @Schema(description = "관리자 전화번호")
    private String telNo;
    @Schema(description = "관리자 휴대번호")
    private String mobileNo;
    @Schema(description = "업체 사용유무")
    private String useYn;
    @Schema(description = "토탈카운트")
    private int totalcnt;

}
