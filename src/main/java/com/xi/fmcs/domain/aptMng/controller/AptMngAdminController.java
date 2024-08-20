package com.xi.fmcs.domain.aptMng.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.support.util.StringUtil;
import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminAPTManagerInsertRequestDto;
import com.xi.fmcs.domain.admin.model.AdminAPTManagerUpdateRequestDto;
import com.xi.fmcs.domain.admin.model.AdminMemberDto;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.apt.model.AdminCompanyAptRequestDto;
import com.xi.fmcs.domain.apt.model.AdminCompanyAptResponseDto;
import com.xi.fmcs.domain.apt.model.AptDetailResponseDto;
import com.xi.fmcs.domain.apt.model.AptMoveInfoDto;
import com.xi.fmcs.domain.aptMng.model.AdminATPMngResponseDto;
import com.xi.fmcs.domain.aptMng.model.AptInfoDto;
import com.xi.fmcs.domain.aptMng.model.AptInfoExcelDto;
import com.xi.fmcs.domain.aptMng.model.AptMngCompanyResponseDto;
import com.xi.fmcs.domain.aptMng.model.AptNameResponseDto;
import com.xi.fmcs.domain.aptMng.model.AptSeqAndXiCodeDto;
import com.xi.fmcs.domain.aptMng.model.AptServiceRequestDto;
import com.xi.fmcs.domain.aptMng.model.AptServiceResponseDto;
import com.xi.fmcs.domain.aptMng.service.AptMngService;
import com.xi.fmcs.domain.file.model.ExcelFileInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/aptmng/admin")
@Tag(name = "AptMngAdminController", description = "관리자 내단지관리 API [접근권한 : 슈퍼관리자, 업체관리자]")
public class AptMngAdminController {

    private final AptMngService aptMngService;

    public AptMngAdminController(AptMngService aptMngService) {
        this.aptMngService = aptMngService;
    }

    @Operation(summary = "관리자 내단지 관리 목록")
    @GetMapping("/list")
    public ResultWithBag<List<AdminATPMngResponseDto>> aptMngAdminList(
            @Parameter(name = "cmpSearch", description = "회사명") @RequestParam(required = false) String cmpSearch,
            @Parameter(name = "aptSearch", description = "내단지명") @RequestParam(required = false) String aptSearch,
            @Parameter(name = "doType", description = "지역 코드 (시도)") @RequestParam(defaultValue = "0") int doType,
            @Parameter(name = "page", description = "현재페이지") @RequestParam(defaultValue = "0") int page,
            @Parameter(name = "pSize", description = "페이징사이즈") @RequestParam(defaultValue = "10") int pSize,
            @Parameter(name = "myAptYn", description = "사용여부") @RequestParam(defaultValue = "YN") String myAptYn,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();

        return aptMngService.aptMngAdminList(cmpSearch, aptSearch, doType, page, pSize, myAptYn, loginDto.getGradeType(), loginDto.getCompanySeq());
    }

    @Operation(summary = "내단지명 검색")
    @GetMapping("/searchAptNameList")
    public Result<List<AptNameResponseDto>> searchAptName(
            @Parameter(name = "aptSearch", description = "내단지명") @RequestParam(required = false) String aptSearch,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return aptMngService.searchAptName(aptSearch, loginDto.getGradeType(), loginDto.getCompanySeq());
    }

