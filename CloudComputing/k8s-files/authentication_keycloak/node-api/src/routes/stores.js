const express = require("express");
const router = express.Router();
const keycloakService = require("../services/keycloakService");

// Middleware to validate tokens
const validateToken = async (req, res, next) => {
  const authHeader = req.headers.authorization;
  if (!authHeader) return res.status(401).send("Authorization header missing");

  const token = authHeader.split(" ")[1];
  try {
    const tokenInfo = await keycloakService.introspectToken(token);
    if (!tokenInfo.active)
      return res.status(401).send("Invalid or expired token");

    req.user = tokenInfo; // Attach user details to the request
    next();
  } catch (error) {
    res.status(401).send("Unauthorized: " + error.message);
  }
};

// Secure endpoint
router.get("/secure", validateToken, (req, res) => {
  res.send(`Hello, user with ID ${req.user.sub}`);
});

// Fetch stores
router.get("/stores", validateToken, async (req, res) => {
  const dbService = require("../services/dbService");
  try {
    const stores = await dbService.getStores();
    res.json(stores);
  } catch (err) {
    res.status(500).json({ error: "Failed to fetch stores" });
  }
});

module.exports = router;
