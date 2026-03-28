# WinWin.travel — Backend Engineer Test Task

Two Spring Boot microservices with JWT auth, inter-service communication, and PostgreSQL, orchestrated via Docker Compose.

---

## Architecture

```
Client
  │
  ▼
┌─────────────────────────────────┐
│  auth-api  (localhost:8080)     │
│  Spring Boot + Security + JPA   │
│  • POST /api/auth/register      │
│  • POST /api/auth/login         │
│  • POST /api/process  [JWT]     │
└────────────┬────────────────────┘
             │ POST /api/transform
             │ X-Internal-Token: <secret>
             ▼
┌─────────────────────────────────┐
│  data-api  (localhost:8081)     │
│  Spring Boot (Web only)         │
│  • POST /api/transform          │
└─────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────┐
│  PostgreSQL  (internal :5432)   │
│  • users                        │
│  • processing_log               │
└─────────────────────────────────┘
```

**Service A (`auth-api`)** handles user registration, login (JWT), and a protected `/process` endpoint that delegates text transformation to Service B and logs results to Postgres.

**Service B (`data-api`)** exposes a single `/api/transform` endpoint. It only accepts requests carrying a valid `X-Internal-Token` header — all other callers receive `403 Forbidden`.

---

## Repository Structure

```
.
├── auth-api/               # Service A — Spring Boot (Web, Security, JPA)
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── data-api/               # Service B — Spring Boot (Web)
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── docker-compose.yml
├── init.sql                # (Optional) DB init / Flyway migrations
└── README.md
```

---

## Data Model

### `users`
| Column          | Type    | Notes              |
|-----------------|---------|--------------------|
| `id`            | UUID    | Primary key        |
| `email`         | VARCHAR | Unique, not null   |
| `password_hash` | VARCHAR | BCrypt, not null   |

### `processing_log`
| Column        | Type        | Notes                        |
|---------------|-------------|------------------------------|
| `id`          | UUID        | Primary key                  |
| `user_id`     | UUID        | FK → users.id                |
| `input_text`  | TEXT        | Original request text        |
| `output_text` | TEXT        | Transformed result           |
| `created_at`  | TIMESTAMPTZ | Set automatically on insert  |

---

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) ≥ 24
- [Docker Compose](https://docs.docker.com/compose/) v2 (ships with Docker Desktop)
- Java 17 + Maven 3.9 *(only needed if you want to build locally outside Docker)*

---

## Quick Start

### 1. Clone the repository

```bash
git clone https://github.com/your-username/winwin-backend-test.git
cd winwin-backend-test
```

### 2. (Optional) Build JARs locally before Docker

> Skip this step if your Dockerfiles build inside the container (multi-stage build).

```bash
mvn -f auth-api/pom.xml clean package -DskipTests
mvn -f data-api/pom.xml clean package -DskipTests
```

### 3. Start all services

```bash
docker compose up -d --build
```

Services will be available at:

| Service  | URL                      |
|----------|--------------------------|
| auth-api | http://localhost:8080    |
| data-api | http://localhost:8081    |

> **First start:** Postgres initialises the schema automatically via `init.sql` (or Flyway). Wait ~10 seconds for the database to be ready before sending requests.

### 4. Stop all services

```bash
docker compose down
```

To also remove the Postgres volume (wipes all data):

```bash
docker compose down -v
```

---

## Environment Variables

All secrets are injected via environment variables defined in `docker-compose.yml`. Override them by creating a `.env` file in the project root:

```env
# Postgres
POSTGRES_DB=winwin
POSTGRES_USER=winwin
POSTGRES_PASSWORD=secret

# auth-api
POSTGRES_URL=jdbc:postgresql://postgres:5432/winwin
JWT_SECRET=change-me-to-a-long-random-string
INTERNAL_TOKEN=super-secret-internal-token

# data-api
INTERNAL_TOKEN=super-secret-internal-token
```

> **Security note:** Never commit real secrets. The values above are placeholders for local development only.

---

## API Reference & Testing

All examples use `curl`. You can also import the collection into Postman / Insomnia.

### Register

```bash
curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"strongpass"}'
```

Expected response — `201 Created`:

```json
{ "message": "User registered successfully" }
```

---

### Login

```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"strongpass"}'
```

Expected response — `200 OK`:

```json
{ "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." }
```

Save the token:

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"strongpass"}' \
  | jq -r '.token')
```

---

### Process text (protected endpoint)

```bash
curl -s -X POST http://localhost:8080/api/process \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"text":"hello world"}'
```

Expected response — `200 OK`:

```json
{ "result": "DLROW OLLEH" }
```

What happens internally:
1. `auth-api` validates the JWT and extracts the user.
2. `auth-api` calls `data-api` at `http://data-api:8081/api/transform` with `X-Internal-Token`.
3. `data-api` transforms the text (reverses + uppercases).
4. `auth-api` saves a row to `processing_log` and returns the result.

---

### Verify the processing log (direct DB check)

```bash
docker compose exec postgres psql -U winwin -d winwin \
  -c "SELECT id, user_id, input_text, output_text, created_at FROM processing_log ORDER BY created_at DESC LIMIT 5;"
```

---

### Service B rejects unauthorised callers

```bash
# Missing token → 403
curl -s -o /dev/null -w "%{http_code}" \
  -X POST http://localhost:8081/api/transform \
  -H "Content-Type: application/json" \
  -d '{"text":"hello"}'
# Output: 403

# Wrong token → 403
curl -s -o /dev/null -w "%{http_code}" \
  -X POST http://localhost:8081/api/transform \
  -H "Content-Type: application/json" \
  -H "X-Internal-Token: wrong-token" \
  -d '{"text":"hello"}'
# Output: 403
```

---

## Security Highlights

| Concern              | Approach                                               |
|----------------------|--------------------------------------------------------|
| Password storage     | BCrypt with cost factor 12 — never stored in plaintext |
| Authentication       | Stateless JWT (HS256), short expiry (1 hour default)  |
| Inter-service auth   | Shared secret via `X-Internal-Token` header from env  |
| Secrets management   | All secrets via env vars — nothing hardcoded in source |
| No secret logging    | Tokens and passwords are never written to logs        |

---

## Design Decisions

- **Stateless JWT** keeps `auth-api` horizontally scalable with no session store.
- **`X-Internal-Token` header** is a lightweight guard for internal traffic; the header value comes from an env var so it is never committed to source control.
- **UUIDs as primary keys** avoid enumerable IDs and work naturally across distributed services.
- **Single `docker-compose.yml`** keeps the dev setup to one command while clearly separating service responsibilities.
- **Flyway / `init.sql`** handles schema migration so the app never relies on Hibernate `ddl-auto=create` in a reproducible environment.

---

## Troubleshooting

| Symptom | Fix |
|---|---|
| `Connection refused` on startup | Postgres may not be ready yet — wait 10 s and retry, or check `docker compose logs postgres` |
| `401 Unauthorized` on `/api/process` | JWT is missing or expired — re-login and use the fresh token |
| `403 Forbidden` from `data-api` | `INTERNAL_TOKEN` mismatch between services — verify `.env` values match |
| Port conflict on 8080/8081 | Change the host-side port in `docker-compose.yml` (e.g. `"9080:8080"`) |
| `mvn: command not found` | Install JDK 17 + Maven, or use the multi-stage Docker build to skip local Maven |