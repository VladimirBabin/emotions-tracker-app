package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.Emotion;
import com.specificgroup.emotionstracker.stats.entry.EmotionEntry;
import com.specificgroup.emotionstracker.stats.entry.EmotionEntryRepository;
import com.specificgroup.emotionstracker.stats.entry.EmotionLoggedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.specificgroup.emotionstracker.stats.entry.Emotion.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)

class EmotionStatsServiceImplTest {

    private EmotionStatsService service;

    @Mock
    EmotionEntryRepository entryRepository;

    @BeforeEach
    void setUp() {
        service = new EmotionStatsServiceImpl(entryRepository);
    }

    @Test
    void whenNewEmotionLoggedEventReceivedThenNewEmotionEntryIsPersisted() {
        // given
        String userId = UUID.randomUUID().toString();
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, userId, PEACEFUL, LocalDateTime.now());
        EmotionEntry emotionEntry = new EmotionEntry(null,
                event.getUserId(),
                event.getEmotion(),
                event.getDateTime());

        // when
        service.newEmotionLogForUser(event);

        // then
        verify(entryRepository).save(emotionEntry);
    }

    @Test
    void whenMoreThanLimitedMostLoggedEmotionsReturnedByRepositoryThenServiceTruncate() {
        // given
        String userId = UUID.randomUUID().toString();
        List<Emotion> expected = List.of(
                SATISFIED,
                PEACEFUL,
                PASSIONATE,
                SCARED,
                STRESSED);
        given(entryRepository.findTopRepeatedEmotionEntriesGropedByEmotionsDesc(eq(userId), any(LocalDateTime.class)))
                .willReturn(List.of(
                        SATISFIED,
                        PEACEFUL,
                        PASSIONATE,
                        SCARED,
                        STRESSED,
                        CONTENT,
                        IRRITATED));

        // when
        List<Emotion> emotions = service.getLastWeekMostLoggedEmotions(userId);

        // then
        verify(entryRepository).findTopRepeatedEmotionEntriesGropedByEmotionsDesc(eq(userId), any(LocalDateTime.class));
        then(emotions).isEqualTo(expected);
    }

    @Test
    void whenUserHasLessThenRequiredLimitNumberThenLessEmotionsReturned() {
        // given
        String userId = UUID.randomUUID().toString();
        List<Emotion> expected = List.of(
                CONTENT,
                STRESSED);
        given(entryRepository.findTopRepeatedEmotionEntriesGropedByEmotionsDesc(eq(userId), any(LocalDateTime.class)))
                .willReturn(List.of(
                        CONTENT,
                        STRESSED));

        // when
        List<Emotion> emotions = service.getLastWeekMostLoggedEmotions(userId);

        // then
        verify(entryRepository).findTopRepeatedEmotionEntriesGropedByEmotionsDesc(eq(userId), any(LocalDateTime.class));
        then(emotions).isEqualTo(expected);
    }
}