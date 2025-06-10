package com.ureca.yoajungserver.user.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;

public class PasswordResetTokenInvalidOrExpiredException extends BusinessException {
    public PasswordResetTokenInvalidOrExpiredException() {
        super(BaseCode.PASSWORD_RESET_TOKEN_INVALID);
    }
}
