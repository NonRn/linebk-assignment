# Assignment Full Stack Developer

## 🛠 Tech Stack

### Backend (API)
- **Java 21** (Eclipse Temurin)
- **Spring Boot 3.x** (Data JPA, Web, Validation)
- **PostgreSQL 16** (Database)
- **Maven** (Build Tool)

### Frontend (Web)
- **React** (JS Application)
- **Axios** (HTTP Client)
- **Nginx** (Web Server & Reverse Proxy)

## 🚀 Setup Instructions

### 1. Docker Setup 🐳
ติดตั้ง Database, Backend, Frontend ผ่าน Docker Containers

**Prerequisites:**
- ติดตั้ง [Docker Desktop](https://www.docker.com/products/docker-desktop/) หรือ Docker Engine

**ขั้นตอนการติดตั้ง:**
1. Clone Project และ เข้าไปที่ Project Folder 
- หากต้องการ initial mock data ลง database อัตโนมัติ ให้วางไฟล์ SQL (Postgresql) ที่ folder initial-db [Mock Data](https://drive.google.com/drive/folders/1A_qSu3rIPKb7LVuZMg0QMf2_Tt4duxP3?usp=sharing)
2. รันคำสั่งสร้างและเริ่มระบบ:
   ```bash
   docker-compose up --build
   ```
3. รอ Docker ทำงาน และ เข้าใช้งานผ่าน Browser ที่: [http://localhost](http://localhost)

---

### 2. Local Development Setup 💻
รันที่ Local ไม่ผ่าน Docker

**Prerequisites:**
- Java 21 SDK
- Node.js (LTS Version)
- Maven 3.9+
- PostgreSQL 16

**ขั้นตอนการติดตั้ง:**

#### Step 1: Database Setup
```sql
-- 1. สร้าง Database ใหม่
CREATE DATABASE assignment template=template0 encoding='unicode' lc_collate='th_TH.UTF8' lc_ctype='th_TH.UTF8';

-- 2. สร้าง User ใหม่
CREATE USER assignment WITH PASSWORD 'assignment';

-- 3. มอบสิทธิ์ให้ User จัดการ Database นี้ได้
GRANT ALL PRIVILEGES ON DATABASE assignment TO assignment;

-- 4. เข้า database และให้ User จัดการ SCHEMA, TABLES, SEQUENCES ได้
GRANT ALL ON SCHEMA public TO assignment;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO assignment;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO assignment;
```

#### Step 2: Backend Setup (Spring Boot)
1. Clone Project และ เข้าไปที่ Folder Backend (assignment)
2. ตรวจสอบการตั้งค่าใน `src/main/resources/application.properties` ให้ตรงกับ Database
3. ติดตั้ง Dependencies และเริ่มรันแอปพลิเคชัน:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
4. Backend จะรันที่พอร์ต `8080`

#### Step 3: Frontend Setup (React)
1. Clone Project และ เข้าไปที่ Folder Frontend (assignment-web)
2. ติดตั้ง Dependencies :
   ```bash
   npm install
   ```
3. เริ่มรันระบบ Frontend:
   ```bash
   npm start
   ```
4. ระบบจะเปิด Browser อัตโนมัติที่พอร์ต `3000`

## 🧪 Testing & Demo Data
- การเข้าสู่ระบบ ยังไม่มีการตรวจสอบ pin หรือ รหัสผ่านจริง สามารถใส่รหัสผ่านอะไรก็ได้
- การเข้าหน้าใส่ pin สามารถเข้าโดยใส่ query param userid เช่น: `http://localhost/?userid=000018b0e1a211ef95a30242ac180002`
- หากไม่ใส่ query param ระบบจะส่งไปยังหน้า login ให้ใส่ userid แทนการกรอก pin

## 📂 Project Structure

```plaintext
├── assignment/                         # Spring Boot Application
│   ├── src/main
│   │   ├── java/com/linkbk/assignment
│   │   │   ├── config                  # Application Configuration Classes
│   │   │   ├── controllers             # REST Controllers (API Endpoints)
│   │   │   ├── models                  # DTO & Entities
│   │   │   ├── repositories            # Spring Data JPA Repositories
│   │   │   ├── services                # Business Logic
│   │   │   └── AssignmentApplication.java # Application Main Class
│   │   └── resources
│   │       └── application.properties  # Application Configuration
│   ├── Dockerfile                      # Multi-stage Build Dockerfile
│   └── pom.xml                         # Maven Configuration
├── assignment-web/                     # React Application
│   ├── src/
│   │   ├── assets                      # Static files (images, fonts, etc.)
│   │   ├── components                  # UI Component
│   │   ├── pages                       # Web Page (Container)
│   │   ├── services                    # API calls logic
│   │   ├── reducers                    # Redux Store Reducers
│   │   ├── utils                       # Shared helper functions
│   │   └── App.js                      # Main Application Component
│   ├── Dockerfile                      # Multi-stage Build with Nginx
│   ├── package.json                    # Node.js/React Configuration
│   └── nginx.conf                      # Nginx Configuration (Reverse Proxy)
├── initial-db                          # Database schema and initial mock data
│   └── ...                             # SQL file for initial database
└── docker-compose.yml                  # Orchestration for DB, App, and Web
```

## 🔌 API Documentation
- Swagger UI สามารถดูได้ที่: [API Docs](http://localhost:8080/swagger-ui/index.html) (หลังจากรัน Docker หรือ Backend แล้ว)

1) Users
- GET /api/v1/user?userid={userid}
  - คำอธิบาย: ดึงข้อมูลโปรไฟล์ผู้ใช้
  - Query params: userid (string, required)

- POST /api/v1/user/auth/passcode
  - คำอธิบาย: ตรวจสอบ passcode ของผู้ใช้
  - Body (JSON): { "userid": "string", "passcode": "string" }

- POST /api/v1/user/auth/login
  - คำอธิบาย: ตรวจสอบ username / password (ปัจจุบันใช้ AuthRequest)
  - Body (JSON): { "userid": "string", "passcode": "string" }

2) Accounts
- GET /api/v1/account?userid={userid}
  - คำอธิบาย: ดึงบัญชีทั้งหมดของผู้ใช้
  - Query params: userid (string, required)

- POST /api/v1/account/withdraw
  - คำอธิบาย: ถอนเงินจากบัญชี
  - Body (JSON): { "accountId": "string", "amount": <number|string> }

- POST /api/v1/account/main
  - คำอธิบาย: ตั้งบัญชีที่ระบุเป็น main account สำหรับผู้ใช้
  - Body (JSON): { "userId": "string", "accountId": "string" }

- POST /api/v1/account/name-color
  - คำอธิบาย: อัปเดต nickname และ color ของบัญชีใน account_details
  - Body (JSON): { "accountId": "string", "nickname": "string", "color": "string" }

3) Transactions
- GET /api/v1/transaction?userid={userid}&limit={limit}&offset={offset}
  - คำอธิบาย: ดึงรายการธุรกรรมของผู้ใช้แบบ pagination
  - Query params:
    - userid (string, required)
    - limit (int, optional, default 10, max 1000)
    - offset (int, optional, default 0)

4) Debit Cards
- GET /api/v1/debitcard?userid={userid}
  - คำอธิบาย: ดึงบัตรเดบิตของผู้ใช้
  - Query params: userid (string, required)

