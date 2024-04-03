import {getAuthToken} from "./AuthApiClient";

class EntriesApiClient {
    static SERVER_URL = 'http://localhost:8000';

    static GET_LAST_STATS_BY_USER_ID = '/entries/last?userId=';
    static POST_STATE = '/entries';

    static getLastEntries(userId: number): Promise<Response> {
        return fetch(EntriesApiClient.SERVER_URL +
            EntriesApiClient.GET_LAST_STATS_BY_USER_ID +
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
                     comment: string,
                     dateTime: string): Promise<Response> {
        return fetch(EntriesApiClient.SERVER_URL + EntriesApiClient.POST_STATE,
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
                        comment: comment,
                        dateTime: dateTime
                    }
                )
            });
    }
}

export default EntriesApiClient;