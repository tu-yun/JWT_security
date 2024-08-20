package com.xi.fmcs.domain.file.repository;

import com.xi.fmcs.support.util.LogUtil;
import com.xi.fmcs.domain.file.model.FileInfoDetailDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class FileInfoRepository{

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //파일 정보 저장
    public void insertFileInfo(FileInfoDetailDto fileInfo, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_FILE_INFO_C";

        try {
            params.put("@ATTACH_TABLE_NAME", fileInfo.getAttachTableName());
            params.put("@ATTACH_TABLE_SEQ", fileInfo.getAttachTableSeq());
            params.put("@FILE_INPUT_NAME", fileInfo.getFileInputName());
            params.put("@FILE_PATH", fileInfo.getFilePath());
            params.put("@FILE_ORG_NAME", fileInfo.getFileOrgName());
            params.put("@FILE_STORED_NAME", fileInfo.getFileStoredName());
            params.put("@FILE_SIZE", fileInfo.getFileSize());
            params.put("@ADMIN_REG_SEQ", regSeq);
            params.put("@REG_DATE", fileInfo.getRegDate());

            sqlSession.insert(mapperName + "." + spName, params);

        }catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //파일 정보 수정
    public void updateFileInfo(FileInfoDetailDto fileInfo, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_FILE_INFO_U";

        try {
            params.put("@ATTACH_TABLE_NAME", fileInfo.getAttachTableName());
            params.put("@ATTACH_TABLE_SEQ", fileInfo.getAttachTableSeq());
            params.put("@FILE_INPUT_NAME", fileInfo.getFileInputName());
            params.put("@FILE_PATH", fileInfo.getFilePath());
            params.put("@FILE_ORG_NAME", fileInfo.getFileOrgName());
            params.put("@FILE_STORED_NAME", fileInfo.getFileStoredName());
            params.put("@FILE_SIZE", fileInfo.getFileSize());
            params.put("@ADMIN_MOD_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);

        }catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //파일 정보 삭제
    public void deleteFileInfo(int seq, String fileInputName, String attachTableName) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_FILE_INFO_D";

        try {
            params.put("@attach_table_seq", seq);
            params.put("@file_input_name", fileInputName);
            params.put("@attach_table_name", attachTableName);

            sqlSession.delete(mapperName + "." + spName, params);

        }catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //커뮤니티(동적멀티첨부) 파일삭제
    public void deleteFileInfoBySeq(int fileSeq, int seq, String attachTableName) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_FILE_SEQ_D";

        try {
            params.put("@file_info_seq", fileSeq);
            params.put("@attach_table_seq", seq);
            params.put("@attach_table_name", attachTableName);

            sqlSession.delete(mapperName + "." + spName, params);

        }catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //파일 목록 조회(인자 2개)
    public List<FileInfoDetailDto> getFileInfo(String fileInputName, int seq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_FILE_L";

        try {
            params.put("@attachTableName", fileInputName);
            params.put("@attachTableSeq", seq);

            return sqlSession.selectList(mapperName + "." + spName, params);

        }catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //파일 목록 조회
    public List<FileInfoDetailDto> getFileInfoV2(String fileInputName, int seq, String attachTableName) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_FILE_V2_L";

        try {
            params.put("@file_input_name", fileInputName);
            params.put("@attach_table_seq", seq);
            params.put("@attach_table_name", attachTableName);

            return sqlSession.selectList(mapperName + "." + spName, params);

        }catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //파일시퀀스로 상세 조회
    public FileInfoDetailDto selectFileInfoByFileSeq(int fileSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_R_FMCS_FILE_INFO_BY_SEQ_R";

        try {
            params.put("@FILE_SEQ", fileSeq);

            return sqlSession.selectOne(mapperName + "." + spName, params);

        }catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //파일 상세 조회
    public FileInfoDetailDto selectFileInfo(String attachTableName, int fileSeq, String fileInputName) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_R_FMCS_FILE_INFO_R";

        try {
            params.put("@ATTACH_TABLE_NAME", attachTableName);
            params.put("@ATTACH_TABLE_SEQ", fileSeq);
            params.put("@FILE_INPUT_NAME", fileInputName);

            return sqlSession.selectOne(mapperName + "." + spName, params);

        }catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }
}

