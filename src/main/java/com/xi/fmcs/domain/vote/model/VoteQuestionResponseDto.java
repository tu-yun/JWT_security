package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VoteQuestionResponseDto {

    @Schema(description = "번호")
    private int rowNum;

    @Schema(description = "VOTE_QUESTION 고유번호")
    private int seq;

    @Schema(description = "투표/설문 고유번호")
    private int voteInfoSeq;

    @Schema(description = "질문 제목")
    private String questionTitle;

    @Schema(description = "답변유형(1:객관식, 2:주관식)")
    private int answerType;

    @Schema(description = "중복선택")
    private int voteChoiceCnt;

    @Schema(description = "항목유형")
    private int answerItemType;

    @Schema(description = "명부 생성일")
    private LocalDateTime regDate;
}
