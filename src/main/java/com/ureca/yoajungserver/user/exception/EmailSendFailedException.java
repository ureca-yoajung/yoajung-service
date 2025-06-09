package com.ureca.yoajungserver.user.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;

public class EmailSendFailedException extends BusinessException {
    public EmailSendFailedException() {
        super(BaseCode.EMAIL_SEND_FAILED);
    }
}
