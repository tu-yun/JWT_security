package com.xi.fmcs.domain.login.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AdminMemberLoginReqDto {

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "비밀번호")
    private String pwd;
    
}
