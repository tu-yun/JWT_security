package com.xi.fmcs.domain.common.service;

import com.xi.fmcs.domain.common.model.DeleteFileBySeqRequestDto;
import com.xi.fmcs.domain.common.model.VoteSaveFileRequestDto;
import com.xi.fmcs.domain.file.repository.FileInfoRepository;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.util.MngUtil;
import com.xi.fmcs.domain.common.model.CommunitySaveFileRequestDto;
import com.xi.fmcs.domain.file.service.FileInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final FileInfoService fileInfoService;
    private final FileInfoRepository fileInfoRepository;

    //커뮤니티
    @Transactional
    public Result<String> SetFileSave2(List<MultipartFile> files, CommunitySaveFileRequestDto communitySaveFileRequest, int regSeq) {

        fileInfoService.attachFileDevelop2(files, communitySaveFileRequest, regSeq);
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM004")) //저장 되었습니다.
                .build();
    }

    //투표
    @Transactional
    public Result<String> SetFileSave3(VoteSaveFileRequestDto voteSaveFileRequest, int regSeq) {

        fileInfoService.attachFileDevelop3(voteSaveFileRequest, regSeq);
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM004")) //저장 되었습니다.
                .build();
    }

    //커뮤니티(동적멀티첨부) 파일삭제
    @Transactional
    public Result<String> setFileDeleteSeq(DeleteFileBySeqRequestDto deleteFileBySeqRequest) {
        fileInfoRepository.deleteFileInfoBySeq(
                deleteFileBySeqRequest.getFileSeq(),
                deleteFileBySeqRequest.getId(),
                deleteFileBySeqRequest.getTableName());
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM005")) //파일이 삭제 되었습니다.
                .build();
    }
}
