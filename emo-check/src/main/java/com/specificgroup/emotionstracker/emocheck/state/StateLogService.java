package com.specificgroup.emotionstracker.emocheck.state;

public interface StateLogService {

    StateLog acceptNewState(StateLogDTO stateLogDTO);
    WeeklyStats getWeeklyStatsForUser(String alias);
}
