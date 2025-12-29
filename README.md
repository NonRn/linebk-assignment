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
2. รันคำสั่งสร้างและเริ่มระบบ:
   ```bash
   docker-compose up --build
   ```
3. รอ Docker ทำงาน และ เข้าใช้งานผ่าน Browser ที่: [http://localhost](http://localhost)

**Remark**
- หากต้องการ initial mock data ลง database อัตโนมัติ ให้วางไฟล์ SQL (Postgresql) ที่ folder initial-db [Mock Data](https://drive.google.com/drive/folders/1A_qSu3rIPKb7LVuZMg0QMf2_Tt4duxP3?usp=sharing)

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

## 📂 Project Structure

```plaintext
├── assignment/                         # Spring Boot Application
│   ├── src/main
│   │   ├── java/com/linkbk/assignment
│   │   │   ├── controllers             # REST Controllers (API Endpoints)
│   │   │   ├── models                  # DTO & Entities
│   │   │   ├── repositories            # Spring Data JPA Repositories
│   │   │   ├── services                # Business Logic
│   │   │   └── AssignmentApplication.java # Application Main Class
│   │   └── resource
│   │       ├── dbchangelog             # Liquibase migration
│   │       └── application.properties  # Application Configuration
│   ├── Dockerfile                      # Multi-stage Build Dockerfile
│   └── pom.xml                         # Maven Configuration
├── assignment-web/                     # React Application
│   ├── src/
│   │   ├── components                  # UI Component
│   │   ├── pages                       # Web Page (Container)
│   │   ├── services                    # API calls logic
│   │   ├── store                       # Redux Toolkit Store
│   │   └── utils                       # Shared helper functions
│   ├── public/                         # assets files (images, CSS)
│   ├── Dockerfile                      # Multi-stage Build with Nginx
│   ├── package.json                    # Node.js/React Configuration
│   └── nginx.conf                      # Nginx Configuration (Reverse Proxy)
├── initial-db                          # Database schema and initial mock data
│   └── ...                             # SQL file for initial database
└── docker-compose.yml                  # Orchestration for DB, App, and Web
```

## 🔌 API Documentation