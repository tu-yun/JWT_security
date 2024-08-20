package com.xi.fmcs.domain.vote.model;

import com.xi.fmcs.domain.apt.model.AptDongInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class VoteCreateResponseDto {

    @Schema(description = "투표 OR 설문 상세")
    private VoteDetailDto voteDetailDto;

    @Schema(description = "아파트 동 목록")
    private List<AptDongInfoDto> aptDongList;

    @Schema(description = "질문답변 목록")
    private List<VoteQuestionAnswerDto> voteQstList;
}
