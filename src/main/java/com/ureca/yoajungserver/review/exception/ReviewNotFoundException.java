package com.ureca.yoajungserver.review.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class ReviewNotFoundException extends BusinessException {

    public ReviewNotFoundException(BaseCode baseCode) {
        super(baseCode);
    }
}
