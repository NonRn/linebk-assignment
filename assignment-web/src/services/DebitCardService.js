import axios from 'axios';

const BASE_URL = process.env.REACT_APP_API_URL ?? 'http://localhost:8080';
const client = axios.create({ baseURL: BASE_URL });
const DEBITCARD_ENDPOINT = '/api/v1/debitcard';

export const getDebitCardByUserIdApi = (userId) => (
    client.get(DEBITCARD_ENDPOINT, { params: { userid: userId } })
);
