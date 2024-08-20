package com.xi.fmcs.support.util;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.xi.fmcs.support.model.Define;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    private int seq;
    private String localPath;

    public FileUtil() {
        this.localPath = Define.ROOT_PATH + Define.FILE_PATH;
    }

    public FileUtil(String localPath) {
        seq = 0;
        this.localPath = Define.ROOT_PATH + localPath;
    }

    public String getRandomString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        String randomString = formatter.format(new Date());
        randomString += String.format("%03d", (++seq % 1000));
        return randomString;
    }

    public String getLocalPath(String folderName, String fileName) {
        return Paths.get(
                Paths.get(localPath, folderName).toString(), //directory 경로
                fileName).toString(); // 파일 경로
    }

    public byte[] download(String folderName, String fileName) throws IOException {
        return Files.readAllBytes(
                Paths.get(
                        Paths.get(Paths.get(localPath, folderName).toString(), // directory 경로
                                fileName).toString()) // 파일 경로
        );
    }

    public void copy(String folderName, String fileName, String targetPath) {
        String sourceFile = Paths.get(Paths.get(localPath, folderName).toString(), fileName).toString();

        String targetDirectory = Paths.get(targetPath, folderName).toString();
        String targetFile = Paths.get(targetDirectory, fileName).toString();

        if(sourceFile.equals(targetFile)) {
            return;
        }

        File source = new File(sourceFile);
        if(source.exists()) {
            File target = new File(targetDirectory);
            if(!target.exists()) {
                target.mkdirs();
            }
            try {
                Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
            	LogUtil.writeLog(e);
            }
        }
    }

    public void delete(String folderName, String storedFileName) {
        String filePath = Paths.get(Paths.get(localPath, folderName).toString(), storedFileName).toString();
        File file = new File(filePath);
        if(file.exists()) {
            file.delete();
        }
    }

    //파일 업로드
    public TypeDictionary<String, String> upload(MultipartFile file, String folderName) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }

            System.out.println("파일 경로 ==============>>>>" + folderName);

            String directory = Paths.get(localPath, folderName).toString();
            File uploadDir = new File(directory); 
            if (!uploadDir.exists()) {
            	uploadDir.mkdirs();
            }            
            
            Path directoryPath = Paths.get(directory);
            if (!Files.exists(directoryPath)) {
                Files.createDirectory(directoryPath);
            }

            String original = file.getOriginalFilename();
            String extension = "";
            int dotIndex  = original.lastIndexOf('.');
            if (dotIndex >= 0) {
                extension = original.substring(dotIndex);
            }
            String stored = getRandomString() + extension;
            String fileSize = Long.toString(file.getSize());
            String path = Paths.get(directory, stored).toString();

            file.transferTo(new File(path));

            TypeDictionary<String, String> fileData = new TypeDictionary<>();
            fileData.put("ORIGINAL_FILE_NAME", original);
            fileData.put("STORED_FILE_NAME", stored);
            fileData.put("FILE_SIZE", fileSize);
            fileData.put("FILE_PATH", path);

            return fileData;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.writeLog(e);
            return null;
        }
    }

    //파일 다운로드 요청
    public ResponseEntity<byte[]> fileDownload(String path, String fileStoredName, String fileOrgName) throws Exception {
        File file = new File(localPath + path + fileStoredName);
        System.out.println(localPath + path + fileStoredName);

        if(file.exists()) {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=" + fileOrgName)
                    .body(bytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
