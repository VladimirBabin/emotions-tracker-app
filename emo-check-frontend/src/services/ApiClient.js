// Import the library
import Datetime from 'react-datetime';

class ApiClient {
    static SERVER_URL = 'http://localhost:8080';
    static GET_WEEKLY_STATS_BY_ALIAS = '/state/statistics/week?alias=';
    static GET_LAST_STATS_BY_ALIAS = '/state/statistics/last?alias=';
    static POST_STATE = '/state';

    static weeklyStats(userAlias: string): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_WEEKLY_STATS_BY_ALIAS + userAlias);
    }

    static lastLogs(userAlias: string): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_LAST_STATS_BY_ALIAS + userAlias);
    }

    static sendState(user: string,
                     state: string,
                     emotions: [],
                     dateTime: Datetime): Promise<Response> {
        console.log(
            JSON.stringify(
                {
                    userAlias: user,
                    state: state,
                    emotions: emotions,
                    dateTime: dateTime
                }
            )
        )
        return fetch(ApiClient.SERVER_URL + ApiClient.POST_STATE,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(
                    {
                        userAlias: user,
                        state: state,
                        emotions: emotions,
                        dateTime: dateTime
                    }
                )
            });
    }
}

export default ApiClient;