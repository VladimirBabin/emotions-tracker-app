package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors.EmotionAlertProcessor;
import com.specificgroup.emotionstracker.alerts.entry.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType.DEPRESSED_TWICE_LAST_WEEK;
import static com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType.SCARED_TWICE_LAST_WEEK;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmotionAlertServiceImplTest {
    private EmotionAlertService alertService;
    @Mock
    private EmotionEntryRepository entryRepository;
    @Mock
    private EmotionAlertRepository alertRepository;
    @Mock
    private EmotionAlertProcessor alertProcessor;

    @BeforeEach
    void setUp() {
        alertService = new EmotionAlertServiceImpl(
                entryRepository,
                alertRepository,
                List.of(alertProcessor));
    }

    @Test
    void whenRecentAlertsQueriedThenRepositoryMethodsCalled() {
        // given
        String userId = UUID.randomUUID().toString();
        EmotionAlert alert1 = new EmotionAlert(userId, SCARED_TWICE_LAST_WEEK);
        EmotionAlert alert2 = new EmotionAlert(userId, DEPRESSED_TWICE_LAST_WEEK);
        List<EmotionAlert> alerts = List.of(alert1, alert2);
        given(alertRepository.getNotShownAlertsByUserIdAfterGivenLocalDateTime(eq(userId), any()))
                .willReturn(alerts);
        List<String> expectedAlerts = alerts.stream().map(EmotionAlert::getEmotionAlertType)
                .map(EmotionAlertType::getDescription).toList();


        // when
        List<String> lastAddedStateAlerts = alertService.getLastAddedEmotionAlerts(userId);

        // then
        verify(alertRepository, times(1)).saveAll(any());
        then(lastAddedStateAlerts).isEqualTo(expectedAlerts);
    }

    @Test
    void whenNoAlertsReturnedByRepositoryNoAlertsUpdatedToShown() {
        // given
        String userId = UUID.randomUUID().toString();
        given(alertRepository.getNotShownAlertsByUserIdAfterGivenLocalDateTime(eq(userId), any()))
                .willReturn(List.of());

        // when
        alertService.getLastAddedEmotionAlerts(userId);

        // then
        verify(alertRepository, never()).saveAll(any());
    }

    @Test
    void whenNewTriggeringStateReceivedThenPersisted() {
        // given
        String userId = UUID.randomUUID().toString();
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, userId, Emotion.SCARED, LocalDateTime.now());
        EmotionEntry emotionEntry = new EmotionEntry(null,
                event.getEntryId(),
                event.getUserId(),
                event.getEmotion(),
                event.getDateTime());
        given(alertProcessor.getEmotionType())
                .willReturn(Emotion.SCARED);

        // when
        alertService.newTriggeringEmotionForUser(event);

        // then
        verify(entryRepository).save(emotionEntry);
    }

    @Test
    void whenEligibleForOneAlertOtherSimilarTimeAlertsNotAdded() {
        // given
        String userId = UUID.randomUUID().toString();
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, userId, Emotion.DRAINED, LocalDateTime.now());
        List<EmotionEntry> foundLogs = List.of(
                new EmotionEntry(1L, 1L, userId, Emotion.DRAINED, LocalDateTime.now()),
                new EmotionEntry(2L, 1L, userId, Emotion.DRAINED, LocalDateTime.now())
        );
        given(entryRepository.findByUserIdAndEmotionOrderByDateTime(userId, Emotion.DRAINED))
                .willReturn(foundLogs);
        given(alertRepository.getAlertsByUserIdAfterGivenLocalDateTime(eq(userId),
                any()))
                .willReturn(List.of());
        given(alertProcessor.getEmotionType())
                .willReturn(Emotion.SCARED);

        // when
        alertService.newTriggeringEmotionForUser(event);

        // then
        verify(alertProcessor, never()).processForOptionalAlertWithCheck(any(), any());
    }

    @Test
    void whenEligibleForNewAlertThenAlertPersisted() {
        // given
        String userId = UUID.randomUUID().toString();
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, userId, Emotion.SCARED, LocalDateTime.now());
        List<EmotionEntry> foundLogs = List.of(
                new EmotionEntry(1L, 1L, userId, Emotion.SCARED, LocalDateTime.now()),
                new EmotionEntry(2L, 1L, userId, Emotion.SCARED, LocalDateTime.now())
        );
        given(entryRepository.findByUserIdAndEmotionOrderByDateTime(userId, Emotion.SCARED))
                .willReturn(foundLogs);
        given(alertRepository.getAlertsByUserIdAfterGivenLocalDateTime(eq(userId),
                any()))
                .willReturn(List.of());
        given(alertProcessor.processForOptionalAlertWithCheck(foundLogs, List.of()))
                .willReturn(Optional.of(SCARED_TWICE_LAST_WEEK));
        given(alertProcessor.getEmotionType())
                .willReturn(Emotion.SCARED);

        // when
        alertService.newTriggeringEmotionForUser(event);

        // then
        ArgumentCaptor<List<EmotionAlert>> listArgumentCaptor = ArgumentCaptor.captor();
        verify(alertRepository, times(1))
                .saveAll(listArgumentCaptor.capture());
        then(listArgumentCaptor.getValue().get(0).getEmotionAlertType())
                .isEqualTo(SCARED_TWICE_LAST_WEEK);
    }

    @Test
    void whenNotEligibleForNewAlertThenNoAlertPersisted() {
        String userId = UUID.randomUUID().toString();
        // given
        EmotionLoggedEvent event = new EmotionLoggedEvent(1L, userId, Emotion.SCARED, LocalDateTime.now());

        given(entryRepository.findByUserIdAndEmotionOrderByDateTime(userId, Emotion.SCARED))
                .willReturn(List.of());
        given(alertRepository.getAlertsByUserIdAfterGivenLocalDateTime(eq(userId),
                any()))
                .willReturn(List.of());
        given(alertProcessor.getEmotionType())
                .willReturn(Emotion.SCARED);
        given(alertProcessor.processForOptionalAlertWithCheck(List.of(), List.of()))
                .willReturn(Optional.empty());


        // when
        alertService.newTriggeringEmotionForUser(event);

        // then
        verify(alertRepository, never()).saveAll(any());
    }

    @Test
    void whenRemoveEntryDataThenRepositoryCalledToDeleteAllByEntryId() {
        // given
        Long entryId = 1L;

        // when
        alertService.removeEntryRelatedData(entryId);

        // then
        verify(entryRepository).deleteAllByEntryId(entryId);
    }
}