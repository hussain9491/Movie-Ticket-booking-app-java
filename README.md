# CineBook

JavaFX desktop movie ticket booking application using Core Java, JDBC, and MySQL.

## Run Requirements
- JDK 17+ (project currently tested with JDK 25 on this machine)
- MySQL 8+
- Maven Wrapper (`mvnw.cmd`)

## Database Files
- `src/main/resources/db/schema.sql` - Creates database and tables.
- `src/main/resources/db/seed.sql` - Minimal movie seed.
- `src/main/resources/db/full-seed.sql` - Full demo dataset for **all tables** (`users`, `movies`, `seats`, `bookings`).

## UI/UX Design Reference
- Source design folder: `stitch_cinebook_javafx_management_system/`
- Design map and screen-by-screen overview: `DESIGN_OVERVIEW.md`
- Shared cinematic theme: `src/main/resources/com/example/cinebook/css/dark-cinema.css`

## Full Seed Accounts
When using `full-seed.sql`, these users are pre-created:
- `admin` / `Admin@123` (role: admin)
- `john` / `User@123` (role: user)
- `sara` / `User@123` (role: user)

## Quick Setup (PowerShell)
```powershell
Set-Location "C:\Users\user1542\Desktop\uni-assignments\2nd-semester\OOPS-Labs-faique\Movie-Ticket-booking-app>"
$env:JAVA_HOME="C:\Program Files\Java\jdk-25"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

## Create DB + Full Seed
```powershell
mysql -u root -p
```

```sql
SOURCE C:/Users/AHMED HUSSAIN/IdeaProjects/CineBook/src/main/resources/db/schema.sql;
SOURCE C:/Users/AHMED HUSSAIN/IdeaProjects/CineBook/src/main/resources/db/full-seed.sql;
EXIT;
```

## Run App
```powershell
.\mvnw.cmd clean test
.\mvnw.cmd javafx:run
```

## Optional DB Env Overrides
If your MySQL credentials are not the defaults in `DBConnection`, set these before running:
- `CINEBOOK_DB_URL`
- `CINEBOOK_DB_USER`
- `CINEBOOK_DB_PASSWORD`

