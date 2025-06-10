package com.ureca.yoajungserver.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ChatbotResponse {
    private final boolean complete;              // true: data 유효, false: message(질문) 유효
    private final String message;                // LLM이 던진 질문
    private final PlanKeywordResponse data;      // 최종 JSON 파싱 결과

    private ChatbotResponse(boolean complete, String message, PlanKeywordResponse data) {
        this.complete = complete;
        this.message = message;
        this.data = data;
    }

    public static ChatbotResponse question(String msg) {
        return new ChatbotResponse(false, msg, null);
    }

    public static ChatbotResponse result(PlanKeywordResponse planKeywordResponse) {
        return new ChatbotResponse(true, null, planKeywordResponse);
    }

    public boolean isComplete() {
        return complete;
    }

    public String getMessage() {
        return message;
    }

    public PlanKeywordResponse getData() {
        return data;
    }
}
