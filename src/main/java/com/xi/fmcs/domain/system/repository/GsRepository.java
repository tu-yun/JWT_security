package com.xi.fmcs.domain.system.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.xi.fmcs.support.util.LogUtil;
import com.xi.fmcs.domain.system.model.GsAdminMemberDetailDto;
import com.xi.fmcs.domain.system.model.GsAdminMemberDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GsRepository {
	
	private final SqlSessionTemplate sqlSession;
	private final String mapperName = this.getClass().getName();
	
	//관리자 GS마스터 목록
	public List<GsAdminMemberDto> getAdminGSMemberList(){
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_GS_MEMBER_L";
		try {
			return sqlSession.selectList(mapperName + "." + spName);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}

	//관리자 GS마스터 목록
	public GsAdminMemberDetailDto getAdminMemberDetailBySeq(int seq){
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_DETAIL_R";

		try {
			params.put("@SEQ", seq);
			
			return sqlSession.selectOne(mapperName + "." + spName);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}
	

}
