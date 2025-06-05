package com.ureca.yoajungserver.review.exception;

import com.ureca.yoajungserver.common.BaseCode;
import lombok.Getter;

@Getter
public class UserNotFoundException extends IllegalArgumentException {

    private final BaseCode baseCode;
    public UserNotFoundException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
