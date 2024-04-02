import {getAuthToken} from "./AuthApiClient";


class StatsApiClient {
    static SERVER_URL = 'http://localhost:8000';

    static GET_WEEKLY_STATS_BY_USER_ID = '/stats/state/week?userId=';

    static weeklyStats(userId: number): Promise<Response> {
        return fetch(StatsApiClient.SERVER_URL +
            StatsApiClient.GET_WEEKLY_STATS_BY_USER_ID +
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