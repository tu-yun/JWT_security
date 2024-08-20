package com.xi.fmcs.domain.adminCompany.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.xi.fmcs.support.util.LogUtil;
import com.xi.fmcs.domain.adminCompany.model.AdminCompanyDetailDto;
import com.xi.fmcs.domain.adminCompany.model.AdminCompanyListDto;
import com.xi.fmcs.domain.aptMng.model.AptMngCompanyDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminCompanyRepository {

	private final SqlSessionTemplate sqlSession;
	private final String mapperName = this.getClass().getName();
	
    //업체 추가
    public int insertAdminCompany(
    		String company,
            String companyPresident,
            String companyFaxNo,
            String companyTelNo,
            String companyNo,
            int regSeq) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_COMPANY_C";
		
		try {
			params.put("@COMPANY", company);
			params.put("@COMPANY_PRESIDENT", companyPresident);
			params.put("@FAX_NO", companyFaxNo);
			params.put("@TEL_NO", companyTelNo);
			params.put("@COMPANY_NO", companyNo);
			params.put("@REG_SEQ", regSeq);
			
			sqlSession.insert(mapperName + "." + spName, params);
			
			return (int) params.get("@seq");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
    }
    
    //업체 수정
    public void updateAdminCompany(
            int companySeq,
            String company,
            String companyPresident,
            String companyFaxNo,
            String companyTelNo,
            String companyNo,
            int regSeq) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_COMPANY_U";
		
		try {
			params.put("@SEQ", companySeq);
			params.put("@COMPANY", company);
			params.put("@COMPANY_PRESIDENT", companyPresident);
			params.put("@FAX_NO", companyFaxNo);
			params.put("@TEL_NO", companyTelNo);
			params.put("@COMPANY_NO", companyNo);
			params.put("@REG_SEQ", regSeq);
			
			sqlSession.update(mapperName + "." + spName, params);			
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    	
    }

    //업체 삭제
    public int deleteAdminCompany(int companySeq, int regSeq) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_COMPANY_D";
		
		try {
			params.put("@seq", companySeq);
			params.put("@reg_seq", regSeq);
			
			sqlSession.delete(mapperName + "." + spName, params);
			
			return (int) params.get("@retval");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
    }
    
	//업체 조회
	public List<AdminCompanyListDto> selectAdminCompanyList(
            String keyword,
            int pNum,
            int pSize,
            String useYn){
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_COMPANY_L";

		try {
			params.put("@keyword", keyword);
			params.put("@page_num", pNum);
			params.put("@page_size", pSize);
			params.put("@USE_YN", useYn);
			
			return sqlSession.selectList(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}

	//업체 상세조회
	public AdminCompanyDetailDto adminCompanyDetail(int seq){
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_COMPANY_R";

		try {
			params.put("@SEQ", seq);
			
			return sqlSession.selectOne(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}

    //사업자번호 중복체크
    public int getAdminCompanyNoCheck(String cmpNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_COMPANY_NO_CHECK";
		
		try {
			params.put("@COMPANY_NO", cmpNo);
			
			sqlSession.selectOne(mapperName + "." + spName, params);
			
			return (int) params.get("@RET_VAL");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
    }
    
    //사업자번호 자신제외 중복체크
    public int getAdminCompanyExceptMeCompanyNoCheckBySeq(int seq, String mobileNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_COMPANY_EXCEPT_ME_COMPANY_NO_CHECK_BY_SEQ_DEV";
		
		try {
			params.put("@SEQ", seq);
			params.put("@COMPANY_NO", mobileNo);
			
			sqlSession.selectOne(mapperName + "." + spName, params);
			
			return (int) params.get("@RET_VAL");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
    }

	//아파트 고유번호로 담당 회사 찾기
	public AdminCompanyDetailDto getAdminCompanyByAptSeq(int aptSeq){
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_COMPANY_BY_APT_SEQ_R";

		try {
			params.put("@APT_SEQ", aptSeq);
			
			return sqlSession.selectOne(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}
	
	//업체관리자도 같이 찾는 프로시저
	public AptMngCompanyDto selectMyAdminCompany(int companySeq){
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_R_FMCS_ADMIN_COMPANY_BY_SEQ_R";

		try {
			params.put("@COMPANY_SEQ", companySeq);
			
			return sqlSession.selectOne(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}
	
	//현재 업체를 제외한 다른 업체 조회
	public List<AptMngCompanyDto> selectExceptMeAdminCompanyList(int companySeq, String keyword){
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_R_FMCS_ADMIN_COMPANY_EXCEPT_ME_COMPANY_BY_SEQ_R";

		try {
			params.put("@COMPANY_SEQ", companySeq);
			params.put("@KEYWORD", keyword);
			
			return sqlSession.selectList(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}	
	
}