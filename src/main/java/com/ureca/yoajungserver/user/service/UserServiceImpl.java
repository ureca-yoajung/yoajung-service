package com.ureca.yoajungserver.user.service;

import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.plan.exception.PlanNotFoundException;
import com.ureca.yoajungserver.plan.repository.PlanRepository;
import com.ureca.yoajungserver.user.dto.reqeust.SignupRequest;
import com.ureca.yoajungserver.user.dto.reqeust.UserUpdateRequest;
import com.ureca.yoajungserver.user.dto.response.UserResponse;
import com.ureca.yoajungserver.user.entity.Role;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.exception.EmailNotVerifiedException;
import com.ureca.yoajungserver.user.exception.UserDuplicatedEmailException;
import com.ureca.yoajungserver.user.exception.UserNotFoundException;
import com.ureca.yoajungserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(SignupRequest request, String verifiedEmail) {
        String email = request.getEmail();

        if (verifiedEmail == null || !verifiedEmail.equals(email)) {
            throw new EmailNotVerifiedException();
        }

        // 이메일 중복 검사
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new UserDuplicatedEmailException();
        });

        Plan plan = null;
        if (request.getPlanId() != null) {
            plan = planRepository.findById(request.getPlanId())
                    .orElseThrow(PlanNotFoundException::new);
        }
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
                .plan(plan)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse getUserInfo(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(UserNotFoundException::new);

        return UserResponse.fromEntity(user);
    }

    @Override
    public UserResponse updateUserInfo(String username, UserUpdateRequest request) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(UserNotFoundException::new);

        Plan plan = null;
        if (request.getPlanId() != null) {
            plan = planRepository.findById(request.getPlanId())
                    .orElseThrow(PlanNotFoundException::new);
        }

        user.updateInfo(
                user.getName(),
                request.getPhoneNumber(),
                request.getGender(),
                request.getAgeGroup(),
                request.getFamilyCount(),
                plan
        );
        return UserResponse.fromEntity(user);
    }
}
