package com.ureca.yoajungserver.api.oauth;

import com.ureca.yoajungserver.external.kakao.KakaoSignupRequest;
import com.ureca.yoajungserver.external.kakao.KakaoTokenResponse;
import com.ureca.yoajungserver.external.kakao.KakaoUserInfoResponse;
import com.ureca.yoajungserver.user.entity.User;

import java.util.Map;

public interface OAuthService {
    KakaoTokenResponse exchangeCodeForToken(String code);

    User kakaoLoginOrGetAdditionalInfo(String accessToken);

    Map<String, String> getEmailAndNameByToken(String accessToken);

    KakaoUserInfoResponse getKakaoUserInfoByToken(String accessToken);

    User kakaoSignup(KakaoSignupRequest request);
}
