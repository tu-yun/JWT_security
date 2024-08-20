package com.xi.fmcs.domain.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminAPTManagerInsertRequestDto {

    @Schema(required = true)
    private int aptSeq;

    @Schema(required = true)
    private int companySeq;

    @Schema(description = "내단지관리 대표자명", required = true)
    private String name;

    @Schema(description = "내단지관리 이메일", required = true)
    private String email;

    @Schema(description = "내단지관리 휴대폰번호", required = true)
    private String mobileNo;
}
