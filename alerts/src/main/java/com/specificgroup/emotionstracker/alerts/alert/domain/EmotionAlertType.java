package com.specificgroup.emotionstracker.alerts.alert.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration with different types of alerts a user can get when logging an emotion.
 */
@RequiredArgsConstructor
@Getter
public enum EmotionAlertType {
    STRESSED_ONCE_A_WEEK("stressed-slightly"),
    STRESSED_FIVE_TIMES_LAST_WEEK("stressed-highly"),
    STRESSED_FOUR_WEEKS_IN_A_ROW("stressed-regularly"),
    DEPRESSED_TWICE_LAST_WEEK("depressed-slightly"),
    DRAINED_TWICE_LAST_WEEK("drained-slightly"),
    SCARED_TWICE_LAST_WEEK("scared-slightly");

    private final String description;
}
