import axios from 'axios';

const BASE_URL = process.env.REACT_APP_API_URL ?? 'http://localhost:8080';
const client = axios.create({ baseURL: BASE_URL });
const TRANSACTION_ENDPOINT = '/api/v1/transaction';

export const getTransactionByUserIdApi = (userId, limit = 10, offset = 0) => (
    client.get(TRANSACTION_ENDPOINT, { params: { userid: userId, limit, offset } })
);
