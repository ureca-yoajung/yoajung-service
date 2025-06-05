package com.ureca.yoajungserver.chatbot.advice;

import com.ureca.yoajungserver.chatbot.exception.BadWordDetectedException;
import com.ureca.yoajungserver.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ChatBotControllerAdvice {

    @ExceptionHandler(BadWordDetectedException.class)
    public ResponseEntity<ApiResponse<?>> handleBadWordDetectedException(BadWordDetectedException e) {
        log.warn("[exceptionHandle] ex", e);

        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }
}
