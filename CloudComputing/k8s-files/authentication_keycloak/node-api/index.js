require("dotenv").config();
const fs = require("fs");
const https = require("https");
const express = require("express");
const router = require("./src/routes");

const app = express();
const PORT = 3000;

// Load the self-signed certificate and key
const httpsOptions = {
  key: fs.readFileSync("nodeappkey.pem"),
  cert: fs.readFileSync("nodeappcert.pem"),
};

app.use(express.json());
app.use("/api", router);

// Create an HTTPS server
https.createServer(httpsOptions, app).listen(PORT, "0.0.0.0", () => {
  console.log(`Server running securely on https://0.0.0.0:${PORT}`);
});
