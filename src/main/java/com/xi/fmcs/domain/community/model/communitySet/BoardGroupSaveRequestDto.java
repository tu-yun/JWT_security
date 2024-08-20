package com.xi.fmcs.domain.community.model.communitySet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BoardGroupSaveRequestDto {

    @Schema(description = "(수정시)고유번호")
    public int seq;

    @Schema(description = "(신규등록시)아파트고유번호", required = true)
    public int aptSeq;

    @Schema(description = "(신규등록시)상위번호", hidden = true)
    public int parentSeq;

    @Schema(description = "(공통)게시판명")
    public String boardNm;

    @Schema(description = "(공통)게시판유형(A:공공게시판, B:일반/동게시판, D:질문답변게시판, E:한줄게시판, V:투표)")
    public String boardType;

    @Schema(description = "(신규등록시)게시판 레벨")
    public int boardLvl;

    @Schema(description = "(공통)순서")
    public int ordNum;

    @Schema(description = "(공통)아이콘 URL")
    public String iconUrl;

    @Schema(description = "(신규등록시)목록 고정여부(관리자)")
    public String fixYn;

    @Schema(description = "(공통)사용여부(Y/N) (프론트 - Y 사용)", required = true)
    public String useYn;

}
