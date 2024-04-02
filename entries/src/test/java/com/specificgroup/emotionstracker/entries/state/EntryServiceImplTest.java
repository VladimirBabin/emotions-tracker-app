package com.specificgroup.emotionstracker.entries.state;

import com.specificgroup.emotionstracker.entries.entry.*;
import com.specificgroup.emotionstracker.entries.entry.domain.Emotion;
import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import com.specificgroup.emotionstracker.entries.entry.domain.State;
import com.specificgroup.emotionstracker.entries.entry.dto.EntryDto;
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

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EntryServiceImplTest {

    private EntriesService entriesService;

    @Mock
    private EntriesRepository entriesRepository;
    @Mock
    private EntriesEventPublisher entriesEventPublisher;

    @BeforeEach
    void setUp() {
        entriesService = new EntriesServiceImpl(
                entriesRepository,
                entriesEventPublisher
        );
    }

    @Test
    void whenNewStateByNewUserLoggedThenUserStateAndEmotionsPersistedAndEventPublished() {
        // given
        String userId = UUID.randomUUID().toString();
        Set<Emotion> emotions = Set.of(Emotion.HAPPY, Emotion.CONTENT);
        EntryDto entryDto = new EntryDto(userId, State.GOOD, emotions, LocalDateTime.now());
        given(entriesRepository.save(any()))
                .will(returnsFirstArg());


        // when
        Entry entry = entriesService.acceptNewEntry(entryDto);

        // then
        BDDAssertions.then(entry.getState()).isEqualTo(State.GOOD);
        verify(entriesRepository).save(entry);
        verify(entriesEventPublisher).stateLogged(entry);
    }

    @Test
    void whenNewStateByExistingUserLoggedThenUserFoundAndStateAndEmotionsPersistedAndEventPublished() {
        // given
        String userId = UUID.randomUUID().toString();
        Set<Emotion> emotions = Set.of(Emotion.HAPPY, Emotion.CONTENT);
        EntryDto entryDto = new EntryDto(userId, State.BAD, emotions, LocalDateTime.now());
        given(entriesRepository.save(any()))
                .will(returnsFirstArg());

        // when
        Entry entry = entriesService.acceptNewEntry(entryDto);

        // then
        BDDAssertions.then(entry.getState()).isEqualTo(State.BAD);
        BDDAssertions.then(entry.getUserId()).isEqualTo(userId);
        verify(entriesRepository).save(entry);
        verify(entriesEventPublisher).stateLogged(entry);
    }

    @Test
    void whenWeeklyStatsQueriedForExistingUserThenFound() {
        // given
        String userId = UUID.randomUUID().toString();
        given(entriesRepository.findAllByUserIdAndDateTimeAfter(eq(userId), any(LocalDateTime.class)))
                .willReturn(List.of(
                        new Entry(null, null, State.BAD,null, null),
                        new Entry(null, null, State.GOOD,null, null),
                        new Entry(null, null, State.EXCELLENT,null, null)
                ));

        // when
        WeeklyStats statsForUser = entriesService.getWeeklyStatsForUser(userId);

        // then
        then(statsForUser.getBadState()).isEqualTo(BigDecimal.valueOf(33.3));
        then(statsForUser.getGoodState()).isEqualTo(BigDecimal.valueOf(33.3));
        then(statsForUser.getExcellentState()).isEqualTo(BigDecimal.valueOf(33.3));
    }

    @Test
    void whenGetLastLogsForUserThenRetrievedSuccessfully() {
        // given
        String userId = UUID.randomUUID().toString();
        Entry log1 = new Entry(1L, userId, State.GOOD,
                Set.of(Emotion.PEACEFUL, Emotion.HAPPY),
                LocalDateTime.now());
        Entry log2 = new Entry(2L, userId, State.BAD,
                Set.of(Emotion.ANGRY, Emotion.HOPEFUL),
                LocalDateTime.now());
        List<Entry> entries = List.of(log1, log2);
        given(entriesRepository.findTop10ByUserIdOrderByDateTimeDesc(userId))
                .willReturn(entries);

        // when
        List<Entry> entryLogsResult = entriesService.getLastLogsForUser(userId);

        // then
        then(entryLogsResult).isEqualTo(entries);
    }
}