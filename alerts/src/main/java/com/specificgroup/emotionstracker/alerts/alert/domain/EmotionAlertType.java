package com.specificgroup.emotionstracker.alerts.alert.domain;

/**
 * Enumeration with different types of alerts a user can get when logging an emotion.
 */
public enum EmotionAlertType {
    STRESSED_ONCE_A_WEEK,
    STRESSED_FIVE_TIMES_LAST_WEEK,
    STRESSED_FOUR_WEEKS_IN_A_ROW,

    DEPRESSED_TWICE_LAST_WEEK,
    DRAINED_TWICE_LAST_WEEK,
    SCARED_TWICE_LAST_WEEK
}
