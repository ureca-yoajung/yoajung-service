package com.ureca.yoajungserver.chatbot.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String reason;

    private List<PersonalPlanRecommendResponse> personalPlanRecommendResponses;
}