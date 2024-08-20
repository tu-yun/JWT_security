package com.xi.fmcs.domain.community.model.boardD;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BoardDSaveRequestDto {

    @Schema(description = "게시글 고유번호", required = true)
    private int seq;

    @Schema(description = "답변 내용", required = true)
    private String answer;
}
