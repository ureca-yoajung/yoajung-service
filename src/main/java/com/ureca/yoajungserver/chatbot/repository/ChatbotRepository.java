package com.ureca.yoajungserver.chatbot.repository;

import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import com.ureca.yoajungserver.chatbot.dto.PersonalPlanRecommendResponse;

import java.util.List;

public interface ChatbotRepository {
    List<PersonalPlanRecommendResponse> recommendPlans(PlanKeywordResponse keyword);
}
