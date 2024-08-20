package com.xi.fmcs.domain.moveReserve.controller;

import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.support.util.StringUtil;
import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.config.security.model.PrincipalDetails;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;
import com.xi.fmcs.domain.moveReserve.model.*;
import com.xi.fmcs.domain.moveReserve.service.MoveReserveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 이사 예약
 * SELECT TOP 10 MOVE_TYPE , *FROM APT_MOVE_INFO
 * --이사방법(1:엘리베이터,2:사다리,3:엘리베이터, 사다리)
 * <p>
 * select top 10 MOVE_TYPE, * from APT_MOVE_RESERVE
 * --이사방법(1:엘리베이터,2:사다리)
 * <p>
 * SELECT TOP 10 ACTION_TYPE , * FROM APT_MOVE_SET
 * --이사예약(1 전화, 2 예약기능)
 */
@RestController
@RequestMapping("/movereserve")
@Tag(name = "MoveReserveController", description = "이사 예약 관리 [권한: 내단지관리자 이상]")
public class MoveReserveController {

    private final MoveReserveService moveReserveService;

    public MoveReserveController(MoveReserveService moveReserveService) {
        this.moveReserveService = moveReserveService;
    }

    @Operation(summary = "이사 예약 목록")
    @GetMapping("/reserveList")
    public ResultWithBag<List<MoveReserveListResponseDto>> reserveList(
            @ParameterObject @ModelAttribute MoveReserveListRequestDto moveReserveListRequest,
            @Parameter(name = "page", description = "페이지") @RequestParam(defaultValue = "0") int page,
            @Parameter(name = "pageSize", description = "페이징처리 사이즈") @RequestParam(defaultValue = "100") int pageSize
    ) {
        if (moveReserveListRequest.getAptSeq() == 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        if (moveReserveListRequest.getStartDate() != null && !StringUtil.stringDateCheck(moveReserveListRequest.getStartDate())) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        if (moveReserveListRequest.getEndDate() != null && !StringUtil.stringDateCheck(moveReserveListRequest.getEndDate())) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        return moveReserveService.reserveList(moveReserveListRequest, page, pageSize);
    }

    @Operation(summary = "이사 예약 히스토리 목록")
    @GetMapping("/reserveHisList")
    public ResultWithBag<List<MoveReserveHisListResponseDto>> reserveHisList(
            @ParameterObject @ModelAttribute MoveReserveListRequestDto moveReserveListRequest,
            @Parameter(name = "page", description = "페이지") @RequestParam(defaultValue = "0") int page,
            @Parameter(name = "pageSize", description = "페이징처리 사이즈") @RequestParam(defaultValue = "100") int pageSize
    ) {
        if (moveReserveListRequest.getAptSeq() == 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        return moveReserveService.reserveHisList(moveReserveListRequest, page, pageSize);
    }

    //안쓸 예정
    @Operation(summary = "달력 이사 예약 목록(예전꺼)")
    @GetMapping("/reserveCalList")
    public ResultWithBag<List<ReserveCalendarDto>> reserveCalList(
            @ParameterObject @ModelAttribute ReserveCalendarRequestDto calendarRequest
    ) {
        if (calendarRequest.getAptSeq() == 0
                || calendarRequest.getYear() == 0 || calendarRequest.getMonth() == 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        return moveReserveService.reserveCalList(calendarRequest);
    }

    @Operation(summary = "달력 이사 예약 목록")
    @GetMapping("/getReserveCal")
    public Result<ReserveCalendarResponseDto> getReserveCal(
            @ParameterObject @ModelAttribute MoveReserveSimpleRequestDto simpleRequest
    ) {
        int aptSeq = simpleRequest.getAptSeq();
        String dong = simpleRequest.getDong();
        String ho = simpleRequest.getHo();
        if (aptSeq <= 0 || dong == null || ho == null
                || dong.trim().equals("") || ho.trim().equals("")
                || dong.length() > 10 || ho.length() > 10) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        return moveReserveService.getReserveCal(aptSeq, dong, ho);
    }

    @Operation(summary = "선택 날짜 이사예약 상세 조회")
    @GetMapping("/getMoveReserveDetailList")
    public ResultWithBag<List<MoveReserveDetailResponseDto>> getMoveReserveDetailList(
            @ParameterObject @ModelAttribute MoveReserveDetailRequestDto moveReserveDetailRequest
    ) {
        if(moveReserveDetailRequest.getAptSeq() <= 0 || moveReserveDetailRequest.getMoveDate() == null
        || moveReserveDetailRequest.getDong() == null || moveReserveDetailRequest.getEvtrNo() == 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        return moveReserveService.getMoveReserveDetailList(moveReserveDetailRequest);
    }

    @Operation(summary = "오늘 이사예약 건수(내단지 메인화면에 있는 오늘 이사현황)")
    @GetMapping("/getTodayMoveReserveCnt")
    public Result<Integer> getTodayMoveReserveCnt(
            @Parameter(name = "aptSeq", required = true) @RequestParam(defaultValue = "0") int aptSeq) {
        if (aptSeq <= 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        return moveReserveService.getTodayMoveReserveCnt(aptSeq);
    }

    @Operation(summary = "이사예약 시간설정 조회")
    @GetMapping("/getMoveTimeSet")
    public Result<List<MoveTimeSetDto>> getMoveTimeSet(
            @Parameter(name = "aptSeq", required = true) @RequestParam(defaultValue = "0") int aptSeq,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if (aptSeq <= 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return moveReserveService.getMoveTimeSet(aptSeq, loginDto.getSeq());
    }

    @Operation(summary = "이사예약 시간 설정 변경 (aptMoveTimeSetSeq 값이 존재하면 수정, 존재X 등록)")
    @PostMapping("/setMoveTimeSet")
    public Result<Object> setMoveTimeSet(
            @RequestBody List<MoveTimeSetDto> aptMoveTimeSet,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if (aptMoveTimeSet == null && aptMoveTimeSet.size() == 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return moveReserveService.setMoveTimeSet(aptMoveTimeSet, loginDto.getSeq());
    }


    @Operation(summary = "입주기간 / 앱설정 저장 API (moveSetSeq 값이 존재하면 수정, 존재X 등록)")
    @PostMapping("/setMoveSet")
    public Result<Object> setMoveSet(
            @RequestBody MoveSetRequestDto moveSet,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return moveReserveService.setMoveSet(moveSet, loginDto.getSeq());
    }

    @Operation(summary = "입주기간 / 앱설정 조회")
    @GetMapping("/getMoveSet")
    public Result<MoveSetResponseDto> getMoveSet(
            @Parameter(name = "aptSeq", description = "아파트 고유번호") @RequestParam(defaultValue = "0") int aptSeq
    ) {
        if (aptSeq <= 0) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        return moveReserveService.getMoveSet(aptSeq);
    }

    @Operation(summary = "이사 예약 저장")
    @PostMapping("/saveMoveReserve")
    public Result<Object> saveMoveReserve(
            @RequestBody MoveReserveInsertRequestDto insertRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if (insertRequest.getAptSeq() <= 0 || insertRequest.getDong() == null || insertRequest.getHo() == null
                || insertRequest.getEvNo() <= 0 || insertRequest.getMoveDate() == null || insertRequest.getAptMoveTimeSetSeq() <= 0
                || insertRequest.getMoveType() <= 0 || insertRequest.getRegName() == null || insertRequest.getIsAdmin() == null
                || insertRequest.getRegTel() == null) {
            throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
        }
        insertRequest.setIsAdmin("Y");
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return moveReserveService.saveMoveReserve(insertRequest, loginDto.getSeq());
    }

    @Operation(summary = "이사 예약 취소")
    @PostMapping("/moveReserveCancel")
    public Result<String> moveReserveCancel(
            @RequestBody MoveReserveCancelRequestDto moveReserveCancelRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        int aptSeq = moveReserveCancelRequest.getAptSeq();
        int aptMoveReserveSeq = moveReserveCancelRequest.getAptMoveReserveSeq();
        if (aptSeq <= 0 || aptMoveReserveSeq <= 0) {
            throw new CustomException("MR014"); //요청 정보 오류입니다. \n관리자에게 문의하세요.
        }
        return moveReserveService.moveReserveCancel(aptSeq, aptMoveReserveSeq, loginDto.getSeq());
    }

    @Operation(summary = "이사 예약 더블체크")
    @PostMapping("/moveReserveCheck")
    public Result<Integer> moveReserveCheck(
            @RequestBody MoveReserveSimpleRequestDto simpleRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        int aptSeq = simpleRequest.getAptSeq();
        String dong = simpleRequest.getDong();
        String ho = simpleRequest.getHo();
        if(aptSeq <= 0 || dong == null || dong.trim().equals("")) {
            throw new CustomException("MR014"); //요청 정보 오류입니다. \n관리자에게 문의하세요.
        }
        AdminMemberLoginDto loginDto = principalDetails.getAdminMemberLoginDto();
        return moveReserveService.moveReserveCheck(aptSeq, dong, ho, loginDto.getSeq());
    }

    @Operation(summary = "엑셀 다운로드")
    @GetMapping("/moveexcelDown")
    public ResponseEntity<byte[]> moveExcelDown(
            @ParameterObject @ModelAttribute MoveReserveListRequestDto moveReserveListRequestDto
    ) throws Exception {
        return moveReserveService.moveExcelDown(moveReserveListRequestDto);
    }
}
