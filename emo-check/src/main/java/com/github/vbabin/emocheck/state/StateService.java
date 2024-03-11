package com.github.vbabin.emocheck.state;


public interface StateService {

    StateLog acceptNewState(StateLogDTO stateLogDTO);

    WeeklyStats getWeeklyStatsForUser(String alias);
}
