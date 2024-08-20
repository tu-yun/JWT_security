package com.xi.fmcs.domain.settings.service;

import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.domain.admin.model.AdminMemberDetailMinDto;
import com.xi.fmcs.domain.admin.model.AdminMemberDto;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.admin.repository.AdminMemberRepository;
import com.xi.fmcs.domain.admin.service.AdminMemberService;
import com.xi.fmcs.domain.adminCompany.repository.AdminCompanyRepository;
import com.xi.fmcs.domain.aptMng.repository.AptMngRepository;
import com.xi.fmcs.domain.member.model.MngCancelRequestDto;
import com.xi.fmcs.domain.settings.model.AptMngInfoDto;
import com.xi.fmcs.domain.settings.model.AptMngSaveRequestDto;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.support.model.ViewBag;
import com.xi.fmcs.support.util.MngUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final AdminMemberRepository adminMemberRepository;
    private final AdminMemberService adminMemberService;
    private final AdminCompanyRepository adminCompanyRepository;
    private final AptMngRepository aptMngRepository;

    // 관리자 목록
    public ResultWithBag<List<AdminMemberDetailMinDto>> getAdminMemberList(int aptSeq) {
        int totalCnt = 0;
        List<AdminMemberDetailMinDto> adminMember = adminMemberRepository.getAdminMemberList(aptSeq);
        if (adminMember != null) {
            totalCnt = adminMember.size();
        }
        return ResultWithBag.<List<AdminMemberDetailMinDto>>builder()
                .result(adminMember)
                .viewBag(ViewBag.builder()
                        .totalCount(totalCnt)
                        .build())
                .build();
    }

    //내단지 관리자 일괄 저장 과 APT 맵핑 (내단지 일반관리자 : 내단지관리자 수정은 내정보 휴대번호만 수정가능)
    @Transactional
    public Result<String> addAptMngSave(AptMngSaveRequestDto aptMngSaveRequest, AdminMemberLoginDto loginDto) {
        int companySeq = loginDto.getCompanySeq();
        int loginGradeType = loginDto.getGradeType();
        //GS 마스터 aptSeq 값으로 companySeq 값 가져오기
        if (loginGradeType == 1) {
            companySeq = adminCompanyRepository.getAdminCompanyByAptSeq(aptMngSaveRequest.getAptSeq()).getCompanySeq();
        }

        List<AptMngInfoDto> aptMngInfoDtoList = aptMngSaveRequest.getAptMngInfoList();
        //Validation
        for (AptMngInfoDto aptMngInfo : aptMngInfoDtoList) {
            if (aptMngInfo.getGradeType() < 3) throw new CustomException("ST002");    //관리자 권한 오류 입니다.
            //내단지 관리자(일반) 본인계정 수정 가능
            //시설,강사,입주자대표,선관위 등록,수정,삭제
            if (loginGradeType == 3 && loginDto.getCompanyYn().equals("N")) {
                //내단지관리자 일반 은 본인을 제외하고 등급 3 이상만 수정 삭제 가능
                if (loginDto.getSeq() != aptMngInfo.getSeq() && aptMngInfo.getGradeType() == 3) {
                    throw new CustomException("ST008"); //등록 수정 권한 오류 입니다.
                }
            }

            //새로운 관리자 이메일 유효성 체크
            if (aptMngInfo.getSeq() == 0) {
                adminMemberService.checkMobileNoAndEmailAreUsed(aptMngInfo.getMobileNo(), aptMngInfo.getEmail());
            } else {
                adminMemberService.checkMobileNoAndEmailAreUsedExceptMe(aptMngInfo.getSeq(), aptMngInfo.getMobileNo(), aptMngInfo.getEmail());
            }
        }

        adminMemberService.aptAdmMngSave(aptMngInfoDtoList, loginDto, companySeq, aptMngSaveRequest.getAptSeq());

        return Result.<String>builder()
                .stateMessage(MngUtil.message("ST003"))   //내단지 관리자 정보가 저장 되었습니다.
                .build();
    }

    //내단지 관리자 내정보 휴대번호만 수정
    @Transactional
    public Result<String> aptMngMyInfoSave(String mobileNo, AdminMemberLoginDto loginDto) {
        int seq = loginDto.getSeq();
        if (adminMemberRepository.getAdminMemberExceptMeMobileNoCheckBySeq(seq, mobileNo) == 1) {
            throw new CustomException("ST010"); //사용중인 휴대번호입니다.
        }
        adminMemberRepository.updateAdminMemberMobileNo(seq, mobileNo, seq);
        return Result.<String>builder()
                .stateMessage("CM003")  //수정 되었습니다.
                .build();
    }

    //대표 내단지 관리자 변경
    @Transactional
    public Result<String> changeAptMngCompanyYN(MngCancelRequestDto changeAptMngRequest, AdminMemberLoginDto loginDto) {
        int memSeq = changeAptMngRequest.getMemSeq();
        //권한 확인
        int gradeType = loginDto.getGradeType();
        if (gradeType == 3 && loginDto.getCompanyYn().equals("N")) {
            throw new CustomException("ST005"); //대표권한 변경 권한이 없습니다.
        }
        AdminMemberDetailMinDto adminMember = adminMemberRepository.getAdminMemberDetailBySeq(memSeq);

        //대상이 내단지 관리자인지 확인
        if (adminMember.getGradeType() != 3) {
            throw new CustomException("ST006"); //대표권한 변경 대상은 내단지 관리자만 가능합니다.
        }
        //(현재)업체 소속된 단지 대표관리자 찾기.
        AdminMemberDto prevMng = aptMngRepository.getAdminAptMngMemberInfoByXiCode(changeAptMngRequest.getAptSeq());
        int prevMngSeq = 0;
        if (prevMng != null) {
            prevMngSeq = prevMng.getSeq();
        }

        //대표 관리자 변경
        adminMemberRepository.updateAdminMemberCompanyYN(memSeq, prevMngSeq, loginDto.getSeq());

        return Result.<String>builder()
                .stateMessage("ST007")  //대표권한 변경 되었습니다.
                .build();
    }

    //단지 관리자 담당자 삭제
    @Transactional
    public Result<String> adminAptMngDel(MngCancelRequestDto aptMngDeleteRequest, AdminMemberLoginDto loginDto) {
        int gradeType = loginDto.getGradeType();
        int aptSeq = aptMngDeleteRequest.getAptSeq();
        int mngMemSeq = aptMngDeleteRequest.getMemSeq();

        if(adminMemberRepository.getAdminMemberCompanyYNCheckBySeq(mngMemSeq) == 1) {
            throw new CustomException("ST004");  //단지관리자 대표 계정은 삭제 권한이 없습니다.
        }

        if (gradeType == 3 && loginDto.getCompanyYn().equals("N")) {
            AdminMemberDetailMinDto adminMember = adminMemberRepository.getAdminMemberDetailBySeq(mngMemSeq);
            if (adminMember.getGradeType() <= 3) throw new CustomException("ST011");   //삭제 권한이 없습니다.
        }

        //aptSeq 삭제 대상 검토
        AdminMemberDetailMinDto adminMember = adminMemberRepository.getAdminMemberList(aptSeq)
                .stream()
                .filter(d -> d.getSeq() == mngMemSeq).findFirst().orElse(null);
        if(adminMember == null) {
            throw new CustomException("ST011");   //삭제 권한이 없습니다.
        }

        //관리자 삭제
        adminMemberRepository.deleteAdminMember(mngMemSeq, loginDto.getSeq());
        return Result.<String>builder()
                .stateMessage("CM002")  //삭제 되었습니다.
                .build();
    }
}
