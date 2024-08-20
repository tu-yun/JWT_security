package com.xi.fmcs.support.service;

import org.springframework.stereotype.Service;

import com.xi.fmcs.support.repository.CommonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupportService {
	
	private final CommonRepository commonRepository;
	
	public String getStatusMessageByCode(String code) {
		return commonRepository.getStatusMessageByCode(code);
	}
	
}
