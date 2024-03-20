package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import com.specificgroup.emotionstracker.alerts.alert.statealertprocessors.StateAlertProcessor;
import com.specificgroup.emotionstracker.alerts.state.State;
import com.specificgroup.emotionstracker.alerts.state.StateLog;
import com.specificgroup.emotionstracker.alerts.state.StateLogRepository;
import com.specificgroup.emotionstracker.alerts.state.StateLoggedEvent;
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
class StateAlertServiceImplTest {
    private StateAlertService alertService;
    @Mock
    private StateLogRepository logRepository;
    @Mock
    private StateAlertRepository alertRepository;

    @Mock
    private StateAlertProcessor alertProcessor;

    @BeforeEach
    void setUp() {
        alertService = new StateAlertServiceImpl(logRepository,
                alertRepository,
                List.of(alertProcessor));
    }

    @Test
    void whenNewTriggeringStateReceivedThenPersisted() {
        // given
        StateLoggedEvent event = new StateLoggedEvent(1L, 1L, State.AWFUL, LocalDateTime.now());
        StateLog stateLog = new StateLog(null,
                event.getUserId(),
                event.getState(),
                event.getDateTime());

        // when
        alertService.newTriggeringStateForUser(event);

        // then
        verify(logRepository).save(stateLog);
    }

    @Test
    void whenEligibleForNewAlertThenAlertPersisted() {
        // given
        StateLoggedEvent event = new StateLoggedEvent(1L, 1L, State.AWFUL, LocalDateTime.now());
        List<StateLog> foundLogs = List.of(
                new StateLog(1L, 1L, State.BAD, LocalDateTime.now()),
                new StateLog(2L, 1L, State.BAD, LocalDateTime.now())
        );
        given(logRepository.findByUserIdOrderByDateTime(1L))
                .willReturn(foundLogs);
        given(alertRepository.getAlertsByUserIdAfterGivenLocalDateTime(eq(1L),
                any()))
                .willReturn(List.of());
        given(alertProcessor.processForOptionalAlertWithCheck(foundLogs, List.of()))
                .willReturn(Optional.of(StateAlertType.LOW_STATE_TWICE_IN_TWO_DAYS));

        // when
        alertService.newTriggeringStateForUser(event);

        // then
        ArgumentCaptor<List<StateAlert>> listArgumentCaptor = ArgumentCaptor.captor();
        verify(alertRepository, times(1))
                .saveAll(listArgumentCaptor.capture());
        then(listArgumentCaptor.getValue().get(0).getStateAlertType())
                .isEqualTo(StateAlertType.LOW_STATE_TWICE_IN_TWO_DAYS);
    }

    @Test
    void whenNotEligibleForNewAlertThenNoAlertPersisted() {
        // given
        StateLoggedEvent event = new StateLoggedEvent(1L, 1L, State.AWFUL, LocalDateTime.now());

        given(logRepository.findByUserIdOrderByDateTime(1L))
                .willReturn(List.of());
        given(alertRepository.getAlertsByUserIdAfterGivenLocalDateTime(eq(1L),
                any()))
                .willReturn(List.of());
        given(alertProcessor.processForOptionalAlertWithCheck(List.of(), List.of()))
                .willReturn(Optional.empty());

        // when
        alertService.newTriggeringStateForUser(event);

        // then
        verify(alertRepository, never()).saveAll(any());
    }
}