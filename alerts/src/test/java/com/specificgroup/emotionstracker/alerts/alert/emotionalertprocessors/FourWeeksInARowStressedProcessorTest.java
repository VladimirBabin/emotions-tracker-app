package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FourWeeksInARowStressedProcessorTest {

    private final FourWeeksInARowStressedProcessor alertProcessor = new FourWeeksInARowStressedProcessor();

    @Test
    void whenEligibleForAlertAndNotReceivedYetThenAlertPresent() {
        // given
        String userId = UUID.randomUUID().toString();
        EmotionAlert emotionAlert1 = new EmotionAlert(1L, userId, nowMinusCustomDays(28),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert2 = new EmotionAlert(2L, userId, nowMinusCustomDays(20),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert3 = new EmotionAlert(3L, userId, nowMinusCustomDays(11),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert4 = new EmotionAlert(4L, userId, nowMinusCustomDays(3),
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
        String userId = UUID.randomUUID().toString();
        EmotionAlert emotionAlert1 = new EmotionAlert(1L, userId, nowMinusCustomDays(28),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert2 = new EmotionAlert(2L, userId, nowMinusCustomDays(20),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert3 = new EmotionAlert(3L, userId, nowMinusCustomDays(11),
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
        String userId = UUID.randomUUID().toString();
        EmotionAlert emotionAlert1 = new EmotionAlert(1L, userId, nowMinusCustomDays(28),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert2 = new EmotionAlert(2L, userId, nowMinusCustomDays(20),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert3 = new EmotionAlert(3L, userId, nowMinusCustomDays(11),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert4 = new EmotionAlert(4L, userId, nowMinusCustomDays(3),
                EmotionAlertType.STRESSED_ONCE_A_WEEK, false);
        EmotionAlert emotionAlert5 = new EmotionAlert(5L, userId, nowMinusCustomDays(3),
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