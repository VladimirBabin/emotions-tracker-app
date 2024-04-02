package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.entry.Emotion;
import com.specificgroup.emotionstracker.alerts.entry.EmotionEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TimeBasedEmotionAlertProcessorTest {

    private final TimeBasedEmotionAlertProcessor alertProcessor = new TimeBasedEmotionAlertProcessor() {
        @Override
        public int checkedDaysPeriod() {
            return 7;
        }

        @Override
        public Emotion getEmotionType() {
            return Emotion.STRESSED;
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
        String userId = UUID.randomUUID().toString();
        EmotionEntry emotionEntry1 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(1));
        EmotionEntry emotionEntry2 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(5));
        EmotionEntry emotionEntry3 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(3));
        EmotionEntry emotionEntry4 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(4));
        EmotionEntry emotionEntry5 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(6));

        List<EmotionEntry> emotionEntries = List.of(emotionEntry1, emotionEntry2, emotionEntry3, emotionEntry4, emotionEntry5);

        // when
        Optional<EmotionAlertType> emotionAlertType = alertProcessor.processForOptionalAlertWithCheck(emotionEntries,
                List.of());

        // then
        assertThat(emotionAlertType).contains(EmotionAlertType.STRESSED_FIVE_TIMES_LAST_WEEK);
    }

    @Test
    void whenNotEligibleForAlertOptionalEmpty() {
        // given
        String userId = UUID.randomUUID().toString();
        EmotionEntry emotionEntry1 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(1));
        EmotionEntry emotionEntry2 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(5));
        EmotionEntry emotionEntry3 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(3));
        EmotionEntry emotionEntry4 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(4));

        List<EmotionEntry> emotionEntries = List.of(emotionEntry1, emotionEntry2, emotionEntry3, emotionEntry4);

        // when
        Optional<EmotionAlertType> emotionAlertType = alertProcessor.processForOptionalAlertWithCheck(emotionEntries,
                List.of());

        // then
        assertThat(emotionAlertType).isEmpty();
    }

    @Test
    void whenAlreadyReceivedAlertInSevenDaysOptionalEmpty() {
        // given
        String userId = UUID.randomUUID().toString();
        EmotionEntry emotionEntry1 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(1));
        EmotionEntry emotionEntry2 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(5));
        EmotionEntry emotionEntry3 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(3));
        EmotionEntry emotionEntry4 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(4));
        EmotionEntry emotionEntry5 = new EmotionEntry(1L, userId, Emotion.STRESSED, nowMinusCustomDays(6));

        List<EmotionEntry> emotionEntries = List.of(emotionEntry1, emotionEntry2, emotionEntry3, emotionEntry4, emotionEntry5);

        // when
        Optional<EmotionAlertType> emotionAlertType = alertProcessor.processForOptionalAlertWithCheck(emotionEntries,
                List.of(
                new EmotionAlert(userId, EmotionAlertType.STRESSED_FIVE_TIMES_LAST_WEEK)
        ));


        // then
        assertThat(emotionAlertType).isEmpty();
    }

    private LocalDateTime nowMinusCustomDays(int days) {
        return LocalDateTime.now().minusDays(days);
    }

}