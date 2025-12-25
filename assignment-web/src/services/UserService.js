import axios from 'axios';

const BASE_URL = process.env.REACT_APP_API_URL ?? 'http://localhost:8080';
const client = axios.create({ baseURL: BASE_URL });
const USER_ENDPOINT = '/api/v1/user';

export const getUserByIdApi = (userId) => (
    client.get(USER_ENDPOINT, { params: { userid: userId } })
);

export const authenticatePasscodeUserApi = (userId, passcode) => (
    client.post(
        `${USER_ENDPOINT}/auth/passcode`, 
        { userid: userId, passcode },
        { validateStatus: (status) => status < 500 }
    )
);

export const authenticateLoginUserApi = (userId, password) => (
    client.post(
        `${USER_ENDPOINT}/auth/login`,
        { userid: userId, passcode: password },
        { validateStatus: (status) => status < 500 }
    )
);