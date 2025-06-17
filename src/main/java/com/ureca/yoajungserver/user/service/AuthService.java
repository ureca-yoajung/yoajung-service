package com.ureca.yoajungserver.user.service;

public interface AuthService {
    void sendVerificationCode(String email);
    // 이메일 인증코드 발송

    boolean verifyCode(String email, String code);
    // 인증 코드 검증
}
