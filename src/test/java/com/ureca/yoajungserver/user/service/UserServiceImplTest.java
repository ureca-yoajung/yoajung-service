//package com.ureca.yoajungserver.user.service;
//
//import com.ureca.yoajungserver.common.BaseCode;
//import com.ureca.yoajungserver.user.dto.reqeust.SignupRequest;
//import com.ureca.yoajungserver.user.entity.User;
//import com.ureca.yoajungserver.user.repository.UserRepository;
//import jakarta.servlet.http.HttpSession;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceImplTest {
//
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    HttpSession session;
//
//    @InjectMocks
//    UserServiceImpl userService;
//
//    @Test
//    @DisplayName("이메일 인증 안할시 회원가입 불가")
//    void 인증안하면회원가입불가() {
//
//        SignupRequest req = SignupRequest.builder()
//                .email("test@t.com")
//                .password("pw")
//                .name("홍길동")
//                .phoneNumber("010-1111-1111")
//                .gender(null)
//                .ageGroup(null)
//                .familyCount(1)
//                .build();
//
//        when(session.getAttribute("verifiedEmail")).thenReturn(null);
//
//        assertThatThrownBy(() -> userService.signup(req, session))
//                .hasMessageContaining(BaseCode.INVALID_REQUEST.getCode());
//    }
//
//    @Test
//    @DisplayName("이메일 중복시 회원가입 불가")
//    void 이메일중복이면회원가입불가() {
//
//        SignupRequest req = SignupRequest.builder()
//                .email("test@t.com")
//                .password("pw")
//                .name("홍길동")
//                .phoneNumber("010-1111-1111")
//                .gender(null)
//                .ageGroup(null)
//                .familyCount(1)
//                .build();
//
//        when(session.getAttribute("verifiedEmail")).thenReturn("test@t.com");
//        when(userRepository.findByEmail("test@t.com")).thenReturn(Optional.of(mock(User.class)));
//
//
//        assertThatThrownBy(() -> userService.signup(req, session))
//                .hasMessageContaining(BaseCode.USER_DUPLICATED_EMAIL.getCode());
//    }
//}