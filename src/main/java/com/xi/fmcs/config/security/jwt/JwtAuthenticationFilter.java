package com.xi.fmcs.config.security.jwt;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.config.exception.model.ExceptionResponse;
import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.login.model.dto.AdminMemberLoginReqDto;
import com.xi.fmcs.domain.login.service.LoginService;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/log_in",
			"POST");
	
    private final LoginService loginService;
    
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, LoginService loginService) {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
		this.loginService = loginService;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
		logger.info("인증시작");
		AdminMemberLoginReqDto adminMemberLoginReqDto = null;
		try {
			ObjectMapper om = new ObjectMapper();
			adminMemberLoginReqDto = om.readValue(request.getInputStream(), AdminMemberLoginReqDto.class);
			if(adminMemberLoginReqDto.getEmail() == null || adminMemberLoginReqDto.getEmail().trim().equals("") 
					|| adminMemberLoginReqDto.getPwd() == null || adminMemberLoginReqDto.getPwd().trim().equals("")) {
				throw new DisabledException("");
			}
		} catch (Exception e) {
			throw new DisabledException("");
		}
		
		//로그인 실패시 unsuccessfulAuthentication에서 후처리하기 위한 email전달
		request.setAttribute("reqEmail", adminMemberLoginReqDto.getEmail());
		
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				adminMemberLoginReqDto.getEmail(), adminMemberLoginReqDto.getPwd());
		return this.getAuthenticationManager().authenticate(authRequest);
	}
	
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		logger.info("인증 성공");
		request.removeAttribute("reqEmail");
		
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		AdminMemberLoginDto adminMemberLoginDto = principalDetails.getAdminMemberLoginDto();
		adminMemberLoginDto.setPwd("[PROTECTED]");
		adminMemberLoginDto.setRefreshToken("[PROTECTED]");
		
		if(adminMemberLoginDto.getLoginFailCount() >= 5) {
			throw new LockedException("");
		} else {
			//Token 생성일자 생성
			Date issuedAt = new Date();
			
			//accessToken 생성 및 header에 셋팅
			String accessToken = jwtTokenUtil.createAccessToken(adminMemberLoginDto, issuedAt);
			response.addHeader(jwtConst.getAccess().getHeaderKey(), "Bearer " + accessToken);
			
			//refreshToken 생성 및 header에 셋팅
			String refreshToken = jwtTokenUtil.createRefreshToken(adminMemberLoginDto, issuedAt);
			response.addHeader(jwtConst.getRefresh().getHeaderKey(), "Bearer " + refreshToken);
			
			//login 정보 업데이트
			String loginIP = WebUtil.getClientIP(request);
			LocalDateTime loginLastDate = loginService.updateLoginSuccessByEmail(adminMemberLoginDto.getEmail(), loginIP, refreshToken);
			adminMemberLoginDto.setLoginLastDate(loginLastDate);
			
			//응답 body
			Result<AdminMemberLoginDto> result = null;
			if(adminMemberLoginDto.getPwResetDate() != null) {
				result = Result.<AdminMemberLoginDto>builder()
						.result(adminMemberLoginDto)
						.build();
			} else {
				result = Result.<AdminMemberLoginDto>builder()
						.result(adminMemberLoginDto)
						.stateCode("LG009")
						.stateMessage(MngUtil.message("LG009"))	//초기비밀번호 입니다. \n안전한 사용을 위해 비밀번호를 변경해주세요.
						.build();
			}

			WebUtil.setHttpServletResponse(response, 200, result);
			
			//로그인 DB 기록
			String seq = Integer.toString(adminMemberLoginDto.getSeq());
			LogUtil.insertAminMemberLog(seq, "[로그인] [adminSeq=" + seq + "]");
			return;
		}
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		//ThreadLocal 변수에 저장되어 있는 SecurityContext를 제거
		SecurityContextHolder.clearContext();
		
		String username = (String) request.getAttribute("reqEmail");
		request.removeAttribute("reqEmail");
		
		//요청 path
		String path = request.getRequestURI();
		
		if(failed instanceof DisabledException) {
			//잘못된 요청
			logger.info("잘못된 요청 400");
			ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "ER003", MngUtil.message("ER003"), path);	//요청하신 정보가 잘못되었습니다.
			WebUtil.setHttpServletResponse(response, 400, exRes);
			return;
		} else if(failed instanceof BadCredentialsException) {
			//인증 실패
			logger.info("인증 실패 401");
			loginService.updateLoginFailByEmail((String) username);
			
			ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG001", MngUtil.message("LG001"), path);	//로그인 정보가 일치하지 않습니다.
			WebUtil.setHttpServletResponse(response, 401, exRes);
        	return;
        } else if(failed instanceof LockedException) {
        	//계정 잠김
        	logger.info("계정 잠김 423");
        	ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG002", MngUtil.message("LG002"), path);	//비밀번호가 5회 이상 잘못 입력되어 사용이 정지됩니다. \n비밀번호를 재설정하고 다시 로그인해 주세요.
        	WebUtil.setHttpServletResponse(response, 423, exRes);
        	return;
        } else {
        	//그외
        	logger.info("인증 실패 그외");
        	failed.printStackTrace();
        	LogUtil.conWriteLog("login log_in", username, failed.getMessage());
        	
        	ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "ER001", MngUtil.message("ER001"), path);	//관리자에게 문의하세요.
        	WebUtil.setHttpServletResponse(response, 500, exRes);
        	return;
        }
	}
}