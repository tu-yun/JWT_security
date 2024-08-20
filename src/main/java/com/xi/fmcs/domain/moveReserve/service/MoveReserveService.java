package com.xi.fmcs.domain.moveReserve.service;

import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.domain.moveReserve.model.*;
import com.xi.fmcs.domain.moveReserve.repository.MoveReserveRepository;
import com.xi.fmcs.support.model.Define;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.model.ResultWithBag;
import com.xi.fmcs.support.model.ViewBag;
import com.xi.fmcs.support.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MoveReserveService {

    private final MoveReserveRepository moveReserveRepository;

    public MoveReserveService(MoveReserveRepository moveReserveRepository) {
        this.moveReserveRepository = moveReserveRepository;
    }

    //이사 예약 목록
    public ResultWithBag<List<MoveReserveListResponseDto>> reserveList(MoveReserveListRequestDto moveReserveListRequest, int page, int pageSize) {
        page = page == 0 ? 1 : page;
        int totalCnt = 0;
        List<MoveReserveListResponseDto> moveReserveListResponseList =
                moveReserveRepository.getAptMoveReserveList(
                        moveReserveListRequest,
                        page,
                        pageSize);
        if (moveReserveListResponseList != null && moveReserveListResponseList.size() > 0) {
            totalCnt = moveReserveListResponseList.get(0).getTotalCnt();
        }

        Map<String, Object> search = new HashMap<>();
        search.put("schVal", moveReserveListRequest.getSchVal());

        return ResultWithBag.<List<MoveReserveListResponseDto>>builder()
                .result(moveReserveListResponseList)
                .viewBag(ViewBag.builder()
                        .totalCount(totalCnt)
                        .pageSize(pageSize)
                        .currentPage(page)
                        .search(search)
                        .build())
                .build();
    }

    //이사 예약 히스토리 목록
    public ResultWithBag<List<MoveReserveHisListResponseDto>> reserveHisList(MoveReserveListRequestDto moveReserveListRequest, int page, int pageSize) {
        page = page == 0 ? 1 : page;
        int totalCnt = 0;

        List<MoveReserveHisListResponseDto> hisList =
                moveReserveRepository.getAptMoveReserveHisList(
                        moveReserveListRequest,
                        page,
                        pageSize);

        if (hisList.size() > 0) {
            totalCnt = hisList.get(0).getTotalCnt();
        }
        Map<String, Object> search = new HashMap<>();
        search.put("schVal", moveReserveListRequest.getSchVal());
        return ResultWithBag.<List<MoveReserveHisListResponseDto>>builder()
                .result(hisList)
                .viewBag(ViewBag.builder()
                        .totalCount(totalCnt)
                        .pageSize(pageSize)
                        .currentPage(page)
                        .search(search)
                        .build())
                .build();
    }

    //달력 이사 예약 목록
    public ResultWithBag<List<ReserveCalendarDto>> reserveCalList(ReserveCalendarRequestDto calendarRequest) {
        int year = calendarRequest.getYear();
        int month = calendarRequest.getMonth();
        String sDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
        String eDate = String.format("%s-%s-%s", year, month, YearMonth.of(year, month).lengthOfMonth());

        List<ReserveCalendarDto> calList = new ArrayList<>();
        try {
            calList = moveReserveRepository.getAptMoveReserveCalList(calendarRequest, sDate, eDate);
        } catch (Exception e) {
            LogUtil.writeLog("XI_SP_FMCS_ADMIN_APT_MOVE_RESERVE_CAL_L", e.getMessage());
            new CustomException("MR015");
        }

        Map<String, Object> view = new HashMap<>();
        view.put("aptSeq", calendarRequest.getAptSeq());
        view.put("year", year);
        view.put("month", month);
        view.put("startDate", sDate);

        return ResultWithBag.<List<ReserveCalendarDto>>builder()
                .result(calList)
                .viewBag(ViewBag.builder()
                        .build())
                .build();
    }

    //달력 이사 예약 목록
    public Result<ReserveCalendarResponseDto> getReserveCal(int aptSeq, String dong, String ho) {
        ReserveCalendarResponseDto responseDto = new ReserveCalendarResponseDto();
        if (ho.length() == 3) {
            ho = "0" + ho;
        }
        if (dong.length() == 3) {
            dong = "0" + dong;
        }

        System.out.println("floor : " + ho.substring(0, 2));
        System.out.println("line : " + ho.substring(2));

        List<ReserveCalendarDto> reserveList = null;
        //해당 호와 동일한 엘레베이터 번호를 가진 이사예약 목록
        try {
            reserveList = moveReserveRepository.getReserveCal(aptSeq, dong, ho);
            //조회된 데이터 없는 경우(프론트단에 MoveType과 ElvtrNo 전달)
            if (reserveList == null || reserveList.size() == 0) {
                ElvtrNoAndMoveTypeDto elvtrNoAndMoveType = moveReserveRepository.getElvtrNoAndMoveType(aptSeq, dong, ho.substring(0, 2), ho.substring(2));
                responseDto.setElvtrNo(elvtrNoAndMoveType.getElvtrNo());
                responseDto.setMoveType(elvtrNoAndMoveType.getMoveType());
            } else {
                //프론트단에 프론트단에 MoveType과 ElvtrNo 직관적으로 보이도록 전달
                ReserveCalendarDto rc = responseDto.getReserveList().get(0);
                responseDto.setElvtrNo(rc.getElvtrNo());
                responseDto.setMoveType(rc.getMoveType());
                responseDto.setReserveList(reserveList);
            }
        } catch (Exception e) {
            throw new CustomException("MR011");   //엘레베이터 라인정보가 등록되지 않았습니다. \n내단지 관리자에게 문의하세요.
        }
        //이사시간설정 리스트
        responseDto.setMoveTimeList(moveReserveRepository.getAptMoveTimeSetInfo(aptSeq));
        //입주기간
        MoveSetResponseDto moveSet = moveReserveRepository.getMoveSet(aptSeq);
        responseDto.setStartDate(moveSet.getStartDate());
        responseDto.setEndDate(moveSet.getEndDate());

        return Result.<ReserveCalendarResponseDto>builder()
                .result(responseDto)
                .build();
    }

    // 선택 날짜 이사예약 상세 조회
    public ResultWithBag<List<MoveReserveDetailResponseDto>> getMoveReserveDetailList(MoveReserveDetailRequestDto moveReserveDetailRequest) {
        int aptSeq = moveReserveDetailRequest.getAptSeq();
        String moveDate = moveReserveDetailRequest.getMoveDate();
        List<MoveReserveDetailResponseDto> detailList = moveReserveRepository.getAptMoveReserveDetailList(
                aptSeq,
                moveReserveDetailRequest.getDong(),
                moveReserveDetailRequest.getEvtrNo(),
                moveDate
        );

        int totalCnt = 0;
        try {
            totalCnt = moveReserveRepository.getAptMoveReserveListDate(aptSeq, moveDate);
        } catch (Exception e) {
            Map<String, Object> param = new HashMap<>();
            param.put("@APT_SEQ", aptSeq);
            param.put("@S_DATE", moveDate);
            LogUtil.writeLog("XI_SP_FMCS_ADMIN_APT_MOVE_RESERVE_L_DATE", e.getMessage());
        }
        return ResultWithBag.<List<MoveReserveDetailResponseDto>>builder()
                .result(detailList)
                .stateMessage(MngUtil.message("MR013"))  // 예약정보가 조회가 되었습니다.
                .viewBag(ViewBag.builder()
                        .totalCount(totalCnt)
                        .build())
                .build();
    }

    // 오늘 이사예약 건수
    public Result<Integer> getTodayMoveReserveCnt(int aptSeq) {
        int result = 0;
        try {
            result = moveReserveRepository.getTodayMoveReserveCnt(aptSeq);
        } catch (Exception e) {
            LogUtil.writeLog("XI_SP_FMCS_ADMIN_APT_TODAY_MOVE_RESERVE_CNT_R", e.getMessage());
        }
        return Result.<Integer>builder()
                .result(result)
                .build();
    }

    // 이사예약 시간설정 조회
    public Result<List<MoveTimeSetDto>> getMoveTimeSet(int aptSeq, int regSeq) {
        List<MoveTimeSetDto> moveTimeSetList = moveReserveRepository.getAptMoveTimeSetInfo(aptSeq);

        if (moveTimeSetList.size() == 0) {
            //이사예약 시간 설정 기본
            moveReserveRepository.insertAptMoveTimeDefaultSet(aptSeq, regSeq);
            LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[이사예약 시간 Default 설정] [aptSeq=" + aptSeq + "]");
            moveTimeSetList = moveReserveRepository.getAptMoveTimeSetInfo(aptSeq);
        }

        return Result.<List<MoveTimeSetDto>>builder()
                .result(moveTimeSetList)
                .stateMessage("MR010") // 시간설정 조회가 되었습니다.
                .build();
    }

    // 이사예약 시간 설정 변경
    @Transactional
    public Result<Object> setMoveTimeSet(List<MoveTimeSetDto> aptMoveTimeSet, int regSeq) {
        boolean timeNullChk = false;
        //1. 유효성 체크 사용자 체크는 1개 이상 있어야 한다.
        if (aptMoveTimeSet.stream().filter(x -> x.getUserView().equals("Y") && !StringUtils.isAnyBlank(x.getStartTime(), x.getEndTime())).count() <= 0) {
            throw new CustomException("MR009"); //사용가능 차수를 1개 이상 선택해주세요.
        }
        int idx = 1;
        for (MoveTimeSetDto item : aptMoveTimeSet) {

            if (item.getSeq() < 0) {
                throw new CustomException("ER003"); //요청하신 정보가 잘못되었습니다.
            }
            String strIdx = String.valueOf(idx);
            // 이전 설정 시간이 빈값이 었다면 이번 설정 시간도 빈값이어야 순차적이지만 그렇지 않다면
            if (timeNullChk) {
                if (!item.getStartTime().trim().isEmpty() || !item.getEndTime().trim().isEmpty()) {
                    throw new CustomException("MR007", MngUtil.message("MR007", strIdx));   //{0}차수 시간설정을 순차적으로 입력하세요.
                }
            }
            // 사용자 체크는 했지만 시간 설정 안되어 있다면
            if (item.getUserView().equals("Y") && (item.getStartTime().trim().isEmpty() || item.getEndTime().trim().isEmpty())) {
                throw new CustomException("MR002", MngUtil.message("MR002", strIdx));   //{0}차 설정시간을 확인하세요.
            }
            // 시작 시간은 있지만 종료 시간이 없다면.
            if (!item.getStartTime().trim().isEmpty() && item.getEndTime().trim().isEmpty()) {
                throw new CustomException("MR006", MngUtil.message("MR006", strIdx));   //{0}차 종료 시간을 확인하세요.
            }
            // 시작 시간은 없지만 종료 시간이 있다면.
            if (item.getStartTime().trim().isEmpty() && !item.getEndTime().trim().isEmpty()) {
                throw new CustomException("MR004", MngUtil.message("MR004", strIdx));   //{0}차 시작시간을 확인하세요.
            }

            if (!StringUtils.isAnyBlank(item.getStartTime()) && !StringUtils.isAnyBlank(item.getEndTime())) {
                //시작 시간과 종료시간 체크
                if (item.getStartTime().equals(item.getEndTime())) {
                    throw new CustomException("MR003", MngUtil.message("MR003", strIdx));   //{0}차 시작시간 과 종료 시간을 확인하세요.
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                LocalTime sTs = LocalTime.parse(item.getStartTime(), formatter);
                LocalTime eTs = LocalTime.parse(item.getEndTime(), formatter);
                if (sTs.isAfter(eTs)) {
                    throw new CustomException("MR005", MngUtil.message("MR005", strIdx));   //{0}차 이사종료시간이 시작시간보다 빠릅니다.
                }
                //차수간 시간 교차 안된다.
                //이전 종료 시간과  this 시작 시간 비교
                if (idx > 1) {
                    eTs = LocalTime.parse(aptMoveTimeSet.get(idx - 2).getEndTime());
                    if (sTs.isBefore(eTs)) {
                        throw new CustomException("MR001", MngUtil.message("MR001", String.valueOf(idx - 1), strIdx));   //[{0}~{1}]차수 간에 시간이 겹치지 않도록 설정해주세요.
                    }
                }
                item.setAdminView("Y");
            } else {
                item.setAdminView("N");
                item.setStartTime("");
                item.setEndTime("");
                timeNullChk = true;
            }
            String spName = "XI_SP_FMCS_ADMIN_APT_MOVE_TIME_SET_U";
            try {
                if (item.getSeq() > 0) { // 존재하면
                    moveReserveRepository.updateAptMoveTimeSet(
                            item.getSeq(),
                            item.getUserView(),
                            item.getAdminView(),
                            item.getStartTime(),
                            item.getEndTime(),
                            regSeq
                    );
                } else { // 존재하지 않으면
                    spName = "XI_SP_FMCS_ADMIN_APT_MOVE_TIME_SET_C";
                    moveReserveRepository.insertAptMoveTimeSet(
                            item.getAptSeq(),
                            item.getOrderNum(),
                            item.getUserView(),
                            item.getAdminView(),
                            item.getStartTime(),
                            item.getEndTime(),
                            regSeq
                    );
                    LogUtil.insertAminMemberLog(String.valueOf(regSeq), "[이사예약 시간 설정] [aptSeq=" + item.getAptSeq() + ", index=" + idx + "]");
                }
            } catch (Exception e) {
                LogUtil.writeLog(spName, e.getMessage());
                throw new CustomException("ER001"); // 관리자에게 문의하세요.
            }
        }
        return Result.<Object>builder()
                .stateMessage(MngUtil.message("CM004"))  //저장 되었습니다.
                .build();
    }

    // 입주기간 / 앱설정 조회
    public Result<MoveSetResponseDto> getMoveSet(int aptSeq) {
        String msg = MngUtil.message("MR010");  //시간설정 조회가 되었습니다.
        MoveSetResponseDto moveSetResponse = moveReserveRepository.getMoveSet(aptSeq);

        if (moveSetResponse == null) {
            msg = MngUtil.message("MR008"); //등록된 데이터가 없습니다.
        }

        return Result.<MoveSetResponseDto>builder()
                .result(moveSetResponse)
                .stateMessage(msg)
                .build();
    }

    // 입주기간 / 앱설정 저장
    @Transactional
    public Result<Object> setMoveSet(MoveSetRequestDto moveSet, int regSeq) {
        String preStartDate = moveSet.getPreStartDate() + "T" + moveSet.getPreStartHour() + ":" + moveSet.getPreStartMin();
        String preEndDate = moveSet.getPreEndDate() + "T" + moveSet.getPreEndHour() + ":" + moveSet.getPreEndMin();
        String startDate = moveSet.getStartDate();
        String endDate = moveSet.getEndDate();

        //입주기간 체크
        LocalDateTime sTs = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime eTs = LocalDateTime.parse(endDate + "T23:59:59");
        if (sTs.compareTo(eTs) > 0) {
            throw new CustomException("MR016"); //입주기간 시작일과 종료일을 확인해주세요.
        }

        //입주예약기간 체크
        sTs = LocalDateTime.parse(preStartDate);
        eTs = LocalDateTime.parse(preEndDate);
        if (sTs.compareTo(eTs) > 0) {
            throw new CustomException("MR017"); //입주예약기간 시작일과 종료일을 확인해주세요.
        }

        if (moveSet.getAppContents() == null) {
            moveSet.setAppContents("");
        }

        if (moveSet.getMoveSetSeq() > 0) {
            moveReserveRepository.updateMoveSet(moveSet, sTs, eTs, regSeq);
        } else {
            moveReserveRepository.insertMoveSet(moveSet, sTs, eTs, regSeq);
        }

        return Result.<Object>builder()
                .stateMessage(MngUtil.message("CM004"))  //저장 되었습니다.
                .build();
    }

    // 이사 예약 저장
    @Transactional
    public Result<Object> saveMoveReserve(MoveReserveInsertRequestDto insertRequest, int regSeq) {
        int result = moveReserveRepository.insertMoveReserve(insertRequest, regSeq);
        if (result == -2) {
            throw new CustomException("MR015"); //이미 예약된 정보가 있습니다. \n예약 시간을 확인하세요.
        }
        if (result == -3) {
            throw new CustomException("MR012"); //예약이 불가한 시간입니다. \n예약 시간을 확인하세요.
        }
        if (result > 0) {
            moveReserveRepository.saveMoveReserveHis(insertRequest, regSeq);
            return Result.<Object>builder()
                    .stateMessage(MngUtil.message("CM004")) //저장 되었습니다.
                    .build();
        }
        throw new CustomException("ER001"); //관리자에게 문의하세요.
    }

    @Transactional
    public Result<String> moveReserveCancel(int aptSeq, int aptMoveReserveSeq, int regSeq) {
        moveReserveRepository.deleteMoveReserve(aptMoveReserveSeq, aptMoveReserveSeq, regSeq);
        return Result.<String>builder()
                .result(MngUtil.message("MR018"))   //취소 되었습니다.
                .build();
    }

    public Result<Integer> moveReserveCheck(int aptSeq, String dong, String ho, int regSeq) {
        int result = moveReserveRepository.checkMoveReserve(aptSeq, dong, ho, regSeq);
        return Result.<Integer>builder()
                .result(result)
                .build();
    }

    public ResponseEntity<byte[]> moveExcelDown(MoveReserveListRequestDto moveReserveListRequest) throws Exception {
        MoveReserveExcelDto excelDtoList = new MoveReserveExcelDto(
                moveReserveRepository.getAptMoveReserveListAll(moveReserveListRequest));
        // 헤더 셋팅 & 파일 셋팅
        String randomFolder = StringUtil.getRandomFolder(new SimpleDateFormat("yyyyMMdd_HHmmss_SSS"));
        String path = Define.EXCEL_FILE_PATH + "/Down/";
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("DONG", "동");
        columnMap.put("HO", "호");
        columnMap.put("MOVE_DATE", "이사일");
        columnMap.put("MOVE_TIME", "이사시간");
        columnMap.put("REG_DATE", "등록일");
        ExcelUtil.createExcelFile(
                path,
                randomFolder,
                ExcelUtil.setDataTable(0, excelDtoList.getDatas(), columnMap)
        );
        return new FileUtil().fileDownload(path + randomFolder + "/", randomFolder + ".xlsx", randomFolder + ".xlsx");
    }
}