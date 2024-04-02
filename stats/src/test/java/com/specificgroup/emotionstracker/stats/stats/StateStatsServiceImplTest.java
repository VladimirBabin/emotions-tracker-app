package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.State;
import com.specificgroup.emotionstracker.stats.entry.StateEntry;
import com.specificgroup.emotionstracker.stats.entry.StateEntryRepository;
import com.specificgroup.emotionstracker.stats.entry.StateLoggedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;


@ExtendWith(MockitoExtension.class)
class StateStatsServiceImplTest {

    private StateStatsService service;

    @Mock
    StateEntryRepository entryRepository;

    @BeforeEach
    void setUp() {
        service = new StateStatsServiceImpl(entryRepository);
    }

    @Test
    void whenNewStateLoggedEventReceivedThenNewStateEntryIsPersisted() {
        // given
        String userId = UUID.randomUUID().toString();
        StateLoggedEvent event = new StateLoggedEvent(1L, userId, State.GOOD, LocalDateTime.now());
        StateEntry stateEntry = new StateEntry(null,
                event.getUserId(),
                event.getState(),
                event.getDateTime());

        // when
        service.newStateLogForUser(event);

        // then
        verify(entryRepository).save(stateEntry);
    }

    @Test
    void whenWeeklyStatsQueriedForExistingUserThenFound() {
        // given
        String userId = UUID.randomUUID().toString();
        given(entryRepository.findAllByUserIdAndDateTimeAfter(eq(userId), any(LocalDateTime.class)))
                .willReturn(List.of(
                        new StateEntry(null, null, State.BAD,null),
                        new StateEntry(null, null, State.GOOD,null),
                        new StateEntry(null, null, State.EXCELLENT,null)
                ));

        // when
        WeeklyStats statsForUser = service.getWeeklyStatsForUser(userId);

        // then
        verify(entryRepository).findAllByUserIdAndDateTimeAfter(eq(userId), any(LocalDateTime.class));
        then(statsForUser.getBadState()).isEqualTo(BigDecimal.valueOf(33.3));
        then(statsForUser.getGoodState()).isEqualTo(BigDecimal.valueOf(33.3));
        then(statsForUser.getExcellentState()).isEqualTo(BigDecimal.valueOf(33.3));
    }
}