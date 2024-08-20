package com.xi.fmcs.domain.community.model.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    @Schema(description = "BOARD_D 고유번호")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int commentSeq;

    @Schema(description = "내용")
    private String contents;

    @Schema(description = "작성자 닉네임")
    private String writerNickNm;

    @Schema(description = "작성자 이름")
    private String writerNm;

    private int dong;
    private int ho;

    @Schema(description = "댓글 레벨")
    private int levelR;

    @Schema(description = "상위 댓글 고유번호")
    private int parentSeq;

    @Schema(description = "게시글 고유번호")
    private int boardSeq;

    @Schema(description = "삭제여부")
    private String delYn;

    @Schema(description = "숨김여부(관리자만 보이도록)")
    private String adminHidYn;

    @Schema(description = "관리자여부")
    private String adminYn;

    @Schema(description = "작성일")
    private String regDate;

    @Schema(description = "수정일")
    private LocalDateTime modDate;

    @JsonIgnore
    private int regSeq;
    @JsonIgnore
    private int modSeq;

    @Schema(description = "파일 고유번호")
    private int fileSeq;

    @Schema(description = "파일 경로")
    private String filePath;

    @JsonIgnore
    private String attachTableName;

    @Schema(description = "파일명")
    private String fileStoredName;

    public void setSeq(int seq) {
        this.commentSeq = seq;
    }

    public int getSeq() {
        return commentSeq;
    }
}
