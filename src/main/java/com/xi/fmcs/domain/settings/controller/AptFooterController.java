package com.xi.fmcs.domain.settings.controller;

import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.settings.model.AptFooterDto;
import com.xi.fmcs.domain.settings.model.AptFooterSaveRequestDto;
import com.xi.fmcs.domain.settings.service.AptFooterService;
import com.xi.fmcs.support.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/aptInfo")
@Tag(name = "AptFooterController", description = "내단지-설정-내단지정보 [권한: 내단지관리자 이상]")
public class AptFooterController {

    private final AptFooterService aptFooterService;

    @Operation(description = "내단지 정보")
    @GetMapping("/index")
    public Result<AptFooterDto> index(
            @Positive
            @Parameter(name = "aptSeq") @RequestParam int aptSeq
    ) {
        return aptFooterService.getAptFooter(aptSeq);
    }

    @Operation(description = "내단지 정보 저장")
    @PostMapping("/saveAptFooter")
    public Result<String> saveAptFooter(
            @RequestBody AptFooterSaveRequestDto aptFooterSaveRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return aptFooterService.saveAptFooter(aptFooterSaveRequest, loginDto.getSeq());
    }
}
