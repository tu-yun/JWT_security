package com.xi.fmcs.domain.member.model;

import lombok.Data;

import java.util.Map;

@Data
public class MemberUniqueIdResponseDto {
    private Map<String,Object> result;
    private String stateCode;
    private String stateMessage;
}