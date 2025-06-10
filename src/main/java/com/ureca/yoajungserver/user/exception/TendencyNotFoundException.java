package com.ureca.yoajungserver.user.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;

public class TendencyNotFoundException extends BusinessException {
    public TendencyNotFoundException() {
        super(BaseCode.TENDENCY_NOT_FOUND);
    }
}
