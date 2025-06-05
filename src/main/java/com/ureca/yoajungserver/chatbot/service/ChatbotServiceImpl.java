package com.ureca.yoajungserver.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    @Override
    public PlanKeywordResponse keywordMapper(String input) throws IOException {
        String inlineTemplate = """
                
                """;

        // ChatClient에 Prompt 전달 → LLM 호출 → ChatResponse 획득
        var chatResponse = chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text(inlineTemplate)
                        .param("input", input))
                .call();

        // LLM이 반환한 컨텐츠(문자열) 추출
        String llmOutput = chatResponse.content().trim();
        System.out.println("LLM JSON 응답 →\n" + llmOutput);

        // JSON 시작/끝이 확실한지 검사
        String trimmed = llmOutput.strip();
        if (!(trimmed.startsWith("{") && trimmed.endsWith("}"))) {
            throw new IllegalStateException("JSON 형식 오류");
        }

        // 실제 파싱 시도
        PlanKeywordResponse planKeywordResponse;
        try {
            planKeywordResponse = objectMapper.readValue(trimmed, PlanKeywordResponse.class);
        } catch (IOException e) {
            throw new IOException("JSON 매핑 실패");
        }

        // 필수 필드 검증
        if (planKeywordResponse.getPrice() == null
                || planKeywordResponse.getCallAllowance() == null
                || planKeywordResponse.getSmsAllowance() == null
                || planKeywordResponse.getDataAllowance() == null
                || planKeywordResponse.getTetheringSharing() == null
                || planKeywordResponse.getMediaService() == null
                || planKeywordResponse.getPremiumService() == null) {
            throw new IllegalArgumentException("필수 필드에 null 값은 불가능합니다.");
        }

        return planKeywordResponse;
    }
}