5) Banners
- GET /api/v1/banner?userid={userid}
  - คำอธิบาย: ดึงรายการ banner สำหรับผู้ใช้
  - Query params: userid (string, required)

---

## 📊 Performance Analysis

* **Tool:** Grafana k6

* **Target Endpoint 1: (Login with passcode)** 
  * `POST /api/v1/user/auth/passcode`
  * **Virtual Users (VUs):** 100 concurrent users
  * **Total Duration:** 1 minute
  
* **Target Endpoint 2: (Get Account By UserId)** 
  * `GET /api/v1/account?userid={userid}`
  * **Virtual Users (VUs):** 10 VUs (10s) → 50 VUs (30s) → 100 VUs (1m), concluded by a 30-second ramp-down period
  * **Total Duration:** 2 minute 10 seconds

### Results Summary 📝
* **Total Requests:** 3,9988 requests
* **Success Rate:** 100.00%
* **Avg Response Time:** 2.43s
* **P(90) Latency:** 8.18s
* **Max Latency:** 27.92s

### Observations 🔍
**Stability:** The application handled up to 100 concurrent users without any errors.
**Latency:** The P90 latency spiked to 8.18 seconds, exceeding the set threshold of 2 seconds.
**Bottleneck:** The maximum latency is 27.92 seconds during the 100 VU, This indicates that the database is working heavily.

![img.png](assignment/src/test/img.png)