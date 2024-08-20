package com.xi.fmcs.domain.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminMemberDetailDto {
	
	@Schema(description = "순번")
	private int rownum;
	   
	@Schema(description = "번호")
	private int adminMemberSeq;
	
	@Schema(description = "이메일")
	private String email;
	
	@Schema(description = "이름")
	private String name;
	
	@Schema(description = "권한")
	private String gradeType;
	
	@Schema(description = "직급")
	private String position;
	
	@Schema(description = "부서")
	private String department;
	
	@Schema(description = "업체 담당자대표 여부")
	private String companyYn;
	
	@Schema(description = "전화번호")
	private String telNo;
	
	@Schema(description = "핸드폰번호")
	private String mobileNo;

	@Schema(description = "총수량")
    private int totalcnt;
		
}
