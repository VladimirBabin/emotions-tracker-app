package com.specificgroup.emotionstracker.emocheck.state;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class WeeklyStats {
    BigDecimal badState;
    BigDecimal goodState;
    BigDecimal excellentState;
}
