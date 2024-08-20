package com.xi.fmcs.domain.community.controller;

import com.xi.fmcs.domain.community.model.memSave.MemSaveDetailResponseDto;
import com.xi.fmcs.domain.community.model.memSave.MemSaveListDto;
import com.xi.fmcs.domain.community.model.memSave.MemSaveListRequestDto;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.domain.apt.model.AptDongHoResponseDto;
import com.xi.fmcs.domain.community.service.MemSaveService;
import com.xi.fmcs.support.model.ResultWithBag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community/memSave")
@Tag(name = "MemSaveController", description = "커뮤니티-회원글보관함 [권한: 내단지관리자 이상]")
public class MemSaveController {

    private final MemSaveService memSaveService;

    public MemSaveController(MemSaveService memSaveService) {
        this.memSaveService = memSaveService;
    }

    @Operation(summary = "아파트 동호 리스트")
    @GetMapping("/getAptDongHoList")
    public Result<List<AptDongHoResponseDto>> GetAptDongHoList(
            @Parameter(name = "aptSeq", required = true) @RequestParam(defaultValue = "0") int aptSeq
    ) {
        if (aptSeq <= 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        return memSaveService.getAptDongHoList(aptSeq);
    }

    @Operation(summary = "회원글보관함 목록")
    @GetMapping("/listPartial")
    public ResultWithBag<List<MemSaveListDto>> memSaveListPartial(
            @ParameterObject @ModelAttribute MemSaveListRequestDto memSaveListRequest,
            @Parameter(name = "page") @RequestParam int page
    ) {
        return memSaveService.getBoardMemSaveList(memSaveListRequest, page);
    }

    @Operation(summary = "회원글보관함 목록")
    @GetMapping("/detailPartial")
    public Result<MemSaveDetailResponseDto> memSaveDetailPartial(
            @Parameter(name = "seq", required = true) @RequestParam int seq,
            @Parameter(name = "boardType",description = "게시판 타입(B:일반/동게시판, D:질문답변게시판, E:한줄게시판)",required = true) @RequestParam String boardType
    ) {
        return memSaveService.getBoardMemSaveDetail(seq, boardType);
    }
}
