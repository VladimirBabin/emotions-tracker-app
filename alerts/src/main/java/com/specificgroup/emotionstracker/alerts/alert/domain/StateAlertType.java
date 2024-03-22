package com.specificgroup.emotionstracker.alerts.alert.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration with different types of alerts a user can get when logging a state.
 */
@RequiredArgsConstructor
@Getter
public enum StateAlertType {
    LOW_STATE_TWICE_IN_TWO_DAYS ("resources-low"),
    LOW_STATE_SEVEN_TIMES_A_WEEK("resources-lowest");

    private final String description;
}
