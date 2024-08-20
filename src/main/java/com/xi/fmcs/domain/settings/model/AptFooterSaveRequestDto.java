package com.xi.fmcs.domain.settings.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class AptFooterSaveRequestDto {

    @Schema(description = "[수정] APT_FOOTER 고유번호")
    private int seq;

    @Positive
    @Schema(description = "APT 고유번호", required = true)
    private int aptSeq;

    @Schema(description = "관리사무소 전화번호")
    private String footerTelNo;

    @Schema(description = "관리사무소 팩스번호")
    private String footerFaxNo;

    @Schema(description = "지번 주소")
    private String footerJibun;

    @Schema(description = "도로명 주소")
    private String footerRoad;

    @Schema(description = "기타 추가사항")
    private String footerContent;

    @Schema(description = "업무시간")
    private String footerHours;

    @Positive
    @Schema(description = "세대수")
    private int footerHouseholdCnt;

    @Schema(description = "경도")
    private String aptX;

    @Schema(description = "위도")
    private String aptY;
}
