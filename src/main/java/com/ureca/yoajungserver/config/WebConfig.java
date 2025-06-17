package com.ureca.yoajungserver.config;

import com.ureca.yoajungserver.chatbot.interceptor.GptBadWordInterceptor;
import com.ureca.yoajungserver.chatbot.interceptor.LocalBadWordInterceptor;
import com.ureca.yoajungserver.common.component.GptBadWordComponent;
import com.ureca.yoajungserver.common.component.LocalBadWordComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final GptBadWordComponent gptBadWordComponent;
    private final LocalBadWordComponent localBadWordComponent;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GptBadWordInterceptor(gptBadWordComponent))
                .order(1)
                .addPathPatterns("/chat")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LocalBadWordInterceptor(localBadWordComponent))
                .order(2)
                .addPathPatterns("/chat")
                .excludePathPatterns("/css/**", "/*.ico", "/error");
    }
}
