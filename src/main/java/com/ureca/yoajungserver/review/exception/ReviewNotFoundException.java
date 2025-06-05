package com.ureca.yoajungserver.review.exception;

import com.ureca.yoajungserver.common.BaseCode;
import lombok.Getter;

@Getter
public class ReviewNotFoundException extends IllegalArgumentException {

    private final BaseCode baseCode;
    public ReviewNotFoundException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
