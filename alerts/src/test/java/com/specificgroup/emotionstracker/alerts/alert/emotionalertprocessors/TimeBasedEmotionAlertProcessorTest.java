package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.state.Emotion;
import com.specificgroup.emotionstracker.alerts.state.EmotionLog;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TimeBasedEmotionAlertProcessorTest {

    private final TimeBasedEmotionAlertProcessor alertProcessor = new TimeBasedEmotionAlertProcessor() {
        @Override
        public int checkedDaysPeriod() {
            return 7;
        }

        @Override
        public int numberOfRequiredRepetitions() {
            return 5;
        }

        @Override
        public EmotionAlertType emotionAlertType() {
            return EmotionAlertType.STRESSED_FIVE_TIMES_LAST_WEEK;
        }
    };

    @Test
    void whenEligibleForAlertAndNotReceivedYetThenAlertPresent() {
        // given
        EmotionLog emotionLog1 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(1));
        EmotionLog emotionLog2 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(5));
        EmotionLog emotionLog3 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(3));
        EmotionLog emotionLog4 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(4));
        EmotionLog emotionLog5 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(6));

        List<EmotionLog> emotionLogs = List.of(emotionLog1, emotionLog2, emotionLog3, emotionLog4, emotionLog5);

        // when
        Optional<EmotionAlertType> emotionAlertType = alertProcessor.processForOptionalAlertWithCheck(emotionLogs,
                List.of());

        // then
        assertThat(emotionAlertType).contains(EmotionAlertType.STRESSED_FIVE_TIMES_LAST_WEEK);
    }

    @Test
    void whenNotEligibleForAlertOptionalEmpty() {
        // given
        EmotionLog emotionLog1 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(1));
        EmotionLog emotionLog2 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(5));
        EmotionLog emotionLog3 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(3));
        EmotionLog emotionLog4 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(4));

        List<EmotionLog> emotionLogs = List.of(emotionLog1, emotionLog2, emotionLog3, emotionLog4);

        // when
        Optional<EmotionAlertType> emotionAlertType = alertProcessor.processForOptionalAlertWithCheck(emotionLogs,
                List.of());

        // then
        assertThat(emotionAlertType).isEmpty();
    }

    @Test
    void whenAlreadyReceivedAlertInSevenDaysOptionalEmpty() {
        // given
        EmotionLog emotionLog1 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(1));
        EmotionLog emotionLog2 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(5));
        EmotionLog emotionLog3 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(3));
        EmotionLog emotionLog4 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(4));
        EmotionLog emotionLog5 = new EmotionLog(1L, 1L, Emotion.STRESSED, nowMinusCustomDays(6));

        List<EmotionLog> emotionLogs = List.of(emotionLog1, emotionLog2, emotionLog3, emotionLog4, emotionLog5);

        // when
        Optional<EmotionAlertType> emotionAlertType = alertProcessor.processForOptionalAlertWithCheck(emotionLogs,
                List.of(
                new EmotionAlert(1L, EmotionAlertType.STRESSED_FIVE_TIMES_LAST_WEEK)
        ));


        // then
        assertThat(emotionAlertType).isEmpty();
    }

    private LocalDateTime nowMinusCustomDays(int days) {
        return LocalDateTime.now().minusDays(days);
    }

}