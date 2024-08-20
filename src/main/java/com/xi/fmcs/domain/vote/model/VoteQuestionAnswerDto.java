package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VoteQuestionAnswerDto {

    @Schema(description = "질문 고유번호")
    private int voteQuestionSeq;

    @Schema(description = "질문 제목")
    private String questionTitle;

    @Schema(description = "답변유형")
    private int answerType;

    @Schema(description = "중복선택 (1이면 단일선택)")
    private int voteChoiceCnt;

    @Schema(description = "항목유형")
    private int answerItemType;

    @Schema(description = "답변 고유번호")
    private int voteAnswerSeq;

    @Schema(description = "답변 순번")
    private int answerNo;

    @Schema(description = "답변 제목")
    private String answerTitle;

    @Schema(description = "답변 내용")
    private String answerContents;

    @Schema(description = "투표 개수")
    private int voteCnt;

    @Schema(description = "파일 고유번호")
    private int fileSeq;

    @Schema(description = "파일 경로")
    private String filePath;

    @Schema(description = "파일명")
    private String fileStoredName;

}
