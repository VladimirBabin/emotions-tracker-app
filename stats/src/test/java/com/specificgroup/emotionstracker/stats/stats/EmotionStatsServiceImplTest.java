package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.verify;

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
}