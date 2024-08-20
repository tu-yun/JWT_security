package com.xi.fmcs.domain.adminCompany.service;

import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.support.model.ViewBag;
import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.domain.admin.model.AdminMemberAddDto;
import com.xi.fmcs.domain.admin.model.AdminMemberDetailDto;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.admin.repository.AdminMemberRepository;
import com.xi.fmcs.domain.admin.service.AdminMemberService;
import com.xi.fmcs.domain.adminCompany.model.AdminCompanyCURequestDto;
import com.xi.fmcs.domain.adminCompany.model.AdminCompanyDetailDto;
import com.xi.fmcs.domain.adminCompany.model.AdminCompanyListDto;
import com.xi.fmcs.domain.adminCompany.repository.AdminCompanyRepository;
import com.xi.fmcs.domain.file.model.FileInfoDetailDto;
import com.xi.fmcs.domain.file.repository.FileInfoRepository;
import com.xi.fmcs.domain.file.service.FileInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminCompanyService {

    private final AdminCompanyRepository adminCompanyRepository;
    private final AdminMemberRepository adminMemberRepository;
    private final AdminMemberService adminMemberService;
    private final FileInfoService fileInfoService;
    private final FileInfoRepository fileInfoRepository;

    //관리자 관리 목록
    public Result<List<AdminCompanyListDto>> selectAdminCompanyList(String keyword, int page, int pSize, String useYn) {
        List<AdminCompanyListDto> adminCompanyList = adminCompanyRepository.selectAdminCompanyList(keyword, (page == 0 ? 1 : page), pSize, useYn);
        return Result.<List<AdminCompanyListDto>>builder()
                .result(adminCompanyList)
                .build();
    }

    //업체 추가 팝업
    @Transactional
    public Result<String> addAdminCompany(AdminCompanyCURequestDto adminCompanyCU, MultipartFile uploadFile, AdminMemberLoginDto loginDto) {
        int result = -1;
        //휴대전화, 이메일 중복체크
        adminMemberService.checkMobileNoAndEmailAreUsed(adminCompanyCU.getMobileNo(), adminCompanyCU.getEmail());
        if (adminCompanyRepository.getAdminCompanyNoCheck(adminCompanyCU.getCompanyNo()) != 0) {
            throw new CustomException("MP008");	  //이미 등록된 사업자 번호입니다. \n사업자 번호를 확인하세요.
        }
        
        result = adminCompanyRepository.insertAdminCompany(
                adminCompanyCU.getCompany(),
                adminCompanyCU.getCompanyPresident(),
                adminCompanyCU.getCompanyFaxNo(),
                adminCompanyCU.getCompanyTelNo(),
                adminCompanyCU.getCompanyNo(),
                loginDto.getSeq()
        );

        String gradeType = "2";
        String companyYn = "Y";
        String nickname = null;
        result = adminMemberService.insertAdminMember(
        		adminCompanyCU.getEmail(),
        		adminCompanyCU.getName(),
        		gradeType,
        		adminCompanyCU.getPosition(),
        		adminCompanyCU.getDepartment(),
        		companyYn,
        		adminCompanyCU.getTelNo(),
        		adminCompanyCU.getMobileNo(),
        		result,
        		loginDto.getSeq(),
        		nickname);
        fileInfoService.attachFileInfoDevelop(uploadFile, "COMPANY_LOGO", result, "company_logo", loginDto.getSeq());
        return Result.<String>builder().build();
    }

    //업체상세
    public ResultWithBag<AdminCompanyDetailDto> adminCompanyDetail(int seq, String search, int page) throws Exception {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("mainNowPage", page);
        searchMap.put("mainSearchText", search);

        ViewBag viewBag = new ViewBag();

        AdminCompanyDetailDto adminCompany = adminCompanyRepository.adminCompanyDetail(seq);
        if(adminCompany != null) {
	        int pageSize = 10;
	        int currentPage = 1;
	        int tCnt = 0;
	
	        adminCompany.setAdminMemberDetailList(adminMemberRepository.getAdminCompanyMemberListBySeq(seq, currentPage, pageSize));
	
	        if (adminCompany.getAdminMemberDetailList() != null && adminCompany.getAdminMemberDetailList().size() > 0) {
	            tCnt = adminCompany.getAdminMemberDetailList().get(0).getTotalcnt();
	            viewBag.setTotalCount(tCnt);
	            viewBag.setCurrentPage(currentPage);
	            viewBag.setPageSize(pageSize);
	            viewBag.setSearch(searchMap);
	        } else {
	            viewBag.setTotalCount(0);
	            viewBag.setCurrentPage(1);
	            viewBag.setPageSize(10);
	            viewBag.setSearch(searchMap);
	        }
        }
        return ResultWithBag.<AdminCompanyDetailDto>builder()
                .result(adminCompany)
                .viewBag(viewBag)
                .build();
    }

    //업체 직원목록
    public ResultWithBag<List<AdminMemberDetailDto>> adminCompanyMemberList(int seq, int page, int pageSize) {
        List<AdminMemberDetailDto> companyMemberList =
                adminMemberService.selectAdminCompanyMemberByCompanySeq(seq, page, pageSize);
        ViewBag viewBag = ViewBag.builder()
                .totalCount(companyMemberList.get(0).getTotalcnt())
                .currentPage(page)
                .pageSize(pageSize)
                .build();
        return ResultWithBag.<List<AdminMemberDetailDto>>builder()
                .result(companyMemberList)
                .viewBag(viewBag)
                .build();
    }

    //삭제
    @Transactional
    public Result<String> deleteAdminCompany(int seq, AdminMemberLoginDto loginDto) {
        if (loginDto.getGradeType() == 2 && loginDto.getCompanyYn().equals("N")) {
            throw new AccessDeniedException("");
        }

        int result = adminCompanyRepository.deleteAdminCompany(seq, loginDto.getSeq());
        
        if (result == 2) {
            throw new CustomException("MP001");	  //단지정보가 있는 업체는 삭제가 불가합니다.
        } else if (result == 1) {
            fileInfoRepository.deleteFileInfo(seq, "uploadFile", "COMPANY_LOGO");
            return Result.<String>builder().build();
        } else {
            throw new CustomException("ER001");	  //관리자에게 문의하세요.
        }
    }

    //업체 담당자 등록
    @Transactional
    public Result<String> addAdminMember(AdminMemberAddDto aminMemberAddDto) {
        adminMemberService.checkEmailIsUsed(aminMemberAddDto.getEmail());

        //최드 업체 등록시에 추가 하는 관리자는 대표관리자
        //업체 상세에서 등록 하는 관리자는 대표 관리자가 아니다.
        String companyYn = "N";
        String gradeType = "2"; 	//2 : 업체관리자
        String nickname = null;

        int adminSeq = adminMemberService.insertAdminMember(
        		aminMemberAddDto.getEmail(), 
        		aminMemberAddDto.getName(), 
        		gradeType, 
        		aminMemberAddDto.getPosition(),
        		aminMemberAddDto.getDepartment(), 
                companyYn, 
                aminMemberAddDto.getTelNo(), 
                aminMemberAddDto.getMobileNo(), 
                aminMemberAddDto.getCompanySeq(), 
                aminMemberAddDto.getRegSeq(), 
                nickname);
        if (adminSeq <= 0) {
            throw new CustomException("ER001");	  //관리자에게 문의하세요.
        }
        return Result.<String>builder().build();
    }


    // 슈퍼관리자 상세 (위 회사 상세정보와 같은 문제가 있음)!!!!!!!!!!!!수정해야함
    public Result<Object> getAdminCompanyBySeq(int seq) {
        AdminCompanyDetailDto companyInfo = adminCompanyRepository.adminCompanyDetail(seq);
        if (companyInfo != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("result", companyInfo);
            // 회사 로고
            List<FileInfoDetailDto> fileInfoList =
                    fileInfoRepository.getFileInfoV2("COMPANY_LOGO", seq, "uploadFile");
            if(fileInfoList.size()> 0) {
                map.put("imgLogoInfo", fileInfoList);
            }
            // 해당 회사 대표관리자
            map.put(
                    "representativeManager",
                    adminMemberService.getAdminMemberComYnByCompanySeq(seq, "2"));
            return Result.<Object>builder()
                    .result(map)
                    .build();
        }
        return Result.<Object>builder()
                .build();
    }

    //업체 수정
    @Transactional
    public Result<String> editAdminCompany(AdminCompanyCURequestDto adminCompanyInfo, MultipartFile file, AdminMemberLoginDto loginDto) {
        if (loginDto.getGradeType().equals(2) && loginDto.getCompanyYn().equals("N")) {
            throw new AccessDeniedException("");
        }
        //해당 업체 대표관리자 SEQ 찾기
        int adminMemSeq = adminCompanyInfo.getAdminMemberSeq();
        if (adminCompanyInfo.getCompanySeq() == 0 || adminMemSeq == 0) {
            throw new CustomException("MP009");	  //정보 오류 입니다.
        }
        //휴대전화 이메일 중복체크
        adminMemberService.checkMobileNoAndEmailAreUsedExceptMe(
                adminCompanyInfo.getAdminMemberSeq(), adminCompanyInfo.getMobileNo(), adminCompanyInfo.getEmail());

        if (adminCompanyRepository.getAdminCompanyExceptMeCompanyNoCheckBySeq(
                adminCompanyInfo.getCompanySeq(), adminCompanyInfo.getCompanyNo()) == 1) {
            throw new CustomException("MP008");	  //이미 등록된 사업자 번호입니다. \n사업자 번호를 확인하세요.
        }

        int regSeq = loginDto.getSeq();
        adminCompanyInfo.setAdminMemberSeq(adminMemSeq);
        int result = adminMemberService.updateAdminMemberAll(adminCompanyInfo, regSeq);
        if (result > 0) {
            adminCompanyRepository.updateAdminCompany(
                    adminCompanyInfo.getCompanySeq(),
                    adminCompanyInfo.getCompany(),
                    adminCompanyInfo.getCompanyPresident(),
                    adminCompanyInfo.getCompanyFaxNo(),
                    adminCompanyInfo.getCompanyTelNo(),
                    adminCompanyInfo.getCompanyNo(),
                    regSeq
            );
            if (result > 0) {
                fileInfoService.attachFileInfoDevelop(
                        file,
                        "COMPANY_LOGO",
                        adminCompanyInfo.getCompanySeq(),
                        "company_logo",
                        regSeq);
                return Result.<String>builder().build();
            }
        }
        throw new CustomException("ER001");	  //관리자에게 문의하세요.
    }
}
