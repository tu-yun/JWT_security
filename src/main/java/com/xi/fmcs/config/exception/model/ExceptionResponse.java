package com.xi.fmcs.config.exception.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExceptionResponse {
	
    @Schema(description = "예외 발생시간")
    private LocalDateTime timestamp;
    
	@Schema(description = "예외 코드")
    private String stateCode;
	
	@Schema(description = "예외 메시지")
    private String stateMessage;
	
    @Schema(description = "요청 경로")
    private String path;
    	
}
