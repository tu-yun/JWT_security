package com.xi.fmcs.domain.system.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GsAdminMemberDetailDto {
			   
	@Schema(description = "번호")
	private int seq;
	
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

	@Schema(description = "전화번호")
	private String telNo;
	
	@Schema(description = "핸드폰번호")
	private String mobileNo;
	
	@Schema(description = "등록일")
    private LocalDateTime regDate;

	@Schema(description = "어드민회사번호")
    private Integer companySeq;
	
	@Schema(description = "업체 담당자대표 여부")
	private String companyYn;

	@Schema(description = "닉네임")
    private String nickname;	
		
}
