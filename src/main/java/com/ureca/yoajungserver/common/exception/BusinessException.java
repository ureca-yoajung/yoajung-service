package com.ureca.yoajungserver.common.exception;

import com.ureca.yoajungserver.common.BaseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final BaseCode baseCode;

    public BusinessException(BaseCode baseCode) {
        super((baseCode.getCode()));
        this.baseCode = baseCode;
    }
}

// 예외를 비즈니스와 시스템으로 분리
// 비즈니스 로직 예외 : 중복 이메일 이런것과 같이 사용자가 잘못한 거는
// IllegalArgumentException -> 400 에러로 처리했었는데 예상가능한 비즈니스 에러를 구분 RuntimeException 상속
// 서비스에서 던져서 컨트롤러에서 전역 예외핸들러에서 처리