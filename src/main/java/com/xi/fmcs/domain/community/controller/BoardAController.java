package com.xi.fmcs.domain.community.controller;

import com.xi.fmcs.domain.community.model.boardA.*;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.service.BoardAService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/community/boardA")
@Tag(name = "BoardAController", description = "커뮤니티-공공게시판 [권한: 내단지관리자 이상]")
public class BoardAController {

    private final BoardAService boardAService;

    @Operation(description = "공공게시판 목록")
    @GetMapping("/listPartial")
    @PreAuthorize("hasAnyRole('1','2','3','7')")
    public ResultWithBag<List<BoardAListDto>> boadAListPartial(
            @ParameterObject @ModelAttribute BoardAListRequestDto boardAListRequest,
            @Parameter(name = "page") @RequestParam int page
    ) {
        return boardAService.boardAListPartial(boardAListRequest, page);
    }

    @Operation(description = "공공게시판 등록/수정 창")
    @GetMapping("/createPartial")
    @PreAuthorize("hasAnyRole('1','2','3','7')")
    public Result<BoardAResponseDto> boardACreatePartial(
            @Parameter(name = "seq", description = "게시글 고유번호") @RequestParam(required = false) int seq,
            @Parameter(name = "groupSeq", description = "BOARD_GROUP 고유번호")  @RequestParam int groupSeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return boardAService.boardACreatePartial(seq, groupSeq, loginDto);
    }

    @Operation(description = "공공게시판 상세")
    @GetMapping("/detailPartial")
    @PreAuthorize("hasAnyRole('1','2','3','7')")
    public Result<BoardADetailResponseDto> boardADetailPartial(
            @Parameter(name = "seq", description = "게시글 고유번호") @RequestParam(required = false) int seq,
            @Parameter(name = "saveYn", description = "저장유무(Y:저장, N:임시저장)")  @RequestParam String saveYn
    ) {
        return boardAService.boardADetailPartial(seq, saveYn);
    }

    @Operation(description = "공공게시판 저장")
    @PostMapping(value = "/setBoardASave")
    @PreAuthorize("hasAnyRole('1','2','3','7')")
    public Result<String> setBoardASave(
            @RequestBody BoardASaveRequestDto boardASaveRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return boardAService.setBoardASave(boardASaveRequest, loginDto);
    }

    @Operation(description = "공공게시판 삭제")
    @DeleteMapping(value = "/setBoardADel/{seq}")
    public Result<String> setBoardADel(
            @Parameter(name = "seq", description = "게시글 고유번호", example = "1") @PathVariable("seq") int seq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return boardAService.setBoardADel(seq, loginDto.getSeq());
    }
}
