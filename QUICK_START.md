# Quick Start: Run Database Demo

## Option 1: Using PowerShell Script (Easiest)

1. **Edit the `RUN_DEMO.ps1` file** and replace the placeholders:
   ```powershell
   $env:NEON_DB_URL = "jdbc:postgresql://your-actual-host:5432/your-database?sslmode=require"
   $env:NEON_DB_USER = "your-actual-username"
   $env:NEON_DB_PASSWORD = "your-actual-password"
   ```

2. **Run the script:**
   ```powershell
   .\RUN_DEMO.ps1
   ```

## Option 2: Manual Setup

1. **Set environment variables in PowerShell:**
   ```powershell
   $env:NEON_DB_URL = "jdbc:postgresql://your-host:5432/your-database?sslmode=require"
   $env:NEON_DB_USER = "your-username"
   $env:NEON_DB_PASSWORD = "your-password"
   ```

2. **Run the demo:**
   ```powershell
   mvn clean compile exec:java
   ```

## Where to Find Your Neon Credentials

1. Go to https://console.neon.tech
2. Click on your project
3. Click on **"Connection Details"** or **"Settings"**
4. Copy the connection string and credentials

Your connection string format should be:
```
jdbc:postgresql://ep-xxxxx.region.aws.neon.tech:5432/neondb?sslmode=require
```

## Verify It Worked

After running the demo, check in Neon Console:

1. Go to https://console.neon.tech
2. Click on your project
3. Click on **"SQL Editor"** tab (left sidebar)
4. Run this query:
   ```sql
   SELECT * FROM books;
   ```
5. You should see the test book that was saved!

## Troubleshooting

**"ERROR: Could not create EntityManager"**
- Make sure you set the environment variables in the SAME PowerShell window
- Check your connection string includes `?sslmode=require`
- Verify your credentials are correct

**"Connection timeout"**
- Check your IP is allowed in Neon dashboard
- Verify the host/port in your connection string

