package com.xi.fmcs.config.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.xi.fmcs.config.security.handler.CustomAccessDeniedHandler;
import com.xi.fmcs.config.security.handler.CustomAuthenticationEntryPoint;
import com.xi.fmcs.config.security.handler.CustomLogoutSuccessHandler;
import com.xi.fmcs.config.security.jwt.JwtAuthenticationFilter;
import com.xi.fmcs.config.security.jwt.JwtAuthorizationFilter;
import com.xi.fmcs.config.security.jwt.JwtTokenUtil;
import com.xi.fmcs.config.security.model.JwtConst;
import com.xi.fmcs.domain.login.service.LoginService;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final LoginService loginService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(new MyCustomDsl())
                .and()
                .authorizeRequests((authorize) -> authorize
                        .antMatchers("/admin/**").hasAnyRole("admin")
                        .antMatchers("/manager/**").hasAnyRole("manager", "admin")
                        .antMatchers("/user/**").authenticated()
                        .anyRequest().permitAll())
                .build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilter(corsFilter())
                    .addFilterBefore(new WebSecurityExceptionFilter(), LogoutFilter.class)
                    .addFilterBefore(new JwtAuthenticationFilter(authenticationManager, loginService), UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(new JwtAuthorizationFilter(authenticationManager, loginService), UsernamePasswordAuthenticationFilter.class);
        }
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        //swagger security필터 제외
        return (web) -> web.ignoring().antMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**");
    }
    
	@Bean
	CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config =  new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedMethod("*");
		config.addAllowedHeader("*");
		config.addExposedHeader("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}