package com.ureca.yoajungserver.tendency.service;

import com.ureca.yoajungserver.tendency.fixture.TendencyFixture;
import com.ureca.yoajungserver.tendency.fixture.TendencyRequestFixture;
import com.ureca.yoajungserver.user.entity.Tendency;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.exception.TendencyAlreadyExistsException;
import com.ureca.yoajungserver.user.exception.TendencyNotFoundException;
import com.ureca.yoajungserver.user.fixture.UserFixture;
import com.ureca.yoajungserver.user.repository.TendencyRepository;
import com.ureca.yoajungserver.user.repository.UserRepository;
import com.ureca.yoajungserver.user.service.TendencyServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TendencyServiceImplTest {

    @InjectMocks
    private TendencyServiceImpl tendencyService;
    @Mock
    private TendencyRepository tendencyRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("유저랑 성향 있을 때 조회")
    void 성향조회_성공() {
        User user = UserFixture.createUserEntity("pwd", null);
        Tendency tendency = TendencyFixture.create(user);

        when(userRepository.findByEmail(UserFixture.EMAIL)).thenReturn(Optional.of(user));
        when(tendencyRepository.findByUser(user)).thenReturn(Optional.of(tendency));

        Optional<Tendency> result = tendencyService.getMyTendency(UserFixture.EMAIL);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(tendency);
    }

    @Test
    @DisplayName("이미 성향이 있으면 예외발생")
    void 성향등록_실패_이미성향있어서_예외() {
        User user = UserFixture.createUserEntity("pwd", null);
        Tendency tendency = TendencyFixture.create(user);

        when(userRepository.findByEmail(UserFixture.EMAIL)).thenReturn(Optional.of(user));
        when(tendencyRepository.findByUser(user)).thenReturn(Optional.of(tendency));

        assertThatThrownBy(() ->
                tendencyService.registerTendency(UserFixture.EMAIL, TendencyRequestFixture.DEFAULT)
        ).isInstanceOf(TendencyAlreadyExistsException.class);
    }

    @Test
    @DisplayName("성향 등록 성공")
    void 성향저장_성공() {
        User user = UserFixture.createUserEntity("pwd", null);

        when(userRepository.findByEmail(UserFixture.EMAIL)).thenReturn(Optional.of(user));
        when(tendencyRepository.findByUser(user)).thenReturn(Optional.empty());

        tendencyService.registerTendency(UserFixture.EMAIL, TendencyRequestFixture.DEFAULT);

        verify(tendencyRepository, times(1)).save(any(Tendency.class));
    }

    @Test
    @DisplayName("성향이 없어서 업데이트 안됨")
    void 성향수정_실패_성향이없음() {
        User user = UserFixture.createUserEntity("pwd", null);

        when(userRepository.findByEmail(UserFixture.EMAIL)).thenReturn(Optional.of(user));
        when(tendencyRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                tendencyService.updateTendency(UserFixture.EMAIL, TendencyRequestFixture.DEFAULT)
        ).isInstanceOf(TendencyNotFoundException.class);
    }

    @Test
    @DisplayName("성향 수정 성공")
    void 성향수정_성공() {
        User user = UserFixture.createUserEntity("pwd", null);
        Tendency tendency = spy(TendencyFixture.create(user));

        when(userRepository.findByEmail(UserFixture.EMAIL)).thenReturn(Optional.of(user));
        when(tendencyRepository.findByUser(user)).thenReturn(Optional.of(tendency));
        
        tendencyService.updateTendency(UserFixture.EMAIL, TendencyRequestFixture.DEFAULT);

        verify(tendency, times(1)).updateTendency(TendencyRequestFixture.DEFAULT);
    }
}