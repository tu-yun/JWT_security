package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class VoteAnswerDelRequestDto {

    @Positive(message = "")
    @Schema(description = "투표/설문 고유번호", required = true)
    private int voteInfoSeq;

    @Positive(message = "")
    @Schema(description = "답안 고유번호", required = true)
    private int ansSeq;
}
