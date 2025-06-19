package com.ureca.yoajungserver.oauth.service;

import com.ureca.yoajungserver.api.oauth.OAuthServiceImpl;
import com.ureca.yoajungserver.external.kakao.KakaoOAuthClient;
import com.ureca.yoajungserver.external.kakao.KakaoSignupRequest;
import com.ureca.yoajungserver.external.kakao.KakaoTokenResponse;
import com.ureca.yoajungserver.external.kakao.KakaoUserInfoResponse;
import com.ureca.yoajungserver.oauth.fixture.KakaoSignupRequestFixture;
import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.plan.repository.PlanRepository;
import com.ureca.yoajungserver.user.entity.Role;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.exception.UserDuplicatedEmailException;
import com.ureca.yoajungserver.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuthServiceImplTest {

    @InjectMocks
    private OAuthServiceImpl oAuthService;

    @Mock
    private KakaoOAuthClient kakaoOAuthClient;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PlanRepository planRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("로그인 했다가 추가입력 창에서 다른기기로 회원가입하면 중복 이메일이된다")
    void 카카오회원가입시_중복이메일_예외() {

        KakaoSignupRequest req = KakaoSignupRequestFixture.VALID;
        when(userRepository.findByEmail(req.getEmail()))
                .thenReturn(Optional.of(mock(User.class)));


        assertThatThrownBy(() -> oAuthService.kakaoSignup(req))
                .isInstanceOf(UserDuplicatedEmailException.class);
    }

    @Test
    @DisplayName("우리디비에 카카오 이메일 회원이 없으면 카카오회원가입")
    void 카카오회원가입_성공() {

        KakaoSignupRequest req = KakaoSignupRequestFixture.VALID;
        Plan plan = mock(Plan.class);

        when(userRepository.findByEmail(req.getEmail()))
                .thenReturn(Optional.empty());
        when(planRepository.findById(req.getPlanId()))
                .thenReturn(Optional.of(plan));
        when(passwordEncoder.encode(anyString()))
                .thenReturn("ENCODED_PW");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        User saved = oAuthService.kakaoSignup(req);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User param = captor.getValue();

        assertThat(param.getEmail()).isEqualTo(req.getEmail());
        assertThat(param.getName()).isEqualTo(req.getName());
        assertThat(param.getPlan()).isEqualTo(plan);
        assertThat(param.getPassword()).isEqualTo("ENCODED_PW");
        assertThat(param.getRole()).isEqualTo(Role.USER);

        assertThat(saved).isSameAs(param);
    }

    @Test
    @DisplayName("인가코드에서 토큰으로 잘 바뀐다.")
    void 인가코드에서_엑세스토큰으로() {
        String code = "test-code";
        KakaoTokenResponse token = new KakaoTokenResponse();
        token.setAccess_token("access");
        token.setRefresh_token("refresh");
        token.setToken_type("aa");
        token.setExpires_in(3000);
        token.setScope("profile");

        when(kakaoOAuthClient.requestToken(code)).thenReturn(token);

        KakaoTokenResponse result = oAuthService.exchangeCodeForToken(code);

        assertThat(result.getAccess_token()).isEqualTo("access");
    }

    @Test
    @DisplayName("카카오 로그인(추가입력) - 이메일 있으면 유저 반환")
    void 로그인했는데_디비에유저있음() {
        String token = "access-token";
        KakaoUserInfoResponse userInfoResponse = mock(KakaoUserInfoResponse.class);
        KakaoUserInfoResponse.KakaoAccount account = mock(KakaoUserInfoResponse.KakaoAccount.class);
        when(userInfoResponse.getKakao_account()).thenReturn(account);
        when(account.getEmail()).thenReturn("test@kakao.com");

        User user = mock(User.class);
        when(kakaoOAuthClient.requestUserInfo(token)).thenReturn(userInfoResponse);
        when(userRepository.findByEmailWithPlan("test@kakao.com")).thenReturn(Optional.of(user));

        User result = oAuthService.kakaoLoginOrGetAdditionalInfo(token);
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("카카오에서 이메일을 제공안하면 예외")
    void 카카오소셜로그인했는데_이메일이없는_예외() {
        String token = "access-token";
        KakaoUserInfoResponse userInfoResponse = mock(KakaoUserInfoResponse.class);
        KakaoUserInfoResponse.KakaoAccount account = mock(KakaoUserInfoResponse.KakaoAccount.class);
        when(userInfoResponse.getKakao_account()).thenReturn(account);
        when(account.getEmail()).thenReturn("");

        when(kakaoOAuthClient.requestUserInfo(token)).thenReturn(userInfoResponse);

        assertThatThrownBy(() ->
                oAuthService.kakaoLoginOrGetAdditionalInfo(token)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("카카오 토큰으로 가입할 때 쓰이는 이메일, 이름 반환")
    void 카카오토큰으로_이메일_추출성공() {
        String token = "access-token";
        KakaoUserInfoResponse userInfoResponse = mock(KakaoUserInfoResponse.class);
        KakaoUserInfoResponse.KakaoAccount account = mock(KakaoUserInfoResponse.KakaoAccount.class);
        KakaoUserInfoResponse.KakaoAccount.Profile profile = mock(KakaoUserInfoResponse.KakaoAccount.Profile.class);

        when(userInfoResponse.getKakao_account()).thenReturn(account);
        when(account.getEmail()).thenReturn("test@kakao.com");
        when(account.getProfile()).thenReturn(profile);
        when(profile.getNickname()).thenReturn("테스트");

        when(kakaoOAuthClient.requestUserInfo(token)).thenReturn(userInfoResponse);

        Map<String, String> result = oAuthService.getEmailAndNameByToken(token);
        assertThat(result.get("email")).isEqualTo("test@kakao.com");
        assertThat(result.get("name")).isEqualTo("테스트");
    }

    @Test
    @DisplayName("카카오 유저 정보 응답 반환")
    void getKakaoUserInfoByToken_success() {
        String token = "access-token";
        KakaoUserInfoResponse userInfoRes = mock(KakaoUserInfoResponse.class);

        when(kakaoOAuthClient.requestUserInfo(token)).thenReturn(userInfoRes);

        KakaoUserInfoResponse result = oAuthService.getKakaoUserInfoByToken(token);
        assertThat(result).isSameAs(userInfoRes);
    }
}