package com.xi.fmcs.domain.community.controller;

import com.xi.fmcs.domain.community.model.community.CommunityIndexRequestDto;
import com.xi.fmcs.domain.community.model.community.CommunityIndexResponseDto;
import com.xi.fmcs.domain.community.service.CommunityService;
import com.xi.fmcs.support.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
@Tag(name = "CommunityController", description = "내단지 홈 컨트롤러 [권한: 내단지관리자 이상]")
public class CommunityController {

    private final CommunityService communityService;

    @Operation(description = "커뮤니티 index")
    @GetMapping("/index")
    public Result<CommunityIndexResponseDto> boardIndex(
            @ParameterObject @ModelAttribute CommunityIndexRequestDto communityIndexRequest
    ) {
        return communityService.boardIndex(communityIndexRequest);
    }

    @Operation(description = "내단지관리자 메인 게시판")
    @GetMapping("/getMainBoardList")
    public Result getMainBoardList(
            @Positive
            @Parameter(name = "aptSeq") @RequestParam int aptSeq) {
        return communityService.getMainBoardList(aptSeq);
    }
}
