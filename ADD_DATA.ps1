# Simple script to add admin and users
# Run this from project root

$env:NEON_DB_URL = "jdbc:postgresql://ep-red-sun-agapswm0-pooler.c-2.eu-central-1.aws.neon.tech:5432/neondb?sslmode=require"
$env:NEON_DB_USER = "neondb_owner"
$env:NEON_DB_PASSWORD = "npg_vFeS7Qoi3WuT"

Write-Host "Running database population..." -ForegroundColor Cyan

# Use Maven with proper escaping
& mvn compile exec:java "-Dexec.mainClass=edu.najah.library.persistence.PopulateDatabase"

