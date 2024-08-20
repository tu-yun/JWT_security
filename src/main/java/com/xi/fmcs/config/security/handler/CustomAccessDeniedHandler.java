package com.xi.fmcs.config.security.handler;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.xi.fmcs.config.exception.model.ExceptionResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		logger.info("인가실패 - 로그인 됨");
		accessDeniedException.printStackTrace();
		
		//권한 없음
		logger.info("권한 없음 403");
		
		String path = request.getRequestURI();
		ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "ER002", MngUtil.message("ER002"), path);	//권한이 없습니다.
		WebUtil.setHttpServletResponse(response, 403, exRes);
		return;		 
	}

}
