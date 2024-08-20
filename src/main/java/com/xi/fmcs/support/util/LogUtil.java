package com.xi.fmcs.support.util;

import com.xi.fmcs.support.model.Define;
import com.xi.fmcs.domain.admin.repository.AdminMemberRepository;

import lombok.experimental.UtilityClass;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@UtilityClass
public class LogUtil {

    //관리자 계정 관리 로그 저장(로그인 아이디, 로그히스토리)
    public void insertAminMemberLog(String loginId, String logHistory) {
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String regIp = WebUtil.getClientIP(request);
        logHistory = logHistory + " [" + request.getRequestURL().toString() + "]";
        
        AdminMemberRepository adminMemberRepository = BeanUtil.search(AdminMemberRepository.class);
        adminMemberRepository.insertLog(loginId, logHistory, regIp);
    }

    //로그 파일 저장(프로시저, 파라미터, 에러메세지)
    public void writeLog(String proName, Map<String, Object> sqlParams, String errMessage) {
        StringBuilder sbLog = new StringBuilder();
        sbLog.append("프로시저명 : ").append(proName).append(System.lineSeparator());
        sbLog.append("실행 : ").append(System.lineSeparator());
        sbLog.append("EXEC : ").append(proName + " ");

        if (sqlParams != null) {
            for (Map.Entry<String, Object> item : sqlParams.entrySet()) {
                sbLog.append(String.format("@%s='%s',", item.getKey(), item.getValue()));
            }
        }

        if (errMessage != null && !errMessage.isEmpty()) {
        	sbLog.append(System.lineSeparator());
            sbLog.append("메세지 : ").append(errMessage);
        }

        writeLog(sbLog.toString());
    }

    //로그 파일 저장(프로시저, 에러메세지)
    public static void writeLog(String proName, String errMessage) {
        StringBuilder sbLog = new StringBuilder();
        sbLog.append("프로시저명 : ").append(proName).append(System.lineSeparator());
        sbLog.append("실행 : ").append(System.lineSeparator());
        sbLog.append("EXEC : ").append(proName + " ");

        if (errMessage != null && !errMessage.isEmpty()) {
            sbLog.append(System.lineSeparator());
            sbLog.append("메세지 : ").append(errMessage);
        }

        writeLog(sbLog.toString());
    }

    /// 로그 파일 Message(controller)
    public void conWriteLog(String controllerActionName, String param, String message) {
        StringBuilder sbLog = new StringBuilder();
        sbLog.append("Controller & Action : " + controllerActionName);

        if (param != null) {
            sbLog.append(System.lineSeparator()).append(String.format("param=%s", param));
        }
        if (message != null && !message.isEmpty()) {
            sbLog.append(System.lineSeparator()).append("메세지 : " + message);
        }
        
        sbLog.append(System.lineSeparator());
        writeLog(sbLog.toString());
    }

    /// 로그 파일 Message(controller)
    public void conWriteLog(String controllerActionName, String message) {
        StringBuilder sbLog = new StringBuilder();
        sbLog.append("Controller & Action : " + controllerActionName);

        if (message != null && !message.isEmpty()) {
            sbLog.append(System.lineSeparator()).append("메세지 : " + message);
        }

        sbLog.append(System.lineSeparator());
        writeLog(sbLog.toString());
    }

    //로그 파일 Message(exception)
    public void writeLog(Exception ex) {
        StringBuilder sbLog = new StringBuilder();
        sbLog.append("Message : ").append(ex.getMessage()).append(System.lineSeparator());
        sbLog.append("Cause : ").append(ex.getCause()).append(System.lineSeparator());
        
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        sbLog.append("StackTrace : ").append(sw.toString()).append(System.lineSeparator());

        writeLog(sbLog.toString());
    }

    //로그 파일 저장
    public void writeLog(String message) {
        StringBuilder sbLog = new StringBuilder();
        sbLog.append("[발생시간] : ")
                .append(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        .toString())
                .append(System.lineSeparator());
        sbLog.append("============================================")
                .append(System.lineSeparator());
        sbLog.append("[메세지] : ")
                .append(System.lineSeparator());
        sbLog.append(message)
                .append(System.lineSeparator());
                
        String folderPath = Define.ROOT_PATH + Define.LOG_PATH;
        File logsDir = new File(folderPath);
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }
                
        String fileName = "FMCS_Admin_Log_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".txt";
        Path fullPath = Paths.get(folderPath, fileName);
        File logsFile = new File(folderPath, fileName);        
        try {
        	sbLog.append(System.lineSeparator());
        	if (!logsFile.exists()) {
    			logsFile.createNewFile();
            }
            Files.write(fullPath, sbLog.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

}
