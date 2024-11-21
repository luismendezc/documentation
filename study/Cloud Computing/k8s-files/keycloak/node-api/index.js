const express = require('express');
const app = express();
const port = 3000;

// Health check endpoint
app.get('/', (req, res) => {
    res.send('Hello from Node.js API!');
});

// Example secured endpoint
app.get('/secure', (req, res) => {
    res.send('This is a secured route!');
});

app.listen(port, () => {
    console.log(`Node.js API running at http://localhost:${port}`);
});