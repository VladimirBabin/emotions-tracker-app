package com.specificgroup.emotionstracker.emocheck.state;

import com.specificgroup.emotionstracker.emocheck.state.domain.Emotion;
import com.specificgroup.emotionstracker.emocheck.state.domain.State;
import com.specificgroup.emotionstracker.emocheck.state.domain.StateLog;
import com.specificgroup.emotionstracker.emocheck.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;

import java.util.Set;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StateLogEventPublisherTest {

    private StateLogEventPublisher stateLogEventPublisher;

    @Mock
    AmqpTemplate amqpTemplate;

    @BeforeEach
    void setUp() {
        stateLogEventPublisher = new StateLogEventPublisher(amqpTemplate,
                "states-test.topic",
                "emotions-test.topic",
                "state.triggering",
                "emotion.triggering");
    }

    @Test
    void whenStateLoggedAndEmotionsNotLoggedThenStateEventPublished() {
        // given
        User user = new User(1L, "john_doe");
        StateLog stateLog = new StateLog(1L, user, State.OK, null, null);
        StateLoggedEvent expected = stateLoggedEvent(State.OK);

        // when
        stateLogEventPublisher.stateLogged(stateLog);

        // then
        var exchangeCaptor = ArgumentCaptor.forClass(String.class);
        var stateEventCaptor = ArgumentCaptor.forClass(StateLoggedEvent.class);

        verify(amqpTemplate).convertAndSend(exchangeCaptor.capture(), stateEventCaptor.capture());
        then(exchangeCaptor.getValue()).isEqualTo("states-test.topic");
        then(stateEventCaptor.getValue()).isEqualTo(expected);
    }

    @Test
    void whenBothStateAndEmotionsLoggedThenBothStateAndEmotionEventsPublished() {
        // given
        User user = new User(1L, "john_doe");
        Set<Emotion> emotions = Set.of(Emotion.ANGRY, Emotion.CONTENT, Emotion.PASSIONATE);
        StateLog stateLog = new StateLog(1L, user, State.OK, emotions, null);
        StateLoggedEvent expectedStateEvent = stateLoggedEvent(State.OK);
        Set<EmotionLoggedEvent> expectedEmotions = Set.of(
                emotionLoggedEvent(Emotion.ANGRY),
                emotionLoggedEvent(Emotion.CONTENT),
                emotionLoggedEvent(Emotion.PASSIONATE));

        // when
        stateLogEventPublisher.stateLogged(stateLog);

        // then
        var stateExchangeCaptor = ArgumentCaptor.forClass(String.class);
        var emotionExchangeCaptor = ArgumentCaptor.forClass(String.class);
        var stateEventCaptor = ArgumentCaptor.forClass(StateLoggedEvent.class);
        var emotionEventCaptor = ArgumentCaptor.forClass(EmotionLoggedEvent.class);

        verify(amqpTemplate, Mockito.times(1))
                .convertAndSend(stateExchangeCaptor.capture(), stateEventCaptor.capture());
        verify(amqpTemplate, Mockito.times(3))
                .convertAndSend(emotionExchangeCaptor.capture(), emotionEventCaptor.capture());
        then(stateExchangeCaptor.getValue()).isEqualTo("states-test.topic");
        then(emotionExchangeCaptor.getValue()).isEqualTo("emotions-test.topic");
        then(stateEventCaptor.getValue()).isEqualTo(expectedStateEvent);
        then(emotionEventCaptor.getAllValues()).containsAll(expectedEmotions);
    }

    @Test
    void whenTriggeringStateAndEmotionLoggedThenEventsWithTriggeringStateAndEmotionPublished() {
        // given
        User user = new User(1L, "john_doe");
        Set<Emotion> emotions = Set.of(Emotion.ANGRY);
        StateLog stateLog = new StateLog(1L, user, State.BAD, emotions, null);
        StateLoggedEvent expectedState = stateLoggedEvent(State.BAD);
        EmotionLoggedEvent expectedEmotion = emotionLoggedEvent(Emotion.ANGRY);

        // when
        stateLogEventPublisher.stateLogged(stateLog);

        // then
        var stateExchangeCaptor = ArgumentCaptor.forClass(String.class);
        var stateRoutingKeyCaptor = ArgumentCaptor.forClass(String.class);
        var stateEventCaptor = ArgumentCaptor.forClass(StateLoggedEvent.class);

        var emotionExchangeCaptor = ArgumentCaptor.forClass(String.class);
        var emotionRoutingKeyCaptor = ArgumentCaptor.forClass(String.class);
        var emotionEventCaptor = ArgumentCaptor.forClass(EmotionLoggedEvent.class);

        verify(amqpTemplate, Mockito.times(1))
                .convertAndSend(stateExchangeCaptor.capture(), stateRoutingKeyCaptor.capture(), stateEventCaptor.capture());
        then(stateExchangeCaptor.getValue()).isEqualTo("states-test.topic");
        then(stateRoutingKeyCaptor.getValue()).isEqualTo("state.triggering");
        then(stateEventCaptor.getValue()).isEqualTo(expectedState);

        verify(amqpTemplate, Mockito.times(1))
                .convertAndSend(emotionExchangeCaptor.capture(), emotionRoutingKeyCaptor.capture(), emotionEventCaptor.capture());
        then(emotionExchangeCaptor.getValue()).isEqualTo("emotions-test.topic");
        then(emotionRoutingKeyCaptor.getValue()).isEqualTo("emotion.triggering");
        then(emotionEventCaptor.getValue()).isEqualTo(expectedEmotion);
    }

    private static StateLoggedEvent stateLoggedEvent(State state) {
        StateLoggedEvent expectedStateEvent
                = new StateLoggedEvent(1L, 1L ,"john_doe", state, null);
        return expectedStateEvent;
    }
    private static EmotionLoggedEvent emotionLoggedEvent(Emotion emotion) {
        EmotionLoggedEvent expectedEmotionEvent
                = new EmotionLoggedEvent(1L, 1L ,"john_doe", emotion, null);
        return expectedEmotionEvent;
    }
}