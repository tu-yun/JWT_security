package com.xi.fmcs.domain.member.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class DongMngSetRequestDto {

    private int aptSeq;

    private int memberSeq;

    private String[] aptDong;

    @Schema(description = "해당 회원 동대표 유무", example = "Y", required = true)
    private String dongMngYn;


}
