package com.xi.fmcs.domain.admin.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminMemberLoginDto {
	
	@Schema(description = "번호")
    private Integer seq;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "비밀번호")
    private String pwd;
    
	@Schema(description = "이름")
	private String name;
	
	@Schema(description = "권한")
	private Integer gradeType;
		
	@Schema(description = "직급")
	private String position;
	
	@Schema(description = "부서")
	private String department;
		
	@Schema(description = "전화번호")
	private String telNo;
	
	@Schema(description = "핸드폰번호")
	private String mobileNo;
	
	@Schema(description = "로그인 실패 횟수")
    private Integer loginFailCount;
	
	@Schema(description = "마지막 로그인 날짜")
    private LocalDateTime loginLastDate;

	@Schema(description = "로그인 실패 횟수 초기화 날짜")
    private LocalDateTime pwResetDate;	

	@Schema(description = "어드민회사번호")
	private Integer companySeq;
  
	@Schema(description = "업체 담당자대표 여부")
    private String companyYn;	
	
	@Schema(description = "닉네임")
	private String nickname;

	@Schema(description = "회사명")
    private String company;
    
	@Schema(description = "회사팩스번호")
    private String companyFaxNo;
    
	@Schema(description = "회사전화번호")
    private String companyTelNo;	

	@Schema(description = "등록 폴더명")
    private String filePath;
    
	@Schema(description = "저장 파일명")
    private String fileStoredName;
    
	@Schema(description = "마지막 로그인 IP")
    private String lastLoginIp;
	
	@Schema(description = "JWT 갱신 토큰")
    private String refreshToken;
		
}
