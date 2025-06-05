package com.ureca.yoajungserver.chatbot.interceptor;

import com.ureca.yoajungserver.chatbot.exception.BadWordDetectedException;
import com.ureca.yoajungserver.common.BaseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.moderation.*;
import org.springframework.ai.openai.OpenAiModerationModel;
import org.springframework.ai.openai.OpenAiModerationOptions;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
public class GptBadWordInterceptor implements HandlerInterceptor {

    private final OpenAiModerationModel moderationModel;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String question = request.getParameter("question");

        if (question == null || question.isBlank()) {
            return true;
        }

        OpenAiModerationOptions options = OpenAiModerationOptions.builder()
                .model("omni-moderation-latest")
                .build();

        ModerationPrompt prompt = new ModerationPrompt(question, options);

        ModerationResponse moderationResponse = moderationModel.call(prompt);
        Moderation moderation = moderationResponse.getResult().getOutput();
        ModerationResult result = moderation.getResults().get(0);

        boolean flagged = result.isFlagged();

        if (flagged) {
            throw new BadWordDetectedException(BaseCode.CHAT_BAD_WORD_DETECTED);
        }

        return true;
    }
}
