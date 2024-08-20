package com.xi.fmcs.domain.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

@Data
public class VoteSaveFileRequestDto {

    @NotNull(message = "파일 저장에 실패했습니다.")
    @Schema(description = "테이블명(투표답안쪽: VOTE_ANSWER, 커뮤니티쪽: BOARD_A, BOARD_B, BOARD_COMM 등등)")
    private String tableName;

    @NotNull(message = "파일 저장에 실패했습니다.")
    @Schema(description = "폴더명(투표답안쪽: VOTE, 커뮤니티쪽: BOARD_B와 BOARD_D는 MYAPT_COMMUNITY, 그 외에는 테이블명과 동일)")
    private String filePath;

    @NotNull(message = "파일 저장에 실패했습니다.")
    @Schema(description = "파일 정보")
    private List<VoteSaveFileDto> voteSaveFileList;

}
