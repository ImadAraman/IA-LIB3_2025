# Script to add admin and users to database
# Run this from the project root directory

Write-Host "=== Adding Admin and Users to Database ===" -ForegroundColor Cyan
Write-Host ""

# Set your Neon database credentials (no spaces!)
$env:NEON_DB_URL = "jdbc:postgresql://ep-red-sun-agapswm0-pooler.c-2.eu-central-1.aws.neon.tech:5432/neondb?sslmode=require"
$env:NEON_DB_USER = "neondb_owner"
$env:NEON_DB_PASSWORD = "npg_vFeS7Qoi3WuT"

Write-Host "✓ Environment variables set!" -ForegroundColor Green
Write-Host ""
Write-Host "Running database population script..." -ForegroundColor Cyan
Write-Host ""

# Run the populate script
mvn exec:java -Dexec.mainClass="edu.najah.library.persistence.PopulateDatabase"

Write-Host ""
Write-Host "=== Done! ===" -ForegroundColor Green
Write-Host ""
Write-Host "Verify in Neon Console:" -ForegroundColor Yellow
Write-Host "1. Go to https://console.neon.tech" -ForegroundColor White
Write-Host "2. Click your project" -ForegroundColor White
Write-Host "3. Click 'SQL Editor' tab" -ForegroundColor White
Write-Host "4. Run: SELECT * FROM admins;" -ForegroundColor White
Write-Host "5. Run: SELECT * FROM users;" -ForegroundColor White

