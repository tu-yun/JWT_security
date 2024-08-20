package com.xi.fmcs.domain.file.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExcelFileInfoDto {

    private Integer fileSeq;

    private String fileOrgName;

    @Schema(description = "동 수")
    private Integer dongCnt;

    @Schema(description = "등록 세대수")
    private Integer householdCnt;

    @Schema(description = "등록자")
    private String adminName;

    @Schema(description = "등록일")
    private String regDate;

}
