package com.xi.fmcs.domain.community.service;

import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.model.BoardCommonDeleteRequestDto;
import com.xi.fmcs.domain.community.model.boardE.BoardEListDto;
import com.xi.fmcs.domain.community.model.boardE.BoardEListRequestDto;
import com.xi.fmcs.domain.community.model.boardE.BoardESaveRequestDto;
import com.xi.fmcs.domain.community.repository.BoardERepository;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.support.model.ViewBag;
import com.xi.fmcs.support.util.MngUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardEService {

    private final BoardERepository boardERepository;

    // 한줄게시판 조회
    public ResultWithBag<List<BoardEListDto>> getBoardEList(BoardEListRequestDto boardEListRequest, int page, int pageSize) {
        int totalCnt = 0;
        page = page == 0 ? 1 : page;
        pageSize = pageSize == 0 ? 10 : pageSize;
        List<BoardEListDto> boardEList = boardERepository.getBoardEList(boardEListRequest, page, pageSize);
        if (boardEList != null && boardEList.size() > 0) {
            totalCnt = boardEList.get(0).getTotalcnt();
        }

        return ResultWithBag.<List<BoardEListDto>>builder()
                .result(boardEList)
                .viewBag(ViewBag.builder()
                        .currentPage(page)
                        .totalCount(totalCnt)
                        .pageSize(pageSize)
                        .build())
                .build();
    }

    @Transactional
    public Result<String> setBoardESave(BoardESaveRequestDto boardESaveRequest, AdminMemberLoginDto loginDto) {
        if (boardESaveRequest.getSeq() > 0) {
            boardERepository.updateBoardE(boardESaveRequest, loginDto);
        } else {
            boardERepository.insertBoardE(boardESaveRequest, loginDto);
        }
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM004")) //저장 되었습니다.
                .build();
    }

    @Transactional
    public Result<String> setBoardEDel(BoardCommonDeleteRequestDto boardCommonDeleteRequest, int regSeq) {
        boardERepository.delBoardD(boardCommonDeleteRequest, regSeq);
        return Result.<String>builder()
                .build();
    }
}
