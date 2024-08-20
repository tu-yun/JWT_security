package com.xi.fmcs.domain.community.controller;

import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.model.BoardCommonDeleteRequestDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDListDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDListRequestDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDResponseDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDSaveRequestDto;
import com.xi.fmcs.domain.community.service.BoardDService;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/boardD")
@Tag(name = "BoardDController", description = "커뮤니티-질문답변게시판 [권한: 내단지관리자 이상]")
public class BoardDController {

    private final BoardDService boardDService;

    @Operation(description = "질문답변게시판 목록")
    @GetMapping("/listPartial")
    public ResultWithBag<List<BoardDListDto>> boadAListPartial(
            @ParameterObject @ModelAttribute BoardDListRequestDto boardDListRequest,
            @Parameter(name = "page") @RequestParam int page
    ) {
        return boardDService.boardDListPartial(boardDListRequest, page);
    }

    @Operation(description = "질문답변게시판 등록/수정 창")
    @GetMapping("/createPartial")
    public Result<BoardDResponseDto> boardDCreatePartial(
            @Parameter(name = "seq", description = "게시글 고유번호") @RequestParam(required = false) int seq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return boardDService.boardDCreatePartial(seq, loginDto);
    }

    @Operation(description = "질문답변게시판 상세")
    @GetMapping("/detailPartial")
    public Result<BoardDResponseDto> boardDDetailPartial(
            @Parameter(name = "seq", description = "게시글 고유번호", required = true) @RequestParam int seq
    ) {
        return boardDService.boardDDetailPartial(seq);
    }

    @Operation(description = "질문답변게시판 답변저장")
    @PostMapping(value = "/setBoardDSave")
    public Result<String> setBoardDSave(
            @RequestBody BoardDSaveRequestDto boardDSaveRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return boardDService.setBoardDSave(boardDSaveRequest, loginDto.getSeq());
    }

    @Operation(description = "질문답변게시판 삭제 (관리자는 삭제버튼이 안보이는듯함 확인필요)")
    @DeleteMapping(value = "/setBoardADel")
    public Result<String> setBoardADel(
            @RequestBody BoardCommonDeleteRequestDto boardDeleteRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return boardDService.setBoardDDel(boardDeleteRequest, loginDto.getSeq());
    }
}
