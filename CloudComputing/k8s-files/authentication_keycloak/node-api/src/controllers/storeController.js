const dbService = require("../services/dbService");

const storeController = {
  async getStores(req, res) {
    try {
      const stores = await dbService.getStores();
      res.json(stores);
    } catch (error) {
      res
        .status(500)
        .json({ error: "Failed to fetch stores", details: error.message });
    }
  },
};

module.exports = storeController;
