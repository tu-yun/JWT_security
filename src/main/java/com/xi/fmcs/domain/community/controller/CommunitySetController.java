package com.xi.fmcs.domain.community.controller;

import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.model.communitySet.BoardDisplaySetDto;
import com.xi.fmcs.domain.community.model.communitySet.BoardGroupResponseDto;
import com.xi.fmcs.domain.community.model.communitySet.BoardGroupSaveListRequestDto;
import com.xi.fmcs.domain.community.model.communitySet.BoardGroupSaveRequestDto;
import com.xi.fmcs.domain.community.service.CommunitySetService;
import com.xi.fmcs.support.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/community/communitySet")
@Tag(name = "CommunitySetController", description = "커뮤니티-설정 [권한: 내단지관리자 이상]")
public class CommunitySetController {
    private final CommunitySetService communitySetService;

    public CommunitySetController(CommunitySetService communitySetService) {
        this.communitySetService = communitySetService;
    }

    @Operation(summary = "커뮤니티 설정 조회")
    @GetMapping("/getBoardGroupList")
    public Result<List<BoardGroupResponseDto>> getBoardGroupList(
            @Positive
            @Parameter(name = "aptSeq", required = true) @RequestParam(defaultValue = "0") int aptSeq
    ) {
        return communitySetService.getBoardGroupList(aptSeq);
    }

    @Operation(summary = "커뮤니티 설정 저장")
    @PostMapping("/setBoardGroupSave")
    public Result<Object> setBoardGroupSave(
            @RequestBody BoardGroupSaveListRequestDto boardGroupSaveRequestList,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return communitySetService.setBoardGroupSave(boardGroupSaveRequestList, loginDto.getSeq());
    }

    @Operation(summary = "커뮤니티 설정 게시판 삭제")
    @PostMapping("/boardGroupDel")
    public Result<Object> boardGroupDel(
            @Parameter(name = "boardGroupSeq", required = true) @RequestParam(defaultValue = "0") int boardGroupSeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if (boardGroupSeq <= 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return communitySetService.boardGroupDel(boardGroupSeq, loginDto.getSeq());
    }

    @Operation(summary = "노출항목 설정 저장")
    @PostMapping("/setDispSave")
    public Result<Object> setDispSave(
            @RequestBody BoardDisplaySetDto boardDisplaySetRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        if(boardDisplaySetRequest.getAptSeq() == 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return communitySetService.setDispSave(boardDisplaySetRequest, loginDto.getSeq());
    }

    @Operation(summary = "노출항목 설정 상세")
    @GetMapping("/getDispDetail")
    public Result<BoardDisplaySetDto> getDispDetail (
            @Positive
            @Parameter(name = "aptSeq", required = true) @RequestParam(defaultValue = "0") int aptSeq
    ) {
        return communitySetService.getDispDetail(aptSeq);
    }
}
