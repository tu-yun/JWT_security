package com.xi.fmcs.support.util;

import com.xi.fmcs.support.model.Define;
import com.xi.fmcs.domain.aptMng.model.AptInfoExcelDto;
import lombok.experimental.UtilityClass;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ExcelUtil {

    private final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public Workbook getWorkbook(MultipartFile file, String kind) throws IOException {

        Workbook workbook = null;
        if (kind.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (kind.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }
        return workbook;
    }

    public TypeDictionary<String, String> writeAptInfoExcel(List<AptInfoExcelDto> aptInfoList, String xiCode) {
        Workbook workbook = new XSSFWorkbook();
        CellStyle style = workbook.createCellStyle();

        style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        Sheet sheet = workbook.createSheet(xiCode);

        //헤더 부분
        Row header = sheet.createRow(0);
        Cell headerCell = header.createCell(0);
        headerCell.setCellStyle(style);
        headerCell.setCellValue("동");
        headerCell = header.createCell(1);
        headerCell.setCellStyle(style);
        headerCell.setCellValue("층");
        headerCell = header.createCell(2);
        headerCell.setCellStyle(style);
        headerCell.setCellValue("룸타입");
        headerCell = header.createCell(3);
        headerCell.setCellStyle(style);
        headerCell.setCellValue("라인");
        headerCell = header.createCell(4);
        headerCell.setCellStyle(style);
        headerCell.setCellValue("최고층");
        headerCell = header.createCell(5);
        headerCell.setCellStyle(style);
        headerCell.setCellValue("최고라인");

        //데이터 쓰기
        for (int i = 1; i < aptInfoList.size(); i++) {
            AptInfoExcelDto aptInfo = aptInfoList.get(i - 1);
            Row row = sheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue(aptInfo.getDong());
            cell = row.createCell(1);
            cell.setCellValue(aptInfo.getFloor());
            cell = row.createCell(2);
            cell.setCellValue(aptInfo.getRoomtypecd());
            cell = row.createCell(3);
            cell.setCellValue(aptInfo.getLine());
            cell = row.createCell(4);
            cell.setCellValue(Integer.parseInt(aptInfo.getMaxfloor()));
            cell = row.createCell(5);
            cell.setCellValue(aptInfo.getMaxline());
        }
        //파일 생성
        FileUtil fileUtil = new FileUtil();
        TypeDictionary<String, String> fileData = new TypeDictionary<>();
        String original = xiCode;
        String extension = ".xlsx";
        String stored = fileUtil.getRandomString() + extension;
        fileData.put("ORIGINAL_FILE_NAME", original + extension);
        fileData.put("STORED_FILE_NAME", stored);
        fileData.put("FILE_PATH", "ExcelFiles");

        File file = new File(Define.ROOT_PATH + Define.EXCEL_FILE_PATH + "/" + stored);
        System.out.println("FILE PATH : " + Define.ROOT_PATH + Define.EXCEL_FILE_PATH + stored);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileData;
    }

    public Workbook setDataTable(
            int startPoint, List<Map<String, Object>> datas, Map<String, String> header) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(startPoint);
        //헤더 그리기
        int columnCount = 0;
        for (String key : header.keySet()) {
            Cell headerCell = headerRow.createCell(columnCount);
            headerCell.setCellValue(header.get(key));
            headerCell.setCellStyle(headerStyle);
            columnCount++;
        }
        // 내용 그리기
        int rowCount = startPoint + 1;
        for (Map<String, Object> data : datas) {
            Row row = sheet.createRow(rowCount);
            int cellCount = 0;
            for (String key : header.keySet()) {
                Cell cell = row.createCell(cellCount);
                try {
                    Object value = data.get(key);
                    cell.setCellValue(value.toString());
                } catch (Exception e) {
                    cell.setCellValue("");
                }
                cellCount++;
            }
            rowCount++;
        }
        return workbook;
    }

    public String createExcelFile(String filePath, String randomFolder, Workbook workbook) {
        //파일 생성
        String targetPath = Define.ROOT_PATH + Define.FILE_PATH + filePath + randomFolder;
        String targetFile = targetPath + "/" + randomFolder + ".xlsx";
        File dir = new File(targetPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            File file = new File(targetFile);
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return targetFile;
    }

    //투표인명부/투표,설문참여자(상단 문서제목 추가)
    public Workbook saveFileToExcel2(
            int startPoint,
            List<Map<String, Object>> datas,
            Map<String, String> header,
            int downType,
            String title,
            String voteMakeDate,
            String voteMemberAuthDate,
            String voteMemberAgeDate,
            String participationPeriod
    ) {
        Workbook workbook = setDataTable(startPoint, datas, header);
        //시트 이름 변경
        workbook.setSheetName(0, title);
        Sheet sheet = workbook.getSheetAt(0);

        //셀 스타일
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        //이름 추가
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue(title);

        //내용 추가
        if(downType == 0) { //투표인명부
            //명부생성일
            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue("* 명부 생성일 : " + voteMakeDate);
            //승인회원 기준일
            row = sheet.createRow(2);
            cell = row.createCell(0);
            cell.setCellValue("* 승인회원 기준일 : " + voteMemberAuthDate);
            //만18세 기준일
            row = sheet.createRow(3);
            cell = row.createCell(0);
            cell.setCellValue("* 만18세 기준일 : " + voteMemberAgeDate);
        } else { //투표참여자, 설문참여자
            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue("* 참여기간 : " + participationPeriod);
        }

        return workbook;
    }
}
