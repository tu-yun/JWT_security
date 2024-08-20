package com.xi.fmcs.domain.aptMng.model;

import lombok.Data;

import java.util.List;

@Data
public class AptInfoExcelResponseDto {
    private List<AptInfoExcelDto> result;
    private String stateCode;
    private String stateMessage;
}
