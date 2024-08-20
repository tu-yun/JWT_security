package com.xi.fmcs.models.sendAuth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendAuth {
    public Integer rowNum;

    public Integer seq;
    public String regDate;
    public Integer sendType;
    public String sendMsg;

    public String sendInfo;
    public Integer requestAuthType;
    public Integer memberSeq;

    public String pwd;
}
