package com.ureca.yoajungserver.user.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;

public class UserDuplicatedEmailException extends BusinessException {
    public UserDuplicatedEmailException() {
        super(BaseCode.USER_DUPLICATED_EMAIL);
    }
}
