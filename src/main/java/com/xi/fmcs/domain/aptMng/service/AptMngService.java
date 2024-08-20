package com.xi.fmcs.domain.aptMng.service;

import com.xi.fmcs.support.model.Define;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.support.model.ViewBag;
import com.xi.fmcs.support.util.*;
import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.domain.admin.model.AdminAPTManagerInsertRequestDto;
import com.xi.fmcs.domain.admin.model.AdminAPTManagerUpdateRequestDto;
import com.xi.fmcs.domain.admin.model.AdminMemberDetailMinDto;
import com.xi.fmcs.domain.admin.model.AdminMemberDto;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.admin.repository.AdminMemberRepository;
import com.xi.fmcs.domain.admin.service.AdminMemberService;
import com.xi.fmcs.domain.adminCompany.model.AdminCompanyDetailDto;
import com.xi.fmcs.domain.adminCompany.repository.AdminCompanyRepository;
import com.xi.fmcs.domain.apt.model.*;
import com.xi.fmcs.domain.apt.repository.AptRepository;
import com.xi.fmcs.domain.aptMng.model.AdminATPMngResponseDto;
import com.xi.fmcs.domain.aptMng.model.AptInfoDto;
import com.xi.fmcs.domain.aptMng.model.AptInfoExcelDto;
import com.xi.fmcs.domain.aptMng.model.AptInfoExcelResponseDto;
import com.xi.fmcs.domain.aptMng.model.AptMngCompanyResponseDto;
import com.xi.fmcs.domain.aptMng.model.AptNameResponseDto;
import com.xi.fmcs.domain.aptMng.model.AptSeqAndXiCodeDto;
import com.xi.fmcs.domain.aptMng.model.AptServiceRequestDto;
import com.xi.fmcs.domain.aptMng.model.AptServiceResponseDto;
import com.xi.fmcs.domain.aptMng.repository.AptMngRepository;
import com.xi.fmcs.domain.file.model.ExcelFileInfoDto;
import com.xi.fmcs.domain.file.model.FileInfoDetailDto;
import com.xi.fmcs.domain.file.repository.FileInfoRepository;
import com.xi.fmcs.domain.file.service.FileInfoService;
import com.xi.fmcs.domain.moveReserve.repository.MoveReserveRepository;

