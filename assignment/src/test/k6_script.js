import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        login_scenario: {
            executor: 'constant-vus',
            vus: 100,
            duration: '1m',
            exec: 'loginTest',
        },
        get_account_scenario: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '10s', target: 10 }, // increase 10 concurrent users for 10 seconds
                { duration: '30s', target: 50 }, // increase 50 concurrent users for 30 seconds
                { duration: '1m', target: 100 }, // increase 100 concurrent users for 1 minute
                { duration: '30s', target: 0 },  // reduce to 0 users
            ],
            exec: 'getAccountTest',
        },
    },
    thresholds: {
        http_req_duration: ['p(90)<2000'], // 90% ของ request ต้องเร็วกว่า 2 วินาที
        http_req_failed: ['rate<0.01'],   // อัตราการ Error ต้องน้อยกว่า 1%
    },
};

const BASE_URL = 'http://localhost:8080/api/v1';

// Setup: เตรียมข้อมูล userid ที่มีอยู่ในระบบ
export function setup() {
    // ดึงข้อมูล User ทั้งหมดมาเก็บไว้ก่อน (API ดึงแค่ 50k users)
    const res = http.get(`${BASE_URL}/user/all`);
    const users = res.json(); 
    
    return { userIds: users };
}

export function loginTest(data) {
    // สุ่มเลือก UserID จากข้อมูลที่ได้มาจาก setup
    const randomId = data.userIds[Math.floor(Math.random() * data.userIds.length)];

    // --- API 1: Login ---
    const loginPayload = JSON.stringify({
        userid: randomId,
        passcode: '123456'
    });
    const loginRes = http.post(`${BASE_URL}/user/auth/passcode`, loginPayload, {
        headers: { 'Content-Type': 'application/json' },
    });
    check(loginRes, { 'login status is 200': (r) => r.status === 200 });

    sleep(1);
}

export function getAccountTest(data) {
    // สุ่มเลือก UserID จากข้อมูลที่ได้มาจาก setup
    const randomId = data.userIds[Math.floor(Math.random() * data.userIds.length)];

    // --- API 2: ดึงข้อมูลบัญชี [GET] ---
    const getRes = http.get(`${BASE_URL}/account?userid=${randomId}`);
    check(getRes, { 
        'get status is 200': (r) => r.status === 200
    });

    sleep(1);
}
