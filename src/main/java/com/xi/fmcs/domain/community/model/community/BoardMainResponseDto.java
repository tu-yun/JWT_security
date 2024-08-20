package com.xi.fmcs.domain.community.model.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class BoardMainResponseDto {

    @Schema(description = "공공게시판")
    private List<BoardMainListDto> boardListA;

    @Schema(description = "동게시판")
    private List<BoardMainListDto> boardListB;

    @Schema(description = "질문게시판")
    private List<BoardMainListDto> boardListD;
}
