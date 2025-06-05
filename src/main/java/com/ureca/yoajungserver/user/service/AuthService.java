package com.ureca.yoajungserver.user.service;

import com.ureca.yoajungserver.user.dto.reqeust.SendCodeRequest;
import com.ureca.yoajungserver.user.dto.reqeust.VerifyCodeRequest;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    void sendVerificationCode(SendCodeRequest request, HttpSession httpSession);
    // 이메일 인증코드 발송

    void verifyCode(VerifyCodeRequest request, HttpSession httpSession);
    // 인증 코드 검증
}
