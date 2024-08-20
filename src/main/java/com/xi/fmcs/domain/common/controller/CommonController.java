package com.xi.fmcs.domain.common.controller;

import com.xi.fmcs.domain.common.model.DeleteFileBySeqRequestDto;
import com.xi.fmcs.domain.common.model.VoteSaveFileDto;
import com.xi.fmcs.domain.common.model.VoteSaveFileRequestDto;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.common.model.CommunitySaveFileRequestDto;
import com.xi.fmcs.domain.common.service.CommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
@Validated
@Tag(name = "CommonController", description = "공통 [권한: 내단지관리자 이상]")
public class CommonController {

    private final CommonService commonService;

    @Operation(description = "커뮤니티 파일저장")
    @PostMapping(value = "/setFileSave2", consumes = "multipart/form-data")
    public Result<String> setFileSave2(
            @Parameter(name = "files", description = "파일 리스트") @RequestPart(required = false) List<MultipartFile> files,
            @ParameterObject @ModelAttribute CommunitySaveFileRequestDto communitySaveFileRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if(files == null || files.size() == 0){
            return Result.<String>builder()
                    .stateMessage("MB007") //데이터가 존재하지 않습니다.
                    .build();
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return commonService.SetFileSave2(files, communitySaveFileRequest, loginDto.getSeq());
    }

    @Operation(description = "투표 파일저장")
    @PostMapping(value = "/setFileSave3", consumes = "multipart/form-data")
    public Result<String> setFileSave3(
            @Valid @ModelAttribute VoteSaveFileRequestDto voteSaveFileRequest,
//            @RequestPart VoteSaveFileRequestDto voteSaveFileRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return commonService.SetFileSave3(voteSaveFileRequest, loginDto.getSeq());
    }

    @Operation(description = "커뮤니티(동적멀티첨부) 파일삭제")
    @DeleteMapping("/setFileDeleteSeq")
    public Result<String> setFileDeleteSeq(
            @RequestBody DeleteFileBySeqRequestDto deleteFileBySeqRequest
    ) {
        return commonService.setFileDeleteSeq(deleteFileBySeqRequest);
    }
}
