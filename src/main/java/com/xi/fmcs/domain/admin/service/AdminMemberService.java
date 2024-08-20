package com.xi.fmcs.domain.admin.service;

import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.config.security.configuration.CustomPasswordEncoder;
import com.xi.fmcs.domain.admin.model.AdminMemberDetailDto;
import com.xi.fmcs.domain.admin.model.AdminMemberDto;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.admin.repository.AdminMemberRepository;
import com.xi.fmcs.domain.adminCompany.model.AdminCompanyCURequestDto;

import com.xi.fmcs.domain.settings.model.AptMngInfoDto;
import com.xi.fmcs.support.util.LogUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final AdminMemberRepository adminMemberRepository;

    @Transactional
    public int insertAdminMember(
            String email, 
            String name, 
            String gradeType, 
            String position,
            String department, 
            String companyYn, 
            String telNo, 
            String mobileNo, 
            int companySeq, 
            int regSeq, 
            String nickname) {
        String encPwd = "";
        CustomPasswordEncoder pwdEncoder = new CustomPasswordEncoder();
        if (email != null && email.indexOf("@") > -1) {
            encPwd = email.split("@")[0].toString() + "123!";
        } else {
            encPwd = email + "123!";
        }
        encPwd = pwdEncoder.encode(encPwd);

        int result = adminMemberRepository.insertAdminMember(
        		email, encPwd, name, gradeType, position, department, companyYn, telNo, mobileNo, companySeq, regSeq, nickname);
        return result;
    }

    @Transactional
    public int updateAdminMemberAll(AdminCompanyCURequestDto adminMember, int regSeq) {
        int result = 0;
        try {
            adminMemberRepository.updateAdminMemberAll(
                    adminMember.getAdminMemberSeq(),
                    adminMember.getEmail(),
                    adminMember.getName(),
                    adminMember.getPosition(),
                    adminMember.getDepartment(),
                    adminMember.getTelNo(),
                    adminMember.getMobileNo(),
                    regSeq,
                    adminMember.getNickName()
            );
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<AdminMemberDetailDto> selectAdminCompanyMemberByCompanySeq(int seq, int page, int pageSize) {
        return adminMemberRepository.getAdminCompanyMemberListBySeq(seq, page, pageSize);
    }
    
    //관리자 업체 대표관리자 조회
    public AdminMemberDto getAdminMemberComYnByCompanySeq(int seq, String gradeType) {
        return adminMemberRepository.getAdminMemberComYnByCompanySeq(seq, gradeType);
    }

    public void checkEmailIsUsed(String email) {
        int result = adminMemberRepository.getAdminMemberCheckByEmail(email); //같은 값 존재하면 1 없으면 0
        if (result == 1) {
        	throw new CustomException("MA005");	  //사용중인 이메일 주소입니다.
        }
    }

    public void checkEmailIsUsedExceptMe(int seq, String email) {
        int result = adminMemberRepository.getAdminMemberExceptMeEmailCheckBySeq(seq, email);
        if (result == 1) {
            throw new CustomException("MA005");	  //사용중인 이메일 주소입니다.
        }
    }

    public void checkMobileNoAndEmailAreUsed(String mobileNo, String email) {
        int result = adminMemberRepository.getAdminMemberCheckByMobile(mobileNo);
        if (result == 0) {
            checkEmailIsUsed(email);
        } else {
        	throw new CustomException("MA006");	  //사용중인 휴대번호입니다.
        }
    }

    public void checkMobileNoAndEmailAreUsedExceptMe(int adminMemberSeq, String mobileNo, String email) {
        int result = adminMemberRepository.getAdminMemberExceptMeMobileNoCheckBySeq(adminMemberSeq, mobileNo);
        if (result == 0) {
            checkEmailIsUsedExceptMe(adminMemberSeq, email);
        } else {
        	throw new CustomException("MA006");	  //사용중인 휴대번호입니다.
        }
    }

    @Transactional
    public void aptAdmMngSave(List<AptMngInfoDto> aptMngInfoDtoList, AdminMemberLoginDto loginDto, int companySeq, int aptSeq) {
        int regSeq = loginDto.getSeq();
        String companyYn = loginDto.getCompanyYn();
        for(AptMngInfoDto item : aptMngInfoDtoList) {
            int seq = item.getSeq();
            if(seq == 0) {
                adminMemberRepository.insertAptAdmin(item, companySeq, regSeq, aptSeq);
            } else {
                if(seq == regSeq && companyYn.equals("Y")) {
                    adminMemberRepository.updateAdminMemberMobileNo(seq, item.getMobileNo(), regSeq);
                } else {
                    adminMemberRepository.updateAptAdmin(item, regSeq);
                }
            }
        }
        LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[내단지 관리자 등록 및 수정] [업체=" + companySeq + ", 등록 여부=Y]");
    }
}
