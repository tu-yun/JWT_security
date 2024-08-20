package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VoteListRequestDto {

    @Schema(required = true)
    private int aptSeq;

    @Schema(description = "제목")
    private String searchText;

    @Schema(description = "유형 (0:전체, 1:투표, 2:설문)")
    private int searchType;

    @Schema(description = "기간(시작)")
    private String searchStartDate;

    @Schema(description = "기간(끝)")
    private String searchEndDate;

    @Schema(description = "진행상황(ALL:전체,1:대기, 2:진행중, 3:종료)", required = true)
    private String searchSt;
}
