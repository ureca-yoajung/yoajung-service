package com.ureca.yoajungserver.user.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;

public class UserLoginFailException extends BusinessException {
    public UserLoginFailException() {
        super(BaseCode.USER_LOGIN_FAIL);
    }
}
