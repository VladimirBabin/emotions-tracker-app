package com.specificgroup.emotionstracker.stats.entry;

import lombok.Value;

import java.time.LocalDateTime;

/**
 * Class represents the event of logging a state for particular user.
 */
@Value
public class StateLoggedEvent {

    long entryId;
    String userId;
    State state;
    LocalDateTime dateTime;
}
