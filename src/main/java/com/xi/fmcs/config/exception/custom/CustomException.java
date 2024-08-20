package com.xi.fmcs.config.exception.custom;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class CustomException extends RuntimeException{
	private String code;
	private String message;
	
	public CustomException(String code) {
		this.code = code;
	}
	
	public CustomException(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
