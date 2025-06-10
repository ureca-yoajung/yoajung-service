package com.ureca.yoajungserver.user.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;

public class UserDuplicatedPhoneNumberException extends BusinessException {
    public UserDuplicatedPhoneNumberException() {
        super(BaseCode.USER_DUPLICATED_PHONE_NUMBER);
    }
}
