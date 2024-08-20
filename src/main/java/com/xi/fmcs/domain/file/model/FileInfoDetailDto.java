package com.xi.fmcs.domain.file.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileInfoDetailDto {

    @Schema(description = "파일 고유번호")
    private int seq;

    private LocalDateTime regDate;

    private String attachTableName;

    private int attachTableSeq;

    private String fileInputName;

    private String filePath;

    private String fileOrgName;

    private String fileStoredName;

    private String fileSize;

    private int adminRegSeq;

    private int adminModSeq;

    private LocalDateTime modDate;
}
