package com.xi.fmcs.domain.aptMng.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.xi.fmcs.support.util.LogUtil;
import com.xi.fmcs.domain.admin.model.AdminMemberDto;
import com.xi.fmcs.domain.aptMng.model.AdminATPMngResponseDto;
import com.xi.fmcs.domain.aptMng.model.AptInfoDto;
import com.xi.fmcs.domain.aptMng.model.AptNameResponseDto;
import com.xi.fmcs.domain.aptMng.model.AptServiceResponseDto;
import com.xi.fmcs.domain.file.model.ExcelFileInfoDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AptMngRepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //슈퍼관리자 내단지관리 리스트
    public List<AdminATPMngResponseDto> getAptMngGsList(
            String cmpSchVal,
            String aptSchVal,
            int doType,
            int pageNum,
            int pageSize,
            String myaptYn) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_ATP_MNG_GS_L";

        try {
            params.put("@COM_SEARCH_VAL", cmpSchVal);
            params.put("@APT_SEARCH_VAL", aptSchVal);
            params.put("@ADDR_DO_TYPE", doType);
            params.put("@PAGE_NUM", pageNum);
            params.put("@PAGE_SIZE", pageSize);
            params.put("@MYAPT_YN", myaptYn);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //업체관리자 내단지관리 리스트
    public List<AdminATPMngResponseDto> getAptMngList(
            int cmpSeq,
            String aptSchVal,
            int doType,
            int pageNum,
            int pageSize,
            String myaptYn) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_ATP_MNG_L";

        try {
            params.put("@COMPANY_SEQ", cmpSeq);
            params.put("@APT_SEARCH_VAL", aptSchVal);
            params.put("@ADDR_DO_TYPE", doType);
            params.put("@PAGE_NUM", pageNum);
            params.put("@PAGE_SIZE", pageSize);
            params.put("@MYAPT_YN", myaptYn);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //내단지명 조회
    public List<AptNameResponseDto> searchAptList(int cmpSeq, String aptSchVal) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_ATP_APTNAME_SEARCH_L";

        try {
            params.put("@COMPANY_SEQ", cmpSeq);
            params.put("@APT_SEARCH_VAL", aptSchVal);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //내단지 동호 리스트 상세
    public List<AptInfoDto> getAptInfoList(int aptSeq, String aptDong) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_INFO_L";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@APT_DONG", aptDong);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //내단지 세대정보 삭제
    public int deleteAptInfoAndDongInfo(int aptSeq, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_DONG_INFO_D";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@REG_SEQ", regSeq);

            sqlSession.delete(mapperName + "." + spName, params);

            return (int) params.get("@RETVAL");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //내단지 세대정보 수정
    public int updateAptInfoAndDongInfo(int aptSeq, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_DONG_INFO_U";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@REG_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);

            return (int) params.get("@RETVAL");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //해당 단지의 대표 내단지관리자 조회
    public AdminMemberDto getAdminAptMngMemberInfoByXiCode(int aptSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MNG_MEMBER_R";

        try {
            params.put("@APT_SEQ", aptSeq);

            return sqlSession.selectOne(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //내단지 제공 서비스 설정 저장
    public void setAptProvideServiceInfo(
            int aptSeq,
            String authYn,
            String operationYn,
            String facilityYn,
            String conciergeYn,
            String myaptYn,
            String xiCode,
            int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_SET_U";

        try {
            params.put("@SEQ", aptSeq);
            params.put("@AUTH_YN", authYn);
            params.put("@OPERATION_YN", operationYn);
            params.put("@FACILITY_YN", facilityYn);
            params.put("@CONCIERGE_YN", conciergeYn);
            params.put("@MYAPT_YN", myaptYn);
            params.put("@XI_CODE", xiCode);
            params.put("@REG_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //내단지 제공 서비스 조회
    public AptServiceResponseDto getAptProvideServiceInfo(int aptSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_SET_R";

        try {
            params.put("@SEQ", aptSeq);

            return sqlSession.selectOne(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //내단지 세대 엑셀 파일 히스토리 조회
    public List<ExcelFileInfoDto> getAptInfoExcelHis(int aptSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_R_FMCS_ADMIN_APT_INFO_EXCEL_FILES_L";

        try {
            params.put("@APT_SEQ", aptSeq);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //관리업체 변경
    public void editAptMngAdminCompany(
            int companySeq,
            int aptSeq,
            int modSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_R_FMCS_ADMIN_COMPANY_APT_U";

        try {
            params.put("@COMPANY_SEQ", companySeq);
            params.put("@APT_SEQ", aptSeq);
            params.put("@MOD_SEQ", modSeq);

            sqlSession.update(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }
}