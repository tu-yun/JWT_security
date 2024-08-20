package com.xi.fmcs.domain.community.controller;

import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.model.BoardCommonDeleteRequestDto;
import com.xi.fmcs.domain.community.model.boardB.*;
import com.xi.fmcs.domain.community.service.BoardBService;
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
@RequestMapping("/community/boardB")
@Tag(name = "BoardBController", description = "커뮤니티-일반/동게시판 [권한: 내단지관리자 이상]")
public class BoardBController {

    private final BoardBService boardBService;

    @Operation(description = "일반게시판(동게시판) 목록")
    @GetMapping("/listPartial")
    public ResultWithBag<List<BoardBListDto>> boadBListPartial(
            @ParameterObject @ModelAttribute BoardBListRequestDto boardBListRequest,
            @Parameter(name = "page") @RequestParam(required = false) int page
    ) {
        return boardBService.boadBListPartial(boardBListRequest, page);
    }

    @Operation(description = "일반/동게시판 등록/수정 창")
    @GetMapping("/createPartial")
    public Result<BoardBResponseDto> boardACreatePartial(
            @Parameter(name = "seq", description = "게시글 고유번호") @RequestParam(required = false) int seq,
            @Parameter(name = "groupSeq", description = "BOARD_GROUP 고유번호")  @RequestParam int groupSeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return boardBService.boardBCreatePartial(seq, groupSeq, loginDto);
    }

    @Operation(description = "일반/동게시판 상세")
    @GetMapping("/detailPartial")
    public Result<BoardBResponseDto> boardBDetailPartial(
            @Parameter(name = "seq", description = "게시글 고유번호") @RequestParam(required = false) int seq
    ) {
        return boardBService.boardBDetailPartial(seq);
    }

    @Operation(description = "일반/동 게시판 저장")
    @PostMapping(value = "/setBoardBSave")
    public Result<String> setBoardBSave(
            @RequestBody BoardBSaveRequestDto boardBSaveRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return boardBService.setBoardBSave(boardBSaveRequest, loginDto);
    }

    @Operation(description = "동게시판 삭제")
    @DeleteMapping(value = "/setBoardBDel")
    public Result<String> setBoardBDel(
            @RequestBody BoardCommonDeleteRequestDto boardBDeleteRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return boardBService.setBoardBDel(boardBDeleteRequest, loginDto.getSeq());
    }
}
