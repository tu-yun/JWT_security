package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VoteListResponseDto {
    private int rowNum;
    @Schema(description = "VOTE_INFO 고유번호")
    private int seq;
    private int voteType;
    private String title;
    private String contents;
    private String nickName;
    private String dongStr;
    private String startDate;
    private String endDate;
    private String voteSt;
    private String voteMakeDate;
    private int totalCnt;
}
