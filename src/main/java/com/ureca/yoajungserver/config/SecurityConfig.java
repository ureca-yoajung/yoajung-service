package com.ureca.yoajungserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.common.BaseCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final ObjectMapper objectMapper;

    public SecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login.html")
                        .maximumSessions(1)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login.html", "/signup.html", "/reset-password.html"
                                , "/reset-request.html", "main.html",
                                "/api/oauth/**", "/api/auth/**", "callback.html"
                        ).permitAll()
                        .requestMatchers(
                                "/api/user/**",
                                "/api/tendency/**"
                        ).authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(BaseCode.STATUS_UNAUTHORIZED.getStatus().value());
                            response.setContentType("application/json; charset=UTF-8");

                            ApiResponse<Void> apiResponse = ApiResponse.ok(BaseCode.STATUS_UNAUTHORIZED);
                            objectMapper.writeValue(response.getWriter(), apiResponse);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(BaseCode.STATUS_FORBIDDEN.getStatus().value());
                            response.setContentType("application/json; charset=UTF-8");

                            ApiResponse<Void> apiResponse = ApiResponse.ok(BaseCode.STATUS_FORBIDDEN);
                            objectMapper.writeValue(response.getWriter(), apiResponse);
                        })
                )
        // 세션관리
        // 예외처리
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
