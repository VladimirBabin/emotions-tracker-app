import {getAuthToken} from "./AuthApiClient";

class AlertsApiClient {
    static SERVER_URL = 'http://localhost:8000';

    static GET_RECENT_ALERTS_BY_USER_ID = '/alerts/recent?userId=';

    static checkForRecentAlerts(userId: number): Promise<Response> {
        return fetch(AlertsApiClient.SERVER_URL +
            AlertsApiClient.GET_RECENT_ALERTS_BY_USER_ID +
            userId,
            {
                headers: new Headers({
                    'Authorization': `Bearer ${getAuthToken()}`,
                    'Content-Type': 'application/json'
                })
            });
    }
}

export default AlertsApiClient;