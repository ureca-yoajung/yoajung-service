package com.ureca.yoajungserver.chatbot.repository;

import com.ureca.yoajungserver.chatbot.entity.ChatHistory;
import com.ureca.yoajungserver.chatbot.entity.ChatMemory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemoryRepository extends JpaRepository<ChatMemory, Long> {
    void deleteByConversationId(String conversationId);
}
