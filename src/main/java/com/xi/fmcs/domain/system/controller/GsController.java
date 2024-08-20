package com.xi.fmcs.domain.system.controller;

import java.util.List;

import javax.validation.Valid;

import com.xi.fmcs.config.exception.custom.CustomException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.system.model.GsAdminMemberAddDto;
import com.xi.fmcs.domain.system.model.GsAdminMemberDetailDto;
import com.xi.fmcs.domain.system.model.GsAdminMemberDto;
import com.xi.fmcs.domain.system.service.GsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/system/gs")
@Tag(name = "GsController", description = "GS마스터")
@RequiredArgsConstructor
public class GsController {
	
	private final GsService gsService;
	
	@Operation(summary = "시스템 GS 마스터 조회")
	@GetMapping("/index")
	public Result<List<GsAdminMemberDto>> index(){
        //1.GS마스터  직원 목록만 출력
		return gsService.getAdminGSMemberList();
	}
	
	@Operation(summary = "GS 마스터 상세")
	@GetMapping("/detail/{seq}")
	public Result<GsAdminMemberDetailDto> index(
			@Parameter(name = "seq", description = "관리자 번호", required = true, example = "1") @PathVariable("seq") int seq
	){
		if(seq == 0) {
			new CustomException("ER004"); // 요청하신 페이지를 찾을 수 없습니다.
		}
		return gsService.getAdminMemberDetailBySeq(seq);
	}
	
	@Operation(summary = "GS 마스터 등록")
	@PostMapping("/addMember")
	public Result<Object> AddMember(
			@Valid @RequestBody GsAdminMemberAddDto adminGSMemberAddDto,
			@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
	){
		AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
		return gsService.insertAdminMember(adminGSMemberAddDto, loginDto.getSeq(), loginDto.getCompanySeq());
	}
	
}
