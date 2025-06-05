package com.ureca.yoajungserver.review.exception;

import com.ureca.yoajungserver.common.BaseCode;
import lombok.Getter;

@Getter
public class ReviewNotAllowedException extends IllegalArgumentException {

    private final BaseCode baseCode;
    public ReviewNotAllowedException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
