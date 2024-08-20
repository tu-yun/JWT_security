package com.xi.fmcs.config.security.configuration;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.xi.fmcs.config.security.model.CorsConst;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;

import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

	private final String dateFormat = "yyyy-MM-dd";
	private final String datetimeFormat = "yyyy-MM-dd HH:mm:ss.SSS";

	private final CorsConst corsConst;
	
	@Bean
	CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config =  new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(corsConst.getOrigins());
		config.setAllowedMethods(corsConst.getMethods());
		config.addAllowedHeader("*");
		config.addExposedHeader("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
	
    @Bean
    PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }
 
    @Bean
    Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return jacksonObjectMapperBuilder -> {
			jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("UTC"));
			jacksonObjectMapperBuilder.simpleDateFormat(datetimeFormat);	//Date
			jacksonObjectMapperBuilder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));	//LocalDate
			jacksonObjectMapperBuilder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(datetimeFormat)));	//LocalDateTime
		};
    }
    
    @Bean
    SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
    	return new SqlSessionTemplate(sqlSessionFactory);
    }
    
	@Bean
	MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.addBasenames("classpath:validationMessage");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	
	@Bean
	LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);
		return bean;
	}
    
    
    
    
    
    
   
	//개발완료 후 삭제
	@Value("${accesToken-temp}")
	private String accessToken_temp;
	
    //개발완료 후 삭제
    @Bean
    OperationCustomizer operationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            Parameter param = new Parameter()
                    .in(ParameterIn.HEADER.toString())  // 전역 헤더 설정
                    .schema(new StringSchema()
                    		._default(accessToken_temp)
                    		.name("Authorization_temp")) // default값 설정
                    .name("Authorization_temp")
                    .description("Access Token")
                    .required(true);
            operation.addParametersItem(param);
            return operation;
        };
    }

}
