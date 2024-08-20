package com.xi.fmcs.domain.community.repository;

import com.xi.fmcs.support.util.LogUtil;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.model.boardA.BoardADto;
import com.xi.fmcs.domain.community.model.boardA.BoardAListDto;
import com.xi.fmcs.domain.community.model.boardA.BoardASaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BoardARepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //공공게시판 목록
    public List<BoardAListDto> getBoardAList(
            int groupSeq, String saveYn, String srchTxt,
            String srchStartDt, String srchEndDt, int page) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDA_L";

        try {
            params.put("@GROUP_SEQ", groupSeq);
            params.put("@SAVE_YN", saveYn);
            params.put("@SRCH_TXT", srchTxt);
            params.put("@SRCH_START_DT", srchStartDt);
            params.put("@SRCH_END_DT", srchEndDt);
            params.put("@PAGE_NUM", page);

            return sqlSession.selectList(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //공공게시판 상세
    public BoardADto getBoardADetail(int seq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDA_R";

        try {
            params.put("@SEQ", seq);

            return sqlSession.selectOne(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }


    //공공게시판 수정
    public void updateBoardA(BoardASaveRequestDto boardASaveRequest, AdminMemberLoginDto loginDto) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDA_U";

        try {
            params.put("@SEQ", boardASaveRequest.getSeq());
            params.put("@WRITER_NICKNM", loginDto.getNickname());
            params.put("@WRITER_NM", loginDto.getName());
            params.put("@TITLE", boardASaveRequest.getTitle());
            params.put("@CONTENTS", boardASaveRequest.getContents());
            params.put("@SAVE_YN", boardASaveRequest.getSaveYn());
            params.put("@TOP_YN", boardASaveRequest.getTopYn());
            params.put("@ADMIN_SEQ", loginDto.getSeq());

            sqlSession.update(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //공공게시판 등록
    public void insertBoardA(BoardASaveRequestDto boardASaveRequest, AdminMemberLoginDto loginDto) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDA_C";

        try {
            params.put("@GROUP_SEQ", boardASaveRequest.getGroupSeq());
            params.put("@WRITER_NICKNM", loginDto.getNickname());
            params.put("@WRITER_NM", loginDto.getName());
            params.put("@TITLE", boardASaveRequest.getTitle());
            params.put("@CONTENTS", boardASaveRequest.getContents());
            params.put("@SAVE_YN", boardASaveRequest.getSaveYn());
            params.put("@TOP_YN", boardASaveRequest.getTopYn());
            params.put("@ADMIN_SEQ", loginDto.getSeq());

            sqlSession.insert(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //공공게시판 삭제
    public void delBoardA(int seq, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDA_D";

        try {
            params.put("@SEQ", seq);
            params.put("@ADMIN_SEQ", regSeq);

            sqlSession.delete(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }
}
