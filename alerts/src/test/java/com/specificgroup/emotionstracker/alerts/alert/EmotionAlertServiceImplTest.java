package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors.EmotionAlertProcessor;
import com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors.EmotionAlertSecondaryProcessor;
import com.specificgroup.emotionstracker.alerts.state.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmotionAlertServiceImplTest {
    EmotionAlertService alertService;
    @Mock
    EmotionLogRepository logRepository;
    @Mock
    EmotionAlertRepository alertRepository;
    @Mock
    EmotionAlertProcessor alertProcessor;
    @Mock
    EmotionAlertSecondaryProcessor secondaryProcessor;

    @BeforeEach
    void setUp() {
        alertService = new EmotionAlertServiceImpl(
                logRepository,
                alertRepository,
                List.of(alertProcessor),
                List.of(secondaryProcessor));
    }

    @Test
    void whenNewTriggeringStateReceivedThenPersisted() {
        // given
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, 1L, Emotion.SCARED, LocalDateTime.now());
        EmotionLog emotionLog = new EmotionLog(null,
                event.getUserId(),
                event.getEmotion(),
                event.getDateTime());

        // when
        alertService.newTriggeringEmotionForUser(event);

        // then
        verify(logRepository).save(emotionLog);
    }

    @Test
    void whenEligibleForNewAlertThenAlertPersisted() {
        // given
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, 1L, Emotion.SCARED, LocalDateTime.now());
        List<EmotionLog> foundLogs = List.of(
                new EmotionLog(1L, 1L, Emotion.SCARED, LocalDateTime.now()),
                new EmotionLog(2L, 1L, Emotion.SCARED, LocalDateTime.now())
        );
        given(logRepository.findByUserIdAndEmotionOrderByDateTime(1L, Emotion.SCARED))
                .willReturn(foundLogs);
        given(alertRepository.getAlertsByUserIdAfterGivenLocalDateTime(eq(1L),
                any()))
                .willReturn(List.of());
        given(alertProcessor.processForOptionalAlert(foundLogs, List.of()))
                .willReturn(Optional.of(EmotionAlertType.SCARED_TWICE_LAST_WEEK));

        // when
        alertService.newTriggeringEmotionForUser(event);

        // then
        ArgumentCaptor<List<EmotionAlert>> listArgumentCaptor = ArgumentCaptor.captor();
        verify(alertRepository, times(1))
                .saveAll(listArgumentCaptor.capture());
        then(listArgumentCaptor.getValue().get(0).getEmotionAlertType())
                .isEqualTo(EmotionAlertType.SCARED_TWICE_LAST_WEEK);
    }

    @Test
    void whenNotEligibleForNewAlertThenNoAlertPersisted() {
        // given
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, 1L, Emotion.SCARED, LocalDateTime.now());

        given(logRepository.findByUserIdAndEmotionOrderByDateTime(1L, Emotion.SCARED))
                .willReturn(List.of());
        given(alertRepository.getAlertsByUserIdAfterGivenLocalDateTime(eq(1L),
                any()))
                .willReturn(List.of());
        given(alertProcessor.processForOptionalAlert(List.of(), List.of()))
                .willReturn(Optional.empty());

        // when
        alertService.newTriggeringEmotionForUser(event);

        // then
        verify(alertRepository, never()).saveAll(any());
    }


}