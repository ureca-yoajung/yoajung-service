package com.ureca.yoajungserver.review.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class DuplicatedReviewLikeException extends BusinessException {

    public DuplicatedReviewLikeException(BaseCode baseCode) {
        super(baseCode);
    }
}
