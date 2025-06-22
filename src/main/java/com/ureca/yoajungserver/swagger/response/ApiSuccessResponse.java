package com.ureca.yoajungserver.swagger.response;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(METHOD)
@Retention(RUNTIME)
@ApiResponse(responseCode = "200")
public @interface ApiSuccessResponse {
    @AliasFor(annotation = ApiResponse.class,
            attribute = "description")
    String description() default "요청 성공";
}
