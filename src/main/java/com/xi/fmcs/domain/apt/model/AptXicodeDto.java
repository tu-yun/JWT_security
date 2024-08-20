package com.xi.fmcs.domain.apt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AptXicodeDto {

	@Schema(description = "등록된 아파트 순번")
	private int seq;
	
	@Schema(description = "관리자 순번")
	private int adminSeq;
	
	@Schema(description = "단지코드")
	private String xiCode;

	@Schema(description = "아파트(단지)명")
	private String aptName;	
	
	@Schema(description = "지역")
	private int addrDoType;

	@Schema(description = "주소")
	private String addr;

	@Schema(description = "문의전화")
	private String tel;

	@Schema(description = "FAX 번호")
	private String fax;

	@Schema(description = "커뮤니티 동호수 노출 여부(Y/N)")
	private char brdDonghoDisp;
	
	@Schema(description = "커뮤니티 사용자명 노출여부(0:노출안함 ,1:이름 ,2:별명)")
	private char brdUsernmDisp;
	
	@Schema(description = "세대수")
    private int householdCnt;
	
}
