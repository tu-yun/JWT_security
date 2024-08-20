package com.xi.fmcs.domain.apt.model;

import com.xi.fmcs.domain.admin.model.AdminMemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AptDetailResponseDto {

    @Schema(description = "아파트 상세")
    private AptDetailDto aptDetail;

    @Schema(description = "내단지 관리자(대표)")
    private AdminMemberDto adminMemberDto;

    @Schema(description = "동 상세")
    private List<AptDongInfoDto> dongInfoList;

}
