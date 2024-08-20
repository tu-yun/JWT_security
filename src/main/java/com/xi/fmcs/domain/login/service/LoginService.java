package com.xi.fmcs.domain.login.service;

import java.time.LocalDateTime;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.util.AES256Util;
import com.xi.fmcs.support.util.MngUtil;
import com.xi.fmcs.support.util.WebUtil;
import com.xi.fmcs.config.exception.model.ExceptionResponse;
import com.xi.fmcs.config.security.jwt.JwtTokenUtil;
import com.xi.fmcs.config.security.model.JwtConst;
import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.login.repository.LoginRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final LoginRepository loginRepository;
	private final JwtTokenUtil jwtTokenUtil; 
	private final JwtConst jwtConst;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AdminMemberLoginDto adminMemberLoginDto = loginRepository.getLoginCheckByEmail(username);
		if(adminMemberLoginDto == null) {
			throw new UsernameNotFoundException("존재하지 않는 계정입니다.");
		}
		return new PrincipalDetails(adminMemberLoginDto);
	}
	
	//로그인 성공(login 정보 DB 업데이트)
    public LocalDateTime updateLoginSuccessByEmail(String email, String loginIP, String refreshToken) {
    	return loginRepository.updateLoginSuccessByEmail(email, loginIP, refreshToken);
    }
    
    //로그인 실패로그인 실패(비밀번호가 틀리면 해당 아이디의 실패 횟수 증가)
    public int updateLoginFailByEmail(String email) {
    	return loginRepository.updateLoginFailByEmail(email);
    }
    
    //로그아웃 성공(DB 리플래쉬 토큰 삭제)
    public void updateLogOutSuccessByEmail(String email) {
    	loginRepository.updateLogOutSuccessByEmail(email);
    }
    
    //어세스 토큰 갱신
    @Transactional
    public void issueRefreshedToken(HttpServletRequest request, HttpServletResponse response,
    		String accessHeader, String refreshHeader) {
    	//요청 path
    	String path = request.getRequestURI();
    	
		//header에 accessToken와 refreshToken이 있는지 검증
		if(accessHeader == null || !accessHeader.startsWith("Bearer ") || refreshHeader == null || !refreshHeader.startsWith("Bearer ")) {
			logger.info("토큰 헤더 오류");
			ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "ER003", MngUtil.message("ER003"), path);	//요청하신 정보가 잘못되었습니다.
			WebUtil.setHttpServletResponse(response, 400, exRes);
			return;
		}
		
		String accessToken = accessHeader.replace("Bearer ", "");
		String refreshToken = refreshHeader.replace("Bearer ", "");
		
		//username 추출
		String pkA = null;
		Date accessIssuedAt = null;
		try {				
			pkA = JWT.decode(accessToken).getClaim("pkA").asString();
			accessIssuedAt = JWT.decode(accessToken).getIssuedAt();
			
			if(pkA == null || accessIssuedAt == null) {
				logger.info("어세스 토큰 Claim 또는 생성일 없음");
				ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG010", MngUtil.message("LG010"), path);	//자격 증명에 실패하였습니다.
				WebUtil.setHttpServletResponse(response, 401, exRes);
				return;
			}
		} catch (JWTVerificationException ex) {
			logger.info("어세스 토큰 추출 중 오류");
			ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG010", MngUtil.message("LG010"), path);	//자격 증명에 실패하였습니다.
			WebUtil.setHttpServletResponse(response, 401, exRes);
			return;
		}
		
		//username 복호화
		String username = null;
		try {
			username = AES256Util.decode(pkA);
		} catch (Exception e) {
			logger.info("ASE256 디코딩 오류");
			ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG010", MngUtil.message("LG010"), path);	//자격 증명에 실패하였습니다.
			WebUtil.setHttpServletResponse(response, 401, exRes);
		}
		
		//계정 조회
		AdminMemberLoginDto adminMemberLoginDto = null;
		try {	
			adminMemberLoginDto = ((PrincipalDetails) this.loadUserByUsername(username)).getAdminMemberLoginDto();
		} catch (AuthenticationException e) {
			logger.info("계정 없음");
			ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG010", MngUtil.message("LG010"), path);	//자격 증명에 실패하였습니다.
			WebUtil.setHttpServletResponse(response, 401, exRes);			
		}
			
		//리플래쉬 토큰 검증	
		if(adminMemberLoginDto.getRefreshToken() == null) { 
			logger.info("기록된 리플래쉬 토근 없음");
			ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG010", MngUtil.message("LG010"), path);	//자격 증명에 실패하였습니다.
			WebUtil.setHttpServletResponse(response, 401, exRes);
			return;			
		} else if(!adminMemberLoginDto.getRefreshToken().equals(refreshToken)) {
			logger.info("리플래쉬 토근 비교 오류");
			//기록된 리플래쉬 토큰 삭제
			this.updateLogOutSuccessByEmail(username);
			ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG010", MngUtil.message("LG010"), path);	//자격 증명에 실패하였습니다.
			WebUtil.setHttpServletResponse(response, 401, exRes);
		}
		
		String pkR = null;
		Date refreshIssuedAt = null;
		try {
			pkR = JWT.require(Algorithm.HMAC512(jwtConst.getRefresh().getSecretKey())).build().verify(refreshToken).getClaim("pkR").asString();
			refreshIssuedAt = JWT.require(Algorithm.HMAC512(jwtConst.getRefresh().getSecretKey())).build().verify(refreshToken).getIssuedAt();
			
			if(pkR == null || accessIssuedAt == null) {
				logger.info("리플래쉬 토큰 Claim 또는 생성일 null 오류");
				//기록된 리플래쉬 토큰 삭제
				this.updateLogOutSuccessByEmail(username);
				ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG010", MngUtil.message("LG010"), path);	//자격 증명에 실패하였습니다.
				WebUtil.setHttpServletResponse(response, 401, exRes);
				return;
			}
		} catch (JWTVerificationException ex) {
			if(ex instanceof TokenExpiredException) {
				logger.info("리플래쉬 토큰 만료");
				//기록된 리플래쉬 토큰 삭제
				this.updateLogOutSuccessByEmail(username);
				ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG007", MngUtil.message("LG007"), path);	//인증 토큰이 만료되었습니다.
				WebUtil.setHttpServletResponse(response, 401, exRes);
				return;
			} else {
				logger.info("리플래쉬 토큰 추출 중 오류");
				//기록된 리플래쉬 토큰 삭제
				this.updateLogOutSuccessByEmail(username);
				ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG010", MngUtil.message("LG010"), path);	//자격 증명에 실패하였습니다.
				WebUtil.setHttpServletResponse(response, 401, exRes);
				return;
			}
		}

		if(!pkA.equals(pkR) || accessIssuedAt.compareTo(refreshIssuedAt) != 0) {
			logger.info("어세스 토큰과 리플래쉬 토큰의 pk값 또는 생성일이 다름");
			//기록된 리플래쉬 토큰 삭제
			this.updateLogOutSuccessByEmail(username);
			ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "LG010", MngUtil.message("LG010"), path);	//자격 증명에 실패하였습니다.
			WebUtil.setHttpServletResponse(response, 401, exRes);
			return;
		}

		String newAccessToken = jwtTokenUtil.createAccessToken(adminMemberLoginDto, refreshIssuedAt);
		response.addHeader(jwtConst.getAccess().getHeaderKey(), "Bearer " + newAccessToken);
    }	
	
	
	
	public Result<Object> pwdChange(int seq, String pwd, String newPwd){
		String encPwd = passwordEncoder.encode(pwd);
		System.out.println(encPwd);
//		int chkCnt = adminMemberRepository.getAdminMemberCheckBySeqAndPwd(seq, encPwd);
//		if(chkCnt > 0) {
//			try {
//				adminMemberRepository.updateAdminMemberPWD(seq, encPwd, seq);
//			} catch (Exception ex) {
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("@SEQ", seq);
//				map.put("@NEW_PWD", newPwd);
//				map.put("@REG_SEQ", seq);
//				LogUtil.writeLog("XI_SP_FMCS_ADMIN_MEMBER_PWD_RE_SET_U", map, ex.getMessage());
//				throw ex;
//			}
//		} else {
//			throw new CustomException("LG005");	  //비밀번호가 일치하지 않습니다.
//		}
		
		return null;
	}
	
}
