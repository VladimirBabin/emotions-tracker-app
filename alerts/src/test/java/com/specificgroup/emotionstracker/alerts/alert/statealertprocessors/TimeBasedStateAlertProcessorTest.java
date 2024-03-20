package com.specificgroup.emotionstracker.alerts.alert.statealertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import com.specificgroup.emotionstracker.alerts.state.State;
import com.specificgroup.emotionstracker.alerts.state.StateLog;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TimeBasedStateAlertProcessorTest {

    private final TimeBasedStateAlertProcessor alertProcessor = new SevenTimesAWeekStateAlertProcessor();

    @Test
    void whenEligibleForAlertAndNotReceivedYetThenAlertPresent() {
        // given
        StateLog stateLog1 = new StateLog(1L, 1L, State.BAD, nowMinusCustomDays(1));
        StateLog stateLog2 = new StateLog(2L, 1L, State.BAD, nowMinusCustomDays(2));
        StateLog stateLog3 = new StateLog(3L, 1L, State.AWFUL, nowMinusCustomDays(3));
        StateLog stateLog4 = new StateLog(4L, 1L, State.BAD, nowMinusCustomDays(4));
        StateLog stateLog5 = new StateLog(5L, 1L, State.BAD, nowMinusCustomDays(5));
        StateLog stateLog6 = new StateLog(6L, 1L, State.AWFUL, nowMinusCustomDays(6));
        StateLog stateLog7 = new StateLog(7L, 1L, State.BAD, nowMinusCustomDays(6));
        List<StateLog> stateLogs = List.of(stateLog1, stateLog2, stateLog3, stateLog4, stateLog5, stateLog6, stateLog7);

        // when
        Optional<StateAlertType> stateAlertType = alertProcessor.processForOptionalAlertWithCheck(stateLogs, List.of());

        // then
        assertThat(stateAlertType).contains(StateAlertType.LOW_STATE_SEVEN_TIMES_A_WEEK);
    }

    @Test
    void whenNotEligibleForAlertOptionalEmpty() {
        // given
        StateLog stateLog1 = new StateLog(1L, 1L, State.BAD, nowMinusCustomDays(1));
        StateLog stateLog2 = new StateLog(2L, 1L, State.BAD, nowMinusCustomDays(2));
        StateLog stateLog3 = new StateLog(3L, 1L, State.AWFUL, nowMinusCustomDays(3));
        StateLog stateLog4 = new StateLog(4L, 1L, State.BAD, nowMinusCustomDays(4));
        StateLog stateLog5 = new StateLog(5L, 1L, State.BAD, nowMinusCustomDays(5));
        StateLog stateLog6 = new StateLog(6L, 1L, State.AWFUL, nowMinusCustomDays(6));
        List<StateLog> stateLogs = List.of(stateLog1, stateLog2, stateLog3, stateLog4, stateLog5, stateLog6);

        // when
        Optional<StateAlertType> stateAlertType = alertProcessor.processForOptionalAlertWithCheck(stateLogs, List.of());

        // then
        assertThat(stateAlertType).isEmpty();
    }

    @Test
    void whenAlreadyReceivedAlertInSevenDaysOptionalEmpty() {
        // given
        StateLog stateLog1 = new StateLog(1L, 1L, State.BAD, nowMinusCustomDays(1));
        StateLog stateLog2 = new StateLog(2L, 1L, State.BAD, nowMinusCustomDays(2));
        StateLog stateLog3 = new StateLog(3L, 1L, State.AWFUL, nowMinusCustomDays(3));
        StateLog stateLog4 = new StateLog(4L, 1L, State.BAD, nowMinusCustomDays(4));
        StateLog stateLog5 = new StateLog(5L, 1L, State.BAD, nowMinusCustomDays(5));
        StateLog stateLog6 = new StateLog(6L, 1L, State.AWFUL, nowMinusCustomDays(6));
        StateLog stateLog7 = new StateLog(7L, 1L, State.BAD, nowMinusCustomDays(6));
        List<StateLog> stateLogs = List.of(stateLog1, stateLog2, stateLog3, stateLog4, stateLog5, stateLog6, stateLog7);

        // when
        Optional<StateAlertType> stateAlertType = alertProcessor.processForOptionalAlertWithCheck(stateLogs,
                List.of(new StateAlert(1L, StateAlertType.LOW_STATE_SEVEN_TIMES_A_WEEK)));

        // then
        assertThat(stateAlertType).isEmpty();
    }

    private LocalDateTime nowMinusCustomDays(int days) {
        return LocalDateTime.now().minusDays(days);
    }
}