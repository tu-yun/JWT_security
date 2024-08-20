package com.xi.fmcs.domain.vote.controller;

import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.vote.model.*;
import com.xi.fmcs.domain.vote.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/vote")
@Tag(name = "VoteController", description = "커뮤니티-투표 [권한: 내단지관리자 이상]")
public class VoteController {

    private final VoteService voteService;

    @Operation(summary = "투표 & 설문 목록")
    @GetMapping("/listPartial")
    public ResultWithBag<List<VoteListResponseDto>> voteListPartial(
            @ParameterObject @ModelAttribute VoteListRequestDto voteListRequest,
            @Parameter(name = "page") @RequestParam(defaultValue = "0") int page,
            @Parameter(name = "pageSize") @RequestParam(defaultValue = "10") int pageSize
    ) {
        if (voteListRequest.getAptSeq() <= 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        return voteService.voteListPartial(voteListRequest, page, pageSize);
    }

    //등록창 / 수정창 따로따로 하게 되면 분리??
    @Operation(summary = "투표 등록/수정 창 조회")
    @GetMapping("/createPartial")
    public Result<VoteCreateResponseDto> voteCreatePartial(
            @Parameter(name = "voteInfoSeq") @RequestParam(defaultValue = "0") int voteInfoSeq,
            @Parameter(name = "aptSeq") @RequestParam(defaultValue = "0") int aptSeq,
            @Parameter(name = "voteType") @RequestParam(defaultValue = "1") int voteType
    ) {
        if (voteInfoSeq < 0 || aptSeq <= 0 || voteType < 1) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        return voteService.voteCreatePartial(voteInfoSeq, aptSeq, voteType);
    }

    @Operation(summary = "투표 상세 (VoteInfo + 질문답변목록)")
    @GetMapping("/detailPartial1")
    public Result<VoteDetailPartial1ResponseDto> voteDetailPartial1(
            @Parameter(name = "voteInfoSeq") @RequestParam(defaultValue = "0") int voteInfoSeq
    ) {
        if (voteInfoSeq <= 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        return voteService.voteDetailPartial1(voteInfoSeq);
    }

    @Operation(summary = "설문 상세 (이지만 기존소스가 VoteInfo만 출력...필요한 데이터 요청해주세요)")
    @GetMapping("/detailPartial2")
    public Result<VoteDetailDto> voteDetailPartial2(
            @Parameter(name = "voteInfoSeq") @RequestParam(defaultValue = "0") int voteInfoSeq
    ) {
        if (voteInfoSeq <= 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        return voteService.voteDetailPartial2(voteInfoSeq);
    }

    @Operation(summary = "투표 기본정보 저장")
    @PostMapping("/setVoteInfoSave")
    public Result<String> setVoteInfoSave(
            @RequestBody VoteInfoSaveRequestDto voteInfoSaveRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if (voteInfoSaveRequest.getVoteInfoSeq() < 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        if (voteInfoSaveRequest.getVoteType() == 2) {
            if (voteInfoSaveRequest.getDongStr() == null) {
                throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
            }
            if (voteInfoSaveRequest.getVoteAge() == null) {
                throw new CustomException("MB05"); //연령을 선택해주세요.
            }
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return voteService.setVoteInfoSave(voteInfoSaveRequest, loginDto.getSeq());
    }

    @Operation(summary = "투표/설문 삭제")
    @DeleteMapping("/setVoteDel")
    public Result<String> setVoteDel(
            @Parameter(name = "voteInfoSeq", required = true) @RequestParam int voteInfoSeq) {
        if (voteInfoSeq <= 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        return voteService.setVoteDel(voteInfoSeq);
    }

    @Operation(summary = "투표/설문 질문_답안 저장")
    @PostMapping(value = "/setVoteQASave")
    public Result<List<VoteAnswerResponseDto>> setVoteQASave(
            @RequestBody VoteQuestionRequestDto voteQuestionRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return voteService.setVoteQASave(voteQuestionRequest, loginDto.getSeq());
    }

    @Operation(summary = "투표/설문 질문_답안 삭제")
    @DeleteMapping("/setVoteQuestionDel")
    public Result<String> setVoteQuestionDel(
            @Parameter(name = "voteQuestionSeq", required = true) @RequestParam int voteQuestionSeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if (voteQuestionSeq <= 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();

        return voteService.setVoteQuestionDel(voteQuestionSeq, loginDto.getSeq());
    }

    @Operation(summary = "투표/설문 답안 삭제")
    @DeleteMapping("/setVoteAnswerDel")
    public Result<String> setVoteAnswerDel(
            @Valid @RequestBody VoteAnswerDelRequestDto voteAnswerDelRequest
    ) {
        return voteService.setVoteAnswerDel(voteAnswerDelRequest);
    }

    @Operation(summary = "투표/설문 질문답안항목 목록(질문/답안 수정)")
    @GetMapping("/getVoteQuestAnsList")
    public Result<List<VoteQuestionAnswerDto>> getVoteQuestAnsList(
            @Parameter(name = "answerType", description = "1:객관식, 2:주관식", required = true) @RequestParam int answerType,
            @Parameter(name = "voteQuestionSeq", description = "질문 고유번호", required = true) @RequestParam int voteQuestionSeq
    ) {
        if (voteQuestionSeq <= 0 || answerType <= 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }

        return voteService.getVoteQuestAnsList(answerType, voteQuestionSeq);
    }

    @Operation(summary = "질문 목록(투표/설문 수정 , 설문 상세)")
    @GetMapping("/getVoteQuestionList")
    public Result<List<VoteQuestionResponseDto>> getVoteQuestionList(
            @Parameter(name = "voteInfoSeq", required = true) @RequestParam int voteInfoSeq
    ) {
        if (voteInfoSeq <= 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }

        return voteService.getVoteQuestionList(voteInfoSeq);
    }

    @Operation(summary = "투표/설문결과(팝업)")
    @GetMapping("/detailResult")
    public Result<VoteResultResponseDto> detailResult(
            @ParameterObject @ModelAttribute VoteResultRequestDto voteResultRequest
    ) {

        return voteService.detailResult(voteResultRequest);
    }

    @Operation(summary = "투표인명부 저장")
    @PostMapping("/setVoteMemberSave")
    public Result<String> setVoteMemberSave(
            @RequestBody VoteMemberSaveRequestDto voteMemberSaveRequest
    ) {
        return voteService.setVoteMemberSave(voteMemberSaveRequest);
    }

    @Operation(summary = "투표 인명부(참여자)목록 / 설문참여자 목록")
    @GetMapping("/voteMemberList")
    public Result<VoteMemberResponseDto> voteMemberList(
            @ParameterObject @ModelAttribute VoteMemberListRequestDto voteMemberExcelRequest,
            @Parameter(name = "page") @RequestParam(defaultValue = "0") int page
    ) {
        return voteService.voteMemberList(voteMemberExcelRequest, page);
    }

    @Operation(summary = "엑셀 다운로드")
    @GetMapping("/voteMemberExcel")
    public ResponseEntity<byte[]> voteMemberExcel(
            @ParameterObject @ModelAttribute VoteMemberListRequestDto voteMemberExcelRequest
    ) throws Exception {
        return voteService.voteMemberExcel(voteMemberExcelRequest);
    }


}
