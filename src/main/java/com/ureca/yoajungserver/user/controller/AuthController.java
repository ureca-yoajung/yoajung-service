package com.ureca.yoajungserver.user.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.user.dto.reqeust.LoginRequest;
import com.ureca.yoajungserver.user.dto.reqeust.SendCodeRequest;
import com.ureca.yoajungserver.user.dto.reqeust.VerifyCodeRequest;
import com.ureca.yoajungserver.user.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ureca.yoajungserver.common.BaseCode.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse<Void>> sendCode(@Valid @RequestBody SendCodeRequest request, HttpSession session) {
        authService.sendVerificationCode(request, session);
        return ResponseEntity.ok(ApiResponse.ok(EMAIL_CODE_SENT));
    }
    //  사용자가 이메일 입력하고 인증 코드 받기 요청

    @PostMapping("/verify-code")
    public ResponseEntity<ApiResponse<Void>> verifyCode(@Valid @RequestBody VerifyCodeRequest request, HttpSession httpSession) {
        authService.verifyCode(request, httpSession);
        return ResponseEntity.ok(ApiResponse.ok(EMAIL_VERIFICATION_SUCCESS));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(ApiResponse.ok(USER_LOGIN_SUCCESS));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(ApiResponse.ok(USER_LOGOUT_SUCCESS));
    }
}

// 스프링 시큐리티가 세션/권한/암호화/세션고정등 제공해서 AuthenticationManager 채택