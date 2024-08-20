package com.xi.fmcs.domain.community.model.boardA;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BoardASaveRequestDto {

    @Schema(description = "[수정]게시글 고유번호")
    private int seq;

    @Schema(description = "[신규등록]게시판번호(GROUP_BOARD 고유번호)")
    private int groupSeq;

    @Schema(description = "제목", required = true)
    private String title;

    @Schema(description = "내용", required = true)
    private String contents;

    @Schema(description = "상단고정유무", required = true)
    private String topYn;

    @Schema(description = "저장유무 (Y:저장 N:임시저장)", required = true)
    private String saveYn;

}
