package com.xi.fmcs.domain.member.model;

import lombok.Data;

import java.util.List;

@Data
public class SyncUserResponse {
    private List<MemberUniqueIdResponseDto> responseList;
}
