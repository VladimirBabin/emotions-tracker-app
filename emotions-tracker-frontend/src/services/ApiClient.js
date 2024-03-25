// Import the library
import Datetime from 'react-datetime';

class ApiClient {
    static SERVER_URL = 'http://localhost:8000';

    static GET_WEEKLY_STATS_BY_USER_ID = '/state/statistics/week?userId=';
    static GET_LAST_STATS_BY_USER_ID = '/state/statistics/last?userId=';
    static POST_STATE = '/state';

    static GET_RECENT_ALERTS_BY_USER_ID = '/alerts/recent?userId=';

    static weeklyStats(userId: number): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_WEEKLY_STATS_BY_USER_ID + userId);
    }

    static lastLogs(userId: number): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_LAST_STATS_BY_USER_ID + userId);
    }

    static checkForRecentAlerts(userId: number): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_RECENT_ALERTS_BY_USER_ID + userId);
    }


    static sendState(userId: number,
                     state: string,
                     emotions: [],
                     dateTime: Datetime): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.POST_STATE,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(
                    {
                        userId: userId,
                        state: state,
                        emotions: emotions,
                        dateTime: dateTime
                    }
                )
            });
    }
}

export default ApiClient;