package com.ureca.yoajungserver.review.exception;

import com.ureca.yoajungserver.common.BaseCode;
import lombok.Getter;

@Getter
public class DuplicatedReviewLikeException extends IllegalArgumentException {

    private final BaseCode baseCode;
    public DuplicatedReviewLikeException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
