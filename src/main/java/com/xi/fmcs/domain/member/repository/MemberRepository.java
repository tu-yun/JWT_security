package com.xi.fmcs.domain.member.repository;

import com.xi.fmcs.support.util.LogUtil;
import com.xi.fmcs.domain.member.model.*;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //회원 조회
    public List<MemberListResponseDto> getMemberList(MemberListRequestDto memberListRequest, int status, String dongMngYn, int page, int pageSize) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MEMBER_L";

        try {
            params.put("@APT_SEQ", memberListRequest.getAptSeq());
            params.put("@STATUS", status);
            params.put("@START_AUTH_DATE", memberListRequest.getStartDate());
            params.put("@END_AUTH_DATE", memberListRequest.getEndDate());
            params.put("@APT_DONG", memberListRequest.getDong());
            params.put("@APT_HO", memberListRequest.getHo());
            params.put("@SEARCH_VAL", memberListRequest.getSchVal());
            params.put("@RESIDENCE_TYPE", memberListRequest.getResidenceType());
            params.put("@HOUSEHOLDER_TYPE", memberListRequest.getHouseType());
            params.put("@DONG_MNG_YN", dongMngYn);
            params.put("@PAGE_NUM", page);
            params.put("@PAGE_SIZE", pageSize);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //승인대기, 미승인 회원 조회
    public List<MemberWaitListResponseDto> getMemberNoneDateList(MemberWaitListRequestDto memberRequest, int status, int page, int pageSize) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MEMBER_NONE_DATE_L";

        try {
            params.put("@APT_SEQ", memberRequest.getAptSeq());
            params.put("@STATUS", status);
            params.put("@APT_DONG", memberRequest.getDong());
            params.put("@APT_HO", memberRequest.getHo());
            params.put("@SEARCH_VAL", memberRequest.getSchVal());
            params.put("@PAGE_NUM", page);
            params.put("@PAGE_SIZE", pageSize);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //전출회원 목록
    public List<MemberListResponseDto> getMoveOutMemberList(MemberListRequestDto memberListRequest, int status, int page, int pageSize) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MEMBER_MOVE_OUT_L";

        try {
            params.put("@APT_SEQ", memberListRequest.getAptSeq());
            params.put("@STATUS", status);
            params.put("@START_AUTH_DATE", memberListRequest.getStartDate());
            params.put("@END_AUTH_DATE", memberListRequest.getEndDate());
            params.put("@APT_DONG", memberListRequest.getDong());
            params.put("@APT_HO", memberListRequest.getHo());
            params.put("@SEARCH_VAL", memberListRequest.getSchVal());
            params.put("@RESIDENCE_TYPE", memberListRequest.getResidenceType());
            params.put("@HOUSEHOLDER_TYPE", memberListRequest.getHouseType());
            params.put("@PAGE_NUM", page);
            params.put("@PAGE_SIZE", pageSize);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //회원 등록
    public int insertMember(MemberInsertOrUpdateRequestDto memberInsertRequest, String xianClubYn, int status, int regSeq, String uniqueId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MEMBER_C";

        try {
            params.put("@APT_SEQ", memberInsertRequest.getAptSeq());
            params.put("@XI_CODE", memberInsertRequest.getXiCode());
            params.put("@APT_DONG", memberInsertRequest.getDong());
            params.put("@APT_HO", memberInsertRequest.getHo());
            params.put("@NAME", memberInsertRequest.getName());
            params.put("@NICKNAME", memberInsertRequest.getNickName());
            params.put("@SEX", memberInsertRequest.getSex());
            params.put("@BIRTHDAY", memberInsertRequest.getBirthday());
            params.put("@PHONE", memberInsertRequest.getPhone());
            params.put("@RESIDENCE_TYPE", memberInsertRequest.getResidenceType());
            params.put("@HOUSEHOLDER_TYPE", memberInsertRequest.getHouseholderType());
            params.put("@XIAN_CLUB_YN", xianClubYn);
            params.put("@STATUS", status);
            params.put("@RF_CARD_NO", memberInsertRequest.getRfCardNo());
            params.put("@REG_SEQ", regSeq);
            params.put("@IMAGE_URL", memberInsertRequest.getImageUrl());
            params.put("@VOTE_YN", memberInsertRequest.getVoteYn());
            params.put("@UNIQUE_ID", uniqueId);
            params.put("@EMAIL", memberInsertRequest.getEmail());

            sqlSession.insert(mapperName + "." + spName, params);
            int result = (int) params.get("@SEQ");
            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[회원 정보 등록] [id=" + result + "]");
            return result;
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //회원 수정
    public void updateMember(MemberInsertOrUpdateRequestDto memberRequest, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MEMBER_U";

        try {
            params.put("@SEQ", memberRequest.getMemberSeq());
            params.put("@NICKNAME", memberRequest.getNickName());
            params.put("@RES_TYPE", memberRequest.getResidenceType());
            params.put("@HOLDER_TYPE", memberRequest.getHouseholderType());
            params.put("@RF_CARD_NO", memberRequest.getRfCardNo());
            params.put("@IMAGE_URL", memberRequest.getImageUrl());
            params.put("@REG_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);
            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[회원 정보 수정] [id=" + memberRequest.getMemberSeq() + "]");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //특정 회원 조회
    public MemberRDto getMemberBySeq(int memberSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MEMBER_R";

        try {
            params.put("@SEQ", memberSeq);

            return sqlSession.selectOne(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //회원 상태변경
    public void updateMemberStatus(int memberSeq, int status, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MEMBER_STATUS_U";

        try {
            params.put("@SEQ", memberSeq);
            params.put("@STATUS", status);
            params.put("@REG_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);
            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[회원 승인 /미승인 /대기 /전출 처리] [id=" + memberSeq + "] [status=" + status + "]");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //MEMBER 테이블 아파트 동대표 설정
    public void updateAptMemberDongMng(int memberSeq, String dongMngYn, String dongMngTitle, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MEMBER_DONG_MNG_U";

        try {
            params.put("@SEQ", memberSeq);
            params.put("@DONG_MNG_YN", dongMngYn);
            params.put("@DONG_MNG_TITLE", dongMngTitle);
            params.put("@REG_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }


    //MEMBER 테이블 아파트 동대표 취소
    public void updateAptMemberDongMngCancel(int memberSeq, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MEMBER_DONG_MNG_CANCEL_U";

        try {
            params.put("@SEQ", memberSeq);
            params.put("@REG_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //동대표 설정 여부 확인
    public int getMemberDongMngCntByArrSeq(String arrSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MEMBER_DONG_MNG_CNT";

        try {
            params.put("@ARR_SEQ", arrSeq);
            sqlSession.selectOne(mapperName + "." + spName, params);
            return (int) params.get("@COUNT");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //회원 이메일 체크
    public int getEmailCheck(String email) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_XI_MEMBER_EMAIL_CHECK";

        try {
            params.put("@EMAIL", email);
            return sqlSession.selectOne(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return -1;
        }
    }

    //회원정보 조회(엑셀 다운로드를 위한)
    public List<MemberExcelDownDto> getMemberListAll(MemberListRequestDto memberListRequest, int status, String dongMngYn) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MEMBER_ALL_L";

        try {
            params.put("@APT_SEQ", memberListRequest.getAptSeq());
            params.put("@STATUS", status);
            params.put("@START_AUTH_DATE", memberListRequest.getStartDate());
            params.put("@END_AUTH_DATE", memberListRequest.getEndDate());
            params.put("@APT_DONG", memberListRequest.getDong());
            params.put("@APT_HO", memberListRequest.getHo());
            params.put("@SEARCH_VAL", memberListRequest.getSchVal());
            params.put("@RESIDENCE_TYPE", memberListRequest.getResidenceType());
            params.put("@HOUSEHOLDER_TYPE", memberListRequest.getHouseType());
            params.put("@DONG_MNG_YN", dongMngYn);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //회원 수정 승인 처리
    public void updateMemberStatusAndInfo(int memberSeq, int resType, int holderType, String xiClubYn, int status, String rfCardNo, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MEMBER_INFO_STATUS_U";

        try {
            params.put("@SEQ", memberSeq);
            params.put("@RES_TYPE", resType);
            params.put("@HOLDER_TYPE", holderType);
            params.put("@XI_CLUB_YN", xiClubYn);
            params.put("@STATUS", status);
            params.put("@RF_CARD_NO", rfCardNo);
            params.put("@REG_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);
            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[회원 승인] [id=" + memberSeq + "]");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }
}
