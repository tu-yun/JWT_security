package com.xi.fmcs.domain.moveReserve.repository;

import com.xi.fmcs.support.util.LogUtil;
import com.xi.fmcs.domain.moveReserve.model.*;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MoveReserveRepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //이사 예약 목록
    public List<MoveReserveListResponseDto> getAptMoveReserveList(MoveReserveListRequestDto moveReserveListRequest, int page, int pageSize) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_RESERVE_L";

        try {
            params.put("@APT_SEQ", moveReserveListRequest.getAptSeq());
            params.put("@DONG", moveReserveListRequest.getDong());
            params.put("@HO", moveReserveListRequest.getHo());
            params.put("@S_DATE", moveReserveListRequest.getStartDate());
            params.put("@E_DATE", moveReserveListRequest.getEndDate());
            params.put("@SCH_VAL", moveReserveListRequest.getSchVal());
            params.put("@PAGE_NUM", page);
            params.put("@PAGE_SIZE", pageSize);

            System.out.println("PARAMS : "+params.toString());
            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //이사 예약 히스토리 목록
    public List<MoveReserveHisListResponseDto> getAptMoveReserveHisList(MoveReserveListRequestDto moveReserveListRequest, int page, int pageSize) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_RESERVE_HIS_L";

        try {
            params.put("@APT_SEQ", moveReserveListRequest.getAptSeq());
            params.put("@DONG", moveReserveListRequest.getDong());
            params.put("@HO", moveReserveListRequest.getHo());
            params.put("@S_DATE", moveReserveListRequest.getStartDate());
            params.put("@E_DATE", moveReserveListRequest.getEndDate());
            params.put("@SCH_VAL", moveReserveListRequest.getSchVal());
            params.put("@PAGE_NUM", page);
            params.put("@PAGE_SIZE", pageSize);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //달력 이사 예약 목록
    public List<ReserveCalendarDto> getAptMoveReserveCalList(
            ReserveCalendarRequestDto calendarRequest,
            String startDate,
            String endDate
    ) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_RESERVE_CAL_L";

        try {
            params.put("@APT_SEQ", calendarRequest.getAptSeq());
            params.put("@DONG", calendarRequest.getDong());
            params.put("@EV_NO", calendarRequest.getEvNo());
            params.put("@START_DATE", startDate);
            params.put("@END_DATE", endDate);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    // 이사 예약 저장
    public int insertMoveReserve(MoveReserveInsertRequestDto insertRequest, Integer regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_RESERVE_C";

        try {
            params.put("@APT_SEQ", insertRequest.getAptSeq());
            params.put("@DONG", insertRequest.getDong());
            params.put("@HO", insertRequest.getHo());
            params.put("@EV_NO", insertRequest.getEvNo());
            params.put("@MOVE_DATE", insertRequest.getMoveDate());
            params.put("@APT_MOVE_TIME_SET_SEQ", insertRequest.getAptMoveTimeSetSeq());
            params.put("@MOVE_TYPE", insertRequest.getMoveType());
            params.put("@MOVE_INFO_MOVE_TYPE", insertRequest.getMoveType());
            params.put("@REG_NAME", insertRequest.getRegName());
            params.put("@IS_ADMIN", insertRequest.getIsAdmin());
            params.put("@REG_TEL", insertRequest.getRegTel());
            params.put("@REG_SEQ", regSeq);

            LogUtil.insertAminMemberLog(String.valueOf(regSeq),
                    "[이사예약 등록] [info =" + insertRequest.getAptSeq() + "|" + insertRequest.getDong() + "|" + insertRequest.getHo() + "]");
            sqlSession.insert(mapperName + "." + spName, params);
            return (int) params.get("@RET_VAL");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }


    // 이사 예약 히스토리 저장
    public void saveMoveReserveHis(MoveReserveInsertRequestDto insertRequest, Integer regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_MOVE_RESERVE_HISTORY_C";

        try {
            params.put("@APT_SEQ", insertRequest.getAptSeq());
            params.put("@DONG", insertRequest.getDong());
            params.put("@HO", insertRequest.getHo());
            params.put("@ELVTR_NO", insertRequest.getEvNo());
            params.put("@MOVE_DATE", insertRequest.getMoveDate());
            params.put("@APT_MOVE_TIME_SET_SEQ", insertRequest.getAptMoveTimeSetSeq());
            params.put("@MOVE_TYPE", insertRequest.getMoveType());
            params.put("@REG_NAME", insertRequest.getRegName());
            params.put("@IS_ADMIN", insertRequest.getIsAdmin());
            params.put("@REG_TEL", insertRequest.getRegTel());
            params.put("@REG_SEQ", regSeq);
            params.put("@UNIQUE_ID", null);

            sqlSession.insert(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //이사예약 시간설정 조회
    public List<MoveTimeSetDto> getAptMoveTimeSetInfo(int aptSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_TIME_SET_L";

        try {
            params.put("@APT_SEQ", aptSeq);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    // 입주기간 / 앱설정 저장
    public int insertMoveSet(MoveSetRequestDto moveSet, LocalDateTime preStartDate, LocalDateTime preEndDate, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_SET_C";

        try {
            params.put("@APT_SEQ", moveSet.getAptSeq());
            params.put("@START_DATE", moveSet.getStartDate());
            params.put("@END_DATE", moveSet.getEndDate());
            params.put("@ACTION_TYPE", moveSet.getActionType());
            params.put("@MOVE_TEL", moveSet.getMoveTel());
            params.put("@APP_CONTENTS", moveSet.getAppContents());
            params.put("@PRE_START_DATE", preStartDate);
            params.put("@PRE_END_DATE", preEndDate);
            params.put("@REG_SEQ", regSeq);

            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[입주기간 설정 등록] [aptSeq=" + moveSet.getAptSeq() + "]");
            return sqlSession.selectOne(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return 0;
        }
    }

    // 입주기간 / 앱설정 수정
    public void updateMoveSet(MoveSetRequestDto moveSet, LocalDateTime preStartDate, LocalDateTime preEndDate, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_SET_U";

        try {
            params.put("@SEQ", moveSet.getMoveSetSeq());
            params.put("@START_DATE", moveSet.getStartDate());
            params.put("@END_DATE", moveSet.getEndDate());
            params.put("@ACTION_TYPE", moveSet.getActionType());
            params.put("@MOVE_TEL", moveSet.getMoveTel());
            params.put("@APP_CONTENTS", moveSet.getAppContents());
            params.put("@PRE_START_DATE", preStartDate);
            params.put("@PRE_END_DATE", preEndDate);
            params.put("@REG_SEQ", regSeq);

            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[입주기간 설정 수정] [SEQ=" + moveSet.getMoveSetSeq() + "]");
            sqlSession.update(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //입주기간 / 앱설정 조회
    public MoveSetResponseDto getMoveSet(int aptSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_SET_R";

        try {
            params.put("@APT_SEQ", aptSeq);
            MoveSetResponseDto moveSet = sqlSession.selectOne(mapperName + "." + spName, params);

            String preStartDate = moveSet.getPreStartDate();
            moveSet.setPreStartDate(preStartDate.substring(0, 10));
            moveSet.setPreStartHour(preStartDate.substring(11, 13));
            moveSet.setPreStartMin(preStartDate.substring(14));

            String preEndDate = moveSet.getPreEndDate();
            moveSet.setPreEndDate(preEndDate.substring(0, 10));
            moveSet.setPreEndHour(preEndDate.substring(11, 13));
            moveSet.setPreEndMin(preEndDate.substring(14));

            return moveSet;
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //해당 호와 동일한 엘레베이터 번호를 가진 이사예약 목록
    public List<ReserveCalendarDto> getReserveCal(int aptSeq, String dong, String ho) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_R_FMCS_ADMIN_APT_MOVE_RESERVE_L";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@DONG", dong);
            params.put("@HO", ho);
            return sqlSession.selectOne(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    // 이사예약 시간 설정 변경
    public void updateAptMoveTimeSet(
            int aptMoveTimeSetSeq,
            String userView,
            String adminView,
            String startTime,
            String endTime,
            int regSeq
    ) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_TIME_SET_U";

        try {
            params.put("@SEQ", aptMoveTimeSetSeq);
            params.put("@USER_VIEW", userView);
            params.put("@ADMIN_VIEW", adminView);
            params.put("@START_TIME", startTime);
            params.put("@END_TIME", endTime);
            params.put("@REG_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    // 이사예약 시간 설정 등록
    public int insertAptMoveTimeSet(
            int aptSeq,
            int orderNum,
            String userView,
            String adminView,
            String startTime,
            String endTime,
            int regSeq
    ) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_TIME_SET_C";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@ORDERNUM", orderNum);
            params.put("@USER_VIEW", userView);
            params.put("@ADMIN_VIEW", adminView);
            params.put("@START_TIME", startTime);
            params.put("@END_TIME", endTime);
            params.put("@REG_SEQ", regSeq);

            sqlSession.insert(mapperName + "." + spName, params);
            return (int) params.get("@SEQ");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //이사예약 시간 설정 기본
    public void insertAptMoveTimeDefaultSet(int aptSeq, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_SYS_APT_MOVE_TIME_SET_C";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@ADMIN_SEQ", regSeq);

            sqlSession.insert(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    // 오늘 이사예약 건수
    public int getTodayMoveReserveCnt(int aptSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_TODAY_MOVE_RESERVE_CNT_R";

        try {
            params.put("@APT_SEQ", aptSeq);

            return sqlSession.selectOne(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return 0;
        }
    }

    // 이사예약 취소
    public void deleteMoveReserve(int aptSeq, int aptMoveReserveSeq, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_RESERVE_D";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@REG_SEQ", aptSeq);

            sqlSession.delete(mapperName + "." + spName, params);
            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[이사예약 취소] [seq =" + aptMoveReserveSeq + "]");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    // 이사예약 확인
    public int checkMoveReserve(int aptSeq, String dong, String ho, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_RESERVE_DUBCHK";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@DONG", aptSeq);
            params.put("@HO", aptSeq);

            int result = sqlSession.selectOne(mapperName + "." + spName, params);
            LogUtil.insertAminMemberLog(String.valueOf(regSeq),
                    "[이사예약 중복확인] [aptseq =" + aptSeq + ",dong=" + dong + ",ho=" + ho + "]");
            return result;
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    // 이사예약 상세
    public List<MoveReserveDetailResponseDto> getAptMoveReserveDetailList(int aptSeq, String dong, int regSeq, String moveDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_RESERVE_DETAIL_R";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@DONG", dong);
            params.put("@EV_NO", regSeq);
            params.put("@MOVE_DATE", moveDate);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    // 이사예약 카운트
    public int getAptMoveReserveListDate(int aptSeq, String moveDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_RESERVE_L_DATE";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@S_DATE", moveDate);

            return sqlSession.selectOne(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //이사예약 엑셀 다운로드용 리스트 뽑기
    public List<MoveReserveListAllDto> getAptMoveReserveListAll(MoveReserveListRequestDto moveReserveListRequest) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_RESERVE_L_DATE";

        try {
            params.put("@APT_SEQ", moveReserveListRequest.getAptSeq());
            params.put("@DONG", moveReserveListRequest.getDong());
            params.put("@HO", moveReserveListRequest.getHo());
            params.put("@S_DATE", moveReserveListRequest.getStartDate());
            params.put("@E_DATE", moveReserveListRequest.getEndDate());
            params.put("@SCH_VAL", moveReserveListRequest.getSchVal());

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //이사예약 조회 : 엘레베이터-이사방법
    public ElvtrNoAndMoveTypeDto getElvtrNoAndMoveType(int aptSeq, String dong, String floor, String ling) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_INFO_R";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@DONG", dong);
            params.put("@FLOOR", floor);
            params.put("@LINE", ling);

            return sqlSession.selectOne(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }
}
