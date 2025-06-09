package com.ureca.yoajungserver.user.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;

public class EmailCodeExpiredException extends BusinessException {
    public EmailCodeExpiredException() {
        super(BaseCode.EMAIL_CODE_EXPIRED);
    }
}
