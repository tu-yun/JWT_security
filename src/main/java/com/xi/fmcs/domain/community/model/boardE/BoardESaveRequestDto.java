package com.xi.fmcs.domain.community.model.boardE;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BoardESaveRequestDto {

    @Schema(description = "[수정]게시글 고유번호")
    private int seq;

    @Schema(description = "[신규]BOARD_GROUP 고유번호")
    private int groupSeq;

    @Schema(description = "[공통]답변 내용", required = true)
    private String contents;
}
