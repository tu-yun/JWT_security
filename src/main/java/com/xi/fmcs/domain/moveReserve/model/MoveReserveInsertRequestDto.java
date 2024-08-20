package com.xi.fmcs.domain.moveReserve.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MoveReserveInsertRequestDto {

    @Schema(required = true)
    private int aptSeq;

    @Schema(required = true)
    private String dong;

    @Schema(required = true)
    private String ho;

    @Schema(required = true, description = "엘레베이터 번호")
    private int evNo;

    @Schema(required = true, description = "이사날짜")
    private String moveDate;

    @Schema(required = true, description = "이사시간 seq")
    private int aptMoveTimeSetSeq;

    @Schema(required = true, description = "이사방법(1:엘리베이터, 2:사다리)")
    private int moveType;

    @Schema(required = true, description = "이사방법(1:엘리베이터,2:엘리베이터,사다리 교차 가능,3:엘리베이터,사다리 둘중 하나만 가능)")
    private int moveInfoMoveType;

    @Schema(required = true, description = "등록자 이름")
    private String regName;

    @Schema(description = "관리자유무")
    private String isAdmin;

    @Schema(required = true, description = "등록자 전화번호")
    private String regTel;

}
