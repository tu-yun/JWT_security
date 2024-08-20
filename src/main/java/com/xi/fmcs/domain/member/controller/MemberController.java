package com.xi.fmcs.domain.member.controller;

import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.apt.model.AptDetailDto;
import com.xi.fmcs.domain.apt.model.AptDongHoRequestDto;
import com.xi.fmcs.domain.apt.model.AptDongHoResponseDto;
import com.xi.fmcs.domain.apt.model.AptDongInfoDto;
import com.xi.fmcs.domain.member.model.*;
import com.xi.fmcs.domain.member.service.MemberService;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.support.util.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Validated
@Tag(name = "MemberController", description = "회원관리 API [권한: 내단지관리자 이상]")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "승인 회원 목록")
    @GetMapping("/list")
    public ResultWithBag<List<MemberListResponseDto>> memberList(
            @ParameterObject @ModelAttribute MemberListRequestDto memberListRequest,
            @Parameter(name = "dongMngYn", description = "동대표 유무") @RequestParam(required = false) String dongMngYn,
            @Parameter(name = "page", description = "현재 페이지") @RequestParam(defaultValue = "0") int page,
            @Parameter(name = "pageSize", description = "페이징처리 사이즈") @RequestParam(defaultValue = "10") int pageSize
    ) {
        return memberService.getMemberList(memberListRequest, dongMngYn, page, pageSize);
    }

    @Operation(summary = "승인대기 회원 목록")
    @GetMapping("/waitList")
    public ResultWithBag<List<MemberWaitListResponseDto>> memberWaitList(
            @ParameterObject @ModelAttribute MemberWaitListRequestDto memberWaitListRequest
    ) {
        return memberService.getMemberWaitOrNotApprovedList(memberWaitListRequest, 1);
    }

    @Operation(summary = "미승인 회원 목록")
    @GetMapping("/notApprovedList")
    public ResultWithBag<List<MemberWaitListResponseDto>> notApprovedList(
            @ParameterObject @ModelAttribute MemberWaitListRequestDto memberWaitListRequest
    ) {
        return memberService.getMemberWaitOrNotApprovedList(memberWaitListRequest, 3);
    }

    @Operation(summary = "전출 회원 목록")
    @GetMapping("/moveOutList")
    public ResultWithBag<List<MemberListResponseDto>> moveOutMemberList(
            @ParameterObject @ModelAttribute MemberListRequestDto memberListRequest,
            @Parameter(name = "page", description = "현재 페이지") @RequestParam(defaultValue = "0") int page,
            @Parameter(name = "pageSize", description = "페이징처리 사이즈") @RequestParam(defaultValue = "10") int pageSize
    ) {
        return memberService.moveOutMemberList(memberListRequest, page, pageSize);
    }

    @Operation(summary = "단지 정보")
    @GetMapping("/getAptInfo")
    public Result<AptDetailDto> getAptInfo(
            @Parameter(name = "aptSeq", required = true) @RequestParam(defaultValue = "0") int aptSeq
    ) {
        if (aptSeq == 0) {
            throw new CustomException("CM004");
        }
        return memberService.getAptInfo(aptSeq);
    }

    @Operation(summary = "이메일 확인(해당 이메일을 사용중인 회원 수)")
    @GetMapping("/getEmailCheck")
    public Result<Integer> getEmailCheck(
            @NotNull(message = "") @Email
            @Parameter(name = "email", required = true) @RequestParam String email
    ) {
        return memberService.getEmailCheck(email);
    }

    @Operation(summary = "단지 동 목록")
    @GetMapping("/getAptDongList")
    public Result<List<AptDongInfoDto>> getAptDongList(
            @RequestParam(defaultValue = "0") int aptSeq) {
        if (aptSeq == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        return memberService.getAptDongList(aptSeq);
    }

    @Operation(summary = "단지 > 동 > 호 목록")
    @GetMapping("/getAptDongHoList")
    public Result<List<AptDongHoResponseDto>> getAptDongHoList(
            @ParameterObject @ModelAttribute AptDongHoRequestDto aptDongHoRequest
    ) {
        if (aptDongHoRequest.getAptSeq() == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        return memberService.getAptDongHoList(aptDongHoRequest);
    }

    @Operation(summary = "회원 상세 조회")
    @GetMapping("/getMemberDetailInfo/{memberSeq}")
    public Result<MemberRDto> getMemberDetailInfo(
            @Parameter(name = "memberSeq", required = true, example = "7") @PathVariable("memberSeq") int memberSeq) {
        if (memberSeq == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        return memberService.getMemberDetailInfo(memberSeq);
    }

    @Operation(summary = "회원 입주민 정보 등록")
    @PostMapping(value = "/insertMember", consumes = "multipart/form-data")
    public Result<String> setMember(
            @Parameter(name = "IMAGE_URL", description = "회원 사진") @RequestPart(required = false) MultipartFile IMAGE_URL,
            @ParameterObject @ModelAttribute MemberInsertOrUpdateRequestDto memberInsertRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        String birthday = memberInsertRequest.getBirthday();
        int sex = memberInsertRequest.getSex();

        if (memberInsertRequest.getAptSeq() == 0 || memberInsertRequest.getXiCode() == null
        || memberInsertRequest.getDong() == null || memberInsertRequest.getHo() == null
        || memberInsertRequest.getName() == null || memberInsertRequest.getNickName() == null
        || sex <1 || sex >2 || birthday == null || memberInsertRequest.getPhone() == null
        || memberInsertRequest.getResidenceType() == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        if (!StringUtil.stringDateCheck(birthday)) {
            throw new CustomException("MM010");	  //생년월일은 날짜 형식으로 입력 하세요.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return memberService.setMember(IMAGE_URL, memberInsertRequest, loginDto.getSeq());
    }

    @Operation(summary = "회원 입주민 정보 수정")
    @PostMapping(value = "/updateMemberInfo", consumes = "multipart/form-data")
    public void setMemberInfo(
            @Parameter(name = "IMAGE_URL", description = "회원 사진") @RequestPart(required = false) MultipartFile IMAGE_URL,
            @ParameterObject @ModelAttribute MemberInsertOrUpdateRequestDto memberInsertRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if(memberInsertRequest.getMemberSeq() < 1){
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        memberService.setMember(IMAGE_URL, memberInsertRequest, loginDto.getSeq());
    }

    @Operation(summary = "회원 대기처리")
    @PostMapping("/memberWait")
    public Result<String> memberWait(
            @Parameter(name = "memberSeq") @RequestParam(defaultValue = "0") int memberSeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if(memberSeq == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return memberService.setStatus(memberSeq, 1, loginDto.getSeq());
    }

    @Operation(summary = "회원 승인처리")
    @PostMapping("/memberApproved")
    public Result<String> memberApproved(
            @Parameter(name = "memberSeq") @RequestParam(defaultValue = "0") int memberSeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        System.out.println("memberSeq : " + memberSeq);
        if(memberSeq == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return memberService.setStatus(memberSeq, 2, loginDto.getSeq());
    }

    @Operation(summary = "회원 미승인처리")
    @PostMapping("/memberUnapproved")
    public Result<String> memberUnapproved(
            @Parameter(name = "memberSeq") @RequestParam(defaultValue = "0") int memberSeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if(memberSeq == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return memberService.setStatus(memberSeq, 3, loginDto.getSeq());
    }

    @Operation(summary = "회원 전출처리")
    @PostMapping("/memberOut")
    public Result<String> memberOut(
            @Parameter(name = "memberSeq") @RequestParam(defaultValue = "0") int memberSeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        System.out.println("memberSeq : " + memberSeq);
        if(memberSeq == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return memberService.setStatus(memberSeq, 4, loginDto.getSeq());
    }

    @Operation(summary = "회원 전출 배열 처리")
    @PostMapping("setMoveOuts")
    public Result<Object> setMoveOuts(
            @RequestBody String[] memberSeqs,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if (memberSeqs == null) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        System.out.println("memberSeqs : "+ String.join(",",memberSeqs));
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();

        return memberService.setMoveOuts(String.join(",",memberSeqs), loginDto.getSeq());
    }

//    @Operation(summary = "회원싱크")
//    @PostMapping("/sendSyncUser")
//    public Result<Object> sendSyncUser(MemberRDto memberR) {
//        return memberService.sendSyncUser(memberR);
//    }

    @Operation(summary = "회원 동대표 설정")
    @PostMapping(value = "/setDongRepresent")//, consumes = {MediaType.APPLICATION_JSON_VALUE}
    public Result<String> setDongRepresent(
            @RequestBody DongMngSetRequestDto dongMngSetRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        if (dongMngSetRequest.getAptDong() == null || dongMngSetRequest.getAptDong().length < 1) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        return memberService.setDongRepresent(dongMngSetRequest, loginDto.getSeq());
    }

    @Operation(summary = "회원 동대표 취소")
    @PostMapping(value = "/setDongRepCancel")
    public Result<String> setDongRepCancel(
            @RequestBody MngCancelRequestDto dongMngCancelRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        if (dongMngCancelRequest.getAptSeq() == 0 || dongMngCancelRequest.getMemSeq() == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        return memberService.updateAptDongRepresentCancel(dongMngCancelRequest, loginDto.getSeq());
    }

    @Operation(summary = "회원정보 엑셀 다운로드")
    @GetMapping("/excelDown")
    public ResponseEntity<byte[]> excelDown(
            @ParameterObject @ModelAttribute MemberListRequestDto memberListRequest,
            @Parameter(name = "status", description = "상태", example = "2") @RequestParam int status,
            @Parameter(name = "dongMngYn", description = "동대표 유무") @RequestParam(required = false) String dongMngYn
            ) throws Exception {
        if(status == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        return memberService.excelDown(memberListRequest, status, dongMngYn);
    }

}
