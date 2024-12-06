require("dotenv").config();
const express = require("express");
const router = require("./src/routes");

const app = express();
const PORT = 3000;

app.use(express.json());
app.use("/api", router);

app.listen(PORT, "0.0.0.0", () => {
  console.log("Server running on http://0.0.0.0:3000");
});
