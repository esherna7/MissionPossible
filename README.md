# Mission Possible

Mission Possible is a household chore management app designed to help families organize responsibilities and track progress. Parents manage chores and review completed work, while children claim chores, submit them for approval, and earn points.

## Project status

The project is in early development. The ERD and initial OpenAPI contract have been drafted. The Spring Boot backend connects to PostgreSQL running in Docker, and the first Liquibase migration has created `users`, `households`, and `household_members`. The tables have been verified in pgAdmin. Java entities, authentication, business endpoints, the remaining tables, and the React frontend are still to be implemented.

## Planned features

- Household creation and membership management
- Invitations to join a household
- Chore creation, claiming, and completion
- Parent approval or rejection of completed chores
- Points tracking and household activity history

## Technology

| Area | Technology |
| --- | --- |
| Backend | Java 21, Spring Boot, Maven |
| Data access | Spring Data JPA |
| Database | PostgreSQL 17 |
| Migrations | Liquibase |
| Security | Spring Security; application authentication planned |
| Local database environment | Docker Compose |
| Frontend (planned) | React and TypeScript |

## Repository layout

- `backend/` — Spring Boot project, Maven wrapper, and application configuration
- `compose.yaml` — local PostgreSQL container configuration
- `.env` — local database password; excluded from Git
- `README.md` — project overview and setup instructions

## Prerequisites

- JDK 21
- Docker Desktop with the Linux engine running
- Docker Compose
- Git
- Optional: pgAdmin for browsing the database

Maven is provided through the wrapper in `backend/`; a separate Maven installation is not required.

## Local setup (Windows PowerShell)

Open a terminal in the repository root.

### 1. Configure the database password

Create a `.env` file in the repository root:

```dotenv
DB_PASSWORD=replace_with_your_local_password
```

Use a local development password and ensure the root `.gitignore` includes `.env`. Do not commit this file.

### 2. Start PostgreSQL

```powershell
docker compose up -d
docker compose ps
```

The first run downloads the PostgreSQL image and initializes the database. Wait for the `db` service to report `healthy`.

| Connection setting | Value |
| --- | --- |
| Host | `localhost` |
| Port | `5433` |
| Database | `mission_possible` |
| Username | `mission_possible` |
| Password | The value of `DB_PASSWORD` in `.env` |

Port `5433` on the computer forwards to PostgreSQL's port `5432` inside the container. Database data is stored locally in the named `postgres_data` volume.

### 3. Start the backend

Docker Compose reads `.env` automatically. The backend currently receives its password separately through an environment variable. In the same terminal, enter the same password at the credential prompt:

```powershell
$dbCredential = Get-Credential -UserName mission_possible -Message "Enter your database password from .env"
$env:DB_PASSWORD = $dbCredential.GetNetworkCredential().Password

cd backend
.\mvnw.cmd spring-boot:run
```

Look for the `Started ...Application` message. The backend uses port `8080` by default. Business endpoints are not implemented yet, and Spring Security's default configuration may require authentication when accessing the server.

The password environment variable applies to the current terminal and its child processes. Set it again when starting the backend from a new terminal or configure it in your IDE's run settings.

### 4. Inspect the database with pgAdmin (optional)

Register a server named `Mission Possible Docker` using the connection settings above. Set the maintenance database to `mission_possible`.

## Database migrations

Liquibase's master changelog is located at:

```text
backend/src/main/resources/db/changelog/db.changelog-master.yaml
```

The YAML master includes `001-changelog.sql` from the same folder:

```yaml
databaseChangeLog:
  - include:
      file: 001-changelog.sql
      relativeToChangelogFile: true
```

The SQL file uses Liquibase's formatted SQL header and changeset markers. Its initial migration creates:

| Table | Purpose |
| --- | --- |
| `users` | Login accounts and password hashes |
| `households` | Household details and their creator |
| `household_members` | Membership, display names, and `ADMIN`/`MEMBER` roles; a login account is optional |

Liquibase also creates `databasechangelog` and `databasechangeloglock` to track migrations and coordinate updates. Pending changesets run automatically during backend startup.

Do not edit applied changesets. Add new changesets for future schema changes. Timestamp columns use `timestamptz`; `CURRENT_TIMESTAMP` sets initial values, while application code will handle subsequent `updated_at` changes.

Hibernate is configured with `ddl-auto: validate`, so it checks entity mappings against the schema rather than creating or altering tables. Liquibase owns schema changes.

## Useful commands

Run these Docker commands from the repository root:

```powershell
# View database status
docker compose ps

# View database logs
docker compose logs db

# Stop and remove containers while keeping database data
docker compose down

# Start the database again
docker compose up -d
```

Stop the backend with `Ctrl+C` in its terminal.

**Do not use `docker compose down -v` unless you intend to delete the database volume and its data.** The volume provides persistence, not a backup. The Compose file reproduces the database setup on another computer; it does not transfer existing records.

Database initialization settings apply when the volume is first created. Changing the password in `.env` later does not change the password of an existing database user.

## Next steps

1. Create Java entities and repositories for users, households, and household members, starting with `User`.
2. Implement authentication and authorization.
3. Build the first household feature and verify it against the OpenAPI contract.
4. Implement invitations, chore workflows, points, and activity tracking.
5. Build the React frontend.
