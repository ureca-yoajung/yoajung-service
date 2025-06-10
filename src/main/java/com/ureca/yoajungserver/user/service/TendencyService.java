package com.ureca.yoajungserver.user.service;

import com.ureca.yoajungserver.user.dto.reqeust.TendencyRequest;
import com.ureca.yoajungserver.user.entity.Tendency;

public interface TendencyService {
    Tendency getMyTendency(String email);

    Tendency registerTendency(String email, TendencyRequest request);

    Tendency updateTendency(String email, TendencyRequest request);
}
