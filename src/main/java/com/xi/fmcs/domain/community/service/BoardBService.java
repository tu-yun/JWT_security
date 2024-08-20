package com.xi.fmcs.domain.community.service;

import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.model.BoardCommonDeleteRequestDto;
import com.xi.fmcs.domain.community.model.boardB.*;
import com.xi.fmcs.domain.community.repository.CommunityRepository;
import com.xi.fmcs.domain.community.repository.BoardBRepository;
import com.xi.fmcs.domain.file.repository.FileInfoRepository;
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
public class BoardBService {

    private final CommunityRepository communityRepository;
    private final FileInfoRepository fileInfoRepository;
    private final BoardBRepository boardBRepository;

    //공공게시판 목록
    public ResultWithBag<List<BoardBListDto>> boadBListPartial(BoardBListRequestDto boardBListRequest, int page) {
        page = page == 0 ? 1 : page;
        int totalCnt = 0;

        List<BoardBListDto> boardBList = boardBRepository.getBoardBList(boardBListRequest, page);
        if (boardBList != null && boardBList.size() > 0) {
            totalCnt = boardBList.get(0).getTotalCnt();
        }
        return ResultWithBag.<List<BoardBListDto>>builder()
                .result(boardBList)
                .viewBag(ViewBag.builder()
                        .currentPage(page)
                        .pageSize(10)
                        .totalCount(totalCnt)
                        .build())
                .build();
    }

    //공공게시판 등록/수정 창
    public Result<BoardBResponseDto> boardBCreatePartial(int seq, int groupSeq, AdminMemberLoginDto loginDto) {
        BoardBResponseDto boardBResponse = new BoardBResponseDto();
        BoardBDto boardB = new BoardBDto();
        if(seq == 0) {
            boardB.setWriterNicknm(loginDto.getNickname());
            boardB.setWriterNm(loginDto.getName());
        } else {
            boardB = boardBRepository.getBoardBDetail(seq);
            boardBResponse.setFileInfoDetailList(fileInfoRepository.getFileInfo("BOARD_B", seq));
        }
        boardBResponse.setBoardB(boardB);

        //푸시발송 동리스트(동게시판)
        boardBResponse.setDongList(communityRepository.getBoardAptDongList(groupSeq));

        return Result.<BoardBResponseDto>builder()
                .result(boardBResponse)
                .build();
    }

    //일반/동 게시판 상세
    public Result<BoardBResponseDto> boardBDetailPartial(int seq) {
        BoardBResponseDto boardBDetailResponse = new BoardBResponseDto();
        boardBDetailResponse.setBoardB(boardBRepository.getBoardBDetail(seq));
        boardBDetailResponse.setFileInfoDetailList(fileInfoRepository.getFileInfo("BOARD_B", seq));

        return Result.<BoardBResponseDto>builder().build();
    }

    //일반/동 게시판 저장
    @Transactional
    public Result<String> setBoardBSave(BoardBSaveRequestDto boardBSaveRequest, AdminMemberLoginDto loginDto) {
        int seq = boardBSaveRequest.getSeq();
        if(seq > 0) {
            boardBRepository.updateBoardB(boardBSaveRequest, loginDto);
        } else {
            seq = boardBRepository.insertBoardB(boardBSaveRequest, loginDto);
        }

        //동게시판 동 저장
        if(boardBSaveRequest.getDongStr() != null) {
            boardBRepository.insertBoardBDong(seq, String.join(",",boardBSaveRequest.getDongStr()));
        }
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM004")) //저장 되었습니다.
                .build();
    }

    //일반/동 게시판 삭제
    @Transactional
    public Result<String> setBoardBDel(BoardCommonDeleteRequestDto boardBDeleteRequest, int regSeq) {
        boardBRepository.delBoardB(boardBDeleteRequest, regSeq);
        return  Result.<String>builder()
                .stateMessage(MngUtil.message("CM002")) //삭제 되었습니다.
                .build();
    }
}
