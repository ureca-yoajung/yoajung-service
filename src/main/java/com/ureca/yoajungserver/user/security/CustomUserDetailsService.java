package com.ureca.yoajungserver.user.security;

import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email));
        // 시큐리티의 UsernameNotFound로 날리는게 낫다. 스프링 시큐리티 핸들러를 동작시킨다.
        // 로그인에서만 사용
        return new CustomUserDetails(user);
    }
}
