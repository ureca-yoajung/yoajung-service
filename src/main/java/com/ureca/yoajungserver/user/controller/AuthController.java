package com.ureca.yoajungserver.user.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.user.dto.reqeust.LoginRequest;
import com.ureca.yoajungserver.user.dto.reqeust.SendCodeRequest;
import com.ureca.yoajungserver.user.dto.reqeust.VerifyCodeRequest;
import com.ureca.yoajungserver.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
    public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        httpServletRequest.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        return ResponseEntity.ok(ApiResponse.ok(USER_LOGIN_SUCCESS));
    }

    /**
     * 시큐리티 컨텍스트가 세션(Redis)에 저장이 안되는 trouble
     * WHY Security Filter Chain이 동작안함, 세션에 직접 저장해야된다.
     * <p>
     * <p>
     * -> 직렬화 에러User 엔티티를 그대로 쓰니까  스프링 - 세션 레디스는 컨텍스트를 지정할 때 JDK 직렬화로
     * 객체를 바이트로 바꿔서 문제 근데 엔티티 직렬화안됨
     */

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(ApiResponse.ok(USER_LOGOUT_SUCCESS));
    }
}

// 스프링 시큐리티가 세션/권한/암호화/세션고정등 제공해서 AuthenticationManager 채택