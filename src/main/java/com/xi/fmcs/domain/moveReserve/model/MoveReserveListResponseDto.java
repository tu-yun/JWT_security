package com.xi.fmcs.domain.moveReserve.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MoveReserveListResponseDto {

    private int rowNum;

    @Schema(description = "MOVE_RESERVE 고유번호")
    private int seq;

    private String dong;

    private String ho;

    @Schema(description = "신청자 이름(이사 당사자)/ 확인필요")
    private String name;

    @Schema(description = "신청자 닉네임(이사 당사자)/ 확인필요")
    private String nickname;

    @Schema(description = "등록자 이름")
    private String regName;

    @Schema(description = "등록자 연락처")
    private String regTel;

    @Schema(description = "이사일시(날짜)")
    private String moveDate;

    @Schema(description = "이사일시(시간)")
    private String moveTime;

    @Schema(description = "이사방법 (1:엘리베이터 2:엘리베이터,사다리 교차 가능 3.엘리베이터,사다리 둘 중 하나)")
    private int moveType;

    @Schema(description = "등록일")
    private String regDate;

    private String isAdmin;

    private int totalCnt;

}
