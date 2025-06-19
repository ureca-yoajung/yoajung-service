package com.ureca.yoajungserver.plan.exception;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class PlanNotFoundException extends BusinessException {

    public PlanNotFoundException() {
        super(BaseCode.PLAN_NOT_FOUND);
    }
}
