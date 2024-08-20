package com.xi.fmcs.domain.community.model.boardB;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class BoardBListDto {

    @Schema(description = "번호")
    private int rowNum;

    @Schema(description = "BOARD_B 고유번호")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int boardBSeq;

    @Schema(description = "작성자명")
    private String writerNm;

    private String dong;

    private String ho;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String contents;

    @Schema(description = "조회수")
    private int vCnt;

    @Schema(description = "상단고정유무")
    private String topYn;

    @JsonIgnore
    private int regSeq;

    @Schema(description = "등록일시")
    private String regDate;

    @Schema(description = "변경일시")
    private LocalDateTime modDate;

    @Schema(description = "댓글수")
    private int commCnt;

    private int totalCnt;

    @Schema(description = "대상동 텍스트")
    private String dongStr;

    public void setSeq(int seq) {
        this.boardBSeq = seq;
    }

    public int getSeq() {
        return boardBSeq;
    }
}
