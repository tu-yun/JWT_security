package com.xi.fmcs.domain.member.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberWaitListRequestDto {

    @Schema(required = true, example = "32")
    private int aptSeq;

    @Schema(example = "401")
    private String dong;

    private String ho;

    @Schema(description = "회원정보 검색어")
    private String schVal;

    private int page;

    @Schema(example = "10")
    private int pageSize;
}
