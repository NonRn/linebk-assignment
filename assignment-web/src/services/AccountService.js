import axios from 'axios';

const BASE_URL = process.env.REACT_APP_API_URL ?? 'http://localhost:8080';
const client = axios.create({ baseURL: BASE_URL });
const ACCOUNT_ENDPOINT = '/api/v1/account';

export const getAccountByUserIdApi = (userId) => (
    client.get(ACCOUNT_ENDPOINT, { params: { userid: userId } })
);

export const withdrawApi = (accountId, amount) => (
    client.post(`${ACCOUNT_ENDPOINT}/withdraw`, { accountId, amount })
);

export const changeNameColorApi = (accountId, nickname, color) => (
    client.post(`${ACCOUNT_ENDPOINT}/name-color`, { accountId, nickname, color })
);

export const setupMainAccountApi = (userId, accountId) => (
    client.post(`${ACCOUNT_ENDPOINT}/main`, { userId, accountId })
);
