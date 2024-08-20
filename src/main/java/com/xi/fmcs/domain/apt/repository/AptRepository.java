package com.xi.fmcs.domain.apt.repository;

import com.xi.fmcs.support.util.LogUtil;
import com.xi.fmcs.domain.apt.model.*;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AptRepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //내단지 추가
    public void insertAdminCompanyApt(
            int cmpSeq,
            String admXicode,
            String aptName,
            String addr,
            int addrDoType,
            int regSeq
    ) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_COMPANY_APT_C";

        try {
            params.put("@COMPANY_SEQ", cmpSeq);
            params.put("@XI_CODE", admXicode);
            params.put("@APT_NAME", aptName);
            params.put("@ADDR", addr);
            params.put("@ADDR_DO_TYPE", addrDoType);
            params.put("@REG_SEQ", regSeq);

            sqlSession.insert(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //내단지 삭제
    public int delAptMng(int cmpSeq, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_D";

        try {
            params.put("@APT_SEQ", cmpSeq);
            params.put("@REG_SEQ", regSeq);

            sqlSession.delete(mapperName + "." + spName, params);
            return (int) params.get("@RETVAL");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //업체에 등록된 단지 목록
    public List<AdminCompanyAptResponseDto> getCompanyAptListByCmpSeq(int cmpSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_COMPANY_APT_L";

        try {
            params.put("@COMPANY_SEQ", cmpSeq);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //내 단지 정보 조회
    public AptXicodeDto getAdminAptMngXicodeByAdminSeq(int adminSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MNG_R";

        try {
            params.put("@ADMIN_SEQ", adminSeq);
            return sqlSession.selectOne(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //내단지 상세
    public AptDetailDto getAdminCompanyAptByAptSeq(int aptSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_BY_SEQ_R";

        try {
            params.put("@SEQ", aptSeq);
            return sqlSession.selectOne(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //단지 동정보 조회
    public List<AptDongInfoDto> getAptDongList(int aptSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_ATP_DONG_L";

        try {
            System.out.println("aptSeq: " + aptSeq);
            params.put("@APT_SEQ", aptSeq);
            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //단지 동호정보 조회
    public List<AptDongHoResponseDto> getAptDongHoList(int aptSeq, String dong) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_ATP_DONG_HO_L";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@DONG", dong);
            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //내단지 동 호 이사 정보 등록 (사용X, 현재 XI_SP_R_FMCS_ADMIN_APT_MOVE_INFO_CU로 통합 사용중)
    public void insertAptMoveInfo(List<AptMoveInfoDto> aptMoveInfoDtoList, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_INFO_C";

        try {
            for (AptMoveInfoDto aptMoveInfo : aptMoveInfoDtoList) {
                params.put("@APT_INFO_SEQ", aptMoveInfo.getAptInfoSeq());
                params.put("@ELVTR_NO", aptMoveInfo.getElvtrNo());
                params.put("@MOVE_TYPE", aptMoveInfo.getMoveType());
                params.put("@REG_SEQ", regSeq);
                sqlSession.insert(mapperName + "." + spName, params);
            }
            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[내단지 이사정보 등록] [등록 여부 = Y]");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //내단지 동 호 이사 정보 수정 (사용X, 현재 XI_SP_R_FMCS_ADMIN_APT_MOVE_INFO_CU로 통합 사용중)
    public void updateAptMoveInfo(List<AptMoveInfoDto> aptMoveInfoDtoList, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_INFO_U";

        try {
            for (AptMoveInfoDto aptMoveInfo : aptMoveInfoDtoList) {
                params.put("@APT_INFO_SEQ", aptMoveInfo.getAptInfoSeq());
                params.put("@ELVTR_NO", aptMoveInfo.getElvtrNo());
                params.put("@MOVE_TYPE", aptMoveInfo.getMoveType());
                params.put("@REG_SEQ", regSeq);
                sqlSession.update(mapperName + "." + spName, params);
            }
            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[내단지 이사정보 수정] [수정 여부 = Y]");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    // APT_MOVE_INFO 존재하면 U, 없으면 C 한번에 처리
    public void insertOrUpdateAptMoveInfo(List<AptMoveInfoDto> aptMoveInfoDtoList, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_R_FMCS_ADMIN_APT_MOVE_INFO_CU";

        try {
            for (AptMoveInfoDto aptMoveInfo : aptMoveInfoDtoList) {
                params.put("@APT_INFO_SEQ", aptMoveInfo.getAptInfoSeq());
                params.put("@ELVTR_NO", aptMoveInfo.getElvtrNo());
                params.put("@MOVE_TYPE", aptMoveInfo.getMoveType());
                params.put("@REG_SEQ", regSeq);
                sqlSession.insert(mapperName + "." + spName, params);
            }
            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[내단지 이사정보 등록/수정] [등록 여부 = Y]");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //APT 주소 또는 MYAPT_YN 변경
    public void updateAptUseYnOrAddr(int aptSeq, int workCd, int modSeq, String myaptYn, String addr, int addrDoType) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_R_FMCS_ADMIN_APT_U";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@WORK_CD", workCd); //1이면 myaptYn 변경, 2면 Addr,AddrDoType 변경
            params.put("@MOD_SEQ", modSeq);
            params.put("@MYAPT_YN", myaptYn);
            params.put("@ADDR", addr);
            params.put("@ADDR_DO_TYPE", addrDoType);
            sqlSession.update(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //관리자 이메일로 중복 체크
    public int checkAptInfoByAptSeq(int aptSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_INFO_CHECK";

        try {
            params.put("@APT_SEQ", aptSeq);
            sqlSession.insert(mapperName + "." + spName, params);
            return (int) params.get("@RET_VAL");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //자이코드 중복체크
    public int checkXicode(String xiCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_R_FMCS_ADMIN_APT_XI_CODE_CHECK";

        try {
            params.put("@XI_CODE", xiCode);
            sqlSession.insert(mapperName + "." + spName, params);
            return (int) params.get("@RET_VAL"); // 자이코드 중복체크 (중복 : 1, 없으면 : 0)
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //아파트 동대표 취소
    public void updateAptDongRepresentCancel(int aptSeq, int memberSeq, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_DONG_REPRESENT_CANCEL_U";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@MEMBER_SEQ", memberSeq);
            params.put("@REG_SEQ", regSeq);
            sqlSession.update(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //APT_DONG_INFO 테이블 아파트 동대표 설정
    public void updatAptDongRepresent(int aptSeq, String dong, int memberSeq, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_DONG_REPRESENT_U";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@DONG", dong);
            params.put("@MEMBER_SEQ", memberSeq);
            params.put("@REG_SEQ", regSeq);
            sqlSession.update(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

}