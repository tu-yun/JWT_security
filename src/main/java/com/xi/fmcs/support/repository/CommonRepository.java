package com.xi.fmcs.support.repository;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.xi.fmcs.support.util.LogUtil;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommonRepository {

	private final SqlSessionTemplate sqlSession;
	private final String mapperName = this.getClass().getName();
	
	//상태 메시지 가져오기
	public String getStatusMessageByCode(String code) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_R_FMCS_STATUS_MESSAGE_R";
		
		try {
			params.put("@MSG_CODE", code);

			sqlSession.selectOne(mapperName + "." + spName, params);
			
			return (String) params.get("@MSG_CONT");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			return null;
		}
	}
}
