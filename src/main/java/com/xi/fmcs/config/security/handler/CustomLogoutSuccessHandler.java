package com.xi.fmcs.config.security.handler;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.auth0.jwt.JWT;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.util.AES256Util;
import com.xi.fmcs.config.exception.model.ExceptionResponse;
import com.xi.fmcs.config.security.model.JwtConst;
import com.xi.fmcs.domain.login.service.LoginService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final LoginService loginService;
	private final JwtConst jwtConst;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		logger.info("로그아웃 시작");
		
    	//요청 path
    	String path = request.getRequestURI();
    	
		String accessHeader = request.getHeader(jwtConst.getAccess().getHeaderKey());
		if(accessHeader != null && accessHeader.startsWith("Bearer ")) {
			String accessToken = accessHeader.replace("Bearer ", "");
			String pkA = JWT.decode(accessToken).getClaim("pkA").asString();

			if(pkA == null) {
				logger.info("어세스 토큰 Claim 없음");
				ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG010", MngUtil.message("LG010"), path);	//자격 증명에 실패하였습니다.
				WebUtil.setHttpServletResponse(response, 401, exRes);
				return;
			}

			//username 복호화
			String username = AES256Util.decode(pkA);
			
			//DB 리플래쉬 토큰 삭제
			if(username != null) {
				loginService.updateLogOutSuccessByEmail(username);
			}
		}
		Result<Object> result = Result.<Object>builder()
				.stateMessage(MngUtil.message("LG0113"))	  //로그아웃 되었습니다.
				.build();
		WebUtil.setHttpServletResponse(response, 200, result);
	}
	
}
