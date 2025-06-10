package com.ureca.yoajungserver.user.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;

public class EmailNotVerifiedException extends BusinessException {
    public EmailNotVerifiedException() {
        super(BaseCode.EMAIL_NOT_VERIFIED);
    }
}
