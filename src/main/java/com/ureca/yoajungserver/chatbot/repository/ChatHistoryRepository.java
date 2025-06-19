package com.ureca.yoajungserver.chatbot.repository;

import com.ureca.yoajungserver.chatbot.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
}
