package com.github.vbabin.emocheck.state;

import com.github.vbabin.emocheck.user.User;
import com.github.vbabin.emocheck.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StateLogServiceImplTest {

    private StateLogService stateLogService;

    @Mock
    private StateLogRepository stateLogRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        stateLogService = new StateLogServiceImpl(
                stateLogRepository,
                userRepository
        );
    }

    @Test
    void checkNewStateLog() {
        // given
        StateLogDTO stateLogDTO = new StateLogDTO("john_doe", State.GOOD, LocalDateTime.now());
        given(stateLogRepository.save(any()))
                .will(returnsFirstArg());

        // when
        StateLog stateLog = stateLogService.acceptNewState(stateLogDTO);

        // then
        then(stateLog.getState()).isEqualTo(State.GOOD);
        verify(userRepository).save(new User("john_doe"));
        verify(stateLogRepository).save(stateLog);
    }

    @Test
    void checkNewStateLogWithExistingUser() {
        // given
        User existingUser = new User(1L, "john_doe");
        given(userRepository.findByAlias("john_doe"))
                .willReturn(Optional.of(existingUser));
        StateLogDTO stateLogDTO = new StateLogDTO("john_doe", State.BAD, LocalDateTime.now());
        given(stateLogRepository.save(any()))
                .will(returnsFirstArg());

        // when
        StateLog stateLog = stateLogService.acceptNewState(stateLogDTO);

        // then
        then(stateLog.getState()).isEqualTo(State.BAD);
        then(stateLog.getUser()).isEqualTo(existingUser);
        verify(userRepository, never()).save(any());
        verify(stateLogRepository).save(stateLog);
    }

    @Test
    void checkGettingWeeklyStatsForExistingUser() {
        // given
        User existingUser = new User(1L, "john_doe");
        given(userRepository.findByAlias("john_doe"))
                .willReturn(Optional.of(existingUser));
        given(stateLogRepository.findAllByUserAndDateTimeAfter(eq(existingUser), any(LocalDateTime.class)))
                .willReturn(List.of(
                        new StateLog(null, null, State.BAD, null),
                        new StateLog(null, null, State.GOOD, null),
                        new StateLog(null, null, State.EXCELLENT, null)
                ));

        // when
        WeeklyStats statsForUser = stateLogService.getWeeklyStatsForUser("john_doe");

        // then
        then(statsForUser.getBadState()).isEqualTo(BigDecimal.valueOf(33.3));
        then(statsForUser.getGoodState()).isEqualTo(BigDecimal.valueOf(33.3));
        then(statsForUser.getExcellentState()).isEqualTo(BigDecimal.valueOf(33.3));
    }

    @Test
    void checkGettingWeeklyStatsForNonExistingUser() {
        // given
        given(userRepository.findByAlias("john_doe"))
                .willReturn(Optional.empty());

        // then
        thenExceptionOfType(NonExistingUserException.class).isThrownBy(() -> stateLogService.getWeeklyStatsForUser("john_doe"));
    }
}