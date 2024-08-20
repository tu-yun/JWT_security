package com.xi.fmcs.domain.settings.model;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class AptMngSaveRequestDto {

    @Valid
    private List<AptMngInfoDto> aptMngInfoList;

    private int aptSeq;
}
