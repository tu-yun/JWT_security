package com.xi.fmcs.domain.community.repository;

import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.model.BoardCommonDeleteRequestDto;
import com.xi.fmcs.domain.community.model.boardB.*;
import com.xi.fmcs.support.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BoardBRepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //일반게시판/동게시판 목록
    public List<BoardBListDto> getBoardBList(BoardBListRequestDto boardBListRequest, int page) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDB_L";
        String srchDongBrd = boardBListRequest.getSrchDongBrd() == "ALL" ? "" : boardBListRequest.getSrchDongBrd();
        String srchDong = boardBListRequest.getSrchDong() == "ALL" ? "" : boardBListRequest.getSrchDong();
        String srchHo = boardBListRequest.getSrchHo() == "ALL" ? "" : boardBListRequest.getSrchHo();
        String srchTxt = boardBListRequest.getSrchTxt() == null ? "" : boardBListRequest.getSrchTxt();

        try {
            params.put("@GROUP_SEQ", boardBListRequest.getGroupSeq());
            params.put("@SRCH_DONG_BRD", srchDongBrd);
            params.put("@SRCH_DONG", srchDong);
            params.put("@SRCH_HO", srchHo);
            params.put("@SRCH_TXT", srchTxt);
            params.put("@SRCH_START_DT", boardBListRequest.getSrchStartDt());
            params.put("@SRCH_END_DT", boardBListRequest.getSrchEndDt());
            params.put("@PAGE_NUM", page);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //일반게시판/동게시판 상세
    public BoardBDto getBoardBDetail(int seq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDB_R";

        try {
            params.put("@SEQ", seq);

            return sqlSession.selectOne(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //일반/동 게시판 저장
    public int insertBoardB(BoardBSaveRequestDto boardBSaveRequest, AdminMemberLoginDto loginDto) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDB_C";

        try {
            params.put("@GROUP_SEQ", boardBSaveRequest.getGroupSeq());
            params.put("@BOARD_TYPE", boardBSaveRequest.getBoardType());
            params.put("@WRITER_NICKNM", loginDto.getNickname());
            params.put("@WRITER_NM", loginDto.getNickname());
            params.put("@TITLE", boardBSaveRequest.getTitle());
            params.put("@CONTENTS", boardBSaveRequest.getContents());
            params.put("@ADMIN_SEQ", loginDto.getSeq());

            sqlSession.insert(mapperName + "." + spName, params);
            return (int) params.get("@SEQ");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //일반/동 게시판 수정
    public void updateBoardB(BoardBSaveRequestDto boardBSaveRequest, AdminMemberLoginDto loginDto) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDB_U";

        try {
            params.put("@SEQ", boardBSaveRequest.getGroupSeq());
            params.put("@WRITER_NICKNM", loginDto.getNickname());
            params.put("@WRITER_NM", loginDto.getNickname());
            params.put("@TITLE", boardBSaveRequest.getTitle());
            params.put("@CONTENTS", boardBSaveRequest.getContents());
            params.put("@ADMIN_SEQ", loginDto.getSeq());

            sqlSession.update(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //일반/동 게시판 삭제
    public void delBoardB(BoardCommonDeleteRequestDto boardBDeleteRequest, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDB_D";

        try {
            params.put("@SEQ", boardBDeleteRequest.getSeq());
            params.put("@DEL_TYPE", boardBDeleteRequest.getDelType());
            params.put("@ADMIN_SEQ", regSeq);

            sqlSession.delete(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //동게시판 동 등록
    public void insertBoardBDong(int seq, String dongStr) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDB_DONG_C";

        try {
            params.put("@SEQ", seq);
            params.put("@DONG_STR", dongStr);

            sqlSession.insert(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }
}
