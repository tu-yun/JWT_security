package com.xi.fmcs.domain.community.repository;

import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.model.BoardCommonDeleteRequestDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDListDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDListRequestDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDSaveRequestDto;
import com.xi.fmcs.domain.community.model.boardE.BoardEListDto;
import com.xi.fmcs.domain.community.model.boardE.BoardEListRequestDto;
import com.xi.fmcs.domain.community.model.boardE.BoardESaveRequestDto;
import com.xi.fmcs.support.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BoardERepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //한줄게시판 목록
    public List<BoardEListDto> getBoardEList(BoardEListRequestDto boardEListRequest, int page, int pageSize) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDE_L";
        String srchDong = boardEListRequest.getSrchDong() == "ALL" ? "" : boardEListRequest.getSrchDong();
        String srchHo = boardEListRequest.getSrchHo() == "ALL" ? "" : boardEListRequest.getSrchHo();
        String srchTxt = boardEListRequest.getSrchTxt() == null ? "" : boardEListRequest.getSrchTxt();

        try {
            params.put("@GROUP_SEQ", boardEListRequest.getGroupSeq());
            params.put("@SRCH_DONG", srchDong);
            params.put("@SRCH_HO", srchHo);
            params.put("@SRCH_TXT", srchTxt);
            params.put("@SRCH_START_DT", boardEListRequest.getSrchStartDt());
            params.put("@SRCH_END_DT", boardEListRequest.getSrchEndDt());
            params.put("@PAGE_NUM", page);
            params.put("@PAGE_SIZE", pageSize);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //한줄게시판 수정
    public void updateBoardE(BoardESaveRequestDto boardESaveRequest, AdminMemberLoginDto loginDto) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDE_U";

        try {
            params.put("@SEQ", boardESaveRequest.getSeq());
            params.put("@CONTENTS", boardESaveRequest.getContents());
            params.put("@WRITER_NICKNM", loginDto.getNickname());
            params.put("@WRITER_NM", loginDto.getName());
            params.put("@ADMIN_SEQ", loginDto.getSeq());

            sqlSession.update(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //한줄게시판 등록
    public void insertBoardE(BoardESaveRequestDto boardESaveRequest, AdminMemberLoginDto loginDto) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDE_C";

        try {
            params.put("@GROUP_SEQ", boardESaveRequest.getGroupSeq());
            params.put("@CONTENTS", boardESaveRequest.getContents());
            params.put("@WRITER_NICKNM", loginDto.getNickname());
            params.put("@WRITER_NM", loginDto.getName());
            params.put("@ADMIN_SEQ", loginDto.getSeq());

            sqlSession.insert(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //한줄게시판 삭제/회원글 보관함이동/회원글복원
    public void delBoardD(BoardCommonDeleteRequestDto boardDeleteRequeste, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDE_D";

        try {
            params.put("@SEQ", boardDeleteRequeste.getSeq());
            params.put("@DEL_TYPE", boardDeleteRequeste.getDelType());
            params.put("@ADMIN_SEQ", regSeq);

            sqlSession.delete(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }
}
