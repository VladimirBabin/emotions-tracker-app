import {getAuthToken} from "./AuthApiClient";

class StatsApiClient {
    static SERVER_URL = 'http://localhost:8000';

    static GET_WEEKLY_STATE_STATS_BY_USER_ID = '/stats/state/week?userId=';
    static GET_WEEKLY_TOP_EMOTIONS_BY_USER_ID = '/stats/emotion/week/top?userId=';

    static getWeeklyStateStats(userId: number): Promise<Response> {
        return fetch(StatsApiClient.SERVER_URL +
            StatsApiClient.GET_WEEKLY_STATE_STATS_BY_USER_ID +
            userId,
            {
                headers: new Headers({
                    'Authorization': `Bearer ${getAuthToken()}`,
                    'Content-Type': 'application/json'
                })
            });
    }

    static getTopEmotions(userId: number): Promise<Response> {
        return fetch(StatsApiClient.SERVER_URL +
            StatsApiClient.GET_WEEKLY_TOP_EMOTIONS_BY_USER_ID +
            userId,
            {
                headers: new Headers({
                    'Authorization': `Bearer ${getAuthToken()}`,
                    'Content-Type': 'application/json'
                })
            });
    }
}

export default StatsApiClient;