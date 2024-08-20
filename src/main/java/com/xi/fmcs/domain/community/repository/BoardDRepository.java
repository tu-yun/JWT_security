package com.xi.fmcs.domain.community.repository;

import com.xi.fmcs.domain.community.model.BoardCommonDeleteRequestDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDListDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDListRequestDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDSaveRequestDto;
import com.xi.fmcs.support.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BoardDRepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //질문답변게시판 목록
    public List<BoardDListDto> getBoardDList(BoardDListRequestDto boardDListRequest, int page) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDD_L";
        String srchDong = boardDListRequest.getSrchDong() == "ALL" ? "" : boardDListRequest.getSrchDong();
        String srchHo = boardDListRequest.getSrchHo() == "ALL" ? "" : boardDListRequest.getSrchHo();
        String srchTxt = boardDListRequest.getSrchTxt() == null ? "" : boardDListRequest.getSrchTxt();
        String srchReplySt = boardDListRequest.getSrchReplySt() == "ALL" ? "" : boardDListRequest.getSrchTxt();

        try {
            params.put("@GROUP_SEQ", boardDListRequest.getGroupSeq());
            params.put("@SRCH_DONG", srchDong);
            params.put("@SRCH_HO", srchHo);
            params.put("@SRCH_TXT", srchTxt);
            params.put("@SRCH_START_DT", boardDListRequest.getSrchStartDt());
            params.put("@SRCH_END_DT", boardDListRequest.getSrchEndDt());
            params.put("@SRCH_REPLY_ST", srchReplySt);
            params.put("@PAGE_NUM", page);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //질문답변게시판 상세
    public BoardDDto getBoardDDetail(int seq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDD_R";

        try {
            params.put("@SEQ", seq);

            return sqlSession.selectOne(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }


    //질문답변게시판 답변저장
    public void updateBoardD(BoardDSaveRequestDto boardDSaveRequest, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDD_U";

        try {
            params.put("@SEQ", boardDSaveRequest.getSeq());
            params.put("@ANSWER", boardDSaveRequest.getAnswer());
            params.put("@ADMIN_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //질문답변게시판 삭제 (실제로 삭제가 아님-> BoardD의 DEL_YN을 변경)
    public void delBoardD(BoardCommonDeleteRequestDto boardDeleteRequeste, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDD_D";

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
