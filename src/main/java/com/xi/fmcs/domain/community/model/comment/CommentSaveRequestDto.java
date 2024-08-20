package com.xi.fmcs.domain.community.model.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentSaveRequestDto {

    @Schema(description = "[수정]댓글 고유번호")
    private int seq;

    @Schema(description = "[신규]GROUP 고유번호")
    private int groupSeq;

    @Schema(description = "[신규]게시글 고유번호")
    private int boardSeq;

    @Schema(description = "[공통]내용", required = true)
    private String contents;

    @Schema(description = "[신규]댓글 레벨(최상위 : 1)", required = true)
    private int levelR;

    @Schema(description = "[신규]부모댓글 고유번호")
    private int parentSeq;
}
