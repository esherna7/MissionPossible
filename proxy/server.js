const express = require("express");
const { createProxyMiddleware } = require("http-proxy-middleware");
const cors = require("cors");

const app = express();

// Allow requests from frontend
app.use(cors({
  origin: "http://localhost:5173",
  credentials: true
}));

// Optional logging for all requests
app.use((req, res, next) => {
  console.log(`[Proxy] Incoming request: ${req.method} ${req.url}`);
  next();
});

// Manual proxy route
app.get("/api/hello", async (req, res) => {
  try {
    console.log("[Proxy] /api/hello request received");

    // Forward request to backend
    const response = await fetch("http://localhost:8080/api/hello");
    const data = await response.text();

    console.log("[Proxy] Forwarding response to client");
    res.send(data);
  } catch (err) {
    console.error("[Proxy] Error:", err);
    res.status(500).send("Proxy error");
  }
});

// Optional root route
app.get("/", (req, res) => {
  res.send("Proxy running. Use /api/* to call backend.");
});

app.listen(3001, () => console.log("Proxy running on http://localhost:3001"));
