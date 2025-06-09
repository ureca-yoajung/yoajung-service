package com.ureca.yoajungserver.user.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;

public class TendencyAlreadyExistsException extends BusinessException {
    public TendencyAlreadyExistsException() {
        super(BaseCode.TENDENCY_ALREADY_EXISTS);
    }
}
