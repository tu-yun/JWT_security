package com.xi.fmcs.domain.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeleteFileBySeqRequestDto {

    @Schema(description = "파일 고유번호")
    private int fileSeq;

    @Schema(description = "(커뮤니티쪽: 게시글 고유번호)")
    private int id;

    @Schema(description = "테이블명(투표답안쪽: VOTE_ANSWER, 커뮤니티쪽: BOARD_A, BOARD_B, BOARD_COMM 등등)")
    private String tableName;

}
