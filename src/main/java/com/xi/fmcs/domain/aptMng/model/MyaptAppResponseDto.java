package com.xi.fmcs.domain.aptMng.model;

import com.xi.fmcs.domain.apt.model.AdminCompanyAptRequestDto;
import lombok.Data;

import java.util.List;

@Data
public class MyaptAppResponseDto {

    private List<AdminCompanyAptRequestDto> result;
    private String stateCode;
    private String stateMessage;
}
