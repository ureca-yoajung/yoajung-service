package com.ureca.yoajungserver.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    @Override
    public PlanKeywordResponse keywordMapper(String input) throws IOException {
        Resource promptResource = resourceLoader.getResource("classpath:prompt/prompt.txt");

        String inlineTemplate = getPrompt(promptResource);

        // ChatClient 에 Prompt 전달 → LLM 호출 → ChatResponse 획득
        var chatResponse = chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text(inlineTemplate)
                        .param("input", input))
                .call();

        String llmOutput = Objects.requireNonNull(chatResponse.content()).strip();
        log.info("LLM JSON 응답: {}", llmOutput);

        // JSON 시작/끝이 확실한지 검사
        checkJsonForm(llmOutput);

        // 실제 파싱 시도
        PlanKeywordResponse planKeywordResponse = parseLlmOutput(llmOutput);

        // 필수 필드 검증
        checkKeywordField(planKeywordResponse);

        return planKeywordResponse;
    }

    private String getPrompt(Resource promptResource) throws IOException {
        String inlineTemplate;
        try (InputStream is = promptResource.getInputStream()) {
            inlineTemplate = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IOException("프롬프트 파일을 읽어오는 도중 에러가 발생했습니다.", e);
        }
        return inlineTemplate;
    }

    private void checkJsonForm(String trimmed) {
        if (!(trimmed.startsWith("{") && trimmed.endsWith("}"))) {
            throw new IllegalStateException("JSON 형식 오류");
        }
    }

    private PlanKeywordResponse parseLlmOutput(String llmOutput) throws IOException {
        PlanKeywordResponse planKeywordResponse;
        try {
            planKeywordResponse = objectMapper.readValue(llmOutput, PlanKeywordResponse.class);
        } catch (IOException e) {
            throw new IOException("JSON 매핑 실패");
        }
        return planKeywordResponse;
    }

    private void checkKeywordField(PlanKeywordResponse planKeywordResponse) {
        if (planKeywordResponse.getPrice() == null
                || planKeywordResponse.getCallAllowance() == null
                || planKeywordResponse.getSmsAllowance() == null
                || planKeywordResponse.getDataAllowance() == null
                || planKeywordResponse.getTetheringSharing() == null
                || planKeywordResponse.getMediaService() == null
                || planKeywordResponse.getPremiumService() == null) {
            throw new IllegalArgumentException("필수 필드에 null 값은 불가능합니다.");
        }
    }
}
