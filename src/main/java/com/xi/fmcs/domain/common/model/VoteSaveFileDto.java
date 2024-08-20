package com.xi.fmcs.domain.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VoteSaveFileDto {

    @Schema(description = "답변 고유번호")
    private int ansId;

    @Schema(description = "첨부파일")
    private MultipartFile file;

}
