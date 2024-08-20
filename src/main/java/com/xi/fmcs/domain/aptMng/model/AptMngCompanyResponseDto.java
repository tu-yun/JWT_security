package com.xi.fmcs.domain.aptMng.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AptMngCompanyResponseDto {

    @Schema(description = "현재 내단지를 담당하고 있는 회사(만약 USE_YN이 'N'이라면 값이 없을 수 있음)")
    AptMngCompanyDto nowAptMngCompany;

    @Schema(description = "현재 내단지를 담당하고 있는 회사를 제외한 회사목록")
    List<AptMngCompanyDto> aptMngCompanyList;

    public AptMngCompanyResponseDto(AptMngCompanyDto nowAptMngCompany, List<AptMngCompanyDto> aptMngCompanyList){
        this.nowAptMngCompany = nowAptMngCompany;
        this.aptMngCompanyList = aptMngCompanyList;
    }
}
