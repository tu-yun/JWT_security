package com.xi.fmcs.domain.community.service;

import com.xi.fmcs.domain.community.model.communitySet.BoardGroupSaveListRequestDto;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.util.MngUtil;
import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.domain.community.model.communitySet.BoardDisplaySetDto;
import com.xi.fmcs.domain.community.model.communitySet.BoardGroupResponseDto;
import com.xi.fmcs.domain.community.model.communitySet.BoardGroupSaveRequestDto;
import com.xi.fmcs.domain.community.repository.CommunitySetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CommunitySetService {

    private final CommunitySetRepository communitySetRepository;

    public CommunitySetService(CommunitySetRepository communitySetRepository) {
        this.communitySetRepository = communitySetRepository;
    }

    //커뮤니티 설정 조회
    public Result<List<BoardGroupResponseDto>> getBoardGroupList(int aptSeq) {
        List<BoardGroupResponseDto> boardGroupList =
                communitySetRepository.getBoardGroupList(aptSeq, 0);

        return Result.<List<BoardGroupResponseDto>>builder()
                .result(boardGroupList)
                .build();
    }

    //커뮤니티 설정 저장
    @Transactional
    public Result<Object> setBoardGroupSave(BoardGroupSaveListRequestDto boardGroupSaveRequestList, int regSeq) {
            AtomicInteger seq = new AtomicInteger();
            AtomicInteger parentSeq = new AtomicInteger();
            List<BoardGroupSaveRequestDto> boardGroupList = boardGroupSaveRequestList.getBoardGroupSaveRequestList();
            boardGroupList.forEach(s -> {
                // Validation
                if (s.getAptSeq() == 0 || s.getUseYn() == null) {
                    throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
                }

                // 등록 실행
                if (s.getSeq() > 0) {
                    communitySetRepository.updateBoardGroup(
                            s.getSeq(),
                            s.getBoardNm(),
                            s.getBoardType(),
                            s.getOrdNum(),
                            s.getIconUrl(),
                            s.getUseYn(),
                            regSeq
                    );
                    seq.set(s.getSeq());
                } else {
                    if (s.boardLvl == 2) {
                        s.setParentSeq(parentSeq.intValue());
                    }
                    seq.set(communitySetRepository.insertBoardGroup(
                            s.getAptSeq(),
                            s.getParentSeq(),
                            s.getBoardNm(),
                            s.getBoardType(),
                            s.getBoardLvl(),
                            s.getOrdNum(),
                            s.getIconUrl(),
                            s.getUseYn(),
                            regSeq
                    ));
                }
                if (s.boardLvl == 1) {
                    parentSeq.set(seq.intValue());
                }
            });
            return Result.<Object>builder().build();
    }

    /**
     * 커뮤니티 설정 게시판 삭제
     * 게시판 등록글이 있을경우 DEL_YN값 변경, 없을 경우 삭제
     *
     * @param boardGroupSeq
     * @param regSeq
     * @return Result
     */
    @Transactional
    public Result<Object> boardGroupDel(int boardGroupSeq, int regSeq) {
        communitySetRepository.delBoardGroup(boardGroupSeq, regSeq);
        return Result.builder()
                .stateMessage(MngUtil.message("CM002"))  //삭제 되었습니다.
                .build();
    }

    // 노출항목 설정 저장
    @Transactional
    public Result<Object> setDispSave(BoardDisplaySetDto boardDisplaySetRequest, int regSeq) {
        communitySetRepository.updateBoardDisplay(boardDisplaySetRequest, regSeq);

        return Result.<Object>builder()
                .stateMessage(MngUtil.message("CM004"))  //저장 되었습니다.
                .build();
    }

    // 노출항목 상세
    public Result<BoardDisplaySetDto> getDispDetail(int aptSeq) {
        BoardDisplaySetDto boardDisplaySet =
                communitySetRepository.getBoardDisplayDetail(aptSeq);
        return Result.<BoardDisplaySetDto>builder()
                .result(boardDisplaySet)
                .build();
    }
}
