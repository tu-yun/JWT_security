package com.xi.fmcs.domain.adminCompany.controller;

import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberAddDto;
import com.xi.fmcs.domain.admin.model.AdminMemberDetailDto;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.admin.service.AdminMemberService;
import com.xi.fmcs.domain.adminCompany.model.AdminCompanyCURequestDto;
import com.xi.fmcs.domain.adminCompany.model.AdminCompanyDetailDto;
import com.xi.fmcs.domain.adminCompany.model.AdminCompanyListDto;
import com.xi.fmcs.domain.adminCompany.service.AdminCompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/company")
@Tag(name = "CompanyController", description = "업체관리 API [접근권한 : 슈퍼관리자, 업체관리자]")
@RequiredArgsConstructor
public class CompanyController {

    private final AdminCompanyService adminCompanyService;
    private final AdminMemberService adminMemberService;

    @Operation(summary = "업체 목록")
    @GetMapping("/companyList")
    public Result<List<AdminCompanyListDto>> selectAdminCompanyList(
            @Parameter(name = "search", description = "검색어") @RequestParam(required = false) String search,
            @Parameter(name = "page", description = "현재페이지") @RequestParam(defaultValue = "0") int page,
            @Parameter(name = "pSize", description = "페이징사이즈") @RequestParam(defaultValue = "10") int pSize,
            @Parameter(name = "useYn", description = "사용여부 (YN: 전체 / Y:사용 / N:미사용)") @RequestParam(defaultValue = "YN") String useYn
    ) {
        return adminCompanyService.selectAdminCompanyList(search, page, pSize, useYn);
    }

    @PreAuthorize("hasRole(1)")
    @Operation(summary = "업체추가 팝업", description = "[권한:슈퍼관리자] 업체 등록 & 대표 담당자 등록 & 이미지 등록 ")
    @PostMapping(value = "/addCompany", consumes = "multipart/form-data")
    public Result<String> addAdminCompany(
            @Parameter(name = "uploadFile", description = "회사로고") @RequestPart(required = false) MultipartFile uploadFile,
            @ParameterObject @ModelAttribute AdminCompanyCURequestDto adminCompanyCU,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();

        return adminCompanyService.addAdminCompany(adminCompanyCU, uploadFile, loginDto);
    }

    @PreAuthorize("hasRole(1)")
    @Operation(summary = "업체 상세보기")
    @GetMapping("/detail")
    public ResultWithBag<AdminCompanyDetailDto> AdminCompanyDetail(
            @Parameter(name = "seq", description = "회사 고유번호") @RequestParam int seq,
            @Parameter(name = "search", description = "검색어") @RequestParam(required = false) String search,
            @Parameter(name = "page", description = "현재 페이지") @RequestParam(required = false, defaultValue = "0") int page
    ) throws Exception {
        return adminCompanyService.adminCompanyDetail(seq, search, page);
    }

    @Operation(summary = "업체 직원목록")
    @GetMapping("/adminMemberList")
    public ResultWithBag<List<AdminMemberDetailDto>> AdminCompanyMemberList(
            @Parameter(name = "seq", description = "회사 고유번호") @RequestParam int seq,
            @Parameter(name = "page", description = "현재페이지") @RequestParam(defaultValue = "1") int page,
            @Parameter(name = "pageSize", description = "페이징사이즈") @RequestParam(defaultValue = "10") int pageSize
    ) {
        return adminCompanyService.adminCompanyMemberList(seq, page, pageSize);
    }

    @PreAuthorize("hasRole(1)")
    @Operation(summary = "업체 삭제", description = "[권한:슈퍼관리자] ")
    @DeleteMapping("/delCompany/{companySeq}")
    public Result<String> delAdminCompany(
            @Parameter(name = "companySeq", description = "회사고유번호", required = true, example = "1") @PathVariable("companySeq") int companySeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if(companySeq <= 0) {
            new CustomException("ER004"); // 요청하신 페이지를 찾을 수 없습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return adminCompanyService.deleteAdminCompany(companySeq, loginDto);
    }

    @Operation(summary = "업체 담당자 등록", description = "업체 상세에서 등록하는 관리자 (등록하는 관리자는 대표관리자가 아님)")
    @PostMapping("/addCompanyMember")
    public Result<String> addAdminMember(
            @ParameterObject @ModelAttribute AdminMemberAddDto aminMemberAddDto
    ) {
        return adminCompanyService.addAdminMember(aminMemberAddDto);
    }

    //////////////////////////////////////
    // 확인 필요!!!!!!!!!!!!!!!!!!!!!!!!!!//
    //////////////////////////////////////
    @PreAuthorize("hasRole(0)")
    @Operation(summary = "슈퍼관리자 상세")
    @PostMapping("/getCompanyInfo")
    public Result<Object> getCompanyInfo(
            @Parameter(name = "seq", description = "회사 고유번호") @RequestParam int seq
    ) {
        return adminCompanyService.getAdminCompanyBySeq(seq);
    }

    @Operation(summary = "업체 수정 & 이미지 수정")
    @PutMapping(value = "/editCompany", consumes = "multipart/form-data")
    public Result<String> editAdminCompany(
            @Parameter(name = "companySeq") @RequestParam Integer companySeq,
            @Parameter(name = "companySeq") @RequestParam Integer adminMemberSeq,
            @ParameterObject @ModelAttribute AdminCompanyCURequestDto adminCompanyCU,
            @Parameter(name = "uploadFile", description = "회사로고") @RequestPart(required = false) MultipartFile uploadFile,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails

    ) {
        adminCompanyCU.setCompanySeq(companySeq);
        adminCompanyCU.setAdminMemberSeq(adminMemberSeq);
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return adminCompanyService.editAdminCompany(adminCompanyCU, uploadFile, loginDto);
    }

    @Operation(summary = "이메일 중복체크")
    @GetMapping(value = "/checkEmail")
    public Result<String> checkEmailIsUsed(
            @Parameter(name = "email") @RequestParam String email
    ) {
        adminMemberService.checkEmailIsUsed(email);
        return Result.<String>builder().build();
    }

    @Operation(summary = "이메일 중복체크 (자신 제외)")
    @PostMapping(value = "/checkEmailExceptMe")
    public Result<String> checkEmailIsUsedExceptMe(
            @Parameter(name = "adminMemberSeq", description = "ADMIN_MEMBER 고유번호") @RequestParam Integer adminMemberSeq,
            @Parameter(name = "email", description = "E-mail") @RequestParam String email
    ) {
        adminMemberService.checkEmailIsUsedExceptMe(adminMemberSeq, email);
        return Result.<String>builder().build();
    }

}
