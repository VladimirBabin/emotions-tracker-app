
export const getAuthToken = () => {
    return window.localStorage.getItem("auth_token");
};

export const getUserId = () => {
    return window.localStorage.getItem("userId");
};

export const setAuthToken = (token) => {
    window.localStorage.setItem("auth_token", token);
};

export const setUserId = (userId) => {
    window.localStorage.setItem("userId", userId);
};

class AuthApiClient {
    static SERVER_URL = 'http://localhost:8000';

    static POST_LOGIN = '/auth/login';
    static POST_REGISTER = '/auth/register';

    static login(login: string, password: string): Promise<Response> {
        return fetch(AuthApiClient.SERVER_URL + AuthApiClient.POST_LOGIN,
            {
                method: 'POST',
                headers: new Headers({
                    'Content-Type': 'application/json'
                }),
                body: JSON.stringify(
                    {
                        login: login,
                        password: password
                    }
                )
            });
    }

    static register(firstName: string, lastName: string, login: string, password: string): Promise<Response> {
        return fetch(AuthApiClient.SERVER_URL + AuthApiClient.POST_REGISTER,
            {
                method: 'POST',
                headers: new Headers({
                    'Content-Type': 'application/json'
                }),
                body: JSON.stringify(
                    {
                        firstName: firstName,
                        lastName: lastName,
                        login: login,
                        password: password
                    }
                )
            });
    }
}

export default AuthApiClient;
