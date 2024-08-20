package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class VoteResultResponseDto {

    @Schema(description = "거주형태")
    private String voteResidenceText;

    @Schema(description = "연령")
    private String voteAgeText;

    @Schema(description = "투표/설문 총 참여인원")
    private int voteTotalCnt;

    @Schema(description = "질문유형")
    private int ansType;

    @Schema(description = "투표/설문 상세")
    private VoteDetailDto voteDetail;

    @Schema(description = "질문/답변 목록")
    private List<VoteQuestionAnswerDto> voteQAList;

    // 이하 voteQAList의 0번째 (최다 득표)
    @Schema(description = "질문제목")
    private String questionTitle;

    @Schema(description = "투표/설문 답변유형 TEXT")
    private String answerTypeNm;

    @Schema(description = "투표 답안 항목유형 TEXT")
    private String answerItemTypeValNm;

    @Schema(description = "최다 득표  ANSWER_NO")
    private int topAnswerNo;
}
