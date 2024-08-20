package com.xi.fmcs.domain.settings.repository;

import com.xi.fmcs.domain.settings.model.AptFooterDto;
import com.xi.fmcs.domain.settings.model.AptFooterSaveRequestDto;
import com.xi.fmcs.support.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AptFooterRepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();


    public AptFooterDto getAptFooter(int aptSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_FOOTER_R";

        try {
            params.put("@APT_SEQ", aptSeq);

            return sqlSession.selectOne(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    /// 내 단지 Footer  등록
    public int insertAptFooter(AptFooterSaveRequestDto aptFooterSaveRequest, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_FOOTER_C";

        try {
            params.put("@APT_SEQ", aptFooterSaveRequest.getAptSeq());
            params.put("@FOOTER_TEL_NO", aptFooterSaveRequest.getFooterTelNo());
            params.put("@FOOTER_FAX_NO", aptFooterSaveRequest.getFooterFaxNo());
            params.put("@FOOTER_JIBUN", aptFooterSaveRequest.getFooterJibun());
            params.put("@FOOTER_ROAD", aptFooterSaveRequest.getFooterRoad());
            params.put("@FOOTER_CONTENT", aptFooterSaveRequest.getFooterContent());
            params.put("@FOOTER_HOURS", aptFooterSaveRequest.getFooterHours());
            params.put("@FOOTER_HOUSEHOLD_CNT", aptFooterSaveRequest.getFooterHouseholdCnt());
            params.put("@APT_X", aptFooterSaveRequest.getAptX());
            params.put("@APT_Y", aptFooterSaveRequest.getAptY());
            params.put("@REG_SEQ", regSeq);

            sqlSession.insert(mapperName + "." + spName, params);
            LogUtil.insertAminMemberLog(String.valueOf(regSeq),"[내단지 footer 등록] [단지 apt.seq =" + aptFooterSaveRequest.getAptSeq() + "]");
            return (int) params.get("@SEQ");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    /// 내 단지 Footer  수정
    public void updateAptFooter(AptFooterSaveRequestDto aptFooterSaveRequest, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_FOOTER_U";

        try {
            params.put("@SEQ", aptFooterSaveRequest.getSeq());
            params.put("@FOOTER_TEL_NO", aptFooterSaveRequest.getFooterTelNo());
            params.put("@FOOTER_FAX_NO", aptFooterSaveRequest.getFooterFaxNo());
            params.put("@FOOTER_JIBUN", aptFooterSaveRequest.getFooterJibun());
            params.put("@FOOTER_ROAD", aptFooterSaveRequest.getFooterRoad());
            params.put("@FOOTER_CONTENT", aptFooterSaveRequest.getFooterContent());
            params.put("@FOOTER_HOURS", aptFooterSaveRequest.getFooterHours());
            params.put("@FOOTER_HOUSEHOLD_CNT", aptFooterSaveRequest.getFooterHouseholdCnt());
            params.put("@APT_X", aptFooterSaveRequest.getAptX());
            params.put("@APT_Y", aptFooterSaveRequest.getAptY());
            params.put("@APT_SEQ", aptFooterSaveRequest.getAptX());
            params.put("@MOD_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);
            LogUtil.insertAminMemberLog(String.valueOf(regSeq),"[apt footer 수정] [apt seq=" + aptFooterSaveRequest.getAptSeq() + "]");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }
}
