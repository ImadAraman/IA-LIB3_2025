# PowerShell script to set environment variables and run the database demo

Write-Host "Setting up Neon database environment variables..." -ForegroundColor Cyan
Write-Host ""

# Set your Neon database credentials here
$env:NEON_DB_URL = "jdbc:postgresql://YOUR-HOST:5432/YOUR-DATABASE?sslmode=require"
$env:NEON_DB_USER = "YOUR-USERNAME"
$env:NEON_DB_PASSWORD = "YOUR-PASSWORD"

Write-Host "Environment variables set!" -ForegroundColor Green
Write-Host ""
Write-Host "NEON_DB_URL: $env:NEON_DB_URL" -ForegroundColor Yellow
Write-Host "NEON_DB_USER: $env:NEON_DB_USER" -ForegroundColor Yellow
Write-Host "NEON_DB_PASSWORD: [HIDDEN]" -ForegroundColor Yellow
Write-Host ""
Write-Host "Running database demo..." -ForegroundColor Cyan
Write-Host ""

# Run the demo
mvn clean compile exec:java

Write-Host ""
Write-Host "Demo completed!" -ForegroundColor Green
Write-Host ""
Write-Host "To verify in Neon Console:" -ForegroundColor Cyan
Write-Host "1. Go to https://console.neon.tech" -ForegroundColor White
Write-Host "2. Click on your project" -ForegroundColor White
Write-Host "3. Click on 'SQL Editor' tab" -ForegroundColor White
Write-Host "4. Run: SELECT * FROM books;" -ForegroundColor White

