package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class VoteMemberResponseDto {
    private List<VoteMemberDto> voteMemberList;

    @Schema(description = "명부 생성일")
    private String voteMakeDate;

    @Schema(description = "승인회원 기준일")
    private String voteMemberAuthDate;

    @Schema(description = "만18세 기준일")
    private String voteMemberAgeDate;

    @Schema(description = "대상(회원)")
    private String voteResidence;

    private int totalCnt;
}
