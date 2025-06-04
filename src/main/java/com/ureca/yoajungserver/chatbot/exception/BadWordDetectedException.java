package com.ureca.yoajungserver.chatbot.exception;

import com.ureca.yoajungserver.common.BaseCode;
import lombok.Getter;

@Getter
public class BadWordDetectedException extends IllegalStateException {
    private final BaseCode baseCode;

    public BadWordDetectedException(BaseCode baseCode) {
        super(baseCode.getMessage());
        this.baseCode = baseCode;
    }
}
