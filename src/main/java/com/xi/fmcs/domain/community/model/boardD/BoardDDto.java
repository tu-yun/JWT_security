package com.xi.fmcs.domain.community.model.boardD;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class BoardDDto {

    @Schema(description = "BOARD_D 고유번호")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int boardDSeq;

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

    private String answer;

    @Schema(description = "조회수")
    private int vCnt;

    @Schema(description = "등록일시")
    private String regDate;

    @Schema(description = "변경일시")
    private String modDate;

    @Schema(description = "답변 등록일시")
    private String ansRegDate;

    @Schema(description = "답변 변경일시")
    private LocalDateTime ansModDate;

    @JsonIgnore
    private int ansRegSeq;

    @Schema(description = "답변 - 관리자 이름")
    private int ansName;

    @Schema(description = "답변 - 관리자 닉네임")
    private int ansNickname;

    public void setSeq(int seq) {
        this.boardDSeq = seq;
    }

    public int getSeq() {
        return boardDSeq;
    }
}
