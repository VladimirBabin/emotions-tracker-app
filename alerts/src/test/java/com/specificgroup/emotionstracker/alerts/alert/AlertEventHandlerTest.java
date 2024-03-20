package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.state.Emotion;
import com.specificgroup.emotionstracker.alerts.state.EmotionLoggedEvent;
import com.specificgroup.emotionstracker.alerts.state.State;
import com.specificgroup.emotionstracker.alerts.state.StateLoggedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class AlertEventHandlerTest {

    AlertEventHandler alertEventHandler;

    @Mock
    StateAlertService stateAlertService;

    @Mock
    EmotionAlertService emotionAlertService;

    @BeforeEach
    void setUp() {
        alertEventHandler = new AlertEventHandler(stateAlertService, emotionAlertService);
    }

    @Test
    void whenHandlesNewStateThenStateServiceIsCalled() {
        // given
        StateLoggedEvent event = new StateLoggedEvent(1L, 1L, State.AWFUL, LocalDateTime.now());

        // when
        alertEventHandler.handleNewStateLogged(event);

        // then
        verify(stateAlertService).newTriggeringStateForUser(event);
    }

    @Test
    void whenHandlesNewEmotionThenEmotionServiceIsCalled() {
        // given
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, 1L, Emotion.SCARED, LocalDateTime.now());

        // when
        alertEventHandler.handleNewEmotionLogged(event);

        // then
        verify(emotionAlertService).newTriggeringEmotionForUser(event);
    }

    @Test
    void whenExceptionArisesInStateServiceThenCorrectAMQPExceptionThrows() {
        // given
        StateLoggedEvent event = new StateLoggedEvent(1L, 1L, State.AWFUL, LocalDateTime.now());
        doThrow(new RuntimeException("exception message"))
                .when(stateAlertService).newTriggeringStateForUser(event);

        // then
        assertThatExceptionOfType(AmqpRejectAndDontRequeueException.class)
                .isThrownBy(() -> alertEventHandler.handleNewStateLogged(event));
    }

    @Test
    void whenExceptionArisesInEmotionServiceThenCorrectAMQPExceptionThrows() {
        // given
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, 1L, Emotion.SCARED, LocalDateTime.now());
        doThrow(new RuntimeException("exception message"))
                .when(emotionAlertService).newTriggeringEmotionForUser(event);

        // then
        assertThatExceptionOfType(AmqpRejectAndDontRequeueException.class)
                .isThrownBy(() -> alertEventHandler.handleNewEmotionLogged(event));
    }
}