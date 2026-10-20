# Voyage-AI — All-in-One Travel Companion

Voyage-AI is an AI-powered travel discovery and itinerary planning application that unifies destination exploration, accommodation and dining discovery, attraction selection, and day-by-day trip scheduling with transparent budget estimation.

The project is structured as a decoupled **modular monolith**:
- **Frontend (`frontend/`):** React 18, TypeScript, Vite, Tailwind CSS with custom design tokens.
- **Backend (`backend/`):** Java 17, Spring Boot 3.3.4 REST API, Spring Data JPA, Hibernate, Flyway.
- **Infrastructure (`infra/`):** Docker Compose local orchestration with MySQL 8.0.

---

## 1. Prerequisites & Required Tools

Verify the following tools are installed on your workstation:

| Tool | Minimum Version | Tested / Supported Version | Role |
|---|---|---|---|
| **Java JDK** | 17 LTS | Oracle JDK 17.0.17 LTS | Backend compilation and runtime |
| **Apache Maven** | 3.8+ | 3.9.15 (or included `./mvnw`) | Backend build and dependency automation |
| **Node.js** | 18.x / 20.x / 22.x / 24.x | v24.13.1 | Frontend build engine and JavaScript runtime |
| **npm** | 9.x+ | 11.8.0 | Frontend package management |
| **Docker Engine** | 24+ | 29.6.2 (WSL2 engine) | Development MySQL containerization |
| **Docker Compose** | v2.20+ | v5.3.1 (Compose v2 syntax) | Multi-container local orchestration |
| **Git** | 2.x | 2.52.0 | Version control |

---

## 2. Port Allocation & Host Collision Avoidance

To prevent port conflicts with running Windows services on the host machine:
- **MySQL Container:** Exposes host port **`3307`** (mapped internally to `mysql:3306`). This prevents collision with any existing host MySQL service listening on port `3306`.
- **Spring Boot Backend:** Runs on host port **`8081`** (`http://localhost:8081`), avoiding port `8080` (occupied on some environments by `AgentService`).
- **Frontend Client:** Served via Vite on port **`5173`** (`http://localhost:5173`).

---

## 3. Quick Start & Local Setup

### Step 1: Clone and Configure Environment
Copy the environment template:
```bash
# Windows PowerShell:
Copy-Item .env.example .env

# Linux / macOS:
cp .env.example .env
```

### Step 2: Start the MySQL Database Container
```bash
docker compose -f infra/docker-compose.yml up -d mysql
```
Verify the container is healthy:
```bash
docker inspect --format="{{.State.Health.Status}}" voyage_ai_mysql
# Output should be: healthy
```

### Step 3: Run the Backend (Spring Boot)
In a terminal window:
```bash
cd backend
./mvnw spring-boot:run
# Or using Maven directly:
mvn spring-boot:run
```
Once started, the backend health endpoint is available at:
👉 **Health Check:** `http://localhost:8081/api/v1/health`  
👉 **Actuator Health:** `http://localhost:8081/actuator/health`

### Step 4: Run the Frontend (React + Vite)
In a second terminal window:
```bash
cd frontend
npm install
npm run dev
```
Open your browser at:
👉 **Frontend Application:** `http://localhost:5173`

---

## 4. Development & Build Commands

### Backend (`backend/`)
```bash
# Run unit & integration tests (runs isolated using in-memory H2 DB)
mvn test

# Package runnable Spring Boot JAR (skipping tests)
mvn clean package -DskipTests

# Run Flyway migrations manually
mvn flyway:migrate
```

### Frontend (`frontend/`)
```bash
# Start local development server with hot reload
npm run dev

# Run TypeScript type-checking
npm run typecheck

# Run ESLint validation
npm run lint

# Run Vitest test suite
npm run test

# Build production bundle
npm run build

# Preview production build locally
npm run preview
```

### Infrastructure (`infra/`)
```bash
# Start all services (MySQL + Backend) via Docker Compose
docker compose -f infra/docker-compose.yml up -d

# Check service logs
docker compose -f infra/docker-compose.yml logs -f

# Stop and tear down containers (preserves volume data)
docker compose -f infra/docker-compose.yml down

# Stop and reset volume data
docker compose -f infra/docker-compose.yml down -v
```

---

## 5. Architecture & Project Layout

```text
voyage-ai/
├── backend/                  # Java 17 Spring Boot modular monolith
│   ├── src/main/java/        # Controllers, configs, services, repositories
│   ├── src/main/resources/   # application.yml, db/migration/
│   ├── src/test/java/        # Context and integration tests
│   ├── pom.xml               # Spring Boot 3.3.4 parent & dependencies
│   ├── Dockerfile            # Multi-stage production container build
│   ├── mvnw / mvnw.cmd       # Maven Wrapper scripts
├── frontend/                 # React 18, TypeScript, Vite SPA
│   ├── src/
│   │   ├── App.tsx           # Foundation shell & backend health ping
│   │   ├── index.css         # Centralized design tokens & Emil Kowalski easing
│   │   └── main.tsx          # React DOM entry point
│   ├── package.json          # Vite 5, Tailwind CSS 3.4, Vitest
│   ├── tailwind.config.js    # Custom brand & terracotta palettes
│   └── vite.config.ts        # Vite configuration & proxy settings
├── infra/
│   └── docker-compose.yml    # MySQL 8.0 (3307:3306) & backend container
├── docs/                     # Specifications, architecture, & audit reports
│   ├── PRD.md / FRD.md / SRS.md / NFRD.md / TDD.md
│   ├── architecture/         # Security & JWT specifications
│   └── audit/                # Pre-implementation audit reports (v1.0 & v1.1)
├── Architecture.md           # High-level architecture documentation
├── task.md                   # Persistent design context & principles
├── .env.example              # Environment variable template
└── .gitignore                # Multi-language git ignore
```

---

## 6. Approved Architecture Decisions & Deferred Items

### Approved Decisions
- **Persistence:** MySQL 8.0 container mapped to host port `3307` to coexist with host services.
- **Backend Port:** Server port `8081` to avoid host collision with `AgentService`.
- **Database Migrations:** Versioned SQL migrations using Flyway (`db/migration/V*`). Baseline established in `V0__baseline.sql`.
- **Authentication Strategy:** JWT Bearer tokens held strictly in browser memory (see `docs/architecture/JWT_AUTH_DESIGN.md`). Implementation planned for Phase 2.
- **Design Foundations:** Emil Kowalski design engineering principles and Impeccable tokens codified in `task.md` and `frontend/src/index.css`.

### Deferred Items (By Specification)
- **Domain Entities & Tables:** User, Trip, Destination, TravelListing, and Itinerary models will be created in Phase 2.
- **Third-Party Travel APIs:** Initial development unblocked via curated seed data migrations in Phase 2.
- **AI Provider:** Configurable adapter with mock implementation in Phase 5.
- **Production Hosting:** Cloud platform selection deferred to Phase 7.
