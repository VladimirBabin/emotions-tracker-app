package com.specificgroup.emotionstracker.emocheck.state-group.emotions-tracker.emocheck.state;


public interface StateLogService {

    StateLog acceptNewState(StateLogDTO stateLogDTO);

    WeeklyStats getWeeklyStatsForUser(String alias);
}
