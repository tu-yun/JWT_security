package com.xi.fmcs.config.security.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jwt")
public class JwtConst {
	
	private String subject;
	private Access access;
	private Refresh refresh; 
	
	@Data
	public static class Access {
		private String headerKey;
		private String secretKey;
		private Long expireTime;
	}

	@Data
	public static class Refresh {
		private String headerKey;
		private String secretKey;
		private Long expireTime;
	}
	
}
