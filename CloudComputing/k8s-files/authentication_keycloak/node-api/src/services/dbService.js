const pool = require("../config/db");

const dbService = {
  getStores: async () => {
    try {
      const result = await pool.query("SELECT * FROM stores"); // Replace with your table name
      return result.rows;
    } catch (error) {
      console.error("Database query failed:", error);
      throw new Error("Failed to fetch stores");
    }
  },
};

module.exports = dbService;
