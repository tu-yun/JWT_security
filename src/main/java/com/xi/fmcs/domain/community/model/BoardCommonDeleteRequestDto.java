package com.xi.fmcs.domain.community.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BoardCommonDeleteRequestDto {

    @Schema(description = "게시글 고유번호", required = true)
    private int seq;

    @Schema(description = "회원글보관함 이동 : M, 삭제 : Y", required = true)
    private String delType;
}