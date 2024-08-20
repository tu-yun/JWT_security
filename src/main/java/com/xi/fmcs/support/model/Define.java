package com.xi.fmcs.support.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class Define {
	
	//메일
	public static String SMTP;
	public static String SSL;
	public static String MAIL;
	public static String PWD;
	
	//파일 경로
	public static String ROOT_PATH;
	public static String UPLOAD_PATH;
	public static String FILE_PATH;
	public static String DRM_FILE_PATH;
	public static String EXCEL_FILE_PATH;
	public static String LOG_PATH;
	public static String CMS_FILE_PATH;
	
	//앱 인터페이스 서버
	public static String APP_IF_URL;
	public static String API_KEY;	
	
	public Define(
			@Value("${define.SMTP}") String SMTP,
			@Value("${define.SSL}") String SSL,
			@Value("${define.MAIL}") String MAIL,
			@Value("${define.PWD}") String PWD,
			
			@Value("${define.ROOT_PATH}") String ROOT_PATH,
			@Value("${define.UPLOAD_PATH}") String UPLOAD_PATH,
			@Value("${define.FILE_PATH}") String FILE_PATH,
			@Value("${define.DRM_FILE_PATH}") String DRM_FILE_PATH,
			@Value("${define.EXCEL_FILE_PATH}") String EXCEL_FILE_PATH,
			@Value("${define.LOG_PATH}") String LOG_PATH,
			@Value("${define.CMS_FILE_PATH}") String CMS_FILE_PATH,
			
			@Value("${define.APP_IF_URL}") String APP_IF_URL,
			@Value("${define.API_KEY}") String API_KEY) {
		Define.SMTP = SMTP;
		Define.SSL = SSL;
		Define.MAIL = MAIL;
		Define.PWD = PWD;

		Define.ROOT_PATH = ROOT_PATH;
		Define.UPLOAD_PATH = UPLOAD_PATH;
		Define.FILE_PATH = FILE_PATH;
		Define.DRM_FILE_PATH = DRM_FILE_PATH;
		Define.EXCEL_FILE_PATH = EXCEL_FILE_PATH;
		Define.LOG_PATH = LOG_PATH;
		Define.CMS_FILE_PATH = CMS_FILE_PATH;
		
		Define.APP_IF_URL = APP_IF_URL;
		Define.API_KEY = API_KEY;
	}

}
