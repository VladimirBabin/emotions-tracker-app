package com.specificgroup.emotionstracker.entries.entry;

import com.specificgroup.emotionstracker.entries.entry.domain.Emotion;
import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import com.specificgroup.emotionstracker.entries.entry.domain.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EntryEventPublisherTest {

    private EntriesEventPublisher entriesEventPublisher;

    @Mock
    AmqpTemplate amqpTemplate;

    @BeforeEach
    void setUp() {
        entriesEventPublisher = new EntriesEventPublisher(amqpTemplate,
                "states-test.topic",
                "emotions-test.topic",
                "state.triggering",
                "emotion.triggering",
                "state.non-triggering",
                "emotion.non-triggering",
                "removed-test.topic");
    }

    @Test
    void whenStateLoggedAndEmotionsNotLoggedThenStateEventPublished() {
        // given
        String userId = UUID.randomUUID().toString();
        Entry entry = new Entry(1L, userId, State.OK, null, null, null);
        StateLoggedEvent expected = stateLoggedEvent(State.OK, userId);

        // when
        entriesEventPublisher.entryLogged(entry);

        // then
        var exchangeCaptor = ArgumentCaptor.forClass(String.class);
        var stateRoutingKeyCaptor = ArgumentCaptor.forClass(String.class);
        var stateEventCaptor = ArgumentCaptor.forClass(StateLoggedEvent.class);

        verify(amqpTemplate).convertAndSend(exchangeCaptor.capture(), stateRoutingKeyCaptor.capture(), stateEventCaptor.capture());
        then(exchangeCaptor.getValue()).isEqualTo("states-test.topic");
        then(stateRoutingKeyCaptor.getValue()).isEqualTo("state.non-triggering");
        then(stateEventCaptor.getValue()).isEqualTo(expected);
    }

    @Test
    void whenBothStateAndNonTriggeringEmotionsLoggedThenBothStateAndEmotionEventsPublished() {
        // given
        String userId = UUID.randomUUID().toString();
        Set<Emotion> emotions = Set.of(Emotion.HOPEFUL, Emotion.CONTENT, Emotion.PASSIONATE);
        Entry entry = new Entry(1L, userId, State.OK, emotions, null, null);
        StateLoggedEvent expectedStateEvent = stateLoggedEvent(State.OK, userId);
        Set<EmotionLoggedEvent> expectedEmotions = Set.of(
                emotionLoggedEvent(Emotion.HOPEFUL, userId),
                emotionLoggedEvent(Emotion.CONTENT, userId),
                emotionLoggedEvent(Emotion.PASSIONATE, userId));

        // when
        entriesEventPublisher.entryLogged(entry);

        // then
        var stateExchangeCaptor = ArgumentCaptor.forClass(String.class);
        var stateRoutingKeyCaptor = ArgumentCaptor.forClass(String.class);
        var stateEventCaptor = ArgumentCaptor.forClass(StateLoggedEvent.class);

        var emotionExchangeCaptor = ArgumentCaptor.forClass(String.class);
        var emotionRoutingKeyCaptor = ArgumentCaptor.forClass(String.class);
        var emotionEventCaptor = ArgumentCaptor.forClass(EmotionLoggedEvent.class);

        verify(amqpTemplate, times(1))
                .convertAndSend(stateExchangeCaptor.capture(), stateRoutingKeyCaptor.capture(), stateEventCaptor.capture());
        verify(amqpTemplate, times(3))
                .convertAndSend(emotionExchangeCaptor.capture(), emotionRoutingKeyCaptor.capture(), emotionEventCaptor.capture());
        then(stateExchangeCaptor.getValue()).isEqualTo("states-test.topic");
        then(stateRoutingKeyCaptor.getValue()).isEqualTo("state.non-triggering");

        then(emotionExchangeCaptor.getValue()).isEqualTo("emotions-test.topic");
        then(emotionRoutingKeyCaptor.getValue()).isEqualTo("emotion.non-triggering");

        then(stateEventCaptor.getValue()).isEqualTo(expectedStateEvent);
        then(emotionEventCaptor.getAllValues()).containsAll(expectedEmotions);
    }

    @Test
    void whenTriggeringStateAndEmotionLoggedThenEventsWithTriggeringStateAndEmotionPublished() {
        // given
        String userId = UUID.randomUUID().toString();
        Set<Emotion> emotions = Set.of(Emotion.ANGRY);
        Entry entry = new Entry(1L, userId, State.BAD, emotions, null, null);
        StateLoggedEvent expectedState = stateLoggedEvent(State.BAD, userId);
        EmotionLoggedEvent expectedEmotion = emotionLoggedEvent(Emotion.ANGRY, userId);

        // when
        entriesEventPublisher.entryLogged(entry);

        // then
        var stateExchangeCaptor = ArgumentCaptor.forClass(String.class);
        var stateRoutingKeyCaptor = ArgumentCaptor.forClass(String.class);
        var stateEventCaptor = ArgumentCaptor.forClass(StateLoggedEvent.class);

        var emotionExchangeCaptor = ArgumentCaptor.forClass(String.class);
        var emotionRoutingKeyCaptor = ArgumentCaptor.forClass(String.class);
        var emotionEventCaptor = ArgumentCaptor.forClass(EmotionLoggedEvent.class);

        verify(amqpTemplate, times(1))
                .convertAndSend(stateExchangeCaptor.capture(), stateRoutingKeyCaptor.capture(), stateEventCaptor.capture());
        then(stateExchangeCaptor.getValue()).isEqualTo("states-test.topic");
        then(stateRoutingKeyCaptor.getValue()).isEqualTo("state.triggering");
        then(stateEventCaptor.getValue()).isEqualTo(expectedState);

        verify(amqpTemplate, times(1))
                .convertAndSend(emotionExchangeCaptor.capture(), emotionRoutingKeyCaptor.capture(), emotionEventCaptor.capture());
        then(emotionExchangeCaptor.getValue()).isEqualTo("emotions-test.topic");
        then(emotionRoutingKeyCaptor.getValue()).isEqualTo("emotion.triggering");
        then(emotionEventCaptor.getValue()).isEqualTo(expectedEmotion);
    }

    @Test
    void whenEntryRemovedEventThenPublished() {
        // given
        Long entryId = 1L;

        // when
        entriesEventPublisher.entryRemoved(entryId);

        // then
        var exchangeCaptor = ArgumentCaptor.forClass(String.class);
        var entryIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(amqpTemplate, times(1))
                .convertAndSend(exchangeCaptor.capture(), entryIdCaptor.capture());
        then(exchangeCaptor.getValue()).isEqualTo("removed-test.topic");
        then(entryIdCaptor.getValue()).isEqualTo(1L);

    }

    private static StateLoggedEvent stateLoggedEvent(State state, String userId) {
        return new StateLoggedEvent(1L, userId , state, null);
    }
    private static EmotionLoggedEvent emotionLoggedEvent(Emotion emotion, String userId) {
        return new EmotionLoggedEvent(1L, userId , emotion, null);
    }
}