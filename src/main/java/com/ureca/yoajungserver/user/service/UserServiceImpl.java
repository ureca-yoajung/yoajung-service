package com.ureca.yoajungserver.user.service;

import com.ureca.yoajungserver.user.dto.reqeust.SignupRequest;
import com.ureca.yoajungserver.user.entity.Role;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String VERIFIED_EMAIL = "verifiedEmail";


    @Override
    public void signup(SignupRequest request, HttpSession httpSession) {
        String email = request.getEmail();

        // 세션에 저장된 인증된 이메일 확인
        Object verified = httpSession.getAttribute(VERIFIED_EMAIL);
        if (verified == null || !verified.equals(email)) {
            throw new IllegalStateException("이메일 인증 필요");
        }

        // 이메일 중복 검사
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("이미 사용 중");
        });

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .ageGroup(request.getAgeGroup())
                .familyCount(request.getFamilyCount())
                .plan(null)
                .role(Role.USER)
                .build();

        userRepository.save(user);

        httpSession.removeAttribute(VERIFIED_EMAIL);
    }
}
