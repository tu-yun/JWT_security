package com.xi.fmcs.domain.community.model.boardE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class BoardEListDto {

    @Schema(description = "번호")
    private int rowNum;

    @Schema(description = "BOARD_E 고유번호")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int boardESeq;

    @Schema(description = "BOARD_GROUP 고유번호")
    private int groupSeq;

    @Schema(description = "내용")
    private String contents;

    @Schema(description = "작성자 닉네임")
    private String writerNicknm;

    @Schema(description = "작성자명")
    private String writerNm;

    private String dong;

    private String ho;

    @Schema(description = "삭제 여부")
    private String delYn;

    @Schema(description = "관리자 여부")
    private String adminYn;

    @Schema(description = "등록일시")
    private String regDate;

    @Schema(description = "변경일시")
    private LocalDateTime modDate;

    @JsonIgnore
    private int regSeq;

    @JsonIgnore
    private int modSeq;

    @Schema(description = "첨부파일 고유번호")
    private int fileSeq;

    @Schema(description = "파일 경로")
    private String filePath;

    @Schema(description = "파일 저장 테이블명")
    private String attachTableName;

    @Schema(description = "파일명")
    private String fileStoredName;

    @Schema(description = "총 게시글 수")
    private int totalcnt;

    @Schema(description = "댓글갯수")
    private int commCnt;

    public void setSeq(int seq) {
        this.boardESeq = seq;
    }

    public int getSeq() {
        return boardESeq;
    }
}
