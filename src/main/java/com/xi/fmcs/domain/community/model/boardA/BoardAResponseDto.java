package com.xi.fmcs.domain.community.model.boardA;

import com.xi.fmcs.domain.file.model.FileInfoDetailDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class BoardAResponseDto {

    private BoardADto boardA;

    private List<FileInfoDetailDto> fileInfoDetailList;

    @Schema(description = "푸시 발송 용 동 리스트")
    private List<String> dongList;
}
