package com.xi.fmcs.domain.admin.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.config.security.configuration.CustomPasswordEncoder;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.settings.model.AptMngInfoDto;
import com.xi.fmcs.domain.settings.model.AptMngSaveRequestDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.xi.fmcs.support.util.LogUtil;
import com.xi.fmcs.domain.admin.model.AdminMemberDetailDto;
import com.xi.fmcs.domain.admin.model.AdminMemberDetailMinDto;
import com.xi.fmcs.domain.admin.model.AdminMemberDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminMemberRepository {

	private final SqlSessionTemplate sqlSession;
	private final String mapperName = this.getClass().getName();
	
    //관리자 비번변경전 비밀번호 확인
    public int getAdminMemberCheckBySeqAndPwd(int seq, String pwd) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_PWD_CHECK";
		
		try {
			params.put("@SEQ", seq);
			params.put("@PWD", pwd);
			
			sqlSession.selectOne(mapperName + "." + spName, params);

			return (int) params.get("@RET_VAL");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    	
    }
    
    //관리자 비번 재설정
    public void updateAdminMemberPWD(int seq, String newPwd, int regSeq) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_PWD_RE_SET_U2";
		
		try {
			params.put("@SEQ", seq);
			params.put("@NEW_PWD", newPwd);
			params.put("@REG_SEQ", regSeq);
			
			sqlSession.update(mapperName + "." + spName, params);			
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    	
    }
    
    //관리자 관리 로그
    public void insertLog(String loginId, String logHistory, String regIp) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_LOG_C";
		
		try {
			params.put("@login_id", loginId);
			params.put("@login_history", logHistory);
			params.put("@reg_ip", regIp);
			
			sqlSession.insert(mapperName + "." + spName, params);			
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    	
    }   
    
    //관리자 휴대폰번호 중복 체크
    public int getAdminMemberCheckByMobile(String mobileNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_MOBILE_NO_CHECK";
		
		try {
			params.put("@MOBILE_NO", mobileNo);
			
			sqlSession.selectOne(mapperName + "." + spName, params);
			
			return (int) params.get("@RET_VAL");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    	
    }      
    
    //관리자 이메일로 중복 체크
    public int getAdminMemberCheckByEmail(String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_EMAIL_CHECK";
		
		try {
			params.put("@EMAIL", email);
			
			sqlSession.selectOne(mapperName + "." + spName, params);
			
			return (int) params.get("@RET_VAL");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    	
    }    
    
    //관리자 나를 제외하고 휴대폰번호 중복 체크
    public int getAdminMemberExceptMeMobileNoCheckBySeq(int seq, String mobileNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_EXCEPT_ME_MOBILE_NO_CHECK";
		
		try {
			params.put("@SEQ", seq);
			params.put("@MOBILE_NO", mobileNo);
			
			sqlSession.selectOne(mapperName + "." + spName, params);
			
			return (int) params.get("@RET_VAL");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    	
    }      
    
    //관리자 나를 제외한 이메일 중복 체크
    public int getAdminMemberExceptMeEmailCheckBySeq(int seq, String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_EXCEPT_ME_EMAIL_CHECK";
		
		try {
			params.put("@SEQ", seq);
			params.put("@EMAIL", email);
			
			sqlSession.selectOne(mapperName + "." + spName, params);
			
			return (int) params.get("@RET_VAL");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    	
    }       
    
    //관리자 추가
    public int insertAdminMember(
            String email, 
            String pwd, 
            String name, 
            String gradeType, 
            String position,
            String department, 
            String companyYn, 
            String telNo, 
            String mobileNo, 
            int companySeq, 
            int regSeq, 
            String nickname) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_C";
		
		try {
			params.put("@EMAIL", email);
			params.put("@PWD", pwd);
			params.put("@NAME", name);
			params.put("@GRADE_TYPE", gradeType);
			params.put("@POSITION", position);
			params.put("@DEPARTMENT", department);
			params.put("@COMPANY_YN", companyYn);
			params.put("@TEL_NO", telNo);
			params.put("@MOBILE_NO", mobileNo);
			params.put("@COMPANY_SEQ", companySeq);
			params.put("@REG_SEQ", regSeq);
			params.put("@NICKNAME", nickname);
			
			sqlSession.insert(mapperName + "." + spName, params);
			
			return (int) params.get("@seq");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    	
    }        
    
    //내 단지 관리자 와 단지 맵핑 등록
    public void insertAptMngXicodeByAdmSeq(int adminSeq, int aptSeq, int regseq) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_APT_MNG_C";
		
		try {
			params.put("@ADMIN_SEQ", adminSeq);
			params.put("@APT_SEQ", aptSeq);
			params.put("@REG_SEQ", regseq);
			
			sqlSession.insert(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    	
    }    
    
    //관리자 수정
    public void updateAdminMemberAll(
    		int seq, String email, String name, String position,
            String department, String telNo, String mobileNo, 
            int regSeq, String nickname) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_U";
		
		try {
			params.put("@SEQ", seq);
			params.put("@EMAIL", email);
			params.put("@NAME", name);
			params.put("@POSITION", position);
			params.put("@DEPARTMENT", department);
			params.put("@TEL_NO", telNo);
			params.put("@MOBILE_NO", mobileNo);
			params.put("@REG_SEQ", regSeq);
			params.put("@NICKNAME", nickname);
			
			sqlSession.update(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    	
    }    
    
	//관리자 업체 대표관리자 조회
	public AdminMemberDto getAdminMemberComYnByCompanySeq(int companySeq, String gradeType){
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_CMP_YN_BY_CMP_SEQ_R";

		try {
			params.put("@COMPANY_SEQ", companySeq);
			params.put("@GRADE_TYPE", gradeType);
			
			return sqlSession.selectOne(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}
    
	//로그인 정보 확인
	public AdminMemberDto getLoginCheckById(String email, String pwd, String login_ip){
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_LOGIN_R";

		try {
			params.put("@email", email);
			params.put("@pwd", pwd);
			params.put("@login_ip", login_ip);
			
			return sqlSession.selectOne(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}   
    
	//관리자 거래처 관리자 목록
	public List<AdminMemberDetailDto> getAdminCompanyMemberListBySeq(int seq, int pageNum, int pageSize){
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_COMPANY_MEMBER_L";

		try {
			params.put("@SEQ", seq);
			params.put("@PAGE_NUM", pageNum);
			params.put("@PAGE_SIZE", pageSize);
			
			return sqlSession.selectList(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}    
    
	//관리자 간단 정보
	public AdminMemberDetailMinDto getAdminMemberDetailBySeq(int seq){
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_DETAIL_R";

		try {
			params.put("@SEQ", seq);
			
			return sqlSession.selectOne(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}  
	
    //관리자 휴대폰번호 변경
    public void updateAdminMemberMobileNo(int aptMngMemberSeq, String mobileNo, int modSeq) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_MOBILE_NO_U";
		
		try {
			params.put("@SEQ", aptMngMemberSeq);
			params.put("@MOBILE_NO", mobileNo);
			params.put("@REG_SEQ", modSeq);
			
			sqlSession.update(mapperName + "." + spName, params);
			LogUtil.insertAminMemberLog(String.valueOf(modSeq), "[관리자 수정] [id=" + aptMngMemberSeq + "]");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}    	
    }

	// 관리자 목록
	public List<AdminMemberDetailMinDto> getAdminMemberList(int aptSeq) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_APT_MNG_L";

		try {
			params.put("@APT_SEQ", aptSeq);

			return sqlSession.selectList(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			return null;
		}
	}

	//아파트 관리자 등록
	public int insertAptAdmin(AptMngInfoDto aptMngInfo, int companySeq, int regSeq, int aptSeq) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_APT_MNG_C";

		try {
			String email = aptMngInfo.getEmail();
			String encPwd = "";
			CustomPasswordEncoder pwdEncoder = new CustomPasswordEncoder();
			if (email != null && email.indexOf("@") > -1) {
				encPwd = email.split("@")[0].toString() + "123!";
			} else {
				encPwd = email + "123!";
			}
			encPwd = pwdEncoder.encode(encPwd);

			params.put("@EMAIL", email);
			params.put("@PWD", encPwd);
			params.put("@NAME", aptMngInfo.getName());
			params.put("@GRADE_TYPE", String.valueOf(aptMngInfo.getGradeType()));
			params.put("@COMPANY_YN", aptMngInfo.getCompanyYn());
			params.put("@TEL_NO", aptMngInfo.getTelNo());
			params.put("@MOBILE_NO", aptMngInfo.getMobileNo());
			params.put("@COMPANY_SEQ", companySeq);
			params.put("@REG_SEQ", regSeq);
			params.put("@NICKNAME", aptMngInfo.getNickName());
			params.put("@APT_SEQ", aptSeq);

			sqlSession.insert(mapperName + "." + spName, params);
			return (int) params.get("@SEQ");
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}

	// 아파트 관리자 수정
	public void updateAptAdmin(AptMngInfoDto aptMngInfo, int regSeq) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_APT_MNG_U";

		try {
			params.put("@SEQ", aptMngInfo.getSeq());
			params.put("@GRADE_TYPE", aptMngInfo.getGradeType());
			params.put("@EMAIL", aptMngInfo.getEmail());
			params.put("@NAME", aptMngInfo.getName());
			params.put("@NICKNAME", aptMngInfo.getNickName());
			params.put("@TEL_NO", aptMngInfo.getTelNo());
			params.put("@MOBILE_NO", aptMngInfo.getMobileNo());
			params.put("@REG_SEQ", regSeq);

			sqlSession.update(mapperName + "." + spName, params);
		}catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}

	//관리업체 변경
	public void updateAdminMemberCompanyYN(int adminMemberSeq, int prevMngSeq, int regSeq) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_COMPANY_YN_U";

		try {
			params.put("@SEQ", adminMemberSeq);
			params.put("@OLD_SEQ", prevMngSeq);
			params.put("@REG_SEQ", regSeq);

			sqlSession.update(mapperName + "." + spName, params);
			LogUtil.insertAminMemberLog(String.valueOf(regSeq),"[대표 관리자 변경] [seq=" + adminMemberSeq + "]");
		} catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}

	//대표관리자 체크
	public int getAdminMemberCompanyYNCheckBySeq(int seq) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_COMPANY_YN_CHECK";

		try {
			params.put("@SEQ", seq);

			sqlSession.selectOne(mapperName + "." + spName, params);
			return (int) params.get("@RET_VAL");
		} catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}

	//관리자 삭제(실질적으로 USE_YN ='N' 변경)
	public void deleteAdminMember(int seq, int regSeq) {
		Map<String, Object> params = new HashMap<String, Object>();
		String spName = "XI_SP_FMCS_ADMIN_MEMBER_D";

		try {
			params.put("@SEQ", seq);

			sqlSession.delete(mapperName + "." + spName, params);
			LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[삭제] [seq =" + seq + "]");
		} catch (Exception e) {
			LogUtil.writeLog(spName, params, e.getMessage());
			throw e;
		}
	}
}
