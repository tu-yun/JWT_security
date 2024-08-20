package com.xi.fmcs.domain.community.model.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
public class CommunityIndexRequestDto {

    @PositiveOrZero
    @Schema(description = "상위 그룹 고유번호")
    private int grpParentSeq;

    @PositiveOrZero
    @Schema(description = "게시판그룹 고유번호")
    private int grpSeq;

    @Positive
    @Schema(description = "아파트 고유번호")
    private int aptSeq;

    @PositiveOrZero
    @Schema(description = "게시글 고유번호")
    private int boardSeq;
}
