package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VoteResultRequestDto {

    @Schema(required = true)
    private int voteInfoSeq;

    @Schema(description = "질문유형(객관식인지, 주관식인지)", required = true)
    private int ansType;

    @Schema(required = true)
    private int voteQuestionSeq;
}
