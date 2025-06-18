package com.ureca.yoajungserver.chatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ureca.yoajungserver.chatbot.dto.ChatResponse;
import com.ureca.yoajungserver.chatbot.dto.PersonalPlanRecommendResponse;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import java.io.IOException;
import java.util.List;

public interface ChatbotService {

    ChatResponse keywordMapper(String input, String userId) throws IOException;

    List<PersonalPlanRecommendResponse> planList(PlanKeywordResponse keywordResponse);

    ChatResponse keywordMapperByPreferences(String question, Long userId) throws JsonProcessingException;

    String responseMapper(String input, String userId, PlanKeywordResponse keywordResponse, List<PersonalPlanRecommendResponse> result) throws JsonProcessingException;
}
