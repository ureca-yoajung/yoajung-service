package com.ureca.yoajungserver.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.yoajungserver.chatbot.dto.ChatbotResponse;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.ureca.yoajungserver.chatbot.dto.PersonalPlanRecommendResponse;
import com.ureca.yoajungserver.chatbot.repository.ChatbotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final ChatbotRepository chatbotRepository;

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
        return ChatbotResponse.result(planKeywordResponse);
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

    // 요금제 조회
    @Override
    public List<PersonalPlanRecommendResponse> planList(PlanKeywordResponse keywordResponse) {

        return chatbotRepository.recommendPlans(keywordResponse);
    }
}