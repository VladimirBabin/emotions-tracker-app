package com.specificgroup.emotionstracker.entries.entry;

import com.specificgroup.emotionstracker.entries.entry.domain.State;
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
