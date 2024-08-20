package com.xi.fmcs.domain.community.controller;

import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.model.comment.CommentDto;
import com.xi.fmcs.domain.community.model.comment.CommentSHRequestDto;
import com.xi.fmcs.domain.community.model.comment.CommentSaveRequestDto;
import com.xi.fmcs.domain.community.service.CommentService;
import com.xi.fmcs.support.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/comment")
@Tag(name = "CommentController", description = "커뮤니티-댓글관련 API [권한: 내단지관리자 이상]")
public class CommentController {

    private final CommentService commentService;

    @Operation(description = "댓글 조회")
    @GetMapping("/getCommentList")
    public Result<List<CommentDto>> getCommentList(
            @Parameter(name = "grpSeq", description = "BOARD_GROUP 고유번호") @RequestParam int grpSeq,
            @Parameter(name = "brdSeq", description = "게시글 고유번호") @RequestParam int brdSeq
    ) {
        return commentService.getBoardGroupList(grpSeq, brdSeq);
    }

    @Operation(description = "댓글 저장")
    @PostMapping("/setCommentSave")
    public Result<String> setCommentSave(
            @RequestBody CommentSaveRequestDto commentSaveRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return commentService.setCommentSave(commentSaveRequest, loginDto);
    }

    @Operation(description = "댓글 숨김/해제")
    @PostMapping("/setCommentSH")
    public Result<String> setCommentSH(
            @RequestBody CommentSHRequestDto commentSHRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return commentService.setCommentSH(commentSHRequest, loginDto.getSeq());
    }

    @Operation(description = "댓글 삭제")
    @DeleteMapping(value = "/setCommentDel/{seq}")
    public Result<String> setCommentDel(
            @Parameter(name = "seq", description = "게시글 고유번호", example = "1") @PathVariable("seq") int seq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return commentService.setCommentDel(seq, loginDto.getSeq());
    }
}
