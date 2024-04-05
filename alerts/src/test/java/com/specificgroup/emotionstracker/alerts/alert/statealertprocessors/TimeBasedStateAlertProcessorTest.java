package com.specificgroup.emotionstracker.alerts.alert.statealertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import com.specificgroup.emotionstracker.alerts.entry.State;
import com.specificgroup.emotionstracker.alerts.entry.StateEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TimeBasedStateAlertProcessorTest {

    private final TimeBasedStateAlertProcessor alertProcessor = new SevenTimesAWeekStateAlertProcessor();

    @Test
    void whenEligibleForAlertAndNotReceivedYetThenAlertPresent() {
        // given
        String userId = UUID.randomUUID().toString();
        Long entryId = 1L;
        StateEntry stateEntry1 = new StateEntry(1L, entryId, userId, State.BAD, nowMinusCustomDays(1));
        StateEntry stateEntry2 = new StateEntry(2L, entryId, userId, State.BAD, nowMinusCustomDays(2));
        StateEntry stateEntry3 = new StateEntry(3L, entryId, userId, State.AWFUL, nowMinusCustomDays(3));
        StateEntry stateEntry4 = new StateEntry(4L, entryId, userId, State.BAD, nowMinusCustomDays(4));
        StateEntry stateEntry5 = new StateEntry(5L, entryId, userId, State.BAD, nowMinusCustomDays(5));
        StateEntry stateEntry6 = new StateEntry(6L, entryId, userId, State.AWFUL, nowMinusCustomDays(6));
        StateEntry stateEntry7 = new StateEntry(7L, entryId, userId, State.BAD, nowMinusCustomDays(6));
        List<StateEntry> stateEntries = List.of(stateEntry1, stateEntry2, stateEntry3, stateEntry4, stateEntry5, stateEntry6, stateEntry7);

        // when
        Optional<StateAlertType> stateAlertType = alertProcessor.processForOptionalAlertWithCheck(stateEntries, List.of());

        // then
        assertThat(stateAlertType).contains(StateAlertType.LOW_STATE_SEVEN_TIMES_A_WEEK);
    }

    @Test
    void whenNotEligibleForAlertOptionalEmpty() {
        // given
        String userId = UUID.randomUUID().toString();
        Long entryId = 1L;
        StateEntry stateEntry1 = new StateEntry(1L, entryId, userId, State.BAD, nowMinusCustomDays(1));
        StateEntry stateEntry2 = new StateEntry(2L, entryId, userId, State.BAD, nowMinusCustomDays(2));
        StateEntry stateEntry3 = new StateEntry(3L, entryId, userId, State.AWFUL, nowMinusCustomDays(3));
        StateEntry stateEntry4 = new StateEntry(4L, entryId, userId, State.BAD, nowMinusCustomDays(4));
        StateEntry stateEntry5 = new StateEntry(5L, entryId, userId, State.BAD, nowMinusCustomDays(5));
        StateEntry stateEntry6 = new StateEntry(6L, entryId, userId, State.AWFUL, nowMinusCustomDays(6));
        List<StateEntry> stateEntries = List.of(stateEntry1, stateEntry2, stateEntry3, stateEntry4, stateEntry5, stateEntry6);

        // when
        Optional<StateAlertType> stateAlertType = alertProcessor.processForOptionalAlertWithCheck(stateEntries, List.of());

        // then
        assertThat(stateAlertType).isEmpty();
    }

    @Test
    void whenAlreadyReceivedAlertInSevenDaysOptionalEmpty() {
        // given
        String userId = UUID.randomUUID().toString();
        Long entryId = 1L;
        StateEntry stateEntry1 = new StateEntry(1L, entryId, userId, State.BAD, nowMinusCustomDays(1));
        StateEntry stateEntry2 = new StateEntry(2L, entryId, userId, State.BAD, nowMinusCustomDays(2));
        StateEntry stateEntry3 = new StateEntry(3L, entryId, userId, State.AWFUL, nowMinusCustomDays(3));
        StateEntry stateEntry4 = new StateEntry(4L, entryId, userId, State.BAD, nowMinusCustomDays(4));
        StateEntry stateEntry5 = new StateEntry(5L, entryId, userId, State.BAD, nowMinusCustomDays(5));
        StateEntry stateEntry6 = new StateEntry(6L, entryId, userId, State.AWFUL, nowMinusCustomDays(6));
        StateEntry stateEntry7 = new StateEntry(7L, entryId, userId, State.BAD, nowMinusCustomDays(6));
        List<StateEntry> stateEntries = List.of(stateEntry1, stateEntry2, stateEntry3, stateEntry4, stateEntry5, stateEntry6, stateEntry7);

        // when
        Optional<StateAlertType> stateAlertType = alertProcessor.processForOptionalAlertWithCheck(stateEntries,
                List.of(new StateAlert(userId, StateAlertType.LOW_STATE_SEVEN_TIMES_A_WEEK)));

        // then
        assertThat(stateAlertType).isEmpty();
    }

    private LocalDateTime nowMinusCustomDays(int days) {
        return LocalDateTime.now().minusDays(days);
    }
}