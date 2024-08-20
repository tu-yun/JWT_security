package com.xi.fmcs.domain.aptMng.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AptInfoDto extends AptInfoExcelDto{

    private Integer seq;
    private Integer aptSeq;
    private Integer elvtrNo;
    private Integer moveType;

}