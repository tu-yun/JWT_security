package com.xi.fmcs.config.exception.handler;

import java.time.LocalDateTime;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.xi.fmcs.support.util.LogUtil;
import com.xi.fmcs.support.util.MngUtil;
import com.xi.fmcs.config.exception.custom.CustomException;
import com.xi.fmcs.config.exception.model.ExceptionResponse;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
	
	/**
	 * 요청 페이지 없음 exception
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ex.printStackTrace();
		
		final String path = request.getDescription(false).substring(4);
		ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "ER004", MngUtil.message("ER004"), path);	//관리자에게 문의하세요. (요청한 페이지를 찾지 못한 오류)
		
		return new ResponseEntity<Object>(exRes, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * DTO validation exception
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println("DTO Valid");
		ex.printStackTrace();
		
        LogUtil.writeLog(ex);
        
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        if(message == null || message.trim().equals("")) {
        	message = MngUtil.message("ER003");
        }
        
        final String path = request.getDescription(false).substring(4);
        ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "ER003", message, path);	//message가 없는 경우: 요청하신 정보가 잘못되었습니다.
        
        return new ResponseEntity<Object>(exRes, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * @RequestPara, @PathVariable validation exception
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	protected final ResponseEntity<Object> handleConstraintViolationExceptions(ConstraintViolationException ex, WebRequest request) {
		System.out.println("Param Valid");
		ex.printStackTrace();
        
		LogUtil.writeLog(ex);
		
        String message = null;
        if(ex.getConstraintViolations().size() > 0) {
        	for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
        		message = violation.getMessage();
        	}
        }
        if(message == null || message.trim().equals("")) {
        	message = MngUtil.message("ER003");
        }
        
        final String path = request.getDescription(false).substring(4);
        ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "ER003", message, path);	//message가 없는 경우: 요청하신 정보가 잘못되었습니다.
        
        return new ResponseEntity<Object>(exRes, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * custom exception
	 */
	@ExceptionHandler(CustomException.class)
	protected final ResponseEntity<Object> handleCustomExceptions(CustomException ex, WebRequest request) {
		ex.printStackTrace();
		
		final String path = request.getDescription(false).substring(4);
		ExceptionResponse exRes = null;
		if(ex.getMessage() == null || ex.getMessage().equals("")){
			exRes = new ExceptionResponse(LocalDateTime.now(), ex.getCode(), MngUtil.message(ex.getCode()), path);
		} else {
			exRes = new ExceptionResponse(LocalDateTime.now(), ex.getCode(), ex.getMessage(), path);
		}
				
        return new ResponseEntity<Object>(exRes, HttpStatus.BAD_REQUEST);
    }
	
	/**
	 * 권한 없음 exception
	 */
	@ExceptionHandler(AccessDeniedException.class)
	protected final ResponseEntity<Object> handleDeniedExceptions(Exception ex, WebRequest request) {
		ex.printStackTrace();
		
		final String path = request.getDescription(false).substring(4);
		ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "ER002", MngUtil.message("ER002"), path);	//권한이 없습니다.
        
        return new ResponseEntity<Object>(exRes, HttpStatus.FORBIDDEN);
    }
	
	/**
	 * 그외 exception
	 */
	@ExceptionHandler(Exception.class)
	protected final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		System.out.println("그외 오류");
		ex.printStackTrace();
		
		LogUtil.writeLog(ex);
		
		final String path = request.getDescription(false).substring(4);
        ExceptionResponse exRes = new ExceptionResponse(LocalDateTime.now(), "ER001", MngUtil.message("ER001"), path);	//관리자에게 문의하세요.
        
        return new ResponseEntity<Object>(exRes, HttpStatus.INTERNAL_SERVER_ERROR);
    }
		
}
