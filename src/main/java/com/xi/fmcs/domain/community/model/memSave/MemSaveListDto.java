package com.xi.fmcs.domain.community.model.memSave;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class MemSaveListDto {

    @Schema(description = "번호")
    private int rowNum;

    @Schema(description = "게시글 고유번호")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int boardSeq;

    @Schema(description = "BOARD_GROUP 고유번호")
    private int groupSeq;

    @Schema(description = "작성자 닉네임")
    private String writerNicknm;

    @Schema(description = "작성자명")
    private String writerNm;

    @Schema(description = "동")
    private int dong;

    @Schema(description = "호")
    private int ho;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "등록일시")
    private String regDate;

    @Schema(description = "이동일시")
    private String modDate;

    @JsonIgnore
    private int modSeq;

    @Schema(description = "게시판타입")
    private String boardType;

    @Schema(description = "게시판명")
    private String boardNm;

    @Schema(description = "게시분류명")
    private String groupNm;

    @Schema(description = "처리자")
    private String moverNm;

    private int totalCnt;

    public void setSeq(int seq) {
        this.boardSeq = seq;
    }

    public int getSeq() {
        return boardSeq;
    }
}
