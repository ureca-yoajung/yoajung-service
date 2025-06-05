package com.ureca.yoajungserver.review.exception;

import com.ureca.yoajungserver.common.BaseCode;
import lombok.Getter;

@Getter
public class NotReviewAuthorException extends IllegalArgumentException {

    private final BaseCode baseCode;
    public NotReviewAuthorException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
