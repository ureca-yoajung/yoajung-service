package com.ureca.yoajungserver.common.component;

import com.ureca.yoajungserver.chatbot.exception.BadWordDetectedException;
import com.ureca.yoajungserver.common.BaseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.moderation.Moderation;
import org.springframework.ai.moderation.ModerationPrompt;
import org.springframework.ai.moderation.ModerationResponse;
import org.springframework.ai.moderation.ModerationResult;
import org.springframework.ai.openai.OpenAiModerationModel;
import org.springframework.ai.openai.OpenAiModerationOptions;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GptBadWordComponent {

    private final OpenAiModerationModel moderationModel;

    public boolean validate(String question) {
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
