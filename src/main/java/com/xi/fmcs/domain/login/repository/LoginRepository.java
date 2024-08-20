package com.xi.fmcs.domain.login.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LoginRepository {

	private final SqlSessionTemplate sqlSession;
	private final String mapperName = this.getClass().getName();
	
	//로그인 정보 확인
	public AdminMemberLoginDto getLoginCheckByEmail(String username){
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_R_FMCS_ADMIN_MEMBER_BY_EMAIL_R";

		try {
			params.put("@EMAIL", username);
			
			return sqlSession.selectOne(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			return null;
		}
	}
	
    //로그인 성공(login 정보 DB 업데이트)
    public LocalDateTime updateLoginSuccessByEmail(String email, String loginIP, String refreshToken) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_R_FMCS_ADMIN_LOGIN_SUCCESS_U";
		
		try {
			params.put("@EMAIL", email);
			params.put("@LOGIN_IP", loginIP);
			params.put("@REFRESH_TOKEN", refreshToken);

			sqlSession.update(mapperName + "." + spName, params);
			
			return ((Timestamp) params.get("@LOGIN_LAST_DATE")).toLocalDateTime();
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    		
    }
    
    //로그인 실패(비밀번호가 틀리면 해당 아이디의 실패 횟수 증가)
    public int updateLoginFailByEmail(String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_R_FMCS_ADMIN_LOGIN_FAIL_U";
		
		try {
			params.put("@EMAIL", email);
			
			sqlSession.update(mapperName + "." + spName, params);

			return (int) params.get("@RET_VAL");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    	
    }

    //로그아웃 성공(DB 리플래쉬 토큰 삭제)
    public void updateLogOutSuccessByEmail(String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_R_FMCS_ADMIN_LOGOUT_SUCCESS_U";
		
		try {
			params.put("@EMAIL", email);
			
			sqlSession.update(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}  
    }
	
}
