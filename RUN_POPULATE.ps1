# PowerShell script to populate database with admin and users
# Make sure you're in the project root directory

Write-Host "=== Populating Database with Admin and Users ===" -ForegroundColor Cyan
Write-Host ""

# Set environment variables (NO SPACES in the URL!)
$env:NEON_DB_URL = "jdbc:postgresql://ep-red-sun-agapswm0-pooler.c-2.eu-central-1.aws.neon.tech:5432/neondb?sslmode=require"
$env:NEON_DB_USER = "neondb_owner"
$env:NEON_DB_PASSWORD = "npg_vFeS7Qoi3WuT"

Write-Host "Environment variables set!" -ForegroundColor Green
Write-Host ""

# Compile first
Write-Host "Compiling..." -ForegroundColor Yellow
mvn compile -q

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 1
}

Write-Host "Running PopulateDatabase..." -ForegroundColor Yellow
Write-Host ""

# Run using java directly
java -cp "target/classes;target/dependency/*" edu.najah.library.persistence.PopulateDatabase

Write-Host ""
Write-Host "=== Done! ===" -ForegroundColor Green
Write-Host ""
Write-Host "Verify in Neon Console SQL Editor:" -ForegroundColor Cyan
Write-Host "  SELECT * FROM admins;" -ForegroundColor White
Write-Host "  SELECT * FROM users;" -ForegroundColor White

