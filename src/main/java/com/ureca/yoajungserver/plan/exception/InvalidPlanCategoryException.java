package com.ureca.yoajungserver.plan.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class InvalidPlanCategoryException extends BusinessException {

    public InvalidPlanCategoryException(BaseCode baseCode) {
        super(baseCode.INVALID_PLAN_CATEGORY);
    }
}
