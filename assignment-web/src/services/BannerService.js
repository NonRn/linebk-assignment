import axios from 'axios';

const BASE_URL = process.env.REACT_APP_API_URL ?? 'http://localhost:8080';
const client = axios.create({ baseURL: BASE_URL });
const BANNER_ENDPOINT = '/api/v1/banner';

export const getBannerByUserIdApi = (userId) => (
    client.get(BANNER_ENDPOINT, { params: { userid: userId } })
);
