package com.ureca.yoajungserver.api.oauth;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.external.kakao.KakaoSignupRequest;
import com.ureca.yoajungserver.external.kakao.KakaoTokenResponse;
import com.ureca.yoajungserver.swagger.api.OAuthControllerSwagger;
import com.ureca.yoajungserver.user.dto.response.UserResponse;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.ureca.yoajungserver.common.BaseCode.USER_ADDITIONAL_INFO_REQUIRED;
import static com.ureca.yoajungserver.common.BaseCode.USER_LOGIN_SUCCESS;

@RestController
@RequestMapping("/api/oauth/kakao")
@RequiredArgsConstructor
public class OAuthController implements OAuthControllerSwagger {
    private final OAuthService oAuthService;

    // 인가 코드 -> 토큰 변환
    @GetMapping("/token")
    public Map<String, String> getAccessToken(@RequestParam("code") String code) {
        KakaoTokenResponse kakaoTokenResponse = oAuthService.exchangeCodeForToken(code);
        return Map.of("accessToken", kakaoTokenResponse.getAccess_token());
    }

    // 액세스 토큰으로 회원조회
    @GetMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> kakaoLogin(
            @RequestParam("accessToken") String accessToken, HttpServletRequest request
    ) {
        Map<String, Object> result = new HashMap<>();
        User user = oAuthService.kakaoLoginOrGetAdditionalInfo(accessToken);
        if (user != null) {
            setAuthenticationSession(user, request);
            result.put("user", UserResponse.fromEntity(user));
            return ResponseEntity.ok(ApiResponse.of(USER_LOGIN_SUCCESS, result));
        }
        Map<String, String> info = oAuthService.getEmailAndNameByToken(accessToken);
        result.putAll(info);
        result.put("accessToken", accessToken);
        return ResponseEntity.ok(ApiResponse.of(USER_ADDITIONAL_INFO_REQUIRED, result));
    }

    private void setAuthenticationSession(User user, HttpServletRequest request) {
        CustomUserDetails userDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );
    }

    // 추가정보기입후 회원가입
    @PostMapping("signup")
    public ResponseEntity<ApiResponse<UserResponse>> kakaoSignup(
            @Valid @RequestBody KakaoSignupRequest request, HttpServletRequest req
    ) {
        User user = oAuthService.kakaoSignup(request);
        setAuthenticationSession(user, req);
        return ResponseEntity.ok(ApiResponse.of(USER_LOGIN_SUCCESS, UserResponse.fromEntity(user)));
    }

}
