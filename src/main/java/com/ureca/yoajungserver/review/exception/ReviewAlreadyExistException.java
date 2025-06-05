package com.ureca.yoajungserver.review.exception;

import com.ureca.yoajungserver.common.BaseCode;
import lombok.Getter;

@Getter
public class ReviewAlreadyExistException extends IllegalArgumentException {

    private final BaseCode baseCode;
    public ReviewAlreadyExistException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
