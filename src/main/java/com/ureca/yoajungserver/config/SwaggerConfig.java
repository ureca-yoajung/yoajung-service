package com.ureca.yoajungserver.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        // 레디스 세션
        name = "SessionCookie",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "JSESSIONID_USER"
)
@SecurityScheme(
        // Oauth
        name = "KakaoOauth2",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "https://kauth.kakao.com/oauth/authorize",
                        tokenUrl = "https://kauth.kakao.com/oauth/token"
                )
        )
)
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addServersItem(new Server().url("/"))
                .info(new Info()
                        .title("yoajung-server API 문서")
                        .version("1.0.0")
                        .description("레디스 세션 인증 및 카카오 OAuth2.0 인가"));
    }
}

/*
  Redis 세션
  Swagger UI Authorize ->  SessionCookie -> 브라우저에 저장된 JSESSIONID_USER 쿠키가 포함
  Kakao OAuth2 인가
  KakaoOAuth2 -> Authorize 카카오 로그인 페이지로 리다이렉트
  동의 후 tokenUrl을 통해 액세스 토큰을 발급받고 Swagger에 저장
*/
