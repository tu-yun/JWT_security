package com.xi.fmcs.domain.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommunitySaveFileRequestDto {
    @Schema(description = "테이블명(투표답안쪽: VOTE_ANSWER, 커뮤니티쪽: BOARD_A, BOARD_B, BOARD_COMM 등등)")
    private String tableName;

    @Schema(description = "게시글 고유번호")
    private int id;

    @Schema(description = "폴더명(투표답안쪽: VOTE, 커뮤니티쪽: BOARD_B와 BOARD_D는 MYAPT_COMMUNITY, 그 외에는 테이블명과 동일)")
    private String filePath;

    @Schema(description = "게시글 고유번호")
    private int status;

}
