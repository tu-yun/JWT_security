package com.xi.fmcs.domain.community.model.boardA;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class BoardAListDto {
    private String topOrd;

    @Schema(description = "번호")
    private int rowNum;

    @Schema(description = "BOARD_A 고유번호")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int boardASeq;

    @Schema(description = "작성자 닉네임")
    private String writerNicknm;

    @Schema(description = "작성자명")
    private String writerName;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String contents;

    @Schema(description = "조회수")
    private int vCnt;

    @Schema(description = "상단고정유무")
    private String topYn;

    @Schema(description = "저장유무 (Y:저장 N:임시저장)")
    private String saveYn;

    @JsonIgnore
    private int regSeq;

    @Schema(description = "등록일시")
    private String regDate;

    @Schema(description = "변경일시")
    private LocalDateTime modDate;

    @Schema(description = "댓글수")
    private int commCnt;

    private int totalCnt;

    public void setSeq(int seq) {
        this.boardASeq = seq;
    }

    public int getSeq() {
        return boardASeq;
    }
}
