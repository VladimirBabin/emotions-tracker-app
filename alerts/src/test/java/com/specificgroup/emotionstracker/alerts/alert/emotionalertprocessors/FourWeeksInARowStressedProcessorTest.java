package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FourWeeksInARowStressedProcessorTest {

    private final FourWeeksInARowStressedProcessor alertProcessor = new FourWeeksInARowStressedProcessor();

    @Test
    void whenEligibleForAlertAndNotReceivedYetThenAlertPresent() {
        // given
        EmotionAlert emotionAlert1 = new EmotionAlert(1L, 1L, nowMinusCustomDays(28),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert2 = new EmotionAlert(2L, 1L, nowMinusCustomDays(20),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert3 = new EmotionAlert(3L, 1L, nowMinusCustomDays(11),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert4 = new EmotionAlert(4L, 1L, nowMinusCustomDays(3),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        List<EmotionAlert> alerts = List.of(emotionAlert1, emotionAlert2, emotionAlert3, emotionAlert4);

        // when
        Optional<EmotionAlertType> emotionAlertType = alertProcessor.processForOptionalAlertWithCheck(List.of(), alerts);

        // then
        assertThat(emotionAlertType).contains(EmotionAlertType.STRESSED_FOUR_WEEKS_IN_A_ROW);
    }

    @Test
    void whenNotEligibleForAlertOptionalEmpty() {
        // given
        EmotionAlert emotionAlert1 = new EmotionAlert(1L, 1L, nowMinusCustomDays(28),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert2 = new EmotionAlert(2L, 1L, nowMinusCustomDays(20),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert3 = new EmotionAlert(3L, 1L, nowMinusCustomDays(11),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);

        List<EmotionAlert> alerts = List.of(emotionAlert1, emotionAlert2, emotionAlert3);

        // when
        Optional<EmotionAlertType> emotionAlertType = alertProcessor.processForOptionalAlertWithCheck(List.of(), alerts);

        // then
        assertThat(emotionAlertType).isEmpty();
    }

    @Test
    void whenAlreadyReceivedAlertInSevenDaysOptionalEmpty() {
        // given
        EmotionAlert emotionAlert1 = new EmotionAlert(1L, 1L, nowMinusCustomDays(28),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert2 = new EmotionAlert(2L, 1L, nowMinusCustomDays(20),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert3 = new EmotionAlert(3L, 1L, nowMinusCustomDays(11),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert4 = new EmotionAlert(4L, 1L, nowMinusCustomDays(3),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert5 = new EmotionAlert(5L, 1L, nowMinusCustomDays(3),
                EmotionAlertType.STRESSED_FOUR_WEEKS_IN_A_ROW, false);
        List<EmotionAlert> alerts = List.of(emotionAlert1, emotionAlert2, emotionAlert3, emotionAlert4, emotionAlert5);

        // when
        Optional<EmotionAlertType> emotionAlertType = alertProcessor.processForOptionalAlertWithCheck(List.of(), alerts);

        // then
        assertThat(emotionAlertType).isEmpty();
    }

    private LocalDateTime nowMinusCustomDays(int days) {
        return LocalDateTime.now().minusDays(days);
    }
}