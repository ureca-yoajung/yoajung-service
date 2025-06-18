package com.ureca.yoajungserver.chatbot.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;

public class KeywordExtractionFailedException extends BusinessException {

    public KeywordExtractionFailedException(BaseCode baseCode) {
        super(baseCode);
    }
}
