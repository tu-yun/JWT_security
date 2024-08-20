package com.xi.fmcs.domain.community.model.boardA;

import com.xi.fmcs.domain.file.model.FileInfoDetailDto;
import lombok.Data;

import java.util.List;

@Data
public class BoardADetailResponseDto {

    private BoardADto boardA;

    private List<FileInfoDetailDto> fileInfoDetailList;

    private String saveYn;
}
