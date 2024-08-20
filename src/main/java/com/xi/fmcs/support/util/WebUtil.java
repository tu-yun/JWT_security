package com.xi.fmcs.support.util;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@UtilityClass
public class WebUtil {

    public String getLocalIP() {
        String ip = "0.0.0.0";
        try {
            ip = InetAddress.getLocalHost().getHostAddress().toString();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return ip;
    }

    public String getClientIP(HttpServletRequest request) {
    	String ip = null;
        ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /*
    랜덤 문자열 생성
    totalLen : 문자열 길이
    랜덤 문자 반환
     */
    public String getRandomString (int totalLen) {
        Random random = new Random();
        int leftAscii = 48; //numeral '0'
        int rightAscii = 122; //letter 'z'
        return random.ints(leftAscii, rightAscii+1)
                .filter(i -> (i <= 57 || i>= 65) && (i <= 90 || i>=97))
                .limit(totalLen)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /*
    랜덤 문자열 생성(핸드폰)
    totalLen : 문자열 길이
     */
    public String getRandomStringNumber (int totalLen) {
        Random random = new Random();
        int leftAscii = 48; //numeral '0'
        int rightAscii = 57; //numeral '9'
        return random.ints(leftAscii, rightAscii+1)
                .limit(totalLen)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /*
     * 응답 body 설정
     */
    public void setHttpServletResponse(HttpServletResponse response, int status, Object result) {
		//new ObjectMapper의 time pattern 정의
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.of("Asia/Seoul")));
		javaTimeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);
				
		//응답 body
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setStatus(status);
		
		try {
			String writer = new ObjectMapper().registerModule(javaTimeModule).writeValueAsString(result);
			response.getWriter().write(writer);
		} catch (Exception e) {
			throw new RuntimeException();
		}
    }

}
