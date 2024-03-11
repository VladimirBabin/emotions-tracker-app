package com.github.vbabin.emocheck.state;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class StateLogDTO {
    String userAlias;
    State state;
    LocalDateTime dateTime;
}
