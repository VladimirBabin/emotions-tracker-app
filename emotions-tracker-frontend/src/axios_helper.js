import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8000'
axios.defaults.headers.post["Content-Type"] = 'application/json'

export const getAuthToken = () => {
    return window.localStorage.getItem("auth_token");
};

export const getUserId = () => {
    return window.localStorage.getItem("userId");
};

export const setAuthToken = (token) => {
    window.localStorage.setItem("auth_token", token);
};

export const setLogin = (login) => {
    window.localStorage.setItem("login", login);
}

export const setUserId = (userId) => {
    window.localStorage.setItem("userId", userId);
}

export const request = (method, url, data) => {
    let headers = {};
    if (getAuthToken() !== null && getAuthToken() !== "null") {
        headers = {"Authorization": `Bearer ${getAuthToken()}`};
    }

    return axios({
        method: method,
        headers: headers,
        url: url,
        data: data
    });
};