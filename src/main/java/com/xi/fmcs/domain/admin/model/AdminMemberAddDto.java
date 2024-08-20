package com.xi.fmcs.domain.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminMemberAddDto {

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "이름")
    private String name;

	@Schema(description = "직급")
	private String position;
	
	@Schema(description = "부서")
	private String department;
	
	@Schema(description = "전화번호")
	private String telNo;
	
	@Schema(description = "핸드폰번호")
	private String mobileNo;    
    
    @Schema(description = "어드민 회사번호")
    private int companySeq;    
    
    @Schema(description = "등록자번호")
    private Integer regSeq;
    
}
