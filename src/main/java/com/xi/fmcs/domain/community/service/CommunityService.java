package com.xi.fmcs.domain.community.service;

import com.xi.fmcs.domain.community.model.community.BoardGroupAptListDto;
import com.xi.fmcs.domain.community.model.community.BoardMainResponseDto;
import com.xi.fmcs.domain.community.model.community.CommunityIndexRequestDto;
import com.xi.fmcs.domain.community.model.community.CommunityIndexResponseDto;
import com.xi.fmcs.domain.community.repository.CommunityRepository;
import com.xi.fmcs.support.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;

    public Result<CommunityIndexResponseDto> boardIndex(CommunityIndexRequestDto communityIndexRequest) {
        int groupSeq = communityIndexRequest.getGrpSeq();
        int parentSeq = communityIndexRequest.getGrpParentSeq();
        //초기화
        int sGroupSeq = groupSeq;
        int sParentSeq = parentSeq;
        String parentNm = null;
        String grpNm = null;
        String boardType = null;
        String dongBoardYn = null; // 동게시판 여부
        CommunityIndexResponseDto communityIndexResponse = new CommunityIndexResponseDto();

        //공공게시판 그룹 조회
        List<BoardGroupAptListDto> boardGroupAptList =
                communityRepository.getAptBoardGroupList(
                        communityIndexRequest.getAptSeq(),
                        sParentSeq,
                        sGroupSeq);

        if(boardGroupAptList != null && boardGroupAptList.size() > 0) {
            //커뮤니티메뉴에서 접근 : 첫번째 게시판
            if(groupSeq == 0) {
                BoardGroupAptListDto boardGroup = boardGroupAptList.get(0);
                sGroupSeq = boardGroup.getSeq();
                sParentSeq = boardGroup.getParentSeq();
                parentNm = boardGroup.getParentNm();
                grpNm = boardGroup.getBoardNm();
                boardType = boardGroup.getBoardType();
                dongBoardYn = boardGroup.getDongBoardYn();
            } else { // 메인페이지 접근 : 지정 게시판
                for(BoardGroupAptListDto item : boardGroupAptList) {
                    if(item.getSeq() == groupSeq) {
                        sGroupSeq = item.getSeq();
                        sParentSeq = item.getParentSeq();
                        parentNm = item.getParentNm();
                        grpNm = item.getBoardNm();
                        boardType = item.getBoardType();
                        dongBoardYn = item.getDongBoardYn();
                    }
                }
            }
        }
        //결과값 세팅
        communityIndexResponse.setBoardGroupList(boardGroupAptList);
        communityIndexResponse.setAptDongHoList(communityRepository.getAptDongHoList(communityIndexRequest.getAptSeq()));
        communityIndexResponse.setGroupSeq(sGroupSeq);
        communityIndexResponse.setGroupNm(grpNm);
        communityIndexResponse.setParentSeq(sParentSeq);
        communityIndexResponse.setParentNm(parentNm);
        communityIndexResponse.setBoardType(boardType);
        communityIndexResponse.setDongBoardYn(dongBoardYn);
        communityIndexResponse.setBoardSeq(communityIndexRequest.getBoardSeq());

        return Result.<CommunityIndexResponseDto>builder()
                .result(communityIndexResponse)
                .build();
    }

    //내단지관리자 메인 게시판
    public Result<BoardMainResponseDto> getMainBoardList(int aptSeq) {
        BoardMainResponseDto boardMainResponse = new BoardMainResponseDto();
        boardMainResponse.setBoardListA(communityRepository.getMainBoardList(aptSeq, "A"));
        boardMainResponse.setBoardListB(communityRepository.getMainBoardList(aptSeq, "B"));
        boardMainResponse.setBoardListD(communityRepository.getMainBoardList(aptSeq, "D"));
        return Result.<BoardMainResponseDto>builder()
                .result(boardMainResponse)
                .build();
    }

}
