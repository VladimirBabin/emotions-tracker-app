package com.specificgroup.emotionstracker.emocheck.state;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class WeeklyStats {
    BigDecimal awfulState;
    BigDecimal badState;
    BigDecimal okState;
    BigDecimal goodState;
    BigDecimal excellentState;
}
