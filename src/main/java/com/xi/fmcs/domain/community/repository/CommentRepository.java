package com.xi.fmcs.domain.community.repository;

import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.model.comment.CommentDto;
import com.xi.fmcs.domain.community.model.comment.CommentSHRequestDto;
import com.xi.fmcs.domain.community.model.comment.CommentSaveRequestDto;
import com.xi.fmcs.support.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //댓글 목록
    public List<CommentDto> getCommentList(int groupSeq, int boardSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_COMMENT_L";

        try {
            params.put("@GROUP_SEQ", groupSeq);
            params.put("@BOARD_SEQ", boardSeq);

            return sqlSession.selectList(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //댓글 수정
    public void updateComment(CommentSaveRequestDto commentSaveRequest, AdminMemberLoginDto loginDto) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_COMMENT_U";

        try {
            params.put("@SEQ", commentSaveRequest.getSeq());
            params.put("@CONTENTS", commentSaveRequest.getContents());
            params.put("@WRITER_NICKNM", loginDto.getNickname());
            params.put("@WRITER_NM", loginDto.getName());
            params.put("@ADIMN_SEQ", loginDto.getSeq());

            sqlSession.update(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //댓글 저장
    public int insertCommnet(CommentSaveRequestDto commentSaveRequest, AdminMemberLoginDto loginDto) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_COMMENT_C";

        try {
            params.put("@GROUP_SEQ", commentSaveRequest.getSeq());
            params.put("@BOARD_SEQ", commentSaveRequest.getSeq());
            params.put("@CONTENTS", commentSaveRequest.getContents());
            params.put("@WRITER_NICKNM", loginDto.getNickname());
            params.put("@WRITER_NM", loginDto.getName());
            params.put("@LEVEL_R", commentSaveRequest.getLevelR());
            params.put("@PARENT_SEQ", commentSaveRequest.getParentSeq());
            params.put("@ADIMN_SEQ", loginDto.getSeq());

            sqlSession.insert(mapperName + "." + spName, params);
            return (int) params.get("@IDX");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //댓글 숨김여부 변경
    public void updateCommentHid(CommentSHRequestDto commentSHRequest, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_COMMENT_HID_U";

        try {
            params.put("@SEQ", commentSHRequest.getSeq());
            params.put("@ADMIN_HID_YN", commentSHRequest.getHidYn());
            params.put("@ADIMN_SEQ", regSeq);

            sqlSession.insert(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //댓글 삭제
    public void delComment(int seq, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_COMMENT_D";

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
