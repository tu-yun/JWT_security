package com.xi.fmcs.domain.community.model.boardD;

import com.xi.fmcs.domain.file.model.FileInfoDetailDto;
import lombok.Data;

import java.util.List;

@Data
public class BoardDResponseDto {

    private BoardDDto boardD;

    private List<FileInfoDetailDto> fileInfoDetailList;

}

