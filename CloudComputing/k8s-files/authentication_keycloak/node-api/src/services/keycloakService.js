const axios = require("axios");
const https = require("https");

const keycloakService = {
  introspectToken: async (token) => {
    const introspectionUrl = `${process.env.KEYCLOAK_URL}/realms/${process.env.KEYCLOAK_REALM}/protocol/openid-connect/token/introspect`;

    const httpsAgent = new https.Agent({
      rejectUnauthorized: false,
    });

    try {
      const response = await axios.post(
        introspectionUrl,
        new URLSearchParams({
          client_id: process.env.CLIENT_ID,
          client_secret: process.env.CLIENT_SECRET,
          token: token,
        }),
        {
          headers: { "Content-Type": "application/x-www-form-urlencoded" },
          httpsAgent: httpsAgent,
        }
      );
      return response.data;
    } catch (error) {
      console.error(
        "Token introspection failed:",
        error.response?.data || error.message
      );
      throw new Error("Token introspection failed: " + error.message);
    }
  },
};

module.exports = keycloakService;
