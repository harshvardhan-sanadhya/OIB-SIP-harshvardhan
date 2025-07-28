const express = require("express");
const mysql = require("mysql");
const cors = require("cors");
const bodyParser = require("body-parser");

const app = express();
const port = 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(express.static("public")); // optional if you serve HTML from server

// MySQL connection
const db = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "harshvardhan",
  database: "oasis_web"
});

db.connect((err) => {
  if (err) {
    console.error("Database connection failed:", err.stack);
    return;
  }
  console.log("Connected to MySQL database.");
});

// ✅ SIGNUP Route
app.post("/signup", (req, res) => {
  const { username, password } = req.body;
  const sql = "INSERT INTO oasis_data (username, password) VALUES (?, ?)";
  db.query(sql, [username, password], (err, result) => {
    if (err) {
      console.error("Error inserting data:", err);
      return res.status(500).json({ message: "Error registering user." });
    }
    res.json({ message: "User registered successfully!" });
  });
});

// ✅ LOGIN Route
app.post("/login", (req, res) => {
  const { username, password } = req.body;
  const sql = "SELECT * FROM oasis_data WHERE username = ? AND password = ?";
  db.query(sql, [username, password], (err, results) => {
    if (err) {
      console.error("Login query error:", err);
      return res.status(500).json({ message: "Server error." });
    }
    if (results.length > 0) {
      window.location.href ="dashboard.html";
      res.json({ message: "Login successful!" });
      
    } else {
      res.status(401).json({ message: "Invalid username or password." });
    }
  });
});

// Start server
app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}`);
});

