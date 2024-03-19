package com.specificgroup.emotionstracker.emocheck.state;

import com.specificgroup.emotionstracker.emocheck.user.User;
import com.specificgroup.emotionstracker.emocheck.user.UserRepository;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Mock
    private StateLogEventPublisher stateLogEventPublisher;

    @BeforeEach
    void setUp() {
        stateLogService = new StateLogServiceImpl(
                stateLogRepository,
                userRepository,
                stateLogEventPublisher
        );
    }

    @Test
    void whenNewStateByNewUserLoggedThenUserStateAndEmotionsPersistedAndEventPublished() {
        // given
        Set<Emotion> emotions = Set.of(Emotion.HAPPY, Emotion.CONTENT);
        StateLogDTO stateLogDTO = new StateLogDTO("john_doe", State.GOOD, emotions, LocalDateTime.now());
        given(stateLogRepository.save(any()))
                .will(returnsFirstArg());


        // when
        StateLog stateLog = stateLogService.acceptNewState(stateLogDTO);

        // then
        BDDAssertions.then(stateLog.getState()).isEqualTo(State.GOOD);
        verify(userRepository).save(new User("john_doe"));
        verify(stateLogRepository).save(stateLog);
        verify(stateLogEventPublisher).stateLogged(stateLog);
    }

    @Test
    void whenNewStateByExistingUserLoggedThenUserFoundAndStateAndEmotionsPersistedAndEventPublished() {
        // given
        User existingUser = new User(1L, "john_doe");
        given(userRepository.findByAlias("john_doe"))
                .willReturn(Optional.of(existingUser));
        Set<Emotion> emotions = Set.of(Emotion.HAPPY, Emotion.CONTENT);
        StateLogDTO stateLogDTO = new StateLogDTO("john_doe", State.BAD, emotions, LocalDateTime.now());
        given(stateLogRepository.save(any()))
                .will(returnsFirstArg());

        // when
        StateLog stateLog = stateLogService.acceptNewState(stateLogDTO);

        // then
        BDDAssertions.then(stateLog.getState()).isEqualTo(State.BAD);
        BDDAssertions.then(stateLog.getUser()).isEqualTo(existingUser);
        verify(userRepository, never()).save(any());
        verify(stateLogRepository).save(stateLog);
        verify(stateLogEventPublisher).stateLogged(stateLog);
    }

    @Test
    void whenWeeklyStatsQueriedForExistingUserThenFound() {
        // given
        User existingUser = new User(1L, "john_doe");
        given(userRepository.findByAlias("john_doe"))
                .willReturn(Optional.of(existingUser));
        given(stateLogRepository.findAllByUserAndDateTimeAfter(eq(existingUser), any(LocalDateTime.class)))
                .willReturn(List.of(
                        new StateLog(null, null, State.BAD,null, null),
                        new StateLog(null, null, State.GOOD,null, null),
                        new StateLog(null, null, State.EXCELLENT,null, null)
                ));

        // when
        WeeklyStats statsForUser = stateLogService.getWeeklyStatsForUser("john_doe");

        // then
        then(statsForUser.getBadState()).isEqualTo(BigDecimal.valueOf(33.3));
        then(statsForUser.getGoodState()).isEqualTo(BigDecimal.valueOf(33.3));
        then(statsForUser.getExcellentState()).isEqualTo(BigDecimal.valueOf(33.3));
    }

    @Test
    void whenWeeklyStatsQueriedForNonExistingUserThenExceptionThrown() {
        // given
        given(userRepository.findByAlias("john_doe"))
                .willReturn(Optional.empty());

        // then
        thenExceptionOfType(NonExistingUserException.class).isThrownBy(() -> stateLogService.getWeeklyStatsForUser("john_doe"));
    }

    @Test
    void whenGetLastLogsForUserThenRetrievedSuccessfully() {
        // given
        User user = new User("john_doe");
        StateLog log1 = new StateLog(1L, user, State.GOOD,
                Set.of(Emotion.PEACEFUL, Emotion.HAPPY),
                LocalDateTime.now());
        StateLog log2 = new StateLog(2L, user, State.BAD,
                Set.of(Emotion.ANGRY, Emotion.HOPEFUL),
                LocalDateTime.now());
        List<StateLog> stateLogs = List.of(log1, log2);
        given(stateLogRepository.findTop10ByUserAliasOrderByDateTimeDesc("john_doe"))
                .willReturn(stateLogs);

        // when
        List<StateLog> stateLogsResult = stateLogService.getLastLogsForUser("john_doe");

        // then
        then(stateLogsResult).isEqualTo(stateLogs);
    }
}