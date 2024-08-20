package com.xi.fmcs.domain.member.model;

import lombok.Data;

@Data
public class MemberSyncResponseDto {

    private MemberRDto result;
    private String stateCode;
    private String stateMessage;
}
