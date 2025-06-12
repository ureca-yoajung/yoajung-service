package com.ureca.yoajungserver.chatbot.service;

import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import com.ureca.yoajungserver.chatbot.dto.PersonalPlanRecommendResponse;

import java.io.IOException;
import java.util.List;

public interface ChatbotService {

    List<PersonalPlanRecommendResponse> keywordMapper(String input, String userId) throws IOException;
}
