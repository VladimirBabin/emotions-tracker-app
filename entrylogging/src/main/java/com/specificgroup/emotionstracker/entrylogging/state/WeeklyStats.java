package com.specificgroup.emotionstracker.entrylogging.state;

import lombok.Value;

import java.math.BigDecimal;

/**
 * This class represents weekly statistics about user logged states in percentage.
 */
@Value
public class WeeklyStats {
    BigDecimal awfulState;
    BigDecimal badState;
    BigDecimal okState;
    BigDecimal goodState;
    BigDecimal excellentState;
}
