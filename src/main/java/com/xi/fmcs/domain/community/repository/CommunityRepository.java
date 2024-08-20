package com.xi.fmcs.domain.community.repository;

import com.xi.fmcs.domain.apt.model.AptDongHoResponseDto;
import com.xi.fmcs.domain.community.model.community.BoardGroupAptListDto;
import com.xi.fmcs.domain.community.model.community.BoardMainListDto;
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
public class CommunityRepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //동게시판 단지 동목록
    public List<String> getBoardAptDongList(int groupSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARD_APTDONG_L";

        try {
            params.put("@GROUP_SEQ", groupSeq);

            return sqlSession.selectList(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //동게시판 단지 동목록
    public List<BoardMainListDto> getMainBoardList(int aptSeq, String boardType) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARD_MAIN_L";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@BOARD_TYPE", boardType);

            return sqlSession.selectList(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //(커뮤니티)아파트 동호 목록(검색 호 SELECT )
    public List<AptDongHoResponseDto> getAptDongHoList(int aptSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARD_APTDONGHO_L";

        try {
            params.put("@APT_SEQ", aptSeq);

            return sqlSession.selectList(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //단지별 게시판 그룹 목록
    public List<BoardGroupAptListDto> getAptBoardGroupList(int aptSeq, int grpParentSeq, int grpSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_BOARDGROUP_APT_L";

        try {
            params.put("@APT_SEQ", aptSeq);
            params.put("@GROUP_PARENT_SEQ", grpParentSeq);
            params.put("@GROUP_SEQ", grpSeq);

            return sqlSession.selectList(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }
}
