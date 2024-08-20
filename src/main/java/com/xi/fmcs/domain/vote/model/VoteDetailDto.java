package com.xi.fmcs.domain.vote.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VoteDetailDto {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int voteInfoSeq;

    public int getSeq() {
        return voteInfoSeq;
    }
    public void setSeq(int seq) {
        voteInfoSeq = seq;
    }

    private int aptSeq;

    @Schema(description = "유형 (1:투표, 2:설문)")
    private int voteType;

    @Schema(description = "제목")
    private String voteTitle;

    public String getTitle() {
        return voteTitle;
    }
    public void setTitle(String title) {
        voteTitle = title;
    }

    @Schema(description = "내용")
    private String contents;

    @Schema(description = "성별 (1:남자, 2:여자, 0:전체) : 왜 처음에 -1값을 넘기는지 확인 필요")
    private int voteGender;

    @Schema(description = "회원투표가능 기준일(만 18세 기준일)")
    private String voteMemberAgeDate;

    @Schema(description = "멤버승인 기준일")
    private String voteMemberAuthDate;

    @Schema(description = "투표인명부 생성일")
    private String voteMakeDate;

    @JsonIgnore
    private int regSeq;

    @Schema(description = "투표 또는 설문 생성날짜")
    private LocalDateTime regDate;

    @JsonIgnore
    private int modSeq;

    @JsonIgnore
    private LocalDateTime modDate;

    @Schema(description = "거주대상(회원) (1:자가, 2:임대, 3:소유주(미거주))")
    private String voteResidence; // DB에 nvarchar(100)으로 되어있음

    @Schema(description = "연령대 (0:전체, 1:10대, 2:20대, 3:30대, 4:40대, 5:50대, 6:60대 이상)")
    private String voteAge;

    @Schema(description = "대상(동)")
    private String dongStr;

    private int memCnt;

    private int resultRate;

    @Schema(description = "진행상황")
    private String voteStatus;

    @Schema(description = "시작날짜")
    private String sDate;
    @Schema(description = "시작날짜(날짜만)")
    private String startDate;
    @Schema(description = "시작시간(시)")
    private String startHour;
    @Schema(description = "시작시간(분)")
    private String startMin;

    @Schema(description = "종료날짜")
    private String eDate;
    @Schema(description = "종료날짜(날짜만)")
    private String endDate;
    @Schema(description = "종료시간(시)")
    private String endHour;
    @Schema(description = "종료시간(분)")
    private String endMin;

    public void setStartDate(String startDate) {
        sDate = startDate;
        this.startDate = startDate.substring(0, 10);
        startHour = startDate.substring(11, 13);
        startMin = startDate.substring(14);
    }
    public void setEndDate(String endDate) {
        eDate = endDate;
        this.endDate = endDate.substring(0, 10);
        endHour = endDate.substring(11, 13);
        endMin = endDate.substring(14);
    }
}
