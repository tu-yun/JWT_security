package com.xi.fmcs.domain.community.service;

import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.domain.community.model.boardA.*;
import com.xi.fmcs.domain.community.repository.CommunityRepository;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.support.model.ViewBag;
import com.xi.fmcs.support.util.MngUtil;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.community.repository.BoardARepository;
import com.xi.fmcs.domain.file.repository.FileInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardAService {

    private final CommunityRepository communityRepository;
    private final BoardARepository boardARepository;
    private final FileInfoRepository fileInfoRepository;

    //공공게시판 목록
    public ResultWithBag<List<BoardAListDto>> boardAListPartial(BoardAListRequestDto boardAListRequest, int page) {
        int totalCnt = 0;
        page = page == 0 ? 1 : page;
        String saveYn = boardAListRequest.getSaveYn();
        saveYn = saveYn == null ? "Y" : saveYn;

        List<BoardAListDto> boardAList = boardARepository.getBoardAList(
                boardAListRequest.getGroupSeq(),
                saveYn,
                boardAListRequest.getSrchTxt(),
                boardAListRequest.getSrchStartDt(),
                boardAListRequest.getSrchEndDt(),
                page);
        if (boardAList != null && boardAList.size() > 0) {
            totalCnt = boardAList.get(0).getTotalCnt();
        }
        Map<String, Object> search = new HashMap<>();
        search.put("saveYn", saveYn);
        search.put("srchTxt", boardAListRequest.getSrchTxt());
        search.put("srchStartDt", boardAListRequest.getSrchStartDt());
        search.put("srchEndDt", boardAListRequest.getSrchEndDt());

        return ResultWithBag.<List<BoardAListDto>>builder()
                .result(boardAList)
                .viewBag(ViewBag.builder()
                        .search(search)
                        .currentPage(page)
                        .totalCount(totalCnt)
                        .build())
                .build();
    }

    //공공게시판 등록/수정 창
    public Result<BoardAResponseDto> boardACreatePartial(int seq, int groupSeq, AdminMemberLoginDto loginDto) {
        BoardAResponseDto boardAResponse = new BoardAResponseDto();
        BoardADto boardA = new BoardADto();
        if(seq == 0) {
            boardA.setWriterNicknm(loginDto.getNickname());
            boardA.setWriterNm(loginDto.getName());
        } else {
            boardA = boardARepository.getBoardADetail(seq);
            boardAResponse.setFileInfoDetailList(fileInfoRepository.getFileInfo("BOARD_A", seq));
        }
        boardAResponse.setBoardA(boardA);

        //푸시발송 동리스트(동게시판)
        boardAResponse.setDongList(communityRepository.getBoardAptDongList(groupSeq));

        return Result.<BoardAResponseDto>builder()
                .result(boardAResponse)
                .build();
    }

    //공공게시판 상세
    public Result<BoardADetailResponseDto> boardADetailPartial(int seq, String saveYn) {
        BoardADetailResponseDto boardADetailResponse = new BoardADetailResponseDto();
        boardADetailResponse.setSaveYn(saveYn);
        boardADetailResponse.setBoardA(boardARepository.getBoardADetail(seq));
        boardADetailResponse.setFileInfoDetailList(fileInfoRepository.getFileInfo("BOARD_A", seq));

        return Result.<BoardADetailResponseDto>builder().build();
    }

    //공공게시판 저장
    @Transactional
    public Result<String> setBoardASave(BoardASaveRequestDto boardASaveRequest, AdminMemberLoginDto loginDto) {

        if(boardASaveRequest.getSeq() == 0) {
            boardARepository.insertBoardA(boardASaveRequest, loginDto);
        } else {
            if(loginDto.getGradeType() == 7) {
                throw new CustomException("ER002");  //권한이 없습니다.
            }
            boardARepository.updateBoardA(boardASaveRequest, loginDto);
        }
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM004")) //저장 되었습니다.
                .build();
    }

    @Transactional
    public Result<String> setBoardADel(int seq, int regSeq) {
        boardARepository.delBoardA(seq, regSeq);
        return  Result.<String>builder()
                .stateMessage(MngUtil.message("CM002")) //삭제 되었습니다.
                .build();
    }
}
