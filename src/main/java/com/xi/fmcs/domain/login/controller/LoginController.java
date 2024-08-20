package com.xi.fmcs.domain.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.config.exception.model.ExceptionResponse;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.login.model.dto.AdminMemberLoginReqDto;
import com.xi.fmcs.domain.login.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "LoginController", description = "로그인 관리")
@RequiredArgsConstructor
public class LoginController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final LoginService loginService;
	
	@Operation(summary = "로그인")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "정상적인 로그인", 
				headers = {
						@Header(name = "authorization", description = "Access Token"),
						@Header(name = "refreshToken", description = "Refresh Token")
				}
		),
		@ApiResponse(responseCode = "202", description = "정상적인 로그인 - 초기비밀번호 입니다.",
				headers = {
						@Header(name = "authorization", description = "Access Token"),
						@Header(name = "refreshToken", description = "Refresh Token")
				}	
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),	
		@ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
		@ApiResponse(responseCode = "423", description = "비밀번호 5회 잘못 입력되어서 잠긴 계정", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
	})
	@PostMapping("/log_in")
	public Result<AdminMemberLoginDto> login(
			@RequestBody AdminMemberLoginReqDto adminMemberLoginReqDto
	) throws Exception {
		// swagger에 /log_in URL을 표시하기 위한 Mock-up URL - 실제 /log_in 요청은 jwtAuthenticationFilter가 인터셉터한다. 
		return null;
	}

	@Operation(summary = "로그아웃")
	@GetMapping("/logout")
	public Result<Object> logout() {
		// swagger에 /logout URL을 표시하기 위한 Mock-up URL - 실제 /logout 요청은 CustomLogoutSuccessHandler가 인터셉터한다.
		return null;
	}
	
	@Operation(summary = "인증 토큰 갱신")
	@Parameters({
		@Parameter(name = "RefreshToken", description = "Refresh Token", required = true, in = ParameterIn.HEADER)
	})
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "토큰 갱신 성공", 
				headers = @Header(name = "authorization", description = "New Access Token")
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),	
		@ApiResponse(responseCode = "401", description = "토큰 검증 오류", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
		@ApiResponse(responseCode = "406", description = "갱신 토큰 만료", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	})
	@PostMapping("/refresh")
    public Result<Object> issueRefreshedToken(HttpServletRequest request, HttpServletResponse response) {
		logger.info("리플래쉬 토큰 요청");
		
		String accessHeader = request.getHeader("Authorization");
		String refreshHeader = request.getHeader("RefreshToken");		
		
		loginService.issueRefreshedToken(request, response, accessHeader, refreshHeader);
		return Result.<Object>builder()
				.build();
    }
	
}
