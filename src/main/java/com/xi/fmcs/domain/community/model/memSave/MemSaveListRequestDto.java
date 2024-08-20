package com.xi.fmcs.domain.community.model.memSave;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemSaveListRequestDto {

    @Schema(description = "아파트 고유번호")
    private int aptSeq;

    @Schema(description = "동", required = true, example = "ALL")
    private String srchDong;

    @Schema(description = "호", required = true, example = "ALL")
    private String srchHo;

    @Schema(description = "검색")
    private String srchTxt;

    @Schema(description = "등록일(시작날짜)")
    private String srchStartDt;

    @Schema(description = "등록일(끝날짜)")
    private String srchEndDt;
}
