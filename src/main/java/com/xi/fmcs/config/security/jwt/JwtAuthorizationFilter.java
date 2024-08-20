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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.xi.fmcs.support.util.AES256Util;
import com.xi.fmcs.config.exception.model.ExceptionResponse;
import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.login.service.LoginService;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final LoginService loginService;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, LoginService loginService) {
		super(authenticationManager);
		this.loginService = loginService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("인가시작");
		String accessHeader = request.getHeader(jwtConst.getAccess().getHeaderKey());

		//header에 accessToken이 있는지 검증 - 없으면 시큐리티 체인 리턴
		if(accessHeader == null || !accessHeader.startsWith("Bearer ")) {
			logger.info("어세스 토큰 헤더 없음");
			chain.doFilter(request, response);
			return;
		}
		
		//요청 path
		String path = request.getRequestURI();
		
		//username 추출
		String accessToken = accessHeader.replace("Bearer ", "");
		String pkA = null;
		try {				
			pkA = JWT.require(Algorithm.HMAC512(jwtConst.getAccess().getSecretKey())).build().verify(accessToken).getClaim("pkA").asString();
		} catch (JWTVerificationException ex) {
			ex.printStackTrace();
			if(ex instanceof TokenExpiredException) {
				logger.info("어세스 토큰 만료");
				ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG007", MngUtil.message("LG007"), path);	//인증 토큰이 만료되었습니다.
				WebUtil.setHttpServletResponse(response, 401, exRes);
				return;
			} else {
				logger.info("어세스 토큰 오류");
				chain.doFilter(request, response);
				return;
			}
		}
	
		if(pkA != null) {
			//username 복호화
			String username = AES256Util.decode(pkA);
			if(username != null) {
				//계정 조회
				AdminMemberLoginDto adminMemberLoginDto = null;
				try {
					adminMemberLoginDto = ((PrincipalDetails) loginService.loadUserByUsername(username)).getAdminMemberLoginDto();
				} catch (AuthenticationException e) {
					logger.info("계정 없음");
					chain.doFilter(request, response);
					return;
				}
				
				//어세스 토큰와 리플래쉬 토큰 검증
				if(adminMemberLoginDto.getRefreshToken() == null) {
					logger.info("기록된 리플래쉬 토근 없음");
					chain.doFilter(request, response);
					return;				
				}
				
				//accessToken 와 refreshToken 생성날짜 비교
				try {
					Date accessIssuedAt = JWT.require(Algorithm.HMAC512(jwtConst.getAccess().getSecretKey())).build().verify(accessToken).getIssuedAt();
					Date refreshIssuedAt = JWT.require(Algorithm.HMAC512(jwtConst.getRefresh().getSecretKey())).build().verify(adminMemberLoginDto.getRefreshToken()).getIssuedAt();
					if(accessIssuedAt == null || refreshIssuedAt == null || !accessIssuedAt.equals(refreshIssuedAt)) {
						logger.info("어세스 토큰 생성날짜와 리플래쉬 토큰 생성날짜 일치하지 않음.");
						//기록된 리플래쉬 토큰 삭제
						loginService.updateLogOutSuccessByEmail(username);
						chain.doFilter(request, response);
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("토큰 생성 날짜 추출 오류");
					//기록된 리플래쉬 토큰 삭제
					loginService.updateLogOutSuccessByEmail(username);
					chain.doFilter(request, response);
					return;
				}
				
				adminMemberLoginDto.setPwd("[PROTECTED]");
				adminMemberLoginDto.setRefreshToken("[PROTECTED]");
				
				//session 생성
				PrincipalDetails principalDetails = new PrincipalDetails(adminMemberLoginDto);
				Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());			
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
		return;
	}
	
}
