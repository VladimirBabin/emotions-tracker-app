package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.Emotion;
import com.specificgroup.emotionstracker.stats.entry.EmotionLoggedEvent;
import com.specificgroup.emotionstracker.stats.entry.State;
import com.specificgroup.emotionstracker.stats.entry.StateLoggedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatsEventHandlerTest {

    private StatsEventHandler eventHandler;

    @Mock
    StateStatsService stateService;

    @Mock
    EmotionStatsService emotionService;

    @BeforeEach
    void setUp() {
        eventHandler = new StatsEventHandler(stateService, emotionService);
    }

    @Test
    void whenHandlesNewStateThenStateStatsServiceIsCalled() {
        // given
        String userId = UUID.randomUUID().toString();
        StateLoggedEvent event = new StateLoggedEvent(1L, userId, State.GOOD, LocalDateTime.now());

        // when
        eventHandler.handleNewStateLogged(event);

        // then
        verify(stateService).newStateLogForUser(event);
    }

    @Test
    void whenExceptionArisesInStateServiceThenCorrectAMQPExceptionThrows() {
        // given
        String userId = UUID.randomUUID().toString();
        StateLoggedEvent event = new StateLoggedEvent(1L, userId, State.GOOD, LocalDateTime.now());
        doThrow(new RuntimeException("exception message"))
                .when(stateService).newStateLogForUser(event);

        // then
        assertThatExceptionOfType(AmqpRejectAndDontRequeueException.class)
                .isThrownBy(() -> eventHandler.handleNewStateLogged(event));
    }

    @Test
    void whenHandlesNewEmotionThenEmotionStatsServiceIsCalled() {
        // given
        String userId = UUID.randomUUID().toString();
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, userId, Emotion.PASSIONATE, LocalDateTime.now());

        // when
        eventHandler.handleNewEmotionLogged(event);

        // then
        verify(emotionService).newEmotionLogForUser(event);
    }

    @Test
    void whenExceptionArisesInEmotionServiceThenCorrectAMQPExceptionThrows() {
        // given
        String userId = UUID.randomUUID().toString();
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, userId, Emotion.PASSIONATE, LocalDateTime.now());
        doThrow(new RuntimeException("exception message"))
                .when(emotionService).newEmotionLogForUser(event);

        // then
        assertThatExceptionOfType(AmqpRejectAndDontRequeueException.class)
                .isThrownBy(() -> eventHandler.handleNewEmotionLogged(event));
    }
}