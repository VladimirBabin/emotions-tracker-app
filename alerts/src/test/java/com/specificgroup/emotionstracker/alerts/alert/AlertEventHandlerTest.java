package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.entry.Emotion;
import com.specificgroup.emotionstracker.alerts.entry.EmotionLoggedEvent;
import com.specificgroup.emotionstracker.alerts.entry.State;
import com.specificgroup.emotionstracker.alerts.entry.StateLoggedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class AlertEventHandlerTest {

    private AlertEventHandler alertEventHandler;

    @Mock
    private StateAlertService stateAlertService;

    @Mock
    private EmotionAlertService emotionAlertService;

    @BeforeEach
    void setUp() {
        alertEventHandler = new AlertEventHandler(stateAlertService, emotionAlertService);
    }

    @Test
    void whenHandlesNewStateThenStateServiceIsCalled() {
        // given
        String userId = UUID.randomUUID().toString();
        StateLoggedEvent event = new StateLoggedEvent(1L, userId, State.AWFUL, LocalDateTime.now());

        // when
        alertEventHandler.handleNewStateLogged(event);

        // then
        verify(stateAlertService).newTriggeringStateForUser(event);
    }

    @Test
    void whenHandlesNewEmotionThenEmotionServiceIsCalled() {
        // given
        String userId = UUID.randomUUID().toString();
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, userId, Emotion.SCARED, LocalDateTime.now());

        // when
        alertEventHandler.handleNewEmotionLogged(event);

        // then
        verify(emotionAlertService).newTriggeringEmotionForUser(event);
    }

    @Test
    void whenExceptionArisesInStateServiceThenCorrectAMQPExceptionThrows() {
        // given
        String userId = UUID.randomUUID().toString();
        StateLoggedEvent event = new StateLoggedEvent(1L, userId, State.AWFUL, LocalDateTime.now());
        doThrow(new RuntimeException("exception message"))
                .when(stateAlertService).newTriggeringStateForUser(event);

        // then
        assertThatExceptionOfType(AmqpRejectAndDontRequeueException.class)
                .isThrownBy(() -> alertEventHandler.handleNewStateLogged(event));
    }

    @Test
    void whenExceptionArisesInEmotionServiceThenCorrectAMQPExceptionThrows() {
        // given
        String userId = UUID.randomUUID().toString();
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, userId, Emotion.SCARED, LocalDateTime.now());
        doThrow(new RuntimeException("exception message"))
                .when(emotionAlertService).newTriggeringEmotionForUser(event);

        // then
        assertThatExceptionOfType(AmqpRejectAndDontRequeueException.class)
                .isThrownBy(() -> alertEventHandler.handleNewEmotionLogged(event));
    }
}