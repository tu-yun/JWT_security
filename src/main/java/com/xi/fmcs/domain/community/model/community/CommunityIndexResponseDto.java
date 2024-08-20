package com.xi.fmcs.domain.community.model.community;

import com.xi.fmcs.domain.apt.model.AptDongHoResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class CommunityIndexResponseDto {

    @Schema(description = "BOARD_GROUP 고유번호")
    private int groupSeq;

    @Schema(description = "게시분류명")
    private String groupNm;

    @Schema(description = "상위 게시 고유번호")
    private int parentSeq;

    @Schema(description = "상위게시분류명")
    private String parentNm;

    @Schema(description = "게시판 타입")
    private String boardType;

    @Schema(description = "동게시판 여부")
    private String dongBoardYn;

    @Schema(description = "게시판 글 고유번호(메인페이지)")
    private int boardSeq;

    @Schema(description = "(커뮤니티) 아파트 동호리스트")
    private List<AptDongHoResponseDto> aptDongHoList;

    @Schema(description = "단지별 게시판 그룹 목록")
    private List<BoardGroupAptListDto> boardGroupList;

}
