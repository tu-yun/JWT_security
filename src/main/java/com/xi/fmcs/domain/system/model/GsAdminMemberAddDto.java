package com.xi.fmcs.domain.system.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GsAdminMemberAddDto {
	
	@NotBlank
	@Size(min = 1, max = 50)
	@Schema(description = "담당자명")
	private String name;

	@Email
	@NotBlank
	@Size(min = 1, max = 100)
	@Schema(description = "이메일")
	private String email;
	
	@NotBlank
	@Size(min = 1, max = 20)
	@Schema(description = "휴대전화")
	private String mobileNo;	

	@Size(max = 20)
	@Schema(description = "일반전화")
	private String telNo;
	
	@Size(max = 100)
	@Schema(description = "부서명")
	private String department;
	
	@Size(max = 50)
	@Schema(description = "직급")
	private String position;	
	
}
