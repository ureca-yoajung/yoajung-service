package com.ureca.yoajungserver.chatbot.service;

import com.ureca.yoajungserver.chatbot.dto.ChatbotResponse;
import java.io.IOException;

public interface ChatbotService {

    ChatbotResponse keywordMapper(String input, String userId) throws IOException;
}
