package com.ureca.yoajungserver.user.service;

import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.plan.repository.PlanRepository;
import com.ureca.yoajungserver.user.dto.reqeust.SignupRequest;
import com.ureca.yoajungserver.user.dto.reqeust.UserUpdateRequest;
import com.ureca.yoajungserver.user.dto.response.UserResponse;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.exception.EmailNotVerifiedException;
import com.ureca.yoajungserver.user.exception.UserDuplicatedEmailException;
import com.ureca.yoajungserver.user.exception.UserNotFoundException;
import com.ureca.yoajungserver.user.fixture.PlanFixture;
import com.ureca.yoajungserver.user.fixture.SignupRequestFixture;
import com.ureca.yoajungserver.user.fixture.UserFixture;
import com.ureca.yoajungserver.user.fixture.UserUpdateRequestFixture;
import com.ureca.yoajungserver.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PlanRepository planRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("이메일 검증안된 예외 발생")
    void 회원가입_실패_이메일검증_예외() {
        SignupRequest req = SignupRequestFixture.VALID.getRequest();

        assertThatThrownBy(() ->
                userService.signup(req, null)
        ).isInstanceOf(EmailNotVerifiedException.class);
    }

    @Test
    @DisplayName("중복 이메일 예외 발생")
    void 회원가입_실패_중복이메일_예외() {
        SignupRequest req = SignupRequestFixture.VALID.getRequest();
        when(userRepository.findByEmail(UserFixture.EMAIL))
                .thenReturn(java.util.Optional.of(mock(User.class)));

        assertThatThrownBy(() ->
                userService.signup(req, UserFixture.EMAIL)
        ).isInstanceOf(UserDuplicatedEmailException.class);
    }

    @Test
    @DisplayName("회원가입 성공")
    void 회원가입_성공() {
        SignupRequest req = SignupRequestFixture.VALID.getRequest();
        Plan plan = PlanFixture.PLAN;
        when(userRepository.findByEmail(UserFixture.EMAIL))
                .thenReturn(java.util.Optional.empty());
        when(planRepository.findById(UserFixture.PLAN_ID))
                .thenReturn(java.util.Optional.of(plan));
        when(passwordEncoder.encode(UserFixture.PASSWORD))
                .thenReturn("ENCODED");

        userService.signup(req, UserFixture.EMAIL);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertThat(saved.getEmail()).isEqualTo(UserFixture.EMAIL);
        assertThat(saved.getPassword()).isEqualTo("ENCODED");
        assertThat(saved.getPlan()).isEqualTo(plan);
    }

    @Test
    @DisplayName("내정보 조회")
    void 내정보조회_성공() {
        User entity = UserFixture.createUserEntity("pw", PlanFixture.PLAN);
        when(userRepository.findByEmail(UserFixture.EMAIL))
                .thenReturn(java.util.Optional.of(entity));

        UserResponse resp = userService.getUserInfo(UserFixture.EMAIL);

        assertThat(resp.getEmail()).isEqualTo(UserFixture.EMAIL);
    }

    @Test
    @DisplayName("유저없을떄")
    void 내정보조회_실패_유저없을때() {
        when(userRepository.findByEmail(UserFixture.EMAIL))
                .thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() ->
                userService.getUserInfo(UserFixture.EMAIL)
        ).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("내정보_수정")
    void 내정보_수정_성공() {
        User entity = spy(UserFixture.createUserEntity("pw", PlanFixture.PLAN));
        when(userRepository.findByEmail(UserFixture.EMAIL))
                .thenReturn(java.util.Optional.of(entity));

        UserUpdateRequest req = UserUpdateRequestFixture.VALID.getRequest();

        UserResponse resp = userService.updateUserInfo(UserFixture.EMAIL, req);

        verify(entity).updateInfo(
                req.getName(),
                req.getPhoneNumber(),
                req.getGender(),
                req.getAgeGroup(),
                req.getFamilyCount()
        );
        assertThat(resp.getPhoneNumber()).isEqualTo(req.getPhoneNumber());
    }

    @Test
    @DisplayName("내정보수정실패")
    void 내정보수정_실패_예외() {
        when(userRepository.findByEmail(UserFixture.EMAIL))
                .thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() ->
                userService.updateUserInfo(UserFixture.EMAIL, UserUpdateRequestFixture.VALID.getRequest())
        ).isInstanceOf(UserNotFoundException.class);
    }
}