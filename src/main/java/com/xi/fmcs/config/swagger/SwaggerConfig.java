package com.xi.fmcs.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
    private static final String API_NAME = "Xi FMCS";
    private static final String API_VERSION = "0.0.1";
    private static final String API_DESCRIPTION = "API Documentation";
    private static final String API_COMPANY = "DNI";
    
    @Bean
    OpenAPI openAPI() {
    	final String securitySchemeName = "Bearer Authentication";
    	Info info = new Info()
    			.title(API_NAME)
    			.version(API_VERSION)
    			.description(API_DESCRIPTION)
    			.contact(new Contact().name(API_COMPANY).email(" "));
    			
    	return new OpenAPI()
    			.info(info)
    			.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
    			.components(
    					new Components()    					
    					.addSecuritySchemes(securitySchemeName, new SecurityScheme()
    							.name(securitySchemeName)
    							.type(SecurityScheme.Type.HTTP)
    							.scheme("bearer")
    							.bearerFormat("JWT")
    							.description("A JWT Access Token is required to access this API")));
    }

}
