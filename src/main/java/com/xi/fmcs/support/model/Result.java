package com.xi.fmcs.support.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Result<T> {

	@Builder.Default
	@Schema(description = "응답 코드")
	private String stateCode = "OK";
	
	@Builder.Default
	@Schema(description = "응답 메세지")
	private String stateMessage = "요청이 성공적으로 되었습니다.";
	
	@Schema(description = "응답 결과")
	private T result;
	
}
