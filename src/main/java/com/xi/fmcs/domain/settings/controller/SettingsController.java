package com.xi.fmcs.domain.settings.controller;

import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberDetailMinDto;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.member.model.MngCancelRequestDto;
import com.xi.fmcs.domain.settings.model.AptMngSaveRequestDto;
import com.xi.fmcs.domain.settings.service.SettingsService;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settings")
@Tag(name = "SettingsController", description = "내단지-설정-운영자 [권한: 내단지관리자 이상]")
@Validated
public class SettingsController {

    private final SettingsService settingsService;

    @Operation(description = "관리자 관리 목록")
    @GetMapping("/adminMngList")
    public ResultWithBag<List<AdminMemberDetailMinDto>> getAdminMngList(
            @Positive(message = "")
            @Parameter @RequestParam int aptSeq
    ) {
        return settingsService.getAdminMemberList(aptSeq);
    }

    @Operation(description = "내단지 관리자 일괄 저장 과 APT 맵핑 (내단지 일반관리자 : 내단지관리자 수정은 내정보 휴대번호만 수정가능)")
    @PostMapping("/addAptMngSave")
    public Result<String> addAptMngSave(
            @Valid @RequestBody AptMngSaveRequestDto aptMngSaveRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return settingsService.addAptMngSave(aptMngSaveRequest, loginDto);
    }

    @Operation(description = "내단지 관리자 내정보 휴대번호만 수정")
    @PutMapping("/aptMngMyInfoSave")
    public Result<String> aptMngMyInfoSave(
            @NotNull(message = "")
            @Parameter @RequestParam String mobileNo,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return settingsService.aptMngMyInfoSave(mobileNo, loginDto);
    }

    @Operation(description = "대표 내단지 관리자 변경")
    @PostMapping("/changeAptMngCmpYN")
    public Result<String> changeAptMngCompanyYN(
            @Valid @RequestBody MngCancelRequestDto changeAptMngRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return settingsService.changeAptMngCompanyYN(changeAptMngRequest, loginDto);
    }

    @Operation(description = "관리자 삭제")
    @PutMapping("/adminAptMngDel")
    public Result<String> adminAptMngDel(
            @Valid @RequestBody MngCancelRequestDto aptMngDeleteRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return settingsService.adminAptMngDel(aptMngDeleteRequest, loginDto);
    }
}
