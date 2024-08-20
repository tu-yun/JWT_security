package com.xi.fmcs.domain.community.repository;

import com.xi.fmcs.domain.community.model.communitySet.BoardDisplaySetDto;
import com.xi.fmcs.domain.community.model.communitySet.BoardGroupResponseDto;
import com.xi.fmcs.support.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CommunitySetRepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //커뮤니티 설정 수정
    public void updateBoardGroup(int seq, String boardNm, String boardType, int orderNum, String iconUrl, String useYn, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDGROUP_U";

        try {
            params.put("@SEQ", seq);
            params.put("@BOARD_NM", boardNm);
            params.put("@BOARD_TYPE", boardType);
            params.put("@ORD_NUM", orderNum);
            params.put("@ICON_URL", iconUrl);
            params.put("@USE_YN", useYn);
            params.put("@ADMIN_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);

        }catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //커뮤니티 설정 저장
    public int insertBoardGroup(int aptSeq, int parentSeq, String boardNm, String boardType, int boardLvl, int orderNum, String iconUrl, String useYn, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDGROUP_C";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@PARENT_SEQ", parentSeq);
            params.put("@BOARD_NM", boardNm);
            params.put("@BOARD_TYPE", boardType);
            params.put("@BOARD_LVL", boardLvl);
            params.put("@ORD_NUM", orderNum);
            params.put("@ICON_URL", iconUrl);
            params.put("@FIX_YN", "N");
            params.put("@USE_YN", useYn);
            params.put("@ADMIN_SEQ", regSeq);

            return sqlSession.selectOne(mapperName + "." + spName, params);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //커뮤니티 설정 조회
    public List<BoardGroupResponseDto> getBoardGroupList(int aptSeq, int parentSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDGROUP_L";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@PARENT_SEQ", parentSeq);

            return sqlSession.selectList(mapperName + "." + spName, params);

        }catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //커뮤니티 설정 게시판 삭제
    public void delBoardGroup(int boardGroupSeq, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDGROUP_D";

        try {
            params.put("@SEQ", boardGroupSeq);
            params.put("@ADMIN_SEQ", regSeq);

            sqlSession.delete(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //노출항목 설정
    public void updateBoardDisplay(BoardDisplaySetDto boardDisplaySetRequest, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARD_DISPLAY_U";

        try {
            params.put("@APT_SEQ", boardDisplaySetRequest.getAptSeq());
            params.put("@BRD_DONGHO_DISP", boardDisplaySetRequest.getBoardDongHoDisp());
            params.put("@BRD_USERNM_DISP", boardDisplaySetRequest.getBoardUserNameDisp());
            params.put("@ADMIN_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //노출항목 상세
    public BoardDisplaySetDto getBoardDisplayDetail(int aptSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARD_DISPLAY_R";

        try {
            params.put("@APT_SEQ", aptSeq);

            return sqlSession.selectOne(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }


}
