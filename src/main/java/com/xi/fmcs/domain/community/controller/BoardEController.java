package com.xi.fmcs.domain.community.controller;

import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.model.BoardCommonDeleteRequestDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDListDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDListRequestDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDResponseDto;
import com.xi.fmcs.domain.community.model.boardD.BoardDSaveRequestDto;
import com.xi.fmcs.domain.community.model.boardE.BoardEListDto;
import com.xi.fmcs.domain.community.model.boardE.BoardEListRequestDto;
import com.xi.fmcs.domain.community.model.boardE.BoardESaveRequestDto;
import com.xi.fmcs.domain.community.service.BoardEService;
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
@RequestMapping("/community/boardE")
@Tag(name = "BoardEController", description = "커뮤니티-한줄게시판 [권한: 내단지관리자 이상]")
public class BoardEController {

    private final BoardEService boardEService;

    @Operation(description = "한줄게시판 목록")
    @GetMapping("/getBoardEList")
    public ResultWithBag<List<BoardEListDto>> getBoardEList(
            @ParameterObject @ModelAttribute BoardEListRequestDto boardEListRequest,
            @Parameter(name = "page") @RequestParam int page,
            @Parameter(name = "pageSize") @RequestParam int pageSize
    ) {
        return boardEService.getBoardEList(boardEListRequest, page, pageSize);
    }

    @Operation(description = "한줄게시판 저장")
    @PostMapping("/setBoardESave")
    public Result<String> setBoardESave(
            @RequestBody BoardESaveRequestDto boardESaveRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return boardEService.setBoardESave(boardESaveRequest, loginDto);
    }

    @Operation(description = "한줄게시판 저장")
    @DeleteMapping("/setBoardEDel")
    public Result<String> setBoardEDel(
            @RequestBody BoardCommonDeleteRequestDto boardCommonDeleteRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return boardEService.setBoardEDel(boardCommonDeleteRequest, loginDto.getSeq());
    }
}
