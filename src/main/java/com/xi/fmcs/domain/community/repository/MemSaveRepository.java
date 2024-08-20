package com.xi.fmcs.domain.community.repository;

import com.xi.fmcs.domain.community.model.memSave.MemSaveDetailResponseDto;
import com.xi.fmcs.domain.community.model.memSave.MemSaveListDto;
import com.xi.fmcs.domain.community.model.memSave.MemSaveListRequestDto;
import com.xi.fmcs.support.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MemSaveRepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //회원글보관함 목록
    public List<MemSaveListDto> getBoardMemSaveList(MemSaveListRequestDto memSaveListRequest, int page) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARD_MEMSAVE_L";
        String aptDong = memSaveListRequest.getSrchDong().equals("ALL") ? "" : memSaveListRequest.getSrchDong();
        String aptHo = memSaveListRequest.getSrchHo().equals("ALL") ? "" : memSaveListRequest.getSrchHo();
        String text = memSaveListRequest.getSrchTxt() == null ? "" : memSaveListRequest.getSrchTxt();
        try {
            params.put("@APT_SEQ", memSaveListRequest.getAptSeq());
            params.put("@SRCH_DONG", aptDong);
            params.put("@SRCH_HO", aptHo);
            params.put("@SRCH_TXT", text);
            params.put("@SRCH_START_DT", memSaveListRequest.getSrchStartDt());
            params.put("@SRCH_END_DT", memSaveListRequest.getSrchEndDt());
            params.put("@PAGE_NUM", page);

            return sqlSession.selectList(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //회원글보관함 상세
    public MemSaveDetailResponseDto getBoardMemSaveDetail(int seq, String boardType) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARD_MEMSAVE_R";
        try {
            params.put("@SEQ", seq);
            params.put("@BOARD_TYPE", boardType);

            return sqlSession.selectOne(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }
}
