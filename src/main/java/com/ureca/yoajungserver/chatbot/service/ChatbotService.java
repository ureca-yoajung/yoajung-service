package com.ureca.yoajungserver.chatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ureca.yoajungserver.chatbot.dto.PersonalPlanRecommendResponse;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import java.io.IOException;
import java.util.List;

public interface ChatbotService {

    List<PersonalPlanRecommendResponse> keywordMapper(String input, String userId) throws IOException;

    List<PersonalPlanRecommendResponse> planList(PlanKeywordResponse keywordResponse);

    List<PersonalPlanRecommendResponse> keywordMapperByPreferences(String question, Long userId);

    String responseMapper(String input, String userId, PlanKeywordResponse keywordResponse, List<PersonalPlanRecommendResponse> result) throws JsonProcessingException;
}
