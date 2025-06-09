package com.ureca.yoajungserver.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.yoajungserver.chatbot.dto.ChatbotResponse;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    @Override
    public ChatbotResponse keywordMapper(String input, String userId) throws IOException {

        // ChatClient 에 Prompt 전달 → LLM 호출 → ChatResponse 획득
        String llmOutput = Objects.requireNonNull(chatClient.prompt()
                        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                        .user(input)
                        .call()
                        .content())
                .strip();

        log.info("LLM JSON 응답: {}", llmOutput);

        // JSON 시작/끝이 확실한지 검사
        checkJsonForm(llmOutput);

        // 실제 파싱 시도
        PlanKeywordResponse planKeywordResponse = parseLlmOutput(llmOutput);

        // 필수 필드 검증
        return validateAndAskMissing(planKeywordResponse);
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

    private ChatbotResponse validateAndAskMissing(PlanKeywordResponse plan) {
        if (plan.getPrice() == null) {
            return ChatbotResponse.question("월 청구액(price)을 알려주세요.");
        }
        if (plan.getCallAllowance() == null || plan.getCallAllowance().isBlank()) {
            return ChatbotResponse.question("음성 통화량(callAllowance)을 알려주세요.");
        }
        if (plan.getSmsAllowance() == null || plan.getSmsAllowance().isBlank()) {
            return ChatbotResponse.question("문자 메시지 사용량(smsAllowance)을 알려주세요.");
        }
        if (plan.getDataAllowance() == null || plan.getDataAllowance().isBlank()) {
            return ChatbotResponse.question("데이터 사용량(dataAllowance)을 알려주세요.");
        }
        if (plan.getTetheringSharing() == null || plan.getTetheringSharing().isBlank()) {
            return ChatbotResponse.question("테더링/공유(tetheringSharing) 여부를 알려주세요.");
        }
        if (plan.getMediaService() == null || plan.getMediaService().isBlank()) {
            return ChatbotResponse.question("미디어 서비스(mediaService) 선호 여부를 알려주세요.");
        }
        if (plan.getPremiumService() == null || plan.getPremiumService().isBlank()) {
            return ChatbotResponse.question("프리미엄 서비스(premiumService) 이용 여부를 알려주세요.");
        }
        // 모든 필드가 유효하면 완료 응답 반환
        return ChatbotResponse.result(plan);
    }
}
