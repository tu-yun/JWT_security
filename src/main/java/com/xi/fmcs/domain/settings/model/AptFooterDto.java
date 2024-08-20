package com.xi.fmcs.domain.settings.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AptFooterDto {

    @Schema(description = "APT_FOOTER 고유번호")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int aptFooterSeq;

    @Schema(description = "아파트 고유번호")
    private int aptSeq;

    @Schema(description = "아파트명")
    private String aptName;

    @Schema(description = "세대수")
    private String footerHouseholdCnt;

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

    @Schema(description = "경도")
    private String aptX;

    @Schema(description = "위도")
    private String aptY;

    public void setSeq(int seq) {
        this.aptFooterSeq = seq;
    }

    public int getSeq() {
        return aptFooterSeq;
    }
}
