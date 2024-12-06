const { Pool } = require("pg");

const pool = new Pool({
  user: process.env.POSTGRES_USER || "keycloak",
  host: process.env.POSTGRES_HOST || "postgres-service",
  database: process.env.POSTGRES_DB || "keycloak",
  password: process.env.POSTGRES_PASSWORD || "mypassword",
  port: process.env.POSTGRES_PORT || 5432,
});

module.exports = pool;
