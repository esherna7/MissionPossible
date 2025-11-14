# MissionPossible

MissionPossible is a full-stack web application with:
* Frontend: React (Vite)
* Backend: Java Spring Boot
* Proxy layer: Node.js to forward API calls and handle CORS

This guide explains how to run the entire app for development.

---

## Prerequisites

Make sure you have installed:

- **Node.js** (v18+)
- **npm** (comes with Node.js)
- **Java JDK** (v17+ recommended, Java 25 supported)
- **Maven** (included with Spring Boot wrapper `mvnw`)
- **PostgreSQL**

---

## 📦 Folder Structure

```bash
MissionPossible/
├─ frontend/mission-possible   # React frontend
├─ proxy/                      # Node proxy layer
├─ backend/                    # Spring Boot backend
```

---

## 📦 Backend (Springboot)

Navigate to backend folder:
```bash
cd backend
```

Run the backend:
```bash
./mvnw spring-boot:run
```

Runs on `http://localhost:8080`.

---

## 📦 Proxy Layer (Node.js)

Navigate to proxy folder:
```bash
cd proxy
```

Install dependencies:
```bash
npm install
```

Run the proxy:
```bash
npm start 
```

Runs in development mode with auto restart on `http://localhost:3001`.

---

## 📦 Frontend (React / Vite)

Navigate to frontend folder:
```bash
cd frontend/mission-possible
```

Install dependencies:
```bash
npm install
```

Run the frontend:
```bash
npm run dev
```

Runs on `http://localhost:5173`.