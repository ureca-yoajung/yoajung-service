package com.ureca.yoajungserver.api.oauth;

import com.ureca.yoajungserver.external.kakao.KakaoOAuthClient;
import com.ureca.yoajungserver.external.kakao.KakaoSignupRequest;
import com.ureca.yoajungserver.external.kakao.KakaoTokenResponse;
import com.ureca.yoajungserver.external.kakao.KakaoUserInfoResponse;
import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.plan.repository.PlanRepository;
import com.ureca.yoajungserver.user.entity.Role;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.exception.UserDuplicatedEmailException;
import com.ureca.yoajungserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OAuthServiceImpl implements OAuthService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public KakaoTokenResponse exchangeCodeForToken(String code) {
        return kakaoOAuthClient.requestToken(code);
    }

    @Override
    public User kakaoLoginOrGetAdditionalInfo(String accessToken) {
        KakaoUserInfoResponse userInfo = kakaoOAuthClient.requestUserInfo(accessToken);
        String email = userInfo.getKakao_account().getEmail();
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("카카오 이메일 동의 필요");
        }
        // 이메일로 조회
        return userRepository.findByEmailWithPlan(email).orElse(null);
        // Plan 즉시 사용시 fetch join 으로 영속성 트랜잭션 끝나기전에 같이 가져옴
    }

    @Override
    public Map<String, String> getEmailAndNameByToken(String accessToken) {
        KakaoUserInfoResponse userInfo = kakaoOAuthClient.requestUserInfo(accessToken);
        String email = userInfo.getKakao_account().getEmail();
        String nickname = userInfo.getKakao_account().getProfile().getNickname();
        return Map.of("email", email, "name", nickname);
    }

    @Override
    public KakaoUserInfoResponse getKakaoUserInfoByToken(String accessToken) {
        return kakaoOAuthClient.requestUserInfo(accessToken);
    }

    @Override
    public User kakaoSignup(KakaoSignupRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(
                u -> {
                    throw new UserDuplicatedEmailException();
                });
        Plan plan = null;
        if (request.getPlanId() != null) {
            plan = planRepository.findById(request.getPlanId()).orElse(null);
        }

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .ageGroup(request.getAgeGroup())
                .familyCount(request.getFamilyCount())
                .plan(plan)
                .password(passwordEncoder.encode("KAKAO"))
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }
}
