package com.xi.fmcs.domain.community.model.boardB;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * BOARD_B : 일반게시판/동게시판
 */
@Data
public class BoardBDto {
    @Schema(description = "BOARD_B 고유번호")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int boardBSeq;

    @Schema(description = "관리자유무")
    private String adminYn;

    @Schema(description = "작성자 닉네임")
    private String writerNicknm;

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

    @JsonIgnore
    private int regSeq;

    @Schema(description = "등록일시")
    private String regDate;

    @Schema(description = "변경일시")
    private String modDate;

    private String dongStr;

    public void setSeq(int seq) {
        this.boardBSeq = seq;
    }

    public int getSeq() {
        return boardBSeq;
    }
}
