package com.github.vbabin.emocheck.state;

import lombok.Value;

@Value
public class WeeklyStats {
    int badState;
    int goodState;
    int excellentState;
}
