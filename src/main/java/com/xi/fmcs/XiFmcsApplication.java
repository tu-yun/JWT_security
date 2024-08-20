package com.xi.fmcs;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class XiFmcsApplication {
	
	@PostConstruct
	public void timezone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(XiFmcsApplication.class, args);
	}

}
