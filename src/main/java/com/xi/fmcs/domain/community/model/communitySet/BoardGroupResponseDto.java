package com.xi.fmcs.domain.community.model.communitySet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BoardGroupResponseDto {
    @Schema(description = "BOARD_GROUP 고유번호")
    private String seq;

    @Schema(description = "상위 BOARD_GROUP 고유번호")
    private int parentSeq;

    @Schema(description = "게시판 이름")
    private String boardNm;

    @Schema(description = "게시판 타입")
    private String boardType;

    @Schema(description = "게시판 레벨")
    private int boardLvl;

    @Schema(description = "순서??")
    private int ordNum;

    private String iconUrl;

    @Schema(description = "고정여부")
    private String fixYn;

    @Schema(description = "사용여부")
    private String useYn;
}
