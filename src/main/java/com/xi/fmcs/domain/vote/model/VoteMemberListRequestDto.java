package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VoteMemberListRequestDto {

    @Schema(description = "투표/설문 고유번호", required = true)
    private int voteInfoSeq;

    @Schema(description = "유형 (0:투표인명부, 1:투표참여자, 2:설문참여자)", required = true)
    private int voteDiv;
}
