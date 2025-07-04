package com.ureca.yoajungserver.review.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class PlanNotFoundException extends BusinessException {

    public PlanNotFoundException(BaseCode baseCode) {
        super(baseCode);
    }
}
