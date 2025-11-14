# MissionPossible Proxy

This is the Node.js proxy server that forwards API requests from the frontend to the Spring Boot backend.
It also handles CORS and logging for development running at `http://localhost:3001`.

---

## Prerequisites

Make sure you have installed:

- **Node.js** (v18+)
- **npm** (comes with Node.js)
- **Java JDK** (v17+ recommended, Java 25 supported)
- **Maven** (included with Spring Boot wrapper `mvnw`)
- **PostgreSQL**

---

## 📦 Install Dependencies

From this folder, run commands:
```bash
npm install
```

---

## ▶️ Run app

```bash
npm start
```

The app will run in development mode with automatic restarts at: `http://localhost:3001`.
This communicates with the backend API at: `http://localhost:8080`.