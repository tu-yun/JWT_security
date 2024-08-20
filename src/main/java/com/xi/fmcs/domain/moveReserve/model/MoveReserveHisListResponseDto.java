package com.xi.fmcs.domain.moveReserve.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MoveReserveHisListResponseDto {

    private int rowNum;

    private int resHisSeq;

    private String dong;

    private String ho;

    @Schema(description = "신청자 이름(이사 당사자)/ 확인필요")
    private String name;

    @Schema(description = "등록자 이름")
    private String regName;

    @Schema(description = "등록자 연락처")
    private String regTel;

    @Schema(description = "이사일시(날짜)")
    private String moveDate;

    @Schema(description = "이사일시(시간)")
    private String moveTime;

    @Schema(description = "이사타입")
    private int moveType;

    private String isAdmin;

    @Schema(description = "삭제일")
    private String delDate;

    private String delFlag;

    private int totalCnt;

}
