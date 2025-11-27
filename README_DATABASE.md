# Database Setup with Neon PostgreSQL

This document explains how to set up and connect to a Neon PostgreSQL database for the Library Management System.

## Prerequisites

1. A Neon PostgreSQL account (sign up at https://neon.tech)
2. A created Neon project and database
3. Database connection credentials (host, port, database name, username, password)

## Setup Instructions

### Step 1: Create a Neon Database

1. Go to https://neon.tech and sign in
2. Create a new project
3. Note down your connection details:
   - Host (e.g., `ep-cool-darkness-123456.us-east-2.aws.neon.tech`)
   - Port (usually `5432`)
   - Database name
   - Username
   - Password

### Step 2: Configure Database Connection

You have two options for configuring the database connection:

#### Option A: Environment Variables (Recommended)

Set the following environment variables:

```bash
export NEON_DB_URL="jdbc:postgresql://your-host:5432/your-database?sslmode=require"
export NEON_DB_USER="your-username"
export NEON_DB_PASSWORD="your-password"
```

For Windows PowerShell:
```powershell
$env:NEON_DB_URL="jdbc:postgresql://your-host:5432/your-database?sslmode=require"
$env:NEON_DB_USER="your-username"
$env:NEON_DB_PASSWORD="your-password"
```

For Windows CMD:
```cmd
set NEON_DB_URL=jdbc:postgresql://your-host:5432/your-database?sslmode=require
set NEON_DB_USER=your-username
set NEON_DB_PASSWORD=your-password
```

#### Option B: Properties File

1. Copy the example properties file:
   ```bash
   cp src/main/resources/database.properties.example src/main/resources/database.properties
   ```

2. Edit `src/main/resources/database.properties` and fill in your credentials:
   ```properties
   db.url=jdbc:postgresql://your-host:5432/your-database?sslmode=require
   db.user=your-username
   db.password=your-password
   ```

**Important:** Never commit `database.properties` to version control. It's already in `.gitignore`.

### Step 3: Database Schema

The database schema will be automatically created by Hibernate when you run the application (using `hibernate.hbm2ddl.auto=update` in `persistence.xml`).

The following tables will be created:
- `admins` - Administrator accounts
- `users` - Library users/patrons
- `books` - Books in the library
- `loans` - Book borrowing records
- `fines` - Overdue fines
- `cds` - CDs in the library (if using)
- `journals` - Journals in the library (if using)

### Step 4: Using the Database

```java
import edu.najah.library.persistence.DatabaseConfig;
import jakarta.persistence.EntityManager;

// Create EntityManager using environment variables
EntityManager em = DatabaseConfig.createEntityManager();

// Or create with explicit credentials
EntityManager em = DatabaseConfig.createEntityManager(
    "jdbc:postgresql://host:5432/db", 
    "username", 
    "password"
);

// Use the EntityManager for database operations
// ...

// Close when done
em.close();
```

## Connection String Format

Neon PostgreSQL connection string format:
```
jdbc:postgresql://[host]:[port]/[database]?sslmode=require
```

Example:
```
jdbc:postgresql://ep-cool-darkness-123456.us-east-2.aws.neon.tech:5432/neondb?sslmode=require
```

## Troubleshooting

### Connection Issues

1. **SSL Required**: Make sure your connection string includes `?sslmode=require`
2. **Firewall**: Check if your IP is whitelisted in Neon dashboard
3. **Credentials**: Verify username and password are correct
4. **Host/Port**: Ensure you're using the correct host and port from Neon dashboard

### Hibernate Issues

1. Check that all JPA dependencies are in `pom.xml`
2. Verify `persistence.xml` is in `src/main/resources/META-INF/`
3. Check logs for specific error messages

## Dependencies

The following dependencies are added to `pom.xml`:
- PostgreSQL JDBC Driver (42.7.1)
- Hibernate Core (6.4.1.Final)
- HikariCP Connection Pool (5.1.0)
- Jakarta Persistence API (3.1.0)

## Next Steps

1. Add JPA annotations to domain entities (see entity classes)
2. Create repositories/DAOs for data access
3. Update services to use database instead of in-memory collections
4. Add database migrations if needed (using Flyway or Liquibase)

## Resources

- Neon Documentation: https://neon.tech/docs
- Hibernate Documentation: https://hibernate.org/orm/documentation/
- PostgreSQL JDBC: https://jdbc.postgresql.org/documentation/

