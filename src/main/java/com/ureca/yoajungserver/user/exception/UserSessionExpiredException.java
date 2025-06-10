package com.ureca.yoajungserver.user.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;

public class UserSessionExpiredException extends BusinessException {
    public UserSessionExpiredException() {
        super(BaseCode.USER_SESSION_EXPIRED);
    }
}
