package com.specificgroup.emotionstracker.alerts.entry;

import lombok.Value;

import java.time.LocalDateTime;

/**
 * Class represents the event of logging a state for particular user.
 */
@Value
public class StateLoggedEvent {

    long stateLogId;
    String userId;
    State state;
    LocalDateTime dateTime;
}
