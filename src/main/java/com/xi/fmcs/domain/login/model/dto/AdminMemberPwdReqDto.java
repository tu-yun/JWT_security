package com.xi.fmcs.domain.login.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminMemberPwdReqDto {

    @Schema(description = "현재 비밀번호")
    private String pwd;

    @Schema(description = "새 비밀번호")
    private String newPwd;
    
}
