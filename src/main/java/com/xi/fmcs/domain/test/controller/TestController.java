package com.xi.fmcs.domain.test.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xi.fmcs.support.model.Define;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.test.model.TestEntity;
import com.xi.fmcs.domain.test.model.TestEntityList;
import com.xi.fmcs.domain.test.service.TestService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "TestController", description = "임시 테스트용")
@RequestMapping("/test")
@RequiredArgsConstructor
@Validated
public class TestController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final TestService testService;
	
	/*
	 * RequestParam
	 */
	@GetMapping("/testRequestParam")
	public Result<List<TestEntity>> testRequestParam(
			@Parameter(name = "aptSeq", description = "아파트번호", required = true, example = "1") @RequestParam int aptSeq,
			@Parameter(name = "costDate", description = "관리비내역일", required = true, example = "2023-02") @RequestParam String costDate,
			@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
		logger.info(loginDto.toString());
		logger.info("***** testRequestParam: aptseq = " + Integer.toString(aptSeq) + ", costDate = " + costDate);
		return testService.costList(costDate, aptSeq);
	}

	/*
	 * PathVariable
	 */
	@GetMapping("/testPathVariable/{aptSeq}")
	public Result<List<TestEntity>> testPathVariable(
			@Parameter(name = "aptSeq", description = "아파트번호", required = true, example = "1") @PathVariable("aptSeq") 
			@Min(value = 10, message = "아파트번호는 {valid.message.min}") 
			@Max(value = 20, message = "아파트번호는 {valid.message.max}")
			@Range(min = 10, max = 20, message = "아파트번호는 {valid.message.range}")
			int aptSeq,
			@Parameter(name = "costDate", description = "관리비내역일", required = true, example = "2023-02") @RequestParam String costDate
	) {
		logger.info("***** testPathVariable: aptseq = " + Integer.toString(aptSeq) + ", costDate = " + costDate);
		return testService.costList2(costDate, aptSeq);
	}

	/*
	 * ModelAttribute
	 */
	@GetMapping("/testModelAttribute")
	public Result<List<TestEntity>> testModelAttribute(
			@ParameterObject @ModelAttribute TestEntity testEntity,
			@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
		logger.info(loginDto.toString());		
		logger.info("***** testModelAttribute: " + testEntity.toString());
		return testService.costList2(testEntity.getCostDate(), testEntity.getAptSeq());	
	}
	
	/*
	 * FileUpload
	 */
	@PostMapping(value = "/testFileUpload", consumes = "multipart/form-data")
	public Result<List<TestEntity>> testFile(
			@ParameterObject @ModelAttribute TestEntity testEntity,
			@Parameter(name = "mfile", description = "파일업로드", required = true) @RequestPart MultipartFile mfile
	) {
		logger.info("***** testFileUpload - TestEntity: " + testEntity.toString());
		logger.info("***** testFileUpload - getOriginalFilename: " + mfile.getOriginalFilename());
		logger.info("***** testFileUpload - getName: " + mfile.getName());
		logger.info("***** testFileUpload - getContentType: " + mfile.getContentType());
		logger.info("***** testFileUpload - getSize: " + mfile.getSize());
		logger.info("***** testFileUpload - getResource1: " + mfile.getResource().getDescription());
		logger.info("***** testFileUpload - getResource2: " + mfile.getResource().getFilename());
		Result<List<TestEntity>> result = testService.costList2(testEntity.getCostDate(), testEntity.getAptSeq());
		for(TestEntity item : result.getResult()) {
			item.setDONGS(mfile.getOriginalFilename());
		}
		return result;	
	}
	
	/*
	 * FileUpload2
	 */
	@PostMapping(value = "/testFileUpload2", consumes = "multipart/form-data")
	public Result<TestEntityList> testFile2(
			@ParameterObject @ModelAttribute TestEntityList testEntityList,
			@Parameter(name = "mfile", description = "파일업로드", required = true) @RequestPart MultipartFile mfile
	) {
		System.out.println(testEntityList.getTestList().size());
		for (TestEntity item : testEntityList.getTestList()) {
			logger.info("***** TestEntity: " + item.toString());
		}
		logger.info("***** testFileUpload - getOriginalFilename: " + mfile.getOriginalFilename());
		logger.info("***** testFileUpload - getName: " + mfile.getName());
		logger.info("***** testFileUpload - getContentType: " + mfile.getContentType());
		logger.info("***** testFileUpload - getSize: " + mfile.getSize());
		logger.info("***** testFileUpload - getResource1: " + mfile.getResource().getDescription());
		logger.info("***** testFileUpload - getResource2: " + mfile.getResource().getFilename());
		Result<TestEntityList> result = Result.<TestEntityList>builder()
				.result(testEntityList)
				.build();
		return result;	
	}
	
	/*
	 * RequestBody
	 */
	@PostMapping("/testRequestBody")
	public Result<List<TestEntity>> testRequestBody(
			@Valid @RequestBody TestEntity testEntity,
			@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		if(testEntity.getAptSeq() == 1) {
			throw new CustomException("ER001");	//관리자에게 문의하세요.
		} else if(testEntity.getAptSeq() == 2) {
			throw new AccessDeniedException("인가실패");
		} else if(testEntity.getAptSeq() == 3) {
			throw new IllegalArgumentException();
		}
		System.out.println("SMTP: " + Define.SMTP);
		if(principalDetails != null) {
			AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
			System.out.println( SecurityContextHolder.getContext().getAuthentication());
			logger.info(loginDto.toString());
		}
		System.out.println(HttpStatus.valueOf(400));
		System.out.println(HttpStatus.valueOf(401));
		System.out.println(HttpStatus.valueOf(402));
		
		logger.info("***** testRequestBody: " + testEntity.toString());
		return testService.costList2(testEntity.getCostDate(), testEntity.getAptSeq());	
	}
	
	@GetMapping("/mng")
	public String mng(
			@Parameter(name = "code", description = "메세지코드0", required = true, example = "CM001") @RequestParam String code		
	) {
		throw new CustomException("ER003");	//요청하신 정보가 잘못되었습니다.
	}
	
}
