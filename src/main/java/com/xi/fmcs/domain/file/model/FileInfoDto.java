package com.xi.fmcs.domain.file.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileInfoDto {

    @Schema(description = "파일 고유번호")
    private int seq;

    private LocalDateTime regDate;

    //등록 테이블 명
    private String attachTableName;

    //등록 테이블 순번
    private int attachTableSeq;

    //등록 type="file" name
    private String fileInputName;

    //등록 폴더
    private String filePath;

    //본 파일명
    private String fileOrgName;

    //저장 파일명
    private String storedName;

    //파일 사이즈
    private String fileSize;

}
