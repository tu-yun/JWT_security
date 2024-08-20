package com.xi.fmcs.config.security.configuration;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.xi.fmcs.support.util.LogUtil;
import com.xi.fmcs.support.util.MngUtil;
import com.xi.fmcs.support.util.WebUtil;
import com.xi.fmcs.config.exception.model.ExceptionResponse;

public class WebSecurityExceptionFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e){
			logger.info("시큐리티 오류");
			e.printStackTrace();
			
			//로그파일 저장
			LogUtil.writeLog(e);
			
			//exception 응답
			String path = request.getRequestURI();
			ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "ER001", MngUtil.message("ER001"), path);	//관리자에게 문의하세요.
			WebUtil.setHttpServletResponse(response, 500, exRes);
			return;
		}		
	}

}
