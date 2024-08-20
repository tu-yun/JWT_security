package com.xi.fmcs.domain.adminCompany.model;

import java.time.LocalDateTime;
import java.util.List;

import com.xi.fmcs.domain.admin.model.AdminMemberDetailDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminCompanyDetailDto {
	
	@Schema(description = "번호")
    private int companySeq;

    @Schema(description = "회사명", example = "자abc건설")
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
    
	@Schema(description = "관리자 목록")
    private List<AdminMemberDetailDto> AdminMemberDetailList;

}
