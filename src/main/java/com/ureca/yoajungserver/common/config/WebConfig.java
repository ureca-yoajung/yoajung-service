package com.ureca.yoajungserver.common.config;

import com.ureca.yoajungserver.chatbot.interceptor.GptBadWordInterceptor;
import com.ureca.yoajungserver.chatbot.interceptor.LocalBadWordInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiModerationModel;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final OpenAiModerationModel moderationModel;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GptBadWordInterceptor(moderationModel))
                .order(1)
                .addPathPatterns("/test")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LocalBadWordInterceptor())
                .order(2)
                .addPathPatterns("/test")
                .excludePathPatterns("/css/**", "/*.ico", "/error");
    }
}