    @Operation(summary = "내단지 추가")
    @PostMapping("/addAptMng")
    public Result<List<AdminCompanyAptResponseDto>> addAptMng(
            @RequestBody AdminCompanyAptRequestDto admXicode,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if (admXicode.getXiCode() == null || admXicode.getXiCode().trim().equals("")) { //자이코드는 required = true 임
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return aptMngService.addAptMng(admXicode, loginDto.getCompanySeq(), loginDto.getSeq());
    }

    @Operation(summary = "내단지 삭제")
    @DeleteMapping("/delAptMng/{aptSeq}")
    public Result<List<AdminCompanyAptResponseDto>> delAptMng(
            @Parameter(name = "aptSeq", description = "아파트번호", required = true, example = "16") @PathVariable("aptSeq") int aptSeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if (aptSeq == 0) {
            throw new CustomException("ER004");	  //요청하신 페이지를 찾을 수 없습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return aptMngService.delAptMng(aptSeq, loginDto.getCompanySeq(), loginDto.getSeq());
    }

    @Operation(summary = "내단지 제공 서비스 설정 저장")
    @PostMapping("/setAptProvideServiceInfo")
    public Result<Object> setAptProvideServiceInfo(
            @RequestBody AptServiceRequestDto aptServiceDto,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AptSeqAndXiCodeDto aptSeqAndXiCode = aptServiceDto.getAptSeqAndXiCodeDto();
        if (aptSeqAndXiCode.getAptSeq() == 0 || aptSeqAndXiCode.getXiCode().trim().equals("")) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return aptMngService.setAptProvideServiceInfo(aptServiceDto, loginDto.getSeq());
    }

    @Operation(summary = "내단지 제공 서비스 조회")
    @GetMapping("/getAptProvideServiceInfo")
    public Result<AptServiceResponseDto> getAptProvideServiceInfo(
            @Parameter(name = "aptSeq", description = "아파트 번호") @RequestParam int aptSeq
    ) {
        if (aptSeq == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        return aptMngService.getAptProvideServiceInfo(aptSeq);
    }

    @Operation(summary = "내단지 상세", description = "아파트/내단지관리자(대표)/동리스트 정보")
    @GetMapping("/detail")
    public Result<AptDetailResponseDto> detail(
            @Parameter(name = "aptSeq", description = "아파트고유번호") @RequestParam(defaultValue = "0") int aptSeq
    ) {
        if (aptSeq == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        return aptMngService.detail(aptSeq);
    }

    @Operation(summary = "내단지 동호 리스트 상세")
    @GetMapping("/aptDongHoList")
    public Result<List<AptInfoDto>> aptDongHoList(
            @Parameter(name = "aptSeq", description = "아파트고유번호") @RequestParam int aptSeq,
            @Parameter(name = "dong", description = "동") @RequestParam String dong
    ) {
        if (aptSeq == 0 || dong.trim().equals("")) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        return aptMngService.aptDongHoList(aptSeq, dong);
    }

    @Operation(summary = "아파트정보 엑셀 업로드(백엔드처리)")
    @PostMapping(value = "/uploadExcel", consumes = "multipart/form-data")
    public Result<Object> uploadExcel(
            @Parameter(name = "excelFile", description = "아파트 정보 엑셀파일") @RequestPart MultipartFile excelFile,
            @Parameter(name = "aptSeq", description = "아파트고유번호") @RequestParam int aptSeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) throws Exception {
        if (aptSeq == 0 || excelFile.isEmpty()) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        // 엑셀파일인지 validation
        String extension = FilenameUtils.getExtension(excelFile.getOriginalFilename());
        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new CustomException("FI004");	  //엑셀파일만 업로드 해주세요.
        }
        // 파일 사이즈 오류 validation
        if (excelFile.getSize() == 0) {
            throw new CustomException("FI001");	  //업로드 오류 (파일사이즈:0)
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();


        return aptMngService.uploadExcel(excelFile, extension, aptSeq, loginDto.getSeq());
    }


    //사용 안하지만 일단 킵
    @Operation(summary = "아파트정보 엑셀 업로드(프론트단처리)")
    @PostMapping(value = "/uploadExcelF", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Result<Object> uploadExcelF(
            @Parameter(name = "excelFile", description = "아파트 정보 엑셀파일") @RequestPart MultipartFile excelFile,
            @Parameter(name = "jsonList", description = "엑셀정보 jsonList") @RequestParam String jsonList,
            @Parameter(name = "aptSeq", description = "아파트고유번호") @RequestParam int aptSeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) throws Exception {

        if (aptSeq == 0 || excelFile.isEmpty()) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        // 엑셀파일인지 validation
        String extension = FilenameUtils.getExtension(excelFile.getOriginalFilename());
        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new CustomException("FI004");	  //엑셀파일만 업로드 해주세요.
        }
        // 파일 사이즈 오류 validation
        if (excelFile.getSize() == 0) {
            throw new CustomException("FI001");	  //업로드 오류 (파일사이즈:0)
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new SimpleModule());
        List<AptInfoExcelDto> aptInfoExceldtoList = objectMapper.readValue(
                jsonList, new TypeReference<List<AptInfoExcelDto>>() {
                });
        return aptMngService.uploadExcelF(excelFile, aptSeq, loginDto.getSeq(), aptInfoExceldtoList);
    }

    @Operation(summary = "내단지 관리자 등록(대표 내단지 관리자)")
    @PostMapping("/addAptMngMember")
    public Result<AdminMemberDto> addAptMngMember(
            @RequestBody AdminAPTManagerInsertRequestDto adminAPTManagerInsertRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        String email = adminAPTManagerInsertRequest.getEmail();
        if (adminAPTManagerInsertRequest.getAptSeq() == 0 || email == null) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        if (adminAPTManagerInsertRequest.getCompanySeq() == 0) {
            throw new CustomException("MA008");	  //업체가 지정 되지 않았습니다. 단지에 업체를 지정후 등록하세요.
        }
        if (email.length() < 1 || email.length() > 100 || !StringUtil.stringMailCheck(email)) {
            throw new CustomException("MA015");	  //이메일을 형식에 맞게 입력해주세요.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return aptMngService.addAptMngMember(adminAPTManagerInsertRequest, loginDto);
    }

    @Operation(summary = "내단지 관리자 수정(대표 내단지 관리자)")
    @PutMapping("/editAptMngMember")
    public Result<String> editAptMngMember(
            @RequestBody AdminAPTManagerUpdateRequestDto adminAPTManagerUpdateRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        String email = adminAPTManagerUpdateRequest.getEmail();
        if (adminAPTManagerUpdateRequest.getAptMngMemberSeq() == 0
                || email == null) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        email = email.trim();
        if (email.length() < 1 || email.length() > 100 || !StringUtil.stringMailCheck(email)) {
            throw new CustomException("MA015");	  //이메일을 형식에 맞게 입력해주세요.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return aptMngService.editAptMngMember(adminAPTManagerUpdateRequest, loginDto.getSeq());
    }

    @Operation(summary = "내단지 동별로 이사 정보 설정")
    @PostMapping("/setAptInfoMoveType")
    public Result<String> setAptInfoMoveType(
            @RequestBody List<AptMoveInfoDto> aptMoveInfoDtoList,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return aptMngService.setAptInfoMoveType(aptMoveInfoDtoList, loginDto.getSeq());
    }

    @Operation(summary = "주소 저장")
    @PutMapping("/setAddr")
    public Result<String> setAddrAndAddrDoType(
            @Parameter(name = "aptSeq", description = "아파트고유번호") @RequestParam int aptSeq,
            @Parameter(name = "addr", description = "주소") @RequestParam String addr,
            @Parameter(name = "addrDoType", description = "지역코드") @RequestParam(defaultValue = "0") int addrDoType,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return aptMngService.updateAptAddr(aptSeq, addr, addrDoType, loginDto.getSeq());
    }

    @Operation(summary = "myaptYN 변경 API")
    @PutMapping("/updateMyaptYn")
    public Result<Object> updateMyaptYn(
            @Parameter(name = "aptSeq", description = "아파트고유번호") @RequestParam int aptSeq,
            @Parameter(name = "myaptYn", description = "아파트고유번호") @RequestParam String myaptYn,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if(aptSeq == 0 || myaptYn == null) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return aptMngService.updateMyaptYn(aptSeq, myaptYn, loginDto.getSeq());
    }

    @Operation(summary = "관리업체 지정시 업체목록 불러오는 API")
    @GetMapping("/companyList")
    public Result<AptMngCompanyResponseDto> selectAdminCompanyList(
            @Parameter(name = "aptSeq", description = "아파트고유번호") @RequestParam(defaultValue = "0") int aptSeq,
            @Parameter(name = "companyName", description = "검색어:업체명") @RequestParam(required = false) String companyName
    ) {
        if (aptSeq == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        return aptMngService.selectAdminCompanyList(aptSeq, companyName);
    }

    @Operation(summary = "관리업체 변경하는 API")
    @PutMapping("/editAptMngAdminCompany")
    public Result<AdminMemberDto> editAptMngAdminCompany(
            @Parameter(name = "aptSeq", description = "아파트고유번호") @RequestParam int aptSeq,
            @Parameter(name = "companySeq", description = "회사 고유번호") @RequestParam int companySeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return aptMngService.editAptMngAdminCompany(aptSeq, companySeq, loginDto.getSeq());
    }

    @Operation(summary = "내단지 세대 정보 가져와서 등록")
    @PostMapping("/getAptInfo")
    public Result<Object> getAptInfo(
            @RequestBody AptSeqAndXiCodeDto aptSeqAndXiCodeDto,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        int aptSeq = aptSeqAndXiCodeDto.getAptSeq();
        String xiCode = aptSeqAndXiCodeDto.getXiCode();
        if (aptSeq == 0 || xiCode == null || xiCode.trim().equals("") || xiCode.length() > 50) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return aptMngService.getAptInfo(xiCode, aptSeq, loginDto.getSeq());
    }

    @Operation(summary = "자이코드 중복체크")
    @GetMapping("/checkXicode")
    public Result<Object> getAptInfo(
            @Parameter(name = "xiCode", description = "자이코드") @RequestParam String xiCode
    ) {
        if (xiCode.length() > 50) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        return aptMngService.checkXicode(xiCode);
    }

    @Operation(summary = "엑셀 업로드 히스토리 조회")
    @GetMapping("/excelHistory")
    public Result<List<ExcelFileInfoDto>> getExcelHistory(
            @Parameter(name = "aptSeq", description = "아파트 고유코드") @RequestParam int aptSeq
    ) {
        return aptMngService.getExcelHistory(aptSeq);
    }

    @Operation(summary = "샘플양식 엑셀 다운로드")
    @GetMapping("/sampleHouseHoldDown")
    public ResponseEntity<byte[]> getSampleExcelDown() throws Exception {
        return aptMngService.getSampleHouseHoldExcel();
    }

    @Operation(summary = "엑셀 다운로드")
    @GetMapping(value = "/aptInfoDownload")
    public ResponseEntity<byte[]> aptInfoDownload(
            @Parameter(name = "aptSeq", description = "아파트 고유코드") @RequestParam int aptSeq,
            @Parameter(name = "fileSeq", description = "파일 시퀀스") @RequestParam(defaultValue = "0") int fileSeq
    ) throws Exception {
        if (aptSeq == 0) {
            throw new CustomException("ER003");	  //요청하신 정보가 잘못되었습니다.
        }
        return aptMngService.aptInfoDownload(aptSeq, fileSeq);
    }
}
