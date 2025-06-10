package com.ureca.yoajungserver.user.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;

public class EmailCodeMismatchException extends BusinessException {
    public EmailCodeMismatchException() {
        super(BaseCode.EMAIL_CODE_MISMATCH);
    }
}
