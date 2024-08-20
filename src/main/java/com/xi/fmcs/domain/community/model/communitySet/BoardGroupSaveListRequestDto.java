package com.xi.fmcs.domain.community.model.communitySet;

import lombok.Data;

import java.util.List;

@Data
public class BoardGroupSaveListRequestDto {
    List<BoardGroupSaveRequestDto> boardGroupSaveRequestList;
}
