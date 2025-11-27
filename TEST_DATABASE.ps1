# Quick Database Test Script
# Run this from the project root directory

Write-Host "=== Setting up Neon Database Connection ===" -ForegroundColor Cyan
Write-Host ""

# Set your credentials here (replace with your actual values)
$env:NEON_DB_URL = "jdbc:postgresql://ep-red-sun-agapswm0-pooler.c-2.eu-central-1.aws.neon.tech:5432/neondb?sslmode=require"
$env:NEON_DB_USER = "neondb_owner"
$env:NEON_DB_PASSWORD = "npg_vFeS7Qoi3WuT"

Write-Host "✓ Environment variables set!" -ForegroundColor Green
Write-Host ""
Write-Host "Running database demo..." -ForegroundColor Cyan
Write-Host ""

# Run the demo
mvn clean compile exec:java

Write-Host ""
Write-Host "=== Done! ===" -ForegroundColor Green
Write-Host ""
Write-Host "Now check in Neon Console:" -ForegroundColor Yellow
Write-Host "1. Go to https://console.neon.tech" -ForegroundColor White
Write-Host "2. Click your project" -ForegroundColor White
Write-Host "3. Click 'SQL Editor' tab" -ForegroundColor White
Write-Host "4. Run: SELECT * FROM books;" -ForegroundColor White

