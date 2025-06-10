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
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    @Override
    public ChatbotResponse keywordMapper(String input, String userId) throws IOException {

        // ChatClient에 Prompt 전달 → LLM 호출 → ChatResponse 획득
        // 강화된 시스템 프롬프트와 ChatMemory(대화기록) 덕분에 LLM이 대화를 주도합니다.
        String llmOutput = Objects.requireNonNull(chatClient.prompt()
                        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                        .user(input)
                        .call()
                        .content())
                .strip();

        log.info("LLM 응답 (User ID: {}): {}", userId, llmOutput);

        // LLM 응답이 JSON 형식인지 확인하여 분기 처리
        if (isJsonResponse(llmOutput)) {
            // JSON 응답이면, 모든 정보가 수집 완료된 상태
            try {
                PlanKeywordResponse planKeywordResponse = objectMapper.readValue(llmOutput, PlanKeywordResponse.class);
                // 성공적으로 파싱되면 최종 결과를 반환합니다.
                return ChatbotResponse.result(planKeywordResponse);
            } catch (IOException e) {
                // LLM이 JSON과 유사하지만 잘못된 형식의 텍스트를 생성한 경우,
                // 이를 사용자에게 다시 질문하도록 처리할 수 있습니다.
                log.warn("JSON 파싱 실패, LLM의 응답을 질문으로 간주: {}", llmOutput, e);
                return ChatbotResponse.question("죄송합니다, 이해하지 못했어요. 조금 더 자세히 설명해주시겠어요?");
            }
        } else {
            // JSON이 아니면, 정보 수집을 위한 추가 질문 상태
            // LLM이 생성한 다음 질문을 그대로 사용자에게 전달합니다.
            return ChatbotResponse.question(llmOutput);
        }
    }

    /**
     * LLM의 응답이 JSON 형식인지 간단히 확인하는 헬퍼 메소드
     *
     * @param response LLM의 응답 문자열
     * @return JSON 형식이면 true, 아니면 false
     */
    private boolean isJsonResponse(String response) {
        return response.startsWith("{") && response.endsWith("}");
    }
}