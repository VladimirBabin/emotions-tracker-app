package com.specificgroup.emotionstracker.emocheck.state;

import com.specificgroup.emotionstracker.emocheck.state.domain.StateLog;

import java.util.List;

public interface StateLogService {

    /**
     * Accepts a new state log coming from presentation layer.
     * @return the resulting StateLog object.
     */
    StateLog acceptNewState(StateLogDTO stateLogDTO);

    /**
     * Retrieves the last 10 logged states for a particular user.
     */
    List<StateLog> getLastLogsForUser(String userAlias);

    /**
     * Retrieves statistics about the states logged during the last week for a particular user.
     * @return the resulting in query WeeklyStats object.
     */
    WeeklyStats getWeeklyStatsForUser(String alias);
}
