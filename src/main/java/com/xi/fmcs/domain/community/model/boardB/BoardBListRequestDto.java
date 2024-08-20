package com.xi.fmcs.domain.community.model.boardB;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BoardBListRequestDto {

    @Schema(description = "BOARD_GROUP 고유번호", required = true)
    private int groupSeq;

    @Schema(description = "[동게시판의 경우] 게시판")
    private String srchDongBrd;

    @Schema(description = "동")
    private String srchDong;

    @Schema(description = "호")
    private String srchHo;

    @Schema(description = "검색")
    private String srchTxt;

    @Schema(description = "등록일(시작날짜)")
    private String srchStartDt;

    @Schema(description = "등록일(끝날짜)")
    private String srchEndDt;
}
