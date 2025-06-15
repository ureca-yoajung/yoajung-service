package com.ureca.yoajungserver.user.service;

import com.ureca.yoajungserver.user.dto.reqeust.TendencyRequest;
import com.ureca.yoajungserver.user.entity.Tendency;

import java.util.Optional;

public interface TendencyService {
    Optional<Tendency> getMyTendency(String email);

    Tendency registerTendency(String email, TendencyRequest request);

    Tendency updateTendency(String email, TendencyRequest request);
}
