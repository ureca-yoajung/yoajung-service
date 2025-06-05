package com.ureca.yoajungserver.review.exception;

import com.ureca.yoajungserver.common.BaseCode;
import lombok.Getter;

@Getter
public class PlanNotFoundException extends IllegalArgumentException {

    private final BaseCode baseCode;
    public PlanNotFoundException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
