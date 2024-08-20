package com.xi.fmcs.support.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BeanUtil {

	public <T> T search(Class<T> type) {
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    	HttpSession session = request.getSession();
    	ServletContext context = session.getServletContext();
    	WebApplicationContext wContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
    	return (T)wContext.getBean(type);
	}
	
}
