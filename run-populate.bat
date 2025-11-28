@echo off
REM Batch script to run PopulateDatabase
REM This avoids PowerShell issues with -D parameters

set NEON_DB_URL=jdbc:postgresql://ep-red-sun-agapswm0-pooler.c-2.eu-central-1.aws.neon.tech:5432/neondb?sslmode=require
set NEON_DB_USER=neondb_owner
set NEON_DB_PASSWORD=npg_vFeS7Qoi3WuT

echo === Adding Admin and Users to Database ===
echo.

mvn compile exec:java -Dexec.mainClass=edu.najah.library.persistence.PopulateDatabase

pause

