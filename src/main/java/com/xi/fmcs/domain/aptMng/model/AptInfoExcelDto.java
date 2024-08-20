package com.xi.fmcs.domain.aptMng.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AptInfoExcelDto {

    @Schema(description = "동", example = "0101")
    private String dong;
    @Schema(description = "아파트 층", example = "01")
    private String floor;
    @Schema(description = "룸타입", example = "84A")
    private String roomtypecd;
    @Schema(description = "호라인", example = "01")
    private String line;
    @Schema(description = "최고층", example = "35")
    private String maxfloor;
    @Schema(description = "최고라인", example = "4")
    private Integer maxline;

}
