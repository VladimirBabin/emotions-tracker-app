package com.specificgroup.emotionstracker.emocheck.state;

public interface StateLogService {

    /**
     * Accepts a new state log coming from presentation layer.
     * @param stateLogDTO
     * @return the resulting StateLog object
     */
    StateLog acceptNewState(StateLogDTO stateLogDTO);

    /**
     * Retrieves statistics about the states logged during the last week for a particular user.
     * @param alias
     * @return the resulting in query WeeklyStats object.
     */
    WeeklyStats getWeeklyStatsForUser(String alias);
}
