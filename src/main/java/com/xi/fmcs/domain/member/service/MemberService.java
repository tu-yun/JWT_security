package com.xi.fmcs.domain.member.service;

import com.xi.fmcs.support.model.Define;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.support.model.ViewBag;
import com.xi.fmcs.support.util.*;
import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.domain.apt.model.AptDetailDto;
import com.xi.fmcs.domain.apt.model.AptDongHoRequestDto;
import com.xi.fmcs.domain.apt.model.AptDongHoResponseDto;
import com.xi.fmcs.domain.apt.model.AptDongInfoDto;
import com.xi.fmcs.domain.apt.repository.AptRepository;
import com.xi.fmcs.domain.facility.model.interfaces.FacilityBasicSetInterface;
import com.xi.fmcs.domain.facility.repository.FacilityRepository;
import com.xi.fmcs.domain.file.model.FileInfoDetailDto;
import com.xi.fmcs.domain.file.repository.FileInfoRepository;
import com.xi.fmcs.domain.file.service.FileInfoService;
import com.xi.fmcs.domain.member.model.*;
import com.xi.fmcs.domain.member.repository.MemberRepository;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final FacilityRepository facilityRepository;
    private final MemberRepository memberRepository;
    private final FileInfoService fileInfoService;
    private final FileInfoRepository fileInfoRepository;
    private final AptRepository aptRepository;

    public MemberService(FacilityRepository facilityRepository,
                         MemberRepository memberRepository, FileInfoService fileInfoService,
                         FileInfoRepository fileInfoRepository, AptRepository aptRepository) {
        this.facilityRepository = facilityRepository;
        this.memberRepository = memberRepository;
        this.fileInfoService = fileInfoService;
        this.fileInfoRepository = fileInfoRepository;
        this.aptRepository = aptRepository;
    }

    public Result<String> getIndex(int aptSeq) {
        List<FacilityBasicSetInterface> fBasicSet = facilityRepository.getFacilityBasicSet(aptSeq);
        if (fBasicSet != null || fBasicSet.size() != 0) {
            return Result.<String>builder()
                    .result(fBasicSet.get(0).getMemSyncUrl()) //이거 왜 전달하는지 확인해보기
                    .build();
        } else {
            return Result.<String>builder()
                    .build();
        }
    }

    //승인 회원 목록
    public ResultWithBag<List<MemberListResponseDto>> getMemberList(MemberListRequestDto memberListRequest, String dongMngYn, int page, int pageSize) {
        LocalDateTime date = LocalDateTime.now();
        page = page == 0 ? 1 : page;

        String sDate = memberListRequest.getStartDate();
        String eDate = memberListRequest.getEndDate();
        if (sDate == null || sDate.trim().equals("")) {
            memberListRequest.setStartDate(date.minusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString());
        }
        if (eDate == null || eDate.trim().equals("")) {
            memberListRequest.setEndDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString());
        }
        List<MemberListResponseDto> memberList = memberRepository.getMemberList(
                memberListRequest,
                2, // status : 2 승인회원
                dongMngYn,
                page,
                pageSize);

        int totalCnt = 0;
        Map search = new HashMap<>();
        if (memberList != null && memberList.size() > 0) {
            totalCnt = memberList.get(0).getTotalCnt();
            search.put("search", memberListRequest.getSchVal());
        }
        return ResultWithBag.<List<MemberListResponseDto>>builder()
                .result(memberList)
                .viewBag(ViewBag.builder()
                        .totalCount(totalCnt)
                        .currentPage(page)
                        .pageSize(pageSize)
                        .search(search)
                        .build())
                .build();
    }

    //승인대기,미승인 회원목록
    public ResultWithBag<List<MemberWaitListResponseDto>> getMemberWaitOrNotApprovedList(MemberWaitListRequestDto memberRequest, int status) {
        int totalCnt = 0;
        int currentPage = memberRequest.getPage() == 0 ? 1 : memberRequest.getPage();
        int pageSize = memberRequest.getPageSize() == 0 ? 10 : memberRequest.getPageSize();

        List<MemberWaitListResponseDto> result = memberRepository.getMemberNoneDateList(
                memberRequest,
                status,
                currentPage,
                pageSize);
        if (result != null && result.size() > 0) {
            totalCnt = result.get(0).getTotalCnt();

        }
        Map<String, Object> search = new HashMap();
        search.put("schVal", memberRequest.getSchVal());
        search.put("schDong", memberRequest.getDong());
        search.put("schHo", memberRequest.getHo());

        return ResultWithBag.<List<MemberWaitListResponseDto>>builder()
                .result(result)
                .viewBag(ViewBag.builder()
                        .totalCount(totalCnt)
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .search(search)
                        .build())
                .build();
    }

    //전출회원 목록
    public ResultWithBag<List<MemberListResponseDto>> moveOutMemberList(MemberListRequestDto memberListRequest, int page, int pageSize) {
        LocalDateTime date = LocalDateTime.now();
        page = page == 0 ? 1 : page;

        String sDate = memberListRequest.getStartDate();
        String eDate = memberListRequest.getEndDate();
        if (sDate == null || sDate.trim().equals("")) {
            memberListRequest.setStartDate(date.minusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString());
        }
        if (eDate == null || eDate.trim().equals("")) {
            memberListRequest.setEndDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString());
        }
        List<MemberListResponseDto> memberList = memberRepository.getMoveOutMemberList(
                memberListRequest,
                4, // status : 4 전출회원
                page,
                pageSize
        );

        int totalCnt = 0;
        Map search = new HashMap<>();
        if (memberList != null && memberList.size() > 0) {
            totalCnt = memberList.get(0).getTotalCnt();
            search.put("search", memberListRequest.getSchVal());
        }
        return ResultWithBag.<List<MemberListResponseDto>>builder()
                .result(memberList)
                .viewBag(ViewBag.builder()
                        .totalCount(totalCnt)
                        .currentPage(page)
                        .pageSize(pageSize)
                        .search(search)
                        .build())
                .build();
    }

    //단지 정보
    public Result<AptDetailDto> getAptInfo(int aptSeq) {
        AptDetailDto aptDetail = aptRepository.getAdminCompanyAptByAptSeq(aptSeq);
        if (aptDetail == null) {
            throw new CustomException("ER001"); //관리자에게 문의하세요.
        }
        return Result.<AptDetailDto>builder()
                .result(aptDetail)
                .build();
    }

    //단지 동정보 조회
    public Result<List<AptDongInfoDto>> getAptDongList(int aptSeq) {

        List<AptDongInfoDto> dongInfoDtoList = aptRepository.getAptDongList(aptSeq);
        if (dongInfoDtoList == null) {
            return Result.<List<AptDongInfoDto>>builder()
                    .stateMessage(MngUtil.message("MM008")) //등록된 정보가 없습니다.
                    .build();
        }
        return Result.<List<AptDongInfoDto>>builder()
                .result(dongInfoDtoList)
                .build();
    }

    public Result<List<AptDongHoResponseDto>> getAptDongHoList(AptDongHoRequestDto aptDongHoRequest) {
        List<AptDongHoResponseDto> aptDongHoList = aptRepository.getAptDongHoList(
                aptDongHoRequest.getAptSeq(),
                aptDongHoRequest.getDong()
        );
        if (aptDongHoList == null) {
            return Result.<List<AptDongHoResponseDto>>builder()
                    .stateMessage(MngUtil.message("MM008")) //등록된 정보가 없습니다.
                    .build();
        }
        return Result.<List<AptDongHoResponseDto>>builder()
                .result(aptDongHoList)
                .build();
    }

    public Result<MemberRDto> getMemberDetailInfo(int memberSeq) {
        MemberRDto memberInfo = memberRepository.getMemberBySeq(memberSeq);
        if (memberInfo == null) {
            throw new CustomException("ER003");      //요청하신 정보가 잘못되었습니다.
        }
        String imgUrl = memberInfo.getImageUrl();
        if (imgUrl == null || imgUrl.trim().equals("")) {
            List<FileInfoDetailDto> fileInfo = fileInfoRepository.getFileInfoV2("MEMBER", memberSeq, "IMAGE_URL");
            if (fileInfo != null && fileInfo.size() > 0) {
                memberInfo.setImageUrl("/Files/Fmcs/Member/" + fileInfo.get(0).getFileStoredName());
            }
        }
        return Result.<MemberRDto>builder()
                .result(memberInfo)
                .build();
    }

    @Transactional
    public Result<String> setMember(MultipartFile image, MemberInsertOrUpdateRequestDto memberRequest, int regSeq) {
        String msg = "";
        int memberSeq = memberRequest.getMemberSeq();
        if (memberSeq > 0) {
            List<FileInfoDetailDto> prevFileInfo = fileInfoRepository.getFileInfoV2("MEMBER", memberSeq, "IMAGE_URL");
            if (image != null && !image.isEmpty()) {
                FileInfoDetailDto fileInfo = fileInfoService.attachFileInfoDevelop(image, "MEMBER", memberSeq, "Member", regSeq);
                memberRequest.setImageUrl("/Files/Fmcs/Member/" + fileInfo.getFileStoredName());
            } else {
                fileInfoRepository.deleteFileInfo(memberSeq, "IMAGE_URL", "MEMBER");
                memberRequest.setImageUrl(null);
            }
            memberRepository.updateMember(memberRequest, regSeq);
            // 기존 이미지는 삭제
            if (prevFileInfo != null && prevFileInfo.size() > 0) {
                new FileUtil(Define.FILE_PATH).delete("Member", prevFileInfo.get(0).getFileStoredName());
            }
            //sendSyncUser
            msg = MngUtil.message("CM003"); //수정 되었습니다.
        } else { // 신규 등록
            HttpUtil<MemberUniqueIdResponseDto> httpUtil = new HttpUtil<>(
                    Define.APP_IF_URL + "/api/app/v1/myapt/generateUniqueId",
                    HttpMethod.POST
            );
            httpUtil.addHeader("ApiKey", Define.API_KEY);
            httpUtil.addBody("EMAIL", memberRequest.getEmail());
            httpUtil.setRequestWithBody();
            ResponseEntity<MemberUniqueIdResponseDto> response = httpUtil.getResponse(MemberUniqueIdResponseDto.class);
            String uniqueId = response.getBody().getResult().get("UNIQUE_ID").toString();
            int result = memberRepository.insertMember(
                    memberRequest,
                    null,
                    2,
                    regSeq,
                    uniqueId
            );
            if (result > 0) {
                if (image != null && !image.isEmpty()) {
                    FileInfoDetailDto fileInfo = fileInfoService.attachFileInfoDevelop(image, "MEMBER", result, "Member", regSeq);
                    memberRequest.setMemberSeq(result);
                    memberRequest.setImageUrl("/Files/Fmcs/Member/" + fileInfo.getFileStoredName());
                    memberRepository.updateMember(memberRequest, regSeq);
                }
                //sendSyncUser
                msg = MngUtil.message("CM001"); //등록 되었습니다.
            }
        }

        return Result.<String>builder()
                .stateMessage(msg)
                .build();
    }

    @Transactional
    public Result<String> setStatus(int memSeq, int status, int modSeq) {
        String msg = "";

        MemberRDto memberInfo = memberRepository.getMemberBySeq(memSeq);
        if (memberInfo == null) {
            throw new CustomException("ER003");      //요청하신 정보가 잘못되었습니다.
        }
        if (memberInfo.getDongMngYn().equals("Y")) {
            throw new CustomException("MM015");      //동대표 회원입니다. \n동대표설정에서 취소 후에 변경이 가능합니다.
        }
        if (status == 1) {
            msg = MngUtil.message("MM001");    //대기처리 되었습니다.
        } else if (status == 2) {
            msg = MngUtil.message("MM011");    //승인 되었습니다.
        } else if (status == 3) {
            msg = MngUtil.message("MM009");    //미승인 되었습니다.
        } else if (status == 4) {
            msg = MngUtil.message("MM013");    //전출 되었습니다.
        } else {
            throw new CustomException("ER003");      //요청하신 정보가 잘못되었습니다.
        }

        memberRepository.updateMemberStatus(memSeq, status, modSeq);
        //sendSyncUser
        return Result.<String>builder()
                .stateMessage(msg)
                .build();
    }

    @Transactional
    public Result<Object> setMoveOuts(String memberSeqs, int regSeq) {

        List<Integer> memberSeqList = Arrays.stream(memberSeqs.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        //전출회원 동대표 설정 여부 확인
        int cnt = memberRepository.getMemberDongMngCntByArrSeq(memberSeqs);
        if (cnt > 0) {
            throw new CustomException("MM015");      //동대표 회원입니다. \n동대표설정에서 취소 후에 변경이 가능합니다.
        }

        try {
            memberSeqList.forEach(memberSeq -> {
                memberRepository.updateMemberStatus(
                        memberSeq, 4, regSeq
                );
                MemberRDto memberR = memberRepository.getMemberBySeq(memberSeq);

                //sendSyncUser(memberR)

                LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[전출 처리] [id=" + memberSeq + "]");
            });
            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[전출 등록] [등록 여부=true]");
        } catch (Exception e) {
            LogUtil.writeLog("XI_SP_FMCS_ADMIN_APT_MEMBER_STATUS_U", e.getMessage());
            throw new CustomException("MM014");      //정보가 잘못되었습니다. 관리자에게 문의하세요.
        }

        return Result.<Object>builder().build();
    }

    public Result<Object> sendSyncUser(MemberRDto memberR) {
        HttpUtil<SyncUserResponse> httpUtil = new HttpUtil<>(
                Define.APP_IF_URL + "/api/app/v1/myapt/syncUser",
                HttpMethod.POST
        );
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        httpUtil.addHeader("ApiKey", Define.API_KEY);
        httpUtil.addBody("UNIQUE_ID", memberR.getUniqueId());
        httpUtil.addBody("APT_SEQ", String.valueOf(memberR.getAptSeq()));
        httpUtil.addBody("DONG", String.valueOf(memberR.getAptDong()));
        httpUtil.addBody("HO", String.valueOf(memberR.getAptHo()));
        httpUtil.addBody("NAME", String.valueOf(memberR.getName()));
        httpUtil.addBody("PHONE", String.valueOf(memberR.getPhone()));
        httpUtil.addBody("HOUSEHOLDER_TYPE", String.valueOf(memberR.getHouseholderType()));
        httpUtil.addBody("RESIDENCE_TYPE", String.valueOf(memberR.getResidenceType()));
        httpUtil.addBody("APT_NAME", "");
        httpUtil.addBody("SEX", memberR.getSex());
        httpUtil.addBody("STATUS", String.valueOf(memberR.getStatus()));
        httpUtil.addBody("BIRTHDAY", memberR.getBirthday());
        httpUtil.addBody("RF_CARD_NO", memberR.getRfCardNo());
        httpUtil.addBody("PROFILE_IMG", memberR.getImageUrl());
        httpUtil.addBody("UPDATED", now);
        httpUtil.addBody("INSERTED", now);
        httpUtil.setRequestWithBody();
        String log = httpUtil.getBody();
        ResponseEntity<SyncUserResponse> response = httpUtil.getResponse(SyncUserResponse.class);


        return Result.<Object>builder().build();
    }

    //아파트 동대표 설정 초기화
    @Transactional
    public void resetAptDongRepresent(int aptSeq, int memberSeq, int regSeq) {
        try {
            aptRepository.updateAptDongRepresentCancel(
                    aptSeq,
                    memberSeq,
                    regSeq
            );
            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[아파트 동대표 설정 취소] [member seq=" + memberSeq + "]");
        } catch (Exception e) {
            LogUtil.writeLog("XI_SP_FMCS_ADMIN_APT_DONG_REPRESENT_CANCEL_U", e.getMessage());
            throw new CustomException("ER001");      //관리자에게 문의하세요.
        }
    }

    //동대표 취소
    @Transactional
    public Result<String> updateAptDongRepresentCancel(MngCancelRequestDto dongMngCancelRequest, int regSeq) {
        int aptSeq = dongMngCancelRequest.getAptSeq();
        int memberSeq = dongMngCancelRequest.getMemSeq();

        // 아파트 동대표 설정 초기화
        resetAptDongRepresent(aptSeq, memberSeq, regSeq);

        try {
            memberRepository.updateAptMemberDongMngCancel(memberSeq, aptSeq);
            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[회원 동대표 취소 처리] [mem seq =" + memberSeq + "]");
        } catch (Exception e) {
            LogUtil.writeLog("XI_SP_FMCS_ADMIN_APT_MEMBER_DONG_MNG_CANCEL_U", e.getMessage());
            throw new CustomException("ER001");      //관리자에게 문의하세요.
        }
        return Result.<String>builder().build();
    }

    //동대표 설정
    @Transactional
    public Result<String> setDongRepresent(DongMngSetRequestDto dongMngSetRequest, int regSeq) {
        int memberSeq = dongMngSetRequest.getMemberSeq();
        int aptSeq = dongMngSetRequest.getAptSeq();
        String[] aptDong = dongMngSetRequest.getAptDong();
        // 이미 동대표라면 설정된 정보는 전부 초기화
        if (dongMngSetRequest.getDongMngYn().equals("Y")) {
            resetAptDongRepresent(dongMngSetRequest.getAptSeq(), memberSeq, regSeq);
        }
        for(int i = 0; i < dongMngSetRequest.getAptDong().length; i++) {
            aptRepository.updatAptDongRepresent(
                    aptSeq,
                    aptDong[i],
                    memberSeq,
                    regSeq
            );
        }
        String dongs = String.join(",", aptDong);
        memberRepository.updateAptMemberDongMng(
                memberSeq,
                "Y",
                dongs,
                regSeq
        );

        return Result.<String>builder()
                .stateMessage("CM004")  //저장 되었습니다.
                .build();
    }

    // 회원정보 엑셀 다운로드
    public ResponseEntity<byte[]> excelDown(MemberListRequestDto memberListRequest, int status, String dongMngYn) throws Exception {
//    public void excelDown(MemberListRequestDto memberListRequest, int status, String dongMngYn) {
        LocalDateTime date = LocalDateTime.now();

        String sDate = memberListRequest.getStartDate();
        String eDate = memberListRequest.getEndDate();
        if (sDate == null || sDate.trim().equals("")) {
            memberListRequest.setStartDate(date.minusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString());
        }
        if (eDate == null || eDate.trim().equals("")) {
            memberListRequest.setEndDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString());
        }
        List<Map<String, Object>> datas = new ArrayList<>();
        List<MemberExcelDownDto> memberAllList = memberRepository.getMemberListAll(
                memberListRequest,
                status,
                dongMngYn
        );
        if (memberAllList.size() < 1) {
            throw new CustomException("MM016");      //데이터가 존재하지 않습니다.
        }
        // 내용 셋팅
        memberAllList.forEach(memberAll -> {
            Map<String, Object> data = new HashMap<>();
//            data.put("NAME",memberAllInterface.getName());
//            data.put("PHONE",memberAllInterface.getPhone());
            data.put("APT_DONG", memberAll.getAptDong());
            data.put("APT_HO", memberAll.getAptHo());
//            data.put("TEXT_SEX",memberAllInterface.getTextSex());
            data.put("NICKNAME", memberAll.getNickName());
//            data.put("BIRTHDAY",memberAllInterface.getBirthday());
            data.put("TEXT_RESIDENCE_TYPE", memberAll.getTextResidenceType());
            data.put("TEXT_HOUSEHOLDER_TYPE", memberAll.getTextHouseholderType());
            data.put("DONG_MNG_YN", memberAll.getDongMngYn());
            data.put("DONG_MNG_TITLE", memberAll.getDongMngTitle());
            datas.add(data);
        });

        // 헤더 셋팅 & 파일 셋팅
        String randomFolder = StringUtil.getRandomFolder(new SimpleDateFormat("yyyyMMdd_HHmmss_SSS"));
        String path = Define.EXCEL_FILE_PATH + "/Down/";
        if (memberAllList.size() > 0) {
            Map<String, String> columnMap = new HashMap<>();
//                columnMap.put("NAME", "이름");
//                columnMap.put("PHONE", "휴대전화");
            columnMap.put("APT_DONG", "동");
            columnMap.put("APT_HO", "호");
//                columnMap.put("TEXT_SEX", "성별");
            columnMap.put("NICKNAME", "닉네임");
//                columnMap.put("BIRTHDAY", "생년월일");
            columnMap.put("TEXT_RESIDENCE_TYPE", "거주형태");
            columnMap.put("TEXT_HOUSEHOLDER_TYPE", "구분");
            columnMap.put("DONG_MNG_YN", "동대표");
            columnMap.put("DONG_MNG_TITLE", "동대표 설정");

            ExcelUtil.createExcelFile(
                    path,
                    randomFolder,
                    ExcelUtil.setDataTable(0, datas, columnMap)
            );
        }

        return new FileUtil().fileDownload(path + randomFolder + "/", randomFolder + ".xlsx", randomFolder + ".xlsx");
    }

    public Result<Integer> getEmailCheck(String email) {
        int count = memberRepository.getEmailCheck(email);
        if(count == 0) {
            return Result.<Integer>builder()
                    .stateMessage(MngUtil.message("MM012")) //이메일을 확인할 수 없습니다.
                    .build();
        }
        return Result.<Integer>builder()
                .result(count)
                .build();
    }
}
