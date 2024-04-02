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
import java.util.Set;
import java.util.UUID;

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
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, userId, Emotion.PEACEFUL, LocalDateTime.now());
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
    void whenLastWeekMostLoggedEmotionsQueriedForUserThenFoundAndReturned() {
        // given
        String userId = UUID.randomUUID().toString();
        Set<Emotion> expected = Set.of(
                Emotion.SATISFIED,
                Emotion.PEACEFUL,
                Emotion.PASSIONATE,
                Emotion.SCARED,
                Emotion.STRESSED);
        given(entryRepository.findTopRepeatedEmotionEntriesGropedByEmotionsDesc(eq(userId), any(LocalDateTime.class)))
                .willReturn(List.of(
                        buildEntry(Emotion.CONTENT),
                        buildEntry(Emotion.PEACEFUL),
                        buildEntry(Emotion.PEACEFUL),
                        buildEntry(Emotion.PASSIONATE),
                        buildEntry(Emotion.PASSIONATE),
                        buildEntry(Emotion.SCARED),
                        buildEntry(Emotion.SCARED),
                        buildEntry(Emotion.STRESSED),
                        buildEntry(Emotion.STRESSED),
                        buildEntry(Emotion.STRESSED),
                        buildEntry(Emotion.SATISFIED),
                        buildEntry(Emotion.SATISFIED),
                        buildEntry(Emotion.SATISFIED),
                        buildEntry(Emotion.SATISFIED),
                        buildEntry(Emotion.IRRITATED)));

        // when
        Set<Emotion> emotions = service.getLastWeekMostLoggedEmotions(userId);

        // then
        verify(entryRepository).findTopRepeatedEmotionEntriesGropedByEmotionsDesc(eq(userId), any(LocalDateTime.class));
        then(emotions).isEqualTo(expected);
    }

    @Test
    void whenUserHasLessThenRequiredMaxNumberThenLessEmotionsReturned() {
        // given
        String userId = UUID.randomUUID().toString();
        Set<Emotion> expected = Set.of(
                Emotion.CONTENT,
                Emotion.STRESSED);
        given(entryRepository.findTopRepeatedEmotionEntriesGropedByEmotionsDesc(eq(userId), any(LocalDateTime.class)))
                .willReturn(List.of(
                        buildEntry(Emotion.CONTENT),
                        buildEntry(Emotion.CONTENT),
                        buildEntry(Emotion.STRESSED)));

        // when
        Set<Emotion> emotions = service.getLastWeekMostLoggedEmotions(userId);

        // then
        verify(entryRepository).findTopRepeatedEmotionEntriesGropedByEmotionsDesc(eq(userId), any(LocalDateTime.class));
        then(emotions).isEqualTo(expected);
    }

    private EmotionEntry buildEntry(Emotion emotion) {
        return new EmotionEntry(null, null, emotion, null);
    }


}