package com.xi.fmcs.support.util;

import com.xi.fmcs.support.service.SupportService;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MngUtil {
	
    public String message(String code) {
    	return MngUtil.getStatusMessageByCode(code);
	}
    
    public String message(String code, String param) {
    	String message = MngUtil.getStatusMessageByCode(code);
    	
    	message = message.replace("{0}", param);
    	return message; 
	}
    
    public String message(String code, String param1, String param2) {
    	String message = MngUtil.getStatusMessageByCode(code);
    	message = message.replace("{0}", param1).replace("{1}", param2);
    	return message; 
	}
    
	private String getStatusMessageByCode(String code) {
		String message = null;
		try {
			SupportService commonService = BeanUtil.search(SupportService.class);
			message = commonService.getStatusMessageByCode(code);
			if(message == null || message.trim().equals("")) {
				message = "관리자에게 문의하세요.";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return "관리자에게 문의하세요.";
		}
		return message;
	}
	
}
