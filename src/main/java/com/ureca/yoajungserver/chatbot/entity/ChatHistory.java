package com.ureca.yoajungserver.chatbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "chatHistory" )
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id", length = 36, nullable = false)
    private String conversationId;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatType type;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Builder
    private ChatHistory(String conversationId, String content, ChatType type, LocalDateTime timestamp) {
        this.conversationId = conversationId;
        this.content = content;
        this.type = type;
        this.timestamp = timestamp;
    }
}
