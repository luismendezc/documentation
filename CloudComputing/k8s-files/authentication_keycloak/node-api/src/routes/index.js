const express = require("express");
const authMiddleware = require("../middleware/authMiddleware");
const storeController = require("../controllers/storeController");

const router = express.Router();

router.get("/secure", authMiddleware, storeController.getStores);

module.exports = router;
