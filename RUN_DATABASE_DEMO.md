# How to Test Database Connection and Save Data

This guide shows you how to save data to your Neon PostgreSQL database and verify it worked.

## Step 1: Make Sure Credentials Are Set

Verify your environment variables are set:

**PowerShell:**
```powershell
echo $env:NEON_DB_URL
echo $env:NEON_DB_USER
echo $env:NEON_DB_PASSWORD
```

**CMD:**
```cmd
echo %NEON_DB_URL%
echo %NEON_DB_USER%
echo %NEON_DB_PASSWORD%
```

If they're not set, set them:
```powershell
$env:NEON_DB_URL="jdbc:postgresql://your-host:5432/your-database?sslmode=require"
$env:NEON_DB_USER="your-username"
$env:NEON_DB_PASSWORD="your-password"
```

## Step 2: Run the Database Demo

Run this command to save test data to the database:

```powershell
mvn exec:java
```

Or if you need to compile first:

```powershell
mvn clean compile exec:java
```

## Step 3: What the Demo Does

The demo will:
1. ✅ Connect to your Neon database
2. ✅ Create the `books` table automatically (if it doesn't exist)
3. ✅ Save a test book: "The Great Gatsby" by F. Scott Fitzgerald
4. ✅ Query it back to verify it was saved
5. ✅ Show you all books in the database

## Step 4: Verify in Neon Console

### Option A: SQL Editor (Easiest)

1. Go to https://console.neon.tech
2. **Click on your project** (the one you created)
3. **Click on the "SQL Editor" tab** (in the left sidebar)
4. Type this query:
   ```sql
   SELECT * FROM books;
   ```
5. **Click "Run"** or press Ctrl+Enter
6. You should see the book that was saved!

### Option B: Tables Tab

1. Go to https://console.neon.tech
2. **Click on your project**
3. **Click on the "Tables" tab** (in the left sidebar)
4. You should see a `books` table listed
5. **Click on the `books` table** to see its structure
6. **Click "Browse Data"** to see the saved records

### Option C: Connect with psql or another client

You can also connect directly using the connection string from Neon dashboard.

## Expected Output

When you run the demo, you should see:

```
=== Database Connection Demo ===

✓ Successfully connected to database!

Transaction started...
Creating test book: The Great Gatsby
✓ Book saved to database successfully!

Querying saved books from database...
✓ Successfully retrieved book from database:
  ID: 1
  Title: The Great Gatsby
  Author: F. Scott Fitzgerald
  ISBN: 978-0-7432-7356-5
  Available: true

Querying all books...
✓ Found 1 book(s) in database:
  - The Great Gatsby by F. Scott Fitzgerald (ID: 1)

Database connection closed.

=== Demo Complete ===
```

## Troubleshooting

### "Could not create EntityManager"
- Check that environment variables are set correctly
- Verify your connection string includes `?sslmode=require`
- Make sure credentials are correct

### "Table does not exist"
- Hibernate should create tables automatically
- Check that `hibernate.hbm2ddl.auto=update` is in persistence.xml (it is)

### Connection timeout
- Check your IP is allowed in Neon dashboard
- Verify the host/port are correct
- Try the connection string in Neon's connection details

## Next Steps

Once you confirm the database works, you can:
1. Add more books manually via SQL
2. Update your services to use the database
3. Add JPA annotations to other entities (User, Loan, Fine, etc.)

