package com.xi.fmcs.domain.apt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminCompanyAptRequestDto {

    @Schema(description = "아파트(단지)명", required = true)
    private String aptName;

    @Schema(description = "단지코드", required = true)
    private String xiCode;

    @Schema(description = "지역 코드 (도)")
    private int addrDoType;

    @Schema(description = "주소", required = true)
    private String addr;

}
