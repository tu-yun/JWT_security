package com.xi.fmcs.domain.community.service;

import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.model.comment.CommentDto;
import com.xi.fmcs.domain.community.model.comment.CommentSHRequestDto;
import com.xi.fmcs.domain.community.model.comment.CommentSaveRequestDto;
import com.xi.fmcs.domain.community.repository.CommentRepository;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.util.MngUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    //커뮤니티 설정 조회
    public Result<List<CommentDto>> getBoardGroupList(int groupSeq, int boardSeq) {
        List<CommentDto> commentList =
                commentRepository.getCommentList(groupSeq, boardSeq);

        return Result.<List<CommentDto>>builder()
                .result(commentList)
                .build();
    }

    //댓글 저장/수정
    @Transactional
    public Result<String> setCommentSave(CommentSaveRequestDto commentSaveRequest, AdminMemberLoginDto loginDto) {
        if(commentSaveRequest.getSeq() > 0) {
            commentRepository.updateComment(commentSaveRequest, loginDto);
        } else {
            commentRepository.insertCommnet(commentSaveRequest, loginDto);
        }
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM004")) //저장 되었습니다.
                .build();
    }

    //댓글 숨김여부 변경
    @Transactional
    public Result<String> setCommentSH(CommentSHRequestDto commentSHRequest, int regSeq) {
        commentRepository.updateCommentHid(commentSHRequest, regSeq);
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM004")) //저장 되었습니다.
                .build();
    }

    //댓글 삭제
    @Transactional
    public Result<String> setCommentDel(int seq, int regSeq) {
        commentRepository.delComment(seq, regSeq);
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM002")) //삭제 되었습니다.
                .build();
    }
}