import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AptMngService {

    private final AptMngRepository aptMngRepository;
    private final AptRepository aptRepository;
    private final AdminMemberService adminMemberService;
    private final AdminMemberRepository adminMemberRepository;
    private final AdminCompanyRepository adminCompanyRepository;
    private final FileInfoService fileInfoService;
    private final JdbcTemplate jdbcTemplate;
    private final FileInfoRepository fileInfoRepository;
    private final MoveReserveRepository moveReserveRepository;

    //관리자 내단지 관리 목록
    public ResultWithBag<List<AdminATPMngResponseDto>> aptMngAdminList(
            String cmpSearch, String aptSearch, int doType, int page, int pSize, String myAptYn, int gradeType, int companySeq) {
        int currentPage = page == 0 ? 1 : page;
        ViewBag viewBag = null;

        List<AdminATPMngResponseDto> aptList = new ArrayList<>();
        if (gradeType == 1) {    // 슈퍼관리자
            aptList = aptMngRepository.getAptMngGsList(cmpSearch, aptSearch, doType, currentPage, pSize, myAptYn);
        } else {    // 업체관리자
            aptList = aptMngRepository.getAptMngList(companySeq, aptSearch, doType, currentPage, pSize, myAptYn);
        }

        if (aptList != null && aptList.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("cmpSearch", cmpSearch);
            map.put("aptSearch", aptSearch);
            map.put("doType", doType);
            viewBag = ViewBag.builder()
                    .totalCount(aptList.get(0).getTotalcnt())
                    .currentPage(currentPage)
                    .pageSize(pSize)
                    .search(map)
                    .build();
        }
        return ResultWithBag.<List<AdminATPMngResponseDto>>builder()
                .viewBag(viewBag)
                .result(aptList)
                .build();
    }

    //내단지명 조회
    public Result<List<AptNameResponseDto>> searchAptName(String aptSearch, int gradeType, int companySeq) {
        List<AptNameResponseDto> aptList = new ArrayList<>();
        if (gradeType == 1) {
            aptList = aptMngRepository.searchAptList(0, aptSearch);
        } else {
            aptList = aptMngRepository.searchAptList(companySeq, aptSearch);
        }
        return Result.<List<AptNameResponseDto>>builder()
                .result(aptList)
                .build();
    }

    //내단지 추가
    @Transactional
    public Result<List<AdminCompanyAptResponseDto>> addAptMng(AdminCompanyAptRequestDto admXicode, int companySeq, int regSeq) {
        List<AdminCompanyAptResponseDto> aptDTOList = null;
        try {

            try {
                aptRepository.insertAdminCompanyApt(
                        companySeq,
                        admXicode.getXiCode(),
                        admXicode.getAptName(),
                        admXicode.getAddr(),
                        admXicode.getAddrDoType(),
                        regSeq
                );
                LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[내단지 등록] [업체=" + companySeq + ", 단지코드=" + admXicode.getXiCode() + "]");
            } catch (Exception e) {
                LogUtil.writeLog("XI_SP_FMCS_ADMIN_COMPANY_APT_C", e.getMessage());
                throw new CustomException("MA004");      //등록되지 않았습니다.
            }

            aptDTOList = aptRepository.getCompanyAptListByCmpSeq(companySeq);
        } catch (Exception e) {
            LogUtil.conWriteLog("AptMngService addAptMng", e.getMessage());
            throw new CustomException("ER001");      //관리자에게 문의하세요.
        }
        return Result.<List<AdminCompanyAptResponseDto>>builder()
                .result(aptDTOList)
                .build();
    }

    //내단지 삭제
    @Transactional
    public Result<List<AdminCompanyAptResponseDto>> delAptMng(int aptSeq, int companySeq, int regSeq) {
        try {
            int result = -1; // del실행 후 -1 이면 롤백된 것

            try {
                result = aptRepository.delAptMng(aptSeq, regSeq);
            } catch (Exception e) {
                LogUtil.writeLog("XI_SP_FMCS_ADMIN_APT_D", e.getMessage());
                new CustomException("ER001");      //관리자에게 문의하세요.
            }
            if (result == 2) {
                throw new CustomException("MA013");      //회원정보가 있는 단지는 삭제가 불가합니다.
            }
            List<AdminCompanyAptResponseDto> aptDTOList = new ArrayList<>();
            if (result == 1) {
                aptDTOList = aptRepository.getCompanyAptListByCmpSeq(companySeq);
                return Result.<List<AdminCompanyAptResponseDto>>builder()
                        .result(aptDTOList)
                        .stateMessage("삭제 되었습니다.")
                        .stateCode("200")
                        .build();
            }
        } catch (Exception e) {
            LogUtil.conWriteLog("AptMngService delAptMng", e.getMessage());
            throw new CustomException("CM002");
        }
        throw new CustomException("ER001");      //관리자에게 문의하세요.
    }

    // 내단지 제공 서비스 설정 저장
    @Transactional
    public Result<Object> setAptProvideServiceInfo(AptServiceRequestDto aptServiceDto, int regSeq) {
        AptSeqAndXiCodeDto aptSeqAndXiCodeDto = aptServiceDto.getAptSeqAndXiCodeDto();
        AptServiceResponseDto aptServiceResponse = aptServiceDto.getAptServiceResponseDto();
        try {
            aptMngRepository.setAptProvideServiceInfo(
                    aptSeqAndXiCodeDto.getAptSeq(),
                    aptServiceResponse.getAuthYn(),
                    aptServiceResponse.getOperationYn(),
                    aptServiceResponse.getFacilityYn(),
                    aptServiceResponse.getConciergeYn(),
                    aptServiceResponse.getMyaptYn(),
                    aptSeqAndXiCodeDto.getXiCode(),
                    regSeq
            );
            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[내단지 설정 등록] [등록 여부=true]");
        } catch (Exception e) {
            Map<String, Object> param = new HashMap<>();
            param.put("@SEQ", aptSeqAndXiCodeDto.getAptSeq());
            param.put("@AUTH_YN", aptServiceResponse.getAuthYn());
            param.put("@OPERATION_YN", aptServiceResponse.getOperationYn());
            param.put("@FACILITY_YN", aptServiceResponse.getFacilityYn());
            param.put("@CONCIERGE_YN", aptServiceResponse.getConciergeYn());
            param.put("@MYAPT_YN", aptServiceResponse.getMyaptYn());
            param.put("@XI_CODE", aptSeqAndXiCodeDto.getXiCode());
            param.put("@REG_SEQ", regSeq);
            LogUtil.writeLog("XI_SP_FMCS_ADMIN_APT_SET_U", param, e.getMessage());
            throw new CustomException("ER001");      //관리자에게 문의하세요.
        }
        return Result.<Object>builder()
                .build();
    }

    // 내단지 제공 서비스 조회
    public Result<AptServiceResponseDto> getAptProvideServiceInfo(int aptSeq) {
        return Result.<AptServiceResponseDto>builder()
                .result(aptMngRepository.getAptProvideServiceInfo(aptSeq))
                .build();
    }

    //내단지 상세 (APT/내단지관리자(대표)/동리스트)
    public Result<AptDetailResponseDto> detail(int aptSeq) {
        // 아파트 상세정보 조회
        AptDetailResponseDto aptDetail = new AptDetailResponseDto();
        AptDetailDto aptInfoDto = aptRepository.getAdminCompanyAptByAptSeq(aptSeq);
        if (aptInfoDto == null) {
            throw new CustomException("MA001"); //APT 정보 오류 입니다. \n관리자에게 문의하세요.
        }
        aptInfoDto.setCompany(adminCompanyRepository.adminCompanyDetail(aptInfoDto.getCompanySeq()).getCompany());
        aptDetail.setAptDetail(aptInfoDto);

        // 해당 단지의 대표 내단지관리자 조회
        aptDetail.setAdminMemberDto(aptMngRepository.getAdminAptMngMemberInfoByXiCode(aptSeq));

        // 해당 단지의 동정보 조회
        aptDetail.setDongInfoList(aptRepository.getAptDongList(aptSeq));

        return Result.<AptDetailResponseDto>builder()
                .result(aptDetail)
                .build();
    }

    //내단지 동호 리스트 상세
    public Result<List<AptInfoDto>> aptDongHoList(Integer aptSeq, String dong) {
        return Result.<List<AptInfoDto>>builder()
                .result(aptMngRepository.getAptInfoList(aptSeq, dong))
                .build();
    }

    // 엑셀 샘플 다운로드
    public ResponseEntity<byte[]> getSampleHouseHoldExcel() throws Exception {
        return new FileUtil().fileDownload(Define.EXCEL_FILE_PATH + "/", "SampleHouseHold.xlsx", "SampleHouseHold.xlsx");
    }

    // 엑셀업로드
    @Transactional
    public Result<Object> uploadExcel(MultipartFile file, String kind, int aptSeq, int regSeq) throws Exception {
        //getWorkbook(file) : xls, xlsx 인지 확인 및 비교 후에 그에 알맞는 Workbook 리턴
        Sheet worksheet = ExcelUtil.getWorkbook(file, kind).getSheetAt(0);
        List<AptInfoExcelDto> aptInfoList = new ArrayList<>();
        int result = aptMngRepository.deleteAptInfoAndDongInfo(aptSeq, regSeq);

        if (result > 0) {

            //엑셀에 숫자로 되어있는 Floor를 지수가 아니도록 만들면서 String으로 변환
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setGroupingUsed(false);

            int rowNum = 0;
            String cell = "";
            try {
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    rowNum = i;
                    AptInfoExcelDto aptInfo = new AptInfoExcelDto();
                    Row row = worksheet.getRow(i);
                    cell = "A";
                    String dong = row.getCell(0).getStringCellValue();
                    if (dong.length() > 10 || dong.trim().length() == 0) throw new Exception();
                    aptInfo.setDong(dong);
                    cell = "B";
                    String floor = row.getCell(1).getStringCellValue();
                    if (floor.length() > 10 || floor.trim().length() == 0) throw new Exception();
                    aptInfo.setFloor(floor);
                    cell = "C";
                    String roomtypecd = row.getCell(2).getStringCellValue();
                    if (roomtypecd.length() > 10 || roomtypecd.trim().length() == 0) throw new Exception();
                    aptInfo.setRoomtypecd(roomtypecd);
                    cell = "D";
                    String line = row.getCell(3).getStringCellValue();
                    if (line.length() > 10 || line.trim().length() == 0) throw new Exception();
                    aptInfo.setLine(line);
                    cell = "E";
                    String maxFloor = numberFormat.format(row.getCell(4).getNumericCellValue());
                    if (maxFloor.length() > 10 || maxFloor.trim().length() == 0) throw new Exception();
                    aptInfo.setMaxfloor(maxFloor);
                    cell = "F";
                    int maxLine = Integer.parseInt(numberFormat.format(row.getCell(5).getNumericCellValue()));
                    if (maxLine == 0) throw new Exception();
                    aptInfo.setMaxline(maxLine);
                    aptInfoList.add(aptInfo);
                }
            } catch (Exception e) {
                throw new CustomException("FI002", MngUtil.message("FI002", cell, Integer.toString(rowNum + 1)));    //{0}행 {1}열 에서 문제 발생
            }
            LocalDateTime now = LocalDateTime.now();
            result = batchInsert(aptInfoList, aptSeq, regSeq, now);
            fileInfoService.excelFileInfoDevelop(file, "APT_INFO", aptSeq, "ExcelFiles", regSeq, now);

            if (result != -1) {
                return Result.<Object>builder()
                        .build();
            }
        }

        throw new CustomException("ER001");      //관리자에게 문의하세요.
    }

    //엑셀 업로드
    @Transactional
    public Result<Object> uploadExcelF(MultipartFile file, int aptSeq, int regSeq, List<AptInfoExcelDto> aptInfoList) throws Exception {
        //getWorkbook(file) : xls, xlsx 인지 확인 및 비교 후에 그에 알맞는 Workbook 리턴

        int result = aptMngRepository.deleteAptInfoAndDongInfo(aptSeq, regSeq);

        if (result > 0) {
            LocalDateTime now = LocalDateTime.now();
            result = batchInsert(aptInfoList, aptSeq, regSeq, now);
            fileInfoService.excelFileInfoDevelop(file, "APT_INFO", aptSeq, "ExcelFiles", regSeq, now);

            if (result == 1) {
                return Result.<Object>builder()
                        .build();
            }
        }

        return Result.<Object>builder()
                .stateMessage("등록실패")
                .build();
    }

    // 대량 업로드 및 파일 저장
    @Transactional
    private int batchInsert(List<AptInfoExcelDto> aptInfoList, int aptSeq, int regSeq, LocalDateTime date) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO APT_INFO (APT_SEQ, DONG, FLOOR, ROOMTYPECD, LINE, MAXFLOOR, MAXLINE, REG_SEQ, REG_DATE)" +
                        " VALUES (?,?,?,?,?,?,?,?,?)",
                aptInfoList,
                10000, //batchSize
                ((ps, aptInfo) -> {
                    ps.setInt(1, aptSeq);
                    ps.setString(2, aptInfo.getDong());
                    ps.setString(3, aptInfo.getFloor());
                    ps.setString(4, aptInfo.getRoomtypecd());
                    ps.setString(5, aptInfo.getLine());
                    ps.setString(6, aptInfo.getMaxfloor());
                    ps.setInt(7, aptInfo.getMaxline());
                    ps.setInt(8, regSeq);
                    ps.setTimestamp(9, java.sql.Timestamp.valueOf(date));
                })
        );
        int result = aptMngRepository.updateAptInfoAndDongInfo(aptSeq, regSeq);
        if (result == -1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    // 내단지 관리자 등록(대표 내단지 관리자)
    @Transactional
    public Result<AdminMemberDto> addAptMngMember(AdminAPTManagerInsertRequestDto adminAPTManager, AdminMemberLoginDto loginDto) {
        //휴대전화, 이메일 중복체크
        adminMemberService.checkMobileNoAndEmailAreUsed(adminAPTManager.getMobileNo(), adminAPTManager.getEmail());

        //company seq 조작 및 오류 방어 위해 재설정
        String gradeType = "3";
        String position = null;
        String department = null;
        String companyYn = "Y";
        String telNo = null;
        int companySeq;
        if (loginDto.getGradeType() >= 2) {
            companySeq = loginDto.getCompanySeq();
        } else {
            companySeq = adminCompanyRepository.getAdminCompanyByAptSeq(adminAPTManager.getAptSeq()).getCompanySeq();
        }
        String nickname = "단지관리자";
        if (adminAPTManager.getAptSeq() > 0) {
            int adminMemberSeq = adminMemberService.insertAdminMember(
                    adminAPTManager.getEmail(),
                    adminAPTManager.getName(),
                    gradeType,
                    position,
                    department,
                    companyYn,
                    telNo,
                    adminAPTManager.getMobileNo(),
                    companySeq,
                    loginDto.getSeq(),
                    nickname);
            if (adminMemberSeq > 0) {
                adminMemberRepository.insertAptMngXicodeByAdmSeq(adminMemberSeq, adminAPTManager.getAptSeq(), loginDto.getSeq());
                // 해당 단지의 대표 내단지관리자 조회
                return Result.<AdminMemberDto>builder()
                        .result(aptMngRepository.getAdminAptMngMemberInfoByXiCode(adminAPTManager.getAptSeq()))
                        .build();
            }
        }
        throw new CustomException("ER001");      //관리자에게 문의하세요.
    }

    // 내단지 관리자 휴대폰번호 변경(대표 내단지 관리자)
    @Transactional
    public Result<String> editAptMngMember(AdminAPTManagerUpdateRequestDto adminAPTManagerUpdateRequest, int modSeq) {
        int aptMngMemberSeq = adminAPTManagerUpdateRequest.getAptMngMemberSeq();
        String email = adminAPTManagerUpdateRequest.getEmail();
        String mobileNo = adminAPTManagerUpdateRequest.getMobileNo();
        if (aptMngMemberSeq > 0) {
            // 수정하려는 이메일주소 나를 제외한 타인이 사용하고 있는지 체크한다.
            adminMemberService.checkMobileNoAndEmailAreUsedExceptMe(aptMngMemberSeq, mobileNo, email);

            // 요청 정보 조작 방지를 위해 및 유효성 체크
            AdminMemberDetailMinDto adminMemberDetailMinDto = adminMemberRepository.getAdminMemberDetailBySeq(aptMngMemberSeq);
            if (adminMemberDetailMinDto != null) {
                adminMemberDetailMinDto.setSeq(aptMngMemberSeq);
                adminMemberDetailMinDto.setEmail(email);
                adminMemberDetailMinDto.setName(adminAPTManagerUpdateRequest.getName());
                adminMemberDetailMinDto.setMobileNo(mobileNo);
                adminMemberRepository.updateAdminMemberMobileNo(aptMngMemberSeq, mobileNo, modSeq);
                return Result.<String>builder().build();
            }
        }
        throw new CustomException("ER001");      //관리자에게 문의하세요.
    }

    // 내단지 동별로 이사 정보 설정
    public Result<String> setAptInfoMoveType(List<AptMoveInfoDto> aptMoveInfoDtoList, int adminSeq) {
        aptRepository.insertOrUpdateAptMoveInfo(aptMoveInfoDtoList, adminSeq);
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM004")) //저장 되었습니다.
                .build();
    }

    //주소 저장
    @Transactional
    public Result<String> updateAptAddr(int aptSeq, String addr, int addrDoType, int regSeq) {
        aptRepository.updateAptUseYnOrAddr(
                aptSeq,
                2, // 2가 Addr 변경
                regSeq,
                null,
                addr,
                addrDoType
        );
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM004")) //저장 되었습니다.
                .build();
    }

    //myaptYN 변경
    @Transactional
    public Result<Object> updateMyaptYn(int aptSeq, String myaptYn, int regSeq) {
        aptRepository.updateAptUseYnOrAddr(
                aptSeq,
                1, // 1가 MyaptYn 변경
                regSeq,
                myaptYn,
                null,
                0
        );
        return Result.<Object>builder()
                .build();
    }

    //관리업체 지정시 업체목록 불러오기
    public Result<AptMngCompanyResponseDto> selectAdminCompanyList(int aptSeq, String companyName) {
        AptMngCompanyResponseDto responseDto = new AptMngCompanyResponseDto();
        int companySeq = 0;

        // 현재 담당 회사 정보
        AdminCompanyDetailDto adminCompanyDetail = adminCompanyRepository.getAdminCompanyByAptSeq(aptSeq);
        if (adminCompanyDetail != null) {
            companySeq = adminCompanyDetail.getCompanySeq();
            responseDto.setNowAptMngCompany(adminCompanyRepository.selectMyAdminCompany(companySeq));
        }

        // 현재 담당 회사 제외한 회사리스트
        responseDto.setAptMngCompanyList(adminCompanyRepository.selectExceptMeAdminCompanyList(companySeq, companyName));

        return Result.<AptMngCompanyResponseDto>builder()
                .result(responseDto)
                .build();
    }

    // 관리업체 변경
    @Transactional
    public Result<AdminMemberDto> editAptMngAdminCompany(int companySeq, int aptSeq, int modSeq) {
        AdminMemberDto adminMemberDto = null;
        try {
            aptMngRepository.editAptMngAdminCompany(companySeq, aptSeq, modSeq);
        } catch (Exception e) {
            LogUtil.writeLog("XI_SP_R_FMCS_ADMIN_COMPANY_APT_U", e.getMessage());
            throw new CustomException("ER001");      //관리자에게 문의하세요.
        }
//        List<AdminAPTMngInterface> adminMember = aptMngRepository.getAdminAptMngMemberInfoByXiCode(aptSeq);
        try {
//            adminMemberDto = new AdminMemberDto(adminMember.get(0));
        } catch (IndexOutOfBoundsException e) {
        } //대표 내단지 관리자 없더라도 스킵
        return Result.<AdminMemberDto>builder()
                .result(adminMemberDto)
                .build();
    }

    // 세대 정보 가져오기
    @Transactional
    public Result<Object> getAptInfo(String xiCode, int aptSeq, int regSeq) {
        try {
            //2는 이미등록된 내단지 ,1 은 유효한 aptSeq
            int aptChk = aptRepository.checkAptInfoByAptSeq(aptSeq);
            if (aptChk > 1) {
                throw new CustomException("MA011"); // 이미 등록된 정보입니다.
            }
            if (aptChk < 1) {
                throw new CustomException("MA001"); // APT 정보 오류 입니다. \n관리자에게 문의하세요.
            }
            HttpUtil<AptInfoExcelResponseDto> httpUtil = new HttpUtil<AptInfoExcelResponseDto>(
                    Define.APP_IF_URL + "/api/app/v1/myapt/getAptFloorStructure",
                    HttpMethod.POST
            );
            httpUtil.addHeader("ApiKey", Define.API_KEY);
            httpUtil.addBody("XI_CODE", xiCode);
            httpUtil.setRequestWithBody();
            ResponseEntity<AptInfoExcelResponseDto> response = httpUtil.getResponse(AptInfoExcelResponseDto.class);
            AptInfoExcelResponseDto aptInfoResult = response.getBody();
            if (aptInfoResult.getResult().size() == 0) {
                throw new CustomException("MA009"); // 요청하신 단지에 세대정보가 없습니다.
            }

            aptMngRepository.deleteAptInfoAndDongInfo(aptSeq, regSeq);
            LocalDateTime now = LocalDateTime.now();
            int result = batchInsert(aptInfoResult.getResult(), aptSeq, regSeq, now);
            if (result > 0) {
                TypeDictionary<String, String> fileInfo = ExcelUtil.writeAptInfoExcel(aptInfoResult.getResult(), xiCode);
                FileInfoDetailDto fileInfoDto = new FileInfoDetailDto();
                fileInfoDto.setAttachTableName("APT_INFO");
                fileInfoDto.setAttachTableSeq(aptSeq);
                fileInfoDto.setFileInputName("execlFile");
                fileInfoDto.setFilePath(fileInfo.getValue("FILE_PATH"));
                fileInfoDto.setFileOrgName(fileInfo.getValue("ORIGINAL_FILE_NAME"));
                fileInfoDto.setFileStoredName(fileInfo.getValue("STORED_FILE_NAME"));
                fileInfoDto.setFileSize("0");
                fileInfoDto.setRegDate(now);
                fileInfoRepository.insertFileInfo(fileInfoDto, regSeq);
                // 이사예약 시간 설정 기본
                try {
                    moveReserveRepository.insertAptMoveTimeDefaultSet(aptSeq, regSeq);
                    LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[이사예약 시간 Default 설정] [aptSeq=" + aptSeq + "]");
                } catch (Exception e) {
                    throw new CustomException("ER001");      //관리자에게 문의하세요.
                }
                return Result.<Object>builder()
                        .stateMessage(MngUtil.message("MA007"))  //세대 정보가 등록 되었습니다.
                        .build();
            }
            return Result.<Object>builder()
                    .stateMessage(MngUtil.message("MA004"))  //등록되지 않았습니다.
                    .build();
        } catch (Exception e) {
            LogUtil.conWriteLog("AptMngService getAptInfo", e.getMessage());
            throw new CustomException("ER001");      //관리자에게 문의하세요.
        }
    }

    public Result<Object> checkXicode(String xiCode) {

        if (aptRepository.checkXicode(xiCode) == 0) {
            return Result.<Object>builder()
                    .build();
        }
        throw new CustomException("MA014");      //중복된 자이코드가 존재합니다.
    }

    //엑셀 히스토리
    public Result<List<ExcelFileInfoDto>> getExcelHistory(Integer aptSeq) {
        List<ExcelFileInfoDto> fileInfoList = aptMngRepository.getAptInfoExcelHis(aptSeq);
        return Result.<List<ExcelFileInfoDto>>builder()
                .result(fileInfoList)
                .build();
    }

    public ResponseEntity<byte[]> aptInfoDownload(int aptSeq, int fileSeq) throws Exception {
        String fileStoredName = null;
        String fileOrgName = null;
        FileInfoDetailDto fileInfo;
        if (fileSeq == 0) {
            fileInfo = fileInfoRepository.getFileInfoV2("APT_INFO", aptSeq, "excelFile").get(0);
            System.out.println(fileInfo.toString());
        } else {
            fileInfo = fileInfoRepository.selectFileInfoByFileSeq(fileSeq);
        }
        fileStoredName = fileInfo.getFileStoredName();
        fileOrgName = fileInfo.getFileOrgName();
        return new FileUtil().fileDownload(Define.EXCEL_FILE_PATH + "/", fileStoredName, fileOrgName);
    }
}