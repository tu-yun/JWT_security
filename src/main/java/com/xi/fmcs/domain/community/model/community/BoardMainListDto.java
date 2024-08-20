package com.xi.fmcs.domain.community.model.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BoardMainListDto {

    @Schema(description = "BOARD_GROUP 고유번호")
    private int groupSeq;

    @Schema(description = "상위 BOARD_GROUP 고유번호")
    private int parentGroupSeq;

    @Schema(description = "게시판 이름")
    private String boardNm;

    @Schema(description = "게시글 고유번호")
    private int seq;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String contents;

    @Schema(description = "작성자명")
    private String writerNm;

    @Schema(description = "작성자 닉네임")
    private String writerNicknm;

    @Schema(description = "등록일")
    private String regDate;
}
