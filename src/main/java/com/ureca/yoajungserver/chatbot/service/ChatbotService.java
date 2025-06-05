package com.ureca.yoajungserver.chatbot.service;

import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import java.io.IOException;

public interface ChatbotService {

    PlanKeywordResponse keywordMapper(String input) throws IOException;
}
