package com.ureca.yoajungserver.chatbot.service;

import com.ureca.yoajungserver.chatbot.dto.ChatbotResponse;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import com.ureca.yoajungserver.chatbot.dto.PersonalPlanRecommendResponse;

import java.io.IOException;
import java.util.List;

public interface ChatbotService {

    ChatbotResponse keywordMapper(String input, String userId) throws IOException;
    List<PersonalPlanRecommendResponse> planList(PlanKeywordResponse keywordResponse);
}
