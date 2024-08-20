package com.xi.fmcs.domain.community.model.boardB;

import com.xi.fmcs.domain.file.model.FileInfoDetailDto;
import lombok.Data;

import java.util.List;

@Data
public class BoardBResponseDto {

    private BoardBDto boardB;

    private List<FileInfoDetailDto> fileInfoDetailList;

    private List<String> dongList;
}
