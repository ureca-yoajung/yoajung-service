package com.ureca.yoajungserver.user.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;

public class EmailAlredayVerifiedException extends BusinessException {
    public EmailAlredayVerifiedException() {
        super(BaseCode.EMAIL_ALREADY_VERIFIED);
    }
}
