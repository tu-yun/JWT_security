package com.xi.fmcs.domain.community.model.communitySet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardDisplaySetDto {

    @Schema(required = true)
    private int aptSeq;

    @Schema(description = "동,호수 노출여부 (Y/N)", required = true)
    private String boardDongHoDisp;

    @Schema(description = "사용자명 노출여부 실 DB 저장값 (0,1,2) - 기준 확인필요", required = true)
    private String boardUserNameDisp;

}
