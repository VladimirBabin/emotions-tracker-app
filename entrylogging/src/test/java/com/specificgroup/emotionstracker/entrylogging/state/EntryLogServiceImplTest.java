package com.specificgroup.emotionstracker.entrylogging.state;

import com.specificgroup.emotionstracker.entrylogging.state.domain.Emotion;
import com.specificgroup.emotionstracker.entrylogging.state.domain.State;
import com.specificgroup.emotionstracker.entrylogging.state.domain.EntryLog;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EntryLogServiceImplTest {

    private EntryLogService entryLogService;

    @Mock
    private EntryLogRepository entryLogRepository;
    @Mock
    private EntryLogEventPublisher entryLogEventPublisher;

    @BeforeEach
    void setUp() {
        entryLogService = new EntryLogServiceImpl(
                entryLogRepository,
                entryLogEventPublisher
        );
    }

    @Test
    void whenNewStateByNewUserLoggedThenUserStateAndEmotionsPersistedAndEventPublished() {
        // given
        String userId = UUID.randomUUID().toString();
        Set<Emotion> emotions = Set.of(Emotion.HAPPY, Emotion.CONTENT);
        EntryLogDTO entryLogDTO = new EntryLogDTO(userId, State.GOOD, emotions, LocalDateTime.now());
        given(entryLogRepository.save(any()))
                .will(returnsFirstArg());


        // when
        EntryLog entryLog = entryLogService.acceptNewEntry(entryLogDTO);

        // then
        BDDAssertions.then(entryLog.getState()).isEqualTo(State.GOOD);
        verify(entryLogRepository).save(entryLog);
        verify(entryLogEventPublisher).stateLogged(entryLog);
    }

    @Test
    void whenNewStateByExistingUserLoggedThenUserFoundAndStateAndEmotionsPersistedAndEventPublished() {
        // given
        String userId = UUID.randomUUID().toString();
        Set<Emotion> emotions = Set.of(Emotion.HAPPY, Emotion.CONTENT);
        EntryLogDTO entryLogDTO = new EntryLogDTO(userId, State.BAD, emotions, LocalDateTime.now());
        given(entryLogRepository.save(any()))
                .will(returnsFirstArg());

        // when
        EntryLog entryLog = entryLogService.acceptNewEntry(entryLogDTO);

        // then
        BDDAssertions.then(entryLog.getState()).isEqualTo(State.BAD);
        BDDAssertions.then(entryLog.getUserId()).isEqualTo(userId);
        verify(entryLogRepository).save(entryLog);
        verify(entryLogEventPublisher).stateLogged(entryLog);
    }

    @Test
    void whenWeeklyStatsQueriedForExistingUserThenFound() {
        // given
        String userId = UUID.randomUUID().toString();
        given(entryLogRepository.findAllByUserIdAndDateTimeAfter(eq(userId), any(LocalDateTime.class)))
                .willReturn(List.of(
                        new EntryLog(null, null, State.BAD,null, null),
                        new EntryLog(null, null, State.GOOD,null, null),
                        new EntryLog(null, null, State.EXCELLENT,null, null)
                ));

        // when
        WeeklyStats statsForUser = entryLogService.getWeeklyStatsForUser(userId);

        // then
        then(statsForUser.getBadState()).isEqualTo(BigDecimal.valueOf(33.3));
        then(statsForUser.getGoodState()).isEqualTo(BigDecimal.valueOf(33.3));
        then(statsForUser.getExcellentState()).isEqualTo(BigDecimal.valueOf(33.3));
    }

    @Test
    void whenGetLastLogsForUserThenRetrievedSuccessfully() {
        // given
        String userId = UUID.randomUUID().toString();
        EntryLog log1 = new EntryLog(1L, userId, State.GOOD,
                Set.of(Emotion.PEACEFUL, Emotion.HAPPY),
                LocalDateTime.now());
        EntryLog log2 = new EntryLog(2L, userId, State.BAD,
                Set.of(Emotion.ANGRY, Emotion.HOPEFUL),
                LocalDateTime.now());
        List<EntryLog> entryLogs = List.of(log1, log2);
        given(entryLogRepository.findTop10ByUserIdOrderByDateTimeDesc(userId))
                .willReturn(entryLogs);

        // when
        List<EntryLog> entryLogsResult = entryLogService.getLastLogsForUser(userId);

        // then
        then(entryLogsResult).isEqualTo(entryLogs);
    }
}