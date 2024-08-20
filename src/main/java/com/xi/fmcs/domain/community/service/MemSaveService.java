package com.xi.fmcs.domain.community.service;

import com.xi.fmcs.domain.apt.model.AptDongHoResponseDto;
import com.xi.fmcs.domain.community.model.memSave.MemSaveDetailResponseDto;
import com.xi.fmcs.domain.community.model.memSave.MemSaveListDto;
import com.xi.fmcs.domain.community.model.memSave.MemSaveListRequestDto;
import com.xi.fmcs.domain.community.repository.CommunityRepository;
import com.xi.fmcs.domain.community.repository.CommunitySetRepository;
import com.xi.fmcs.domain.community.repository.MemSaveRepository;
import com.xi.fmcs.domain.file.repository.FileInfoRepository;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.support.model.ViewBag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemSaveService {
    private final CommunitySetRepository communitySetRepository;
    private final CommunityRepository communityRepository;
    private final MemSaveRepository memSaveRepository;
    private final FileInfoRepository fileInfoRepository;

    @Transactional(readOnly = true)
    public Result<List<AptDongHoResponseDto>> getAptDongHoList(int aptSeq) {
        List<AptDongHoResponseDto> aptDongHoList =
                communityRepository.getAptDongHoList(aptSeq);
        return Result.<List<AptDongHoResponseDto>>builder()
                .result(aptDongHoList)
                .build();
    }

    //회원글보관함 목록
    public ResultWithBag<List<MemSaveListDto>> getBoardMemSaveList(MemSaveListRequestDto memSaveListRequest, int page) {
        int totalCnt = 0;
        page = page == 0 ? 1 : page;
        List<MemSaveListDto> memSaveList = memSaveRepository.getBoardMemSaveList(memSaveListRequest, page);
        if (memSaveList != null && memSaveList.size() > 0) {
            totalCnt = memSaveList.get(0).getTotalCnt();
        }

        return ResultWithBag.<List<MemSaveListDto>>builder()
                .result(memSaveList)
                .viewBag(ViewBag.builder()
                        .currentPage(page)
                        .totalCount(totalCnt)
                        .pageSize(10)
                        .build())
                .build();
    }

    //회원글보관함 상세
    public Result<MemSaveDetailResponseDto> getBoardMemSaveDetail(int seq, String boardType) {
        MemSaveDetailResponseDto memSaveDetail = memSaveRepository.getBoardMemSaveDetail(seq, boardType);
        memSaveDetail.setFileInfoList(fileInfoRepository.getFileInfo("BOARD_" + boardType, seq));
        return Result.<MemSaveDetailResponseDto>builder()
                .result(memSaveDetail)
                .build();
    }
}
