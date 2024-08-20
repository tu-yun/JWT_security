package com.xi.fmcs.domain.moveReserve.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoveTimeSetDto {

    @Schema(description = "MOVE_TIME_SET 고유번호")
    private int seq;

    private int aptSeq;

    private int orderNum;

    private String userView;

    private String adminView;

    private String startTime;

    private String endTime;

    @JsonIgnore
    private int regSeq;

    @JsonIgnore
    private String regDate;

}
