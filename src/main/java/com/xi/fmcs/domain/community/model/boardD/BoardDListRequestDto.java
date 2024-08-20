package com.xi.fmcs.domain.community.model.boardD;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BoardDListRequestDto {

    @Schema(description = "BOARD_GROUP 고유번호", required = true)
    private int groupSeq;

    @Schema(description = "동", required = true)
    private String srchDong;

    @Schema(description = "호", required = true)
    private String srchHo;

    @Schema(description = "검색")
    private String srchTxt;

    @Schema(description = "등록일(시작날짜)")
    private String srchStartDt;

    @Schema(description = "등록일(끝날짜)")
    private String srchEndDt;

    @Schema(description = "답변상태???")
    private String srchReplySt;
}
