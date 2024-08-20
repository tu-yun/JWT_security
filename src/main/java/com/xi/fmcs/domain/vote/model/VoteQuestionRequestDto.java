package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class VoteQuestionRequestDto {

    @Schema(description = "투표/설문 고유번호", required = true)
    private int voteInfoSeq;

    @Schema(description = "VOTE_QUESTION 고유번호")
    private int seq;

    @Schema(description = "질문 제목", required = true, example = "주관식 질문입니다.")
    private String questionTitle;

    @Schema(description = "답변유형(1:객관식, 2:주관식)", required = true, example = "2")
    private int answerType;

    @Schema(description = "중복선택 (1이면 단일선택, 주관식은 값 없이)")
    private int voteChoiceCnt;

    @Schema(description = "항목유형")
    private int answerItemType;

    @Schema(description = "답안 리스트")
    private List<VoteAnswerRequestDto> answers;
}