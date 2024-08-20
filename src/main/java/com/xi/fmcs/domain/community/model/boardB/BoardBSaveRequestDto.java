package com.xi.fmcs.domain.community.model.boardB;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BoardBSaveRequestDto {

    @Schema(description = "[수정]게시글 고유번호")
    private int seq;

    @Schema(description = "[신규등록]게시판번호(GROUP_BOARD 고유번호)")
    private int groupSeq;

    @Schema(description = "[신규등록]게시판 유형", required = true)
    private String boardType;

    @Schema(description = "제목", required = true)
    private String title;

    @Schema(description = "내용", required = true)
    private String contents;

    @Schema(description = "[동게시판] 대상 동 배열")
    private String[] dongStr;
}
