# Yelp Database Management (Java + SQL Server)

A Java Swing desktop app that connects to Microsoft SQL Server to explore and manage a Yelp-like dataset. It includes:

- User login and sign-up
- Search Businesses by name/city/min stars with optional sorting
- Search Users by name, min review count, and max average stars
- Click a Business to open details and submit a review
- Click a User to add them as a friend

Main entry point: `main.login`


## Repository layout

- `src/main/` — Java sources (package `main`)
- `bin/` — Compiled classes output (Eclipse default)
- `sqljdbc_12.4.2.0_enu/` — Microsoft SQL Server JDBC driver (multiple JRE variants)
- `readme.txt` — Original notes
- `.project`, `.classpath` — Eclipse project metadata


## Prerequisites

- Java JDK 8 or 11+ installed (choose JDBC JAR accordingly)
- Access to a SQL Server instance with the expected schema (see below)
- Windows PowerShell (commands below assume PowerShell)

Bundled JDBC driver JARs:
- Java 11+: `sqljdbc_12.4.2.0_enu/sqljdbc_12.4/enu/jars/mssql-jdbc-12.4.2.jre11.jar`
- Java 8:  `sqljdbc_12.4.2.0_enu/sqljdbc_12.4/enu/jars/mssql-jdbc-12.4.2.jre8.jar`


## Configure the database connection

Database connection is defined in `src/main/login.java` in the `getConnection()` method.

Before running, update the following to your environment:
- JDBC URL, username, password

Example patterns:
- Local instance: `jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;loginTimeout=30;`
- Remote host:    `jdbc:sqlserver://<hostname>;encrypt=true;trustServerCertificate=true;loginTimeout=30;`

Security tips:
- Do not commit real credentials to source control.
- Prefer loading credentials from environment variables or a config file ignored by VCS.


## Expected database schema (columns used by the app)

The code references these tables/columns (types are suggestions):

- `Users(
  UserID VARCHAR PRIMARY KEY,
  Password VARCHAR NOT NULL
)`

- `user_yelp(
  user_id VARCHAR PRIMARY KEY,
  name VARCHAR,
  review_count INT,
  yelping_since VARCHAR,
  useful INT,
  funny INT,
  cool INT,
  fans INT,
  average_stars FLOAT
)`

- `business(
  business_id VARCHAR PRIMARY KEY,
  name VARCHAR,
  address VARCHAR,
  city VARCHAR,
  postal_code VARCHAR,
  stars INT,
  review_count INT
)`

- `review(
  review_id VARCHAR PRIMARY KEY,
  user_id VARCHAR,
  business_id VARCHAR,
  stars INT
)`

- `friendship(
  user_id VARCHAR,
  friend VARCHAR
)`

The sign-up flow inserts into `Users` and `user_yelp`. Searches read from `user_yelp` and `business`. Reviews insert into `review`. Adding friend inserts into `friendship`.


## Build and run (Windows PowerShell)

The project is a plain Java project (no Maven/Gradle). Compile with `javac` and include the JDBC JAR on the classpath.

1) Set variables (adjust paths if your folder differs):

```powershell
$PROJ = "C:\Users\goura\Downloads\Yelp-Database-Management"
$SRC  = Join-Path $PROJ 'src\main'
$BIN  = Join-Path $PROJ 'bin'
# Pick the JAR matching your Java version
$JDBC_JAR = Join-Path $PROJ 'sqljdbc_12.4.2.0_enu\sqljdbc_12.4\enu\jars\mssql-jdbc-12.4.2.jre11.jar'
```

2) Create the output folder and compile:

```powershell
New-Item -ItemType Directory -Force -Path $BIN | Out-Null
javac -encoding UTF-8 -cp "$JDBC_JAR" -d $BIN (Get-ChildItem "$SRC\*.java").FullName
```

3) Run the app (main class is `main.login`):

```powershell
java -cp "$BIN;$JDBC_JAR" main.login
```

If you are on Java 8, replace `$JDBC_JAR` with the `jre8` jar path shown above.


## Run from an IDE (Eclipse/VS Code)

- Eclipse: Import Existing Java Project, ensure `bin` is the output folder, add the JDBC JAR to the project Build Path, and run `main.login`.
- VS Code: Install Java extensions, add the JDBC JAR as a Referenced Library, and run the `main.login` class.


## Feature walkthrough

- Launch → Login screen (`Users` table)
  - New user? Click Sign Up: inserts into `Users` and `user_yelp`.
- Select Category → Search Business or Search User.
- Search Business:
  - Filters: name, city, min stars. Optional sorting by Name/City/Stars.
  - Click a row → Business details dialog → submit a review.
- Search User:
  - Filters: user name, minimum review count, maximum average stars.
  - Click a row → prompt to add that user as a friend.


## Troubleshooting

- Driver not found / Classpath errors:
  - Ensure the correct `mssql-jdbc-*.jar` is included on both compile and run classpaths.
- Cannot connect to SQL Server:
  - Verify host/port, credentials, firewall, and encryption settings (`encrypt=true` with proper certificates or `trustServerCertificate=true` for dev only).
- SQL errors about missing tables/columns:
  - Create tables with at least the columns listed in the schema section.
- UI does not open:
  - Ensure compilation succeeded and you are running `main.login`.


## Notes and known issues

- SQL injection risk: Several queries are built via string concatenation. Replace with prepared statements.
- Plain-text passwords: The app stores and compares passwords as plain text. Use salted hashing (e.g., BCrypt) in production.
- Input validation: Validate numeric fields (e.g., min stars, review counts) and enforce ranges.
- Business search query construction: Some logical mistakes when combining filters (missing column names in some `AND` fragments). Review before production.
- Review submission dialog: Currently ignores the user-entered stars field and inserts an existing value; wire it to the input and validate 1–5.


