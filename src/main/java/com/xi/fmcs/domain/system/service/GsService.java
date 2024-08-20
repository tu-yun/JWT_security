package com.xi.fmcs.domain.system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.config.security.configuration.CustomPasswordEncoder;
import com.xi.fmcs.domain.admin.repository.AdminMemberRepository;
import com.xi.fmcs.domain.system.model.GsAdminMemberAddDto;
import com.xi.fmcs.domain.system.model.GsAdminMemberDetailDto;
import com.xi.fmcs.domain.system.model.GsAdminMemberDto;
import com.xi.fmcs.domain.system.repository.GsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GsService {

	private final GsRepository gsRepository;
	private final AdminMemberRepository adminMemberRepository;
	private final CustomPasswordEncoder pwdEncoder;
	
	//시스템 GS 마스터 조회
	public Result<List<GsAdminMemberDto>> getAdminGSMemberList(){
		List<GsAdminMemberDto> gsAdminMemberList = gsRepository.getAdminGSMemberList();
		for(GsAdminMemberDto gsAdminMemberDto : gsAdminMemberList) {
			gsAdminMemberDto.setPwd("[PROTECTED]");
		}
		return Result.<List<GsAdminMemberDto>>builder()
				.result(gsAdminMemberList)
				.build();
	}
	
	//GS 마스터 상세
	public Result<GsAdminMemberDetailDto> getAdminMemberDetailBySeq(int seq){
		return Result.<GsAdminMemberDetailDto>builder()
				.result(gsRepository.getAdminMemberDetailBySeq(seq))
				.build();
	}
	
	//GS 마스터 등록
	@Transactional
	public Result<Object> insertAdminMember(GsAdminMemberAddDto adminGSMemberAddDto, Integer seq, Integer companySeq){
		if(adminMemberRepository.getAdminMemberCheckByEmail(adminGSMemberAddDto.getEmail()) == 1){
			throw new CustomException("SY005");	  //사용중인 이메일 주소입니다.
		} else if(adminMemberRepository.getAdminMemberCheckByMobile(adminGSMemberAddDto.getMobileNo()) == 1){
			throw new CustomException("SY006");	  //사용중인 휴대번호입니다.
		}
		
	    String email = adminGSMemberAddDto.getEmail();
	    String encPwd = "";
	    
        if (email != null && email.indexOf("@") > -1) {
            encPwd = email.split("@")[0].toString() + "123!";
        } else {
            encPwd = email + "123!";
        }
        encPwd = pwdEncoder.encode(encPwd);
		
        //최드 업체 GS마스터 관리자는 DB 에서 직접 Insert COMPANY_YN = Y 대표관리자 수정 삭제 불가
        String gradeType = "1";
        String companyYn = "N";
        String nickname = "단지관리자";
        
		int admResult = adminMemberRepository.insertAdminMember(
				adminGSMemberAddDto.getEmail(), 
				encPwd, 
				adminGSMemberAddDto.getName(), 
				gradeType, 
				adminGSMemberAddDto.getPosition(), 
				adminGSMemberAddDto.getDepartment(), 
				companyYn, 
				adminGSMemberAddDto.getTelNo(), 
				adminGSMemberAddDto.getMobileNo(), 
				companySeq, 
				seq,
				nickname);

        if (admResult < 0) {
        	throw new CustomException("ER001");	  //관리자에게 문의하세요.
        }
        return Result.<Object>builder().build();
	}
	
}
