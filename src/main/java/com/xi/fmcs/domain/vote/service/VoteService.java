package com.xi.fmcs.domain.vote.service;

import com.xi.fmcs.support.enums.VoteAgeEnum;
import com.xi.fmcs.support.enums.VoteAnswerItemTypeEnum;
import com.xi.fmcs.support.enums.VoteAnswerTypeEnum;
import com.xi.fmcs.support.enums.VoteResidenceTypeEnum;
import com.xi.fmcs.support.model.Define;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.support.model.ViewBag;
import com.xi.fmcs.support.util.ExcelUtil;
import com.xi.fmcs.support.util.FileUtil;
import com.xi.fmcs.support.util.MngUtil;
import com.xi.fmcs.support.util.StringUtil;
import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.domain.apt.repository.AptRepository;
import com.xi.fmcs.domain.file.model.FileInfoDetailDto;
import com.xi.fmcs.domain.file.repository.FileInfoRepository;
import com.xi.fmcs.domain.vote.model.*;
import com.xi.fmcs.domain.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final AptRepository aptRepository;
    private final FileInfoRepository fileInfoRepository;


    // 투표/설문 목록 조회
    public ResultWithBag<List<VoteListResponseDto>> voteListPartial(VoteListRequestDto voteListRequest, int page, int pageSize) {
        page = page == 0 ? 1 : page;
        int totalCnt = 0;
        List<VoteListResponseDto> voteList = voteRepository.getVoteList(voteListRequest, page, pageSize);
        if (voteList.size() > 0) {
            totalCnt = voteList.get(0).getTotalCnt();
        }

        return ResultWithBag.<List<VoteListResponseDto>>builder()
                .result(voteList)
                .viewBag(ViewBag.builder()
                        .totalCount(totalCnt)
                        .currentPage(page)
                        .pageSize(pageSize)
                        .build())
                .build();
    }

    //투표 등록/수정 창 조회
    public Result<VoteCreateResponseDto> voteCreatePartial(int voteInfoSeq, int aptSeq, int voteType) {
        VoteCreateResponseDto voteCreateResponse = new VoteCreateResponseDto();
        VoteDetailDto voteInfo = new VoteDetailDto();
        if (voteInfoSeq == 0) {
            voteInfo.setVoteType(voteType);
            voteInfo.setVoteGender(-1);
            voteInfo.setDongStr("ALL");
            voteCreateResponse.setVoteDetailDto(voteInfo);
        } else {
            voteInfo = voteRepository.getVoteDetail(voteInfoSeq);
            voteCreateResponse.setVoteDetailDto(voteInfo);

            //질문답변 리스트
            voteCreateResponse.setVoteQstList(voteRepository.getVoteQuestAnsList1(voteInfoSeq));
        }

        //해당 단지 동 리스트
        voteCreateResponse.setAptDongList(aptRepository.getAptDongList(aptSeq));


        return Result.<VoteCreateResponseDto>builder()
                .result(voteCreateResponse)
                .build();
    }


    //투표 상세
    public Result<VoteDetailPartial1ResponseDto> voteDetailPartial1(int voteInfoSeq) {
        VoteDetailPartial1ResponseDto voteDetailPartial = new VoteDetailPartial1ResponseDto();
        voteDetailPartial.setVoteDetail(voteRepository.getVoteDetail(voteInfoSeq));
        List<VoteQuestionAnswerDto> voteQstList = voteRepository.getVoteQuestAnsList1(voteInfoSeq);
        if (voteQstList != null && voteQstList.size() > 0) {
            int voteTotalCnt = 0;
            for (VoteQuestionAnswerDto v : voteQstList) {
                voteTotalCnt += v.getVoteCnt();
            }
            voteDetailPartial.setVoteTotalCnt(voteTotalCnt);
        }
        return Result.<VoteDetailPartial1ResponseDto>builder()
                .result(voteDetailPartial)
                .build();
    }

    //설문 상세(아마도 추후 수정 필요할듯- 프론트단에서 필요한 데이터 무엇인지 확인해서 코드 수정)
    public Result<VoteDetailDto> voteDetailPartial2(int voteInfoSeq) {
        VoteDetailDto voteDetail = voteRepository.getVoteDetail(voteInfoSeq);
        return Result.<VoteDetailDto>builder()
                .result(voteDetail)
                .build();
    }

    //투표 기본정보 저장
    @Transactional
    public Result<String> setVoteInfoSave(VoteInfoSaveRequestDto voteInfoSaveRequest, int regSeq) {
        int voteInfoSeq = voteInfoSaveRequest.getVoteInfoSeq();
        String voteAge = null;
        voteInfoSaveRequest.setStartDate(StringUtil.getFormatSumDate(voteInfoSaveRequest.getStartDate(), voteInfoSaveRequest.getStartHour(), voteInfoSaveRequest.getStartMin()));
        voteInfoSaveRequest.setEndDate(StringUtil.getFormatSumDate(voteInfoSaveRequest.getEndDate(), voteInfoSaveRequest.getEndHour(), voteInfoSaveRequest.getEndMin()));

        if (voteInfoSeq > 0) {
            voteRepository.updateVoteInfo(voteInfoSaveRequest, regSeq);
        } else {
            voteInfoSeq = voteRepository.insertVoteInfo(voteInfoSaveRequest, regSeq);
        }
        //설문이면
        if (voteInfoSaveRequest.getVoteType() == 2) {
            voteRepository.setVoteDong(voteInfoSeq, String.join(",", voteInfoSaveRequest.getDongStr()));
        }
        return Result.<String>builder()
                .result(String.valueOf(voteInfoSeq))
                .build();
    }

    //투표 설문 삭제
    @Transactional
    public Result<String> setVoteDel(int voteInfoSeq) {
        int result = voteRepository.deleteVoteInfo(voteInfoSeq);
        if (result == -999) {
            throw new CustomException("MB003");   //삭제가 불가합니다.\n진행상황이 대기중일경우만 삭제가능합니다.
        }
        return Result.<String>builder().build();
    }

    //투표(설문) 질문,답안유형 저장
    @Transactional
    public Result<List<VoteAnswerResponseDto>> setVoteQASave(VoteQuestionRequestDto voteQuestionRequest, int regSeq) {

        int voteQstSeq = voteRepository.insertVoteQuestion(voteQuestionRequest, regSeq);
        List<VoteAnswerRequestDto> answers = voteQuestionRequest.getAnswers();

        //Response
        List<VoteAnswerResponseDto> ansList = null;

        if (answers != null && answers.size() > 0) {
            ansList = voteRepository.insertVoteAnswer(voteQstSeq, answers, regSeq);
        }
        return Result.<List<VoteAnswerResponseDto>>builder()
                .result(ansList)
                .build();
    }

    //투표(설문) 질문/답 삭제
    @Transactional
    public Result<String> setVoteQuestionDel(int voteQuestionSeq, int regSeq) {
        List<FileInfoDetailDto> fileInfoDetailList = voteRepository.getVoteQuestFileInfo(voteQuestionSeq);
        int result = voteRepository.delVoteQuestion(voteQuestionSeq);
        if (result == -999) {
            System.out.println("result 값 : " + result);
            throw new CustomException("MB003"); //삭제가 불가합니다.\n진행상황이 대기중일경우만 삭제가능합니다.
        }
        FileUtil fileUtil = new FileUtil(Define.FILE_PATH);
        //업로드 파일 삭제
        fileInfoDetailList.forEach(s -> {
            fileUtil.delete("VOTE", s.getFileStoredName());
        });
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM002")) //삭제 되었습니다.
                .build();
    }

    //투표/설문 답안 삭제
    @Transactional
    public Result<String> setVoteAnswerDel(VoteAnswerDelRequestDto voteAnswerDelRequest) {
        List<FileInfoDetailDto> fileInfoDetailList = fileInfoRepository.getFileInfo("VOTE_ANSWER", voteAnswerDelRequest.getAnsSeq());

        int result = voteRepository.delVoteAnswer(voteAnswerDelRequest.getVoteInfoSeq(), voteAnswerDelRequest.getAnsSeq());
        if (result == -999) {
            throw new CustomException("MB003"); //삭제가 불가합니다.\n진행상황이 대기중일경우만 삭제가능합니다.
        }
        if (fileInfoDetailList != null && fileInfoDetailList.size() > 0) {
            FileUtil fileUtil = new FileUtil(Define.FILE_PATH);
            fileUtil.delete(fileInfoDetailList.get(0).getFilePath(), fileInfoDetailList.get(0).getFileStoredName());
        }
        return Result.<String>builder()
                .stateMessage(MngUtil.message("CM002")) //삭제 되었습니다.
                .build();
    }

    //투표/설문 질문답안항목 목록(질문/답안 수정)
    public Result<List<VoteQuestionAnswerDto>> getVoteQuestAnsList(int answerType, int voteQuestionSeq) {

        List<VoteQuestionAnswerDto> voteQuestionAnswerList = voteRepository.getVoteQuestAnsList2(answerType, voteQuestionSeq);

        return Result.<List<VoteQuestionAnswerDto>>builder()
                .result(voteQuestionAnswerList)
                .build();
    }

    //질문 목록(투표/설문 수정 , 설문 상세)
    public Result<List<VoteQuestionResponseDto>> getVoteQuestionList(int voteInfoSeq) {

        List<VoteQuestionResponseDto> voteQuestionList = voteRepository.getVoteQuestionList(voteInfoSeq);

        return Result.<List<VoteQuestionResponseDto>>builder()
                .result(voteQuestionList)
                .build();
    }

    //투표/설문결과(팝업)
    public Result<VoteResultResponseDto> detailResult(VoteResultRequestDto voteResultRequest) {
        int voteTotalCnt = 0;
        int ansType = voteResultRequest.getAnsType();
        VoteResultResponseDto voteResultResponse = new VoteResultResponseDto();
        voteResultResponse.setAnsType(ansType);

        VoteDetailDto voteDetail = voteRepository.getVoteDetail(voteResultRequest.getVoteInfoSeq());
        voteResultResponse.setVoteDetail(voteDetail);
        //연령 TEXT
        String[] voteAges = voteDetail.getVoteAge().split(",");
        for (int i = 0; i < voteAges.length; i++) {
            voteAges[i] = VoteAgeEnum.getDescriptionByCode(Integer.parseInt(voteAges[i]));
        }
        voteResultResponse.setVoteAgeText(String.join(",", voteAges));
        //거주형태 TEXT
        voteResultResponse.setVoteResidenceText(VoteResidenceTypeEnum.getDescriptionByCode(Integer.parseInt(voteDetail.getVoteResidence())));

        //질문/답변 목록
        List<VoteQuestionAnswerDto> voteResultList = voteRepository.getVoteQuestAnsResult(
                ansType,
                voteResultRequest.getVoteInfoSeq(),
                voteResultRequest.getVoteQuestionSeq()
        );
        voteResultResponse.setVoteQAList(voteResultList);
        if (voteResultList != null && voteResultList.size() > 0) {
            VoteQuestionAnswerDto voteQA = voteResultList.get(0);
            voteResultResponse.setQuestionTitle(voteQA.getQuestionTitle());
            voteResultResponse.setAnswerTypeNm(VoteAnswerTypeEnum.getDescriptionByCode(voteQA.getAnswerType()));
            voteResultResponse.setAnswerItemTypeValNm(VoteAnswerItemTypeEnum.getDescriptionByCode(voteQA.getAnswerItemType()));
            voteResultResponse.setTopAnswerNo(voteQA.getAnswerNo());

            //투표/설문 참여인원
            if (ansType == 1) { //객관식
                for (VoteQuestionAnswerDto item : voteResultList) {
                    voteTotalCnt += item.getVoteCnt();
                }
            } else {
                voteTotalCnt = voteResultList.size();
            }
        }
        voteResultResponse.setVoteTotalCnt(voteTotalCnt);

        return Result.<VoteResultResponseDto>builder()
                .result(voteResultResponse)
                .build();
    }

    //투표인명부 저장
    @Transactional
    public Result<String> setVoteMemberSave(VoteMemberSaveRequestDto voteMemberSaveRequest) {

        int result = voteRepository.insertVoteMember(voteMemberSaveRequest);
        if (result == -999) {
            throw new CustomException("MB004"); //투표인명부 생성이 불가합니다.\n진행상황이 대기중일경우만 생성가능합니다.
        }

        return Result.<String>builder()
                .stateMessage(MngUtil.message("MB006"))  //투표인명부가 생성 되었습니다.
                .build();
    }

    //투표 인명부(참여자)목록 / 설문참여자 목록
    public Result<VoteMemberResponseDto> voteMemberList(VoteMemberListRequestDto voteMemberExcelRequest, int page) {
        VoteMemberResponseDto responseDto = new VoteMemberResponseDto();
        int voteDiv = voteMemberExcelRequest.getVoteDiv();
        int voteInfoSeq = voteMemberExcelRequest.getVoteInfoSeq();
        page = page == 0 ? 1 : page;
        List<VoteMemberDto> voteMemberList;
        //투표
        if (voteDiv < 2) {
            voteMemberList = voteRepository.getVoteMemberList(voteInfoSeq, voteDiv, page);
        } else { //설문
            voteMemberList = voteRepository.getVoteMemberList2(voteInfoSeq, page);
        }
        if (voteMemberList != null && voteMemberList.size() > 0) {
            responseDto.setVoteMemberList(voteMemberList);
            responseDto.setTotalCnt(voteMemberList.get(0).getTotalCnt());
            VoteDetailDto voteDetail = voteRepository.getVoteDetail(voteMemberExcelRequest.getVoteInfoSeq());
            if(voteDetail != null) {
                responseDto.setVoteMakeDate(voteDetail.getVoteMakeDate());
                responseDto.setVoteMemberAgeDate(voteDetail.getVoteMemberAgeDate());
                responseDto.setVoteMemberAuthDate(voteDetail.getVoteMemberAuthDate());
                responseDto.setVoteResidence((voteDetail.getVoteResidence()));
            }
        }
        return Result.<VoteMemberResponseDto>builder()
                .result(responseDto)
                .build();
    }

    //엑셀 다운로드
    public ResponseEntity<byte[]> voteMemberExcel(VoteMemberListRequestDto voteMemberExcelRequest) throws Exception {
        int voteDiv = voteMemberExcelRequest.getVoteDiv();
        int startPoint = 0;

        VoteDetailDto voteDetail = voteRepository.getVoteDetail(voteMemberExcelRequest.getVoteInfoSeq());

        List<Map<String, Object>> datas = voteRepository.getVoteMemberExcelList(voteMemberExcelRequest.getVoteInfoSeq(), voteDiv);
        System.out.println();
        if (datas == null || datas.size() == 0) {
            throw new CustomException("MB007");      //데이터가 존재하지 않습니다.
        }
        //헤더 셋팅
        String voteTitle = "";
        Map<String, String> header = new LinkedHashMap<>();
        header.put("rowNum", "번호");
        header.put("DONG", "동");
        header.put("HO", "호");
        header.put("NAME", "회원명");
        if (voteDiv == 0) { //투표인명부
            voteTitle = "투표인명부";
            startPoint = 5;
            header.put("RESIDENCE_TYPE_NM", "거주형태");
            header.put("HOUSEHOLDER_TYPE_NM", "구분");
        } else if (voteDiv == 1) { //투표참여자
            voteTitle = "투표참여자";
            startPoint = 3;
            header.put("RESIDENCE_TYPE_NM", "거주형태");
            header.put("HOUSEHOLDER_TYPE_NM", "구분");
            header.put("VOTE_REG_DATE", "투표일시");
        } else if (voteDiv == 2) { //설문참여자
            voteTitle = "설문참여자";
            startPoint = 3;
            header.put("GENDER_NM", "성별");
            header.put("BIRTHDAY", "생년월일");
            header.put("VOTE_REG_DATE", "설문일시");
        }

        String path = Define.EXCEL_FILE_PATH + "/VOTE_MEMBER/";
        String randomFolder = URLEncoder.encode(voteTitle, "UTF-8") + "_" + StringUtil.getFolderName(new SimpleDateFormat("yyyyMMddHHmmssSSS"));
        ExcelUtil.createExcelFile(
                path,
                randomFolder,
                ExcelUtil.saveFileToExcel2(
                        startPoint,
                        datas,
                        header,
                        voteDiv,
                        voteTitle,
                        voteDetail.getVoteMakeDate(),
                        voteDetail.getVoteMemberAuthDate(),
                        voteDetail.getVoteMemberAgeDate(),
                        voteDetail.getSDate()
                                + " ~ "
                                + voteDetail.getEDate()
                )
        );
        return new FileUtil().fileDownload(path + randomFolder + "/", randomFolder + ".xlsx", randomFolder + ".xlsx");
    }

}
