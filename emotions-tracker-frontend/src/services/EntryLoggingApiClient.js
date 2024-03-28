// Import the library
import Datetime from 'react-datetime';

import {getAuthToken} from "./AuthApiClient";

class EntryLoggingApiClient {
    static SERVER_URL = 'http://localhost:8000';

    static GET_WEEKLY_STATS_BY_USER_ID = '/state/statistics/week?userId=';
    static GET_LAST_STATS_BY_USER_ID = '/state/statistics/last?userId=';
    static POST_STATE = '/state';

    static weeklyStats(userId: number): Promise<Response> {
        return fetch(EntryLoggingApiClient.SERVER_URL +
            EntryLoggingApiClient.GET_WEEKLY_STATS_BY_USER_ID +
            userId,
            {
                headers: new Headers({
                    'Authorization': `Bearer ${getAuthToken()}`,
                    'Content-Type': 'application/json'
                })
            });
    }

    static lastLogs(userId: number): Promise<Response> {
        return fetch(EntryLoggingApiClient.SERVER_URL +
            EntryLoggingApiClient.GET_LAST_STATS_BY_USER_ID +
            userId,
            {
                headers: new Headers({
                    'Authorization': `Bearer ${getAuthToken()}`,
                    'Content-Type': 'application/json'
                })
            });
    }


    static sendState(userId: number,
                     state: string,
                     emotions: [],
                     dateTime: Datetime): Promise<Response> {
        return fetch(EntryLoggingApiClient.SERVER_URL + EntryLoggingApiClient.POST_STATE,
            {
                method: 'POST',
                headers: new Headers({
                    'Authorization': `Bearer ${getAuthToken()}`,
                    'Content-Type': 'application/json'
                }),
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

export default EntryLoggingApiClient;