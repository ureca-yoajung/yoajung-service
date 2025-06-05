package com.ureca.yoajungserver.user.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.user.dto.reqeust.SendCodeRequest;
import com.ureca.yoajungserver.user.dto.reqeust.VerifyCodeRequest;
import com.ureca.yoajungserver.user.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.ureca.yoajungserver.common.BaseCode.EMAIL_CODE_SENT;
import static com.ureca.yoajungserver.common.BaseCode.EMAIL_VERIFICATION_SUCCESS;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/api/auth/send-code")
    public ResponseEntity<ApiResponse<Void>> sendCode(@Valid @RequestBody SendCodeRequest request, HttpSession session) {
        authService.sendVerificationCode(request, session);
        return ResponseEntity.ok(ApiResponse.ok(EMAIL_CODE_SENT));
    }
    //  사용자가 이메일 입력하고 인증 코드 받기 요청

    @PostMapping("/api/auth/verify-code")
    public ResponseEntity<ApiResponse<Void>> verifyCode(@Valid @RequestBody VerifyCodeRequest request, HttpSession httpSession) {
        authService.verifyCode(request, httpSession);
        return ResponseEntity.ok(ApiResponse.ok(EMAIL_VERIFICATION_SUCCESS));
    }
}
