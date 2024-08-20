package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VoteAnswerRequestDto {

    @Schema(description = "답변 고유번호")
    private int voteAnswerSeq;

    @Schema(description = "답변 순번", required = true)
    private int answerNo;

    @Schema(description = "답변 제목", required = true)
    private String answerTitle;

    @Schema(description = "답변 내용")
    private String answerContents;

    @Schema(description = "파일 고유번호")
    private int fileSeq;

}
