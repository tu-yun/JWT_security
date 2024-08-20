package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class VoteDetailPartial1ResponseDto {
    private VoteDetailDto voteDetail;

    private List<VoteQuestionAnswerDto> voteQstList;

    @Schema(description = "총 투표수")
    private int voteTotalCnt;
}
