package com.xi.fmcs.domain.community.model.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentSHRequestDto {

    @Schema(description = "댓글 고유번호", required = true)
    private int seq;

    @Schema(description = "숨김여부", required = true)
    private String hidYn;
}
