package com.xi.fmcs.domain.vote.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VoteInfoSaveRequestDto {

    @Schema(description = "[공통] 투표/설문 고유번호")
    private int voteInfoSeq;

    @Schema(description = "[공통] 아파트 고유번호", example = "32", required = true)
    private int aptSeq;

    @Schema(description = "[공통] 유형 (1:투표, 2:설문)", example = "2", required = true)
    private int voteType;

    @Schema(description = "[공통] 제목", example = "테스트 제목입니다.", required = true)
    private String voteTitle;

    @Schema(description = "[공통] 내용", example = "테스트 내용입니다.", required = true)
    private String contents;

    @Schema(description = "[공통] 시작날짜", example = "2023-04-15", required = true)
    private String startDate;
    @Schema(description = "[공통] 시작시간(시)", example = "12", required = true)
    private String startHour;
    @Schema(description = "[공통] 시작시간(분)", example = "00", required = true)
    private String startMin;

    @Schema(description = "[공통] 종료날짜", example = "2023-05-01", required = true)
    private String endDate;
    @Schema(description = "[공통] 종료시간(시)", example = "17", required = true)
    private String endHour;
    @Schema(description = "[공통] 종료시간(분)", example = "30", required = true)
    private String endMin;

    @Schema(description = "[설문] 성별 (1:남자, 2:여자, 0:전체)", example = "0")
    private int voteGender;

    @Schema(description = "[설문] 연령대 (0:전체, 1:10대, 2:20대, 3:30대, 4:40대, 5:50대, 6:60대 이상)", example = "1,2,3,4,5")
    private String[] voteAge;

    @Schema(description = "[설문] 대상(동) 예시 : ALL 또는 동 문자배열", example = "ALL")
    private String[] dongStr;

}
