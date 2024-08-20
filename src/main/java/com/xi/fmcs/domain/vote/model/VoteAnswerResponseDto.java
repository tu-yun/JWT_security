package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VoteAnswerResponseDto {

    @Schema(description = "답안 고유번호")
    private int voteAnswerSeq;

    @Schema(description = "답안 순서")
    private int answerNo;
}
