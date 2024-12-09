const keycloakService = require("../services/keycloakService");

const authMiddleware = async (req, res, next) => {
  const authHeader = req.headers.authorization;
  if (!authHeader) {
    return res.status(401).json({ error: "Authorization header missing" });
  }

  const token = authHeader.split(" ")[1];
  try {
    const introspection = await keycloakService.introspectToken(token);
    if (!introspection.active) {
      return res.status(401).json({ error: "Token is invalid or expired" });
    }
    req.user = introspection; // Attach user details to the request
    next();
  } catch (error) {
    res
      .status(500)
      .json({ error: "Authentication failed", details: error.message });
  }
};

module.exports = authMiddleware;
