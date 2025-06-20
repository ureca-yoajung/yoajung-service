package com.ureca.yoajungserver.user.service;

import com.ureca.yoajungserver.user.dto.reqeust.TendencyRequest;
import com.ureca.yoajungserver.user.entity.Tendency;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.exception.TendencyAlreadyExistsException;
import com.ureca.yoajungserver.user.exception.TendencyNotFoundException;
import com.ureca.yoajungserver.user.exception.UserNotFoundException;
import com.ureca.yoajungserver.user.repository.TendencyRepository;
import com.ureca.yoajungserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TendencyServiceImpl implements TendencyService {
    private final TendencyRepository tendencyRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Tendency> getMyTendency(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return tendencyRepository.findByUser(user);
    }

    @Override
    public Tendency registerTendency(String email, TendencyRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        // 동시성 처리
        tendencyRepository.findByUser(user).ifPresent(
                tendency -> {
                    throw new TendencyAlreadyExistsException();
                }
        );

        Tendency tendency = Tendency.builder()
                .user(user)
                .avgMonthlyDataGB(normalizeValue(request.getAvgMonthlyDataGB()))
                .avgMonthlyVoiceMin(normalizeValue(request.getAvgMonthlyVoiceMin()))
                .comment(request.getComment())
                .preferencePrice(request.getPreferencePrice())
                .build();

        return tendencyRepository.save(tendency);
    }

    @Override
    public Tendency updateTendency(String email, TendencyRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        Tendency tendency = tendencyRepository.findByUser(user)
                .orElseThrow(TendencyNotFoundException::new);

        request.setAvgMonthlyDataGB(normalizeValue(request.getAvgMonthlyDataGB()));
        request.setAvgMonthlyVoiceMin(normalizeValue(request.getAvgMonthlyVoiceMin()));

        tendency.updateTendency(request);

        return tendency;
    }

    private static Integer normalizeValue(Integer value) {
        if (value == null) return null;
        return value > 200 ? 9999 : value;
    }
}
