package com.xi.fmcs.domain.community.model.boardA;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BoardAListRequestDto {

    @Schema(description = "BOARD_GROUP 고유번호", required = true)
    private int groupSeq;

    @Schema(description = "저장유무 (Y:저장(default), N:임시저장)")
    private String saveYn;

    @Schema(description = "검색")
    private String srchTxt;

    @Schema(description = "등록일(시작날짜)")
    private String srchStartDt;

    @Schema(description = "등록일(끝날짜)")
    private String srchEndDt;
}
