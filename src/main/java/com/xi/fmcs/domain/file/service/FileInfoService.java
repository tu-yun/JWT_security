package com.xi.fmcs.domain.file.service;

import com.xi.fmcs.domain.common.model.VoteSaveFileDto;
import com.xi.fmcs.domain.common.model.VoteSaveFileRequestDto;
import com.xi.fmcs.support.model.Define;
import com.xi.fmcs.support.util.FileUtil;
import com.xi.fmcs.support.util.StringUtil;
import com.xi.fmcs.support.util.TypeDictionary;
import com.xi.fmcs.domain.common.model.CommunitySaveFileRequestDto;
import com.xi.fmcs.domain.file.model.FileInfoDetailDto;
import com.xi.fmcs.domain.file.repository.FileInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FileInfoService {
    private final FileInfoRepository fileInfoRepository;

    private FileUtil fileUtil;

    public FileInfoService(FileInfoRepository fileInfoRepository, HttpServletRequest request) {
        this.fileInfoRepository = fileInfoRepository;
        fileUtil = new FileUtil(Define.FILE_PATH);
    }

    //파일 1개 업로드(있으면 수정, 없으면 추가)
    public FileInfoDetailDto attachFileInfoDevelop(
            MultipartFile file,
            String attachTableName,
            int attachTableSeq,
            String filePath,
            int regSeq
    ) {
        FileInfoDetailDto fileInfo = saveFileAndSetFileInfo(file, attachTableName, attachTableSeq, filePath);
        if (fileInfo != null) {
            List<FileInfoDetailDto> status = fileInfoRepository.getFileInfoV2(attachTableName, attachTableSeq, fileInfo.getFileInputName());
            if (status.size() > 0) {
                fileInfoRepository.updateFileInfo(fileInfo, regSeq);
            } else {
                fileInfo.setRegDate(LocalDateTime.now());
                fileInfoRepository.insertFileInfo(fileInfo, regSeq);
            }
        }
        return fileInfo;
    }

    //커뮤니티 파일저장
    public void attachFileDevelop2(List<MultipartFile> files, CommunitySaveFileRequestDto communitySaveFileRequest, int regSeq) {
        FileUtil fileUtil = new FileUtil();
        LocalDateTime dateTime = LocalDateTime.now();
        int i = 1;

        for (MultipartFile file : files) {
            TypeDictionary<String, String> fileInfo = fileUtil.upload(file, communitySaveFileRequest.getFilePath());
            if (fileInfo != null) {
                FileInfoDetailDto fileInfoDetail = new FileInfoDetailDto();
                String orgFilename = file.getOriginalFilename();
                String tableName = communitySaveFileRequest.getFilePath();
                String filePath = communitySaveFileRequest.getFilePath();
                String fileInputName = "upload-name-" + i;
                int attachTableSeq = communitySaveFileRequest.getId();

                //파일 정보 셋팅
                fileInfoDetail.setRegDate(dateTime);
                fileInfoDetail.setAttachTableName(tableName);
                fileInfoDetail.setAttachTableSeq(attachTableSeq);
                fileInfoDetail.setFilePath(filePath);
                fileInfoDetail.setFileOrgName(orgFilename);
                fileInfoDetail.setFileStoredName(
                        StringUtil.getRandomFolder(new SimpleDateFormat("yyyyMMdd_HHmmss_SSS"))
                                + orgFilename.substring(orgFilename.lastIndexOf("."))
                );
                fileInfoDetail.setFileSize(String.valueOf(file.getSize()));

                if (filePath.equals("BOARD_D")) { //질문&답변
                    fileInputName = "REPLY";    //답변 첨부파일 구분
                }

                fileInfoDetail.setFileInputName(fileInputName);

                if (filePath.equals("BOARD_COMM") || filePath.equals("BOARD_E")) { //한줄게시판, 댓글
                    List<FileInfoDetailDto> status = fileInfoRepository.getFileInfo(tableName, attachTableSeq);
                    if (status != null && status.size() > 0) {
                        fileInfoRepository.updateFileInfo(fileInfoDetail, regSeq);
                    } else {
                        fileInfoRepository.insertFileInfo(fileInfoDetail, regSeq);
                    }
                } else {
                    fileInfoRepository.insertFileInfo(fileInfoDetail, regSeq);
                }
            }
            i++;
        }
    }

    //투표
    public void attachFileDevelop3(VoteSaveFileRequestDto voteSaveFileRequest, int regSeq) {
        FileUtil fileUtil = new FileUtil();

        for (VoteSaveFileDto voteFile : voteSaveFileRequest.getVoteSaveFileList()) {
            MultipartFile file = voteFile.getFile();
            TypeDictionary<String, String> fileInfo = fileUtil.upload(file, voteSaveFileRequest.getFilePath());
            if (fileInfo != null) {
                FileInfoDetailDto fileInfoDetail = new FileInfoDetailDto();
                String orgFilename = file.getOriginalFilename();
                //파일 정보 셋팅
                fileInfoDetail.setRegDate(LocalDateTime.now());
                fileInfoDetail.setAttachTableName(voteSaveFileRequest.getTableName());
                fileInfoDetail.setAttachTableSeq(voteFile.getAnsId());
                fileInfoDetail.setFilePath(voteSaveFileRequest.getFilePath());
                fileInfoDetail.setFileOrgName(orgFilename);
                fileInfoDetail.setFileStoredName(
                        StringUtil.getRandomFolder(new SimpleDateFormat("yyyyMMdd_HHmmss_SSS"))
                                + orgFilename.substring(orgFilename.lastIndexOf("."))
                );
                fileInfoDetail.setFileSize(String.valueOf(file.getSize()));
                fileInfoDetail.setFileInputName("imgUpload");
                List<FileInfoDetailDto> fileList = fileInfoRepository.getFileInfo(voteSaveFileRequest.getTableName(), voteFile.getAnsId());
                if(fileList != null && fileList.size() > 0) {
                    fileInfoRepository.updateFileInfo(fileInfoDetail, regSeq);
                } else {
                    fileInfoRepository.insertFileInfo(fileInfoDetail, regSeq);
                }
            }
        }
    }

    //무조건 저장만
    public FileInfoDetailDto excelFileInfoDevelop(
            MultipartFile file,
            String attachTableName,
            int attachTableSeq,
            String filePath,
            int regSeq,
            LocalDateTime now
    ) {
        FileInfoDetailDto fileInfo = saveFileAndSetFileInfo(file, attachTableName, attachTableSeq, filePath);
        if (fileInfo != null) {
            fileInfo.setAdminRegSeq(regSeq);
            fileInfo.setRegDate(now);
            fileInfoRepository.insertFileInfo(fileInfo, regSeq);
        }
        return fileInfo;
    }

    //파일 업로드 및 FileInfo 셋팅
    public FileInfoDetailDto saveFileAndSetFileInfo(MultipartFile file,
                                                    String attachTableName,
                                                    int attachTableSeq,
                                                    String filePath) {
        FileInfoDetailDto fileInfo = null;
        TypeDictionary<String, String> dictionary = fileUtil.upload(file, filePath);
        if (dictionary != null) {
            fileInfo = new FileInfoDetailDto();
            fileInfo.setAttachTableName(attachTableName);
            fileInfo.setAttachTableSeq(attachTableSeq);
            fileInfo.setFileInputName(file.getName());
            fileInfo.setFilePath(filePath);
            fileInfo.setFileOrgName(file.getOriginalFilename());
            fileInfo.setFileStoredName(dictionary.getValue("STORED_FILE_NAME"));
            fileInfo.setFileSize(dictionary.getValue("FILE_SIZE"));
        }
        return fileInfo;
    }

}
