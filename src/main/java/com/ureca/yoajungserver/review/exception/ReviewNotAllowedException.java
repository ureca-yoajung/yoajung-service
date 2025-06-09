package com.ureca.yoajungserver.review.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class ReviewNotAllowedException extends BusinessException {

    public ReviewNotAllowedException(BaseCode baseCode) {
        super(baseCode);
    }
}
