package com.xi.fmcs.domain.settings.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class AptMngInfoDto {

    @Schema(description = "관리자 고유번호")
    private int seq;

    @Min(value = 3, message = "")
    @Max(value = 7, message = "")
    @Schema(description = "권한", required = true)
    private int gradeType;

//    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "아이디(이메일 주소)를 확인해주세요.")
    @Email(message = "아이디(이메일 주소)를 확인해주세요.")
    @NotNull(message = "아이디를 입력하세요.")
    @Schema(description = "아이디(이메일)", required = true)
    private String email;

    @NotNull(message = "이름을 입력하세요.")
    @Schema(description = "이름", required = true)
    private String name;

    @NotNull(message = "게시물 등록자명을 입력하세요.")
    @Schema(description = "닉네임", required = true)
    private String nickName;

    @Schema(description = "대표유무", required = true)
    private String companyYn;

    private String telNo;

    @NotNull(message = "휴대전화를 입력하세요.")
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "휴대전화의 양식과 맞지 않습니다.")
    @Schema(description = "휴대전화", required = true)
    private String mobileNo;

}
