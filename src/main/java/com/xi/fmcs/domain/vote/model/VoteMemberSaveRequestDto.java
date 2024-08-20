package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VoteMemberSaveRequestDto {

    @Schema(description = "아파트 고유번호", required = true)
    private int aptSeq;

    @Schema(description = "투표/설문 고유번호", required = true)
    private int voteInfoSeq;

    @Schema(description = "승인기준일", required = true)
    private String voteMemberAuthDate;

    @Schema(description = "만 18세 기준일", required = true)
    private String voteMemberAgeDate;

    @Schema(description = "대상(회원) 거주유형 (1: 자가, 2:임대, 3:소유주)", required = true)
    private String[] voteResidenceArr;

    @Schema(description = "대상(동) (전체: ALL, 또는 동 문자배열로)", required = true)
    private String[] dong;
}
