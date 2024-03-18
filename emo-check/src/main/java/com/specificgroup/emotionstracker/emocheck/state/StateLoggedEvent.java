package com.specificgroup.emotionstracker.emocheck.state;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class StateLoggedEvent {

    long stateLogId;
    long userId;
    String userAlias;
    State state;
    LocalDateTime dateTime;
}
