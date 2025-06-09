package com.ureca.yoajungserver.chatbot.interceptor;

import com.ureca.yoajungserver.chatbot.exception.BadWordDetectedException;
import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.component.GptBadWordComponent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
public class GptBadWordInterceptor implements HandlerInterceptor {

    private final GptBadWordComponent gptBadWordComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String question = request.getParameter("question");

        if (question == null || question.isBlank()) {
            return true;
        }

        if(!gptBadWordComponent.validate(question)){
            throw new BadWordDetectedException(BaseCode.CHAT_BAD_WORD_DETECTED);
        }
        return true;
    }
}
