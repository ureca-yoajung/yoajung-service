package com.ureca.yoajungserver.user.dto.reqeust;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {
    @Email(message = "이메일 형식을 지키세요")
    @NotBlank(message = "필수 입력입니다")
    private String email;
}

// 이메일 인증 코드 요청 시 바디로 사용