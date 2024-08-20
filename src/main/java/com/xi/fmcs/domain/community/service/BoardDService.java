package com.xi.fmcs.domain.community.service;

import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.model.BoardCommonDeleteRequestDto;
import com.xi.fmcs.domain.community.model.boardD.*;
import com.xi.fmcs.domain.community.repository.BoardDRepository;
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
public class BoardDService {

    private final BoardDRepository boardDRepository;
    private final FileInfoRepository fileInfoRepository;

    //질문답변게시판 목록
    public ResultWithBag<List<BoardDListDto>> boardDListPartial(BoardDListRequestDto boardDListRequest, int page) {
        int totalCnt = 0;
        page = page == 0 ? 1 : page;

        List<BoardDListDto> boardDList = boardDRepository.getBoardDList(boardDListRequest, page);
        if (boardDList != null && boardDList.size() > 0) {
            totalCnt = boardDList.get(0).getTotalCnt();
        }

        return ResultWithBag.<List<BoardDListDto>>builder()
                .result(boardDList)
                .viewBag(ViewBag.builder()
                        .currentPage(page)
                        .totalCount(totalCnt)
                        .build())
                .build();
    }

    //공공게시판 등록/수정 창
    public Result<BoardDResponseDto> boardDCreatePartial(int seq, AdminMemberLoginDto loginDto) {
        BoardDResponseDto boardDResponse = new BoardDResponseDto();
        BoardDDto boardD = new BoardDDto();
        if (seq == 0) {
            boardD.setWriterNm(loginDto.getName());
            boardD.setTitle("");
            boardD.setContents("");
        } else {
            BoardDDto tempBoardD = boardDRepository.getBoardDDetail(seq);
            boardD.setWriterNm(loginDto.getName());
            boardD.setTitle(tempBoardD.getTitle());
            boardD.setContents(tempBoardD.getContents());
            boardDResponse.setFileInfoDetailList(fileInfoRepository.getFileInfo("BOARD_D", seq));
        }
        boardDResponse.setBoardD(boardD);


        return Result.<BoardDResponseDto>builder()
                .result(boardDResponse)
                .build();
    }

    //공공게시판 상세
    public Result<BoardDResponseDto> boardDDetailPartial(int seq) {
        BoardDResponseDto boardDResponse = new BoardDResponseDto();
        boardDResponse.setFileInfoDetailList(fileInfoRepository.getFileInfo("BOARD_D", seq));
        boardDResponse.setBoardD(boardDRepository.getBoardDDetail(seq));

        return Result.<BoardDResponseDto>builder()
                .result(boardDResponse)
                .build();
    }

    //질문답변게시판 답변저장
    @Transactional
    public Result<String> setBoardDSave(BoardDSaveRequestDto boardDSaveRequest, int regSeq) {

        boardDRepository.updateBoardD(boardDSaveRequest, regSeq);
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM004")) //저장 되었습니다.
                .build();
    }

    //질문답변게시판 삭제 (실제로 삭제가 아님-> BoardD의 DEL_YN을 변경)
    @Transactional
    public Result<String> setBoardDDel(BoardCommonDeleteRequestDto boardDeleteRequest, int regSeq) {
        boardDRepository.delBoardD(boardDeleteRequest, regSeq);
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM002")) //삭제 되었습니다.
                .build();
    }
}
