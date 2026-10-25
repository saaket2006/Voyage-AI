# Voyage-AI — All-in-One Travel Companion

Voyage-AI is an AI-powered travel discovery and itinerary planning application that unifies destination exploration, accommodation and dining discovery, attraction selection, and day-by-day trip scheduling with transparent budget estimation and verified data provenance.

The project is structured as a decoupled **modular monolith**:
- **Frontend (`frontend/`):** React 18, TypeScript, Vite, Tailwind CSS with custom editorial tokens and Emil Kowalski interaction easing, integrated with persistent backend search.
- **Backend (`backend/`):** Java 17, Spring Boot 4.1.1 REST API, Spring Data JPA, Hibernate, Flyway migrations (V0-V3), user profile persistence, JWT authentication, user-scoped trips CRUD, and automated Wikimedia destination data ingestion pipeline.
- **Centralized Testing (`tests/`):** Java Playwright, JUnit 5, and black-box REST API suites targeting local development servers with 100% test pass rate.
- **Infrastructure (`infra/`):** Docker Compose local orchestration with MySQL 8.0.

---

## 1. Prerequisites & Required Tools

Verify the following tools are installed on your workstation:

| Tool | Minimum Version | Tested / Supported Version | Role |
| :--- | :--- | :--- | :--- |
| **Java JDK** | 17 LTS | Oracle JDK 17.0.17 LTS | Backend compilation and test execution |
| **Apache Maven** | 3.8+ | 3.9.15 (or included `./mvnw`) | Backend & test suite build automation |
| **Node.js** | 18.x / 20.x / 22.x / 24.x | v24.13.1 | Frontend build engine and JavaScript runtime |
| **npm** | 9.x+ | 11.8.0 | Frontend package management |
| **Docker Engine** | 24+ | 29.6.2 (WSL2 engine) | Development MySQL containerization |
| **Docker Compose** | v2.20+ | v5.3.1 (Compose v2 syntax) | Multi-container local orchestration |
| **Git** | 2.x | 2.52.0 | Version control |

---

## 2. Port Allocation & Host Collision Avoidance

To prevent port conflicts with running Windows services on the host machine:
- **MySQL Container:** Exposes host port **`3307`** (mapped internally to `mysql:3306`) with the canonical application database **`voyage`**. External tools (such as MySQL Workbench or local CLI tools) access the Docker database at `127.0.0.1:3307/voyage` (user: `voyage_user`, password: `voyage_dev_password`).
- **Host Native MySQL:** Remains untouched on port `3306` to avoid conflict with local developer services.
- **Spring Boot Backend:** Runs on port **`8081`** (`http://localhost:8081`), avoiding port `8080` (occupied on some environments by `AgentService`). Inside Docker, it connects directly to `jdbc:mysql://mysql:3306/voyage`.
- **Frontend Client:** Served via Vite on port **`5173`** (`http://localhost:5173`).

---

## 3. Quick Start & Local Setup

### One-Click Launch (Windows)
Run `start.bat` in the repository root to automatically verify and launch all dependencies in the correct order (MySQL -> Spring Boot backend -> Vite frontend), wait for readiness checks, open `http://localhost:5173` in your default browser, and display active service URLs:
```cmd
start.bat
```
The script safely reuses any already-running services and never modifies or resets existing database records.

---

### Manual Step-by-Step Setup

#### Step 1: Configure Local Environment
Copy the environment template:
```powershell
# Windows PowerShell:
Copy-Item .env.example .env

# Linux / macOS:
cp .env.example .env
```

### Step 2: Start the MySQL Database
Start the containerized Docker MySQL instance:
```bash
docker compose -f infra/docker-compose.yml up -d mysql
```
Verify the container is healthy:
```bash
docker inspect --format="{{.State.Health.Status}}" voyage_ai_mysql
# Output should be: healthy
```
The container automatically provisions the canonical database **`voyage`** (see `infra/init-db.sql`). For local administration via MySQL Workbench, connect to `127.0.0.1:3307`, user `voyage_user`, password `voyage_dev_password`, default schema `voyage`.

### Step 3: Run the Backend (Spring Boot)
In a terminal window:
```bash
cd backend
./mvnw spring-boot:run
# Or using Maven directly:
mvn spring-boot:run
```
Once started, the backend services are available at:
- 👉 **Health Check:** `http://localhost:8081/api/v1/health`
- 👉 **Actuator Health:** `http://localhost:8081/actuator/health`
- 👉 **Live Weather Forecast:** `http://localhost:8081/api/v1/weather?latitude=26.9124&longitude=75.7873`

### Step 4: Run the Frontend (React + Vite)
In a second terminal window:
```bash
cd frontend
npm install
npm run dev
```
Open your browser at:
- 👉 **Frontend Application:** `http://localhost:5173`

---

## 4. Automated Testing Framework

Voyage-AI features a centralized, deterministic, multi-tiered test framework designed for offline-first development with isolated database guarantees.

### Running All Tests (Unified Runner)
To run backend unit/integration tests, frontend unit tests, and Java Playwright browser/API tests in one command:
```powershell
# Windows PowerShell:
.\scripts\run-all-tests.ps1

# Linux / macOS:
./scripts/run-all-tests.sh
```

### Individual Test Suites

#### Backend Tests (`backend/`)
Uses an isolated in-memory H2 database (`MODE=MySQL`); never touches or modifies host or Docker databases:
```bash
cd backend
mvn test
```

#### Frontend Unit & Component Tests (`frontend/`)
Executes React component tests in jsdom via Vitest:
```bash
cd frontend
npm run test:unit
```

#### Java Playwright Browser & Black-Box API Tests (`tests/`)
Executes multi-viewport browser journeys and REST API tests with mocked network boundaries:
```bash
cd tests
mvn test
```

#### Live Real-Service Smoke Tests (`@smoke`)
Exercises the running local backend (`http://localhost:8081`) and the real Open-Meteo weather integration:
```powershell
# Using the dedicated smoke runner:
.\scripts\run-smoke-tests.ps1

# Or via Maven profile directly:
cd tests
mvn test -Psmoke
```

---

## 5. Development & Build Commands

### Backend (`backend/`)
```bash
# Compile and test
mvn test

# Package runnable Spring Boot executable JAR (skipping tests)
mvn clean package -DskipTests

# Run Flyway migrations manually against configured database
mvn flyway:migrate
```

### Frontend (`frontend/`)
```bash
# Start local development server with hot module replacement (HMR)
npm run dev

# Run TypeScript type-checking
npm run typecheck

# Run ESLint validation
npm run lint

# Run Vitest unit tests
npm run test:unit

# Build production bundle
npm run build

# Preview production build locally
npm run preview
```

### Infrastructure (`infra/`)
```bash
# Start all containerized services (MySQL + Backend)
docker compose -f infra/docker-compose.yml up -d

# Tail container logs
docker compose -f infra/docker-compose.yml logs -f

# Stop containers (preserves database volumes)
docker compose -f infra/docker-compose.yml down

# Stop containers and reset database volumes
docker compose -f infra/docker-compose.yml down -v
```

---

## 6. Project Layout & Directory Structure

```text
voyage-ai/
├── backend/                       # Java 17 Spring Boot 4.1.1 modular monolith
│   ├── src/main/java/             # Domain controllers, configs, services, DTOs
│   │   └── com/voyageai/
│   │       ├── common/            # Shared ApiResponse, error handling, health
│   │       ├── config/            # CORS and security configurations
│   │       └── integration/       # External adapters (Open-Meteo weather)
│   ├── src/main/resources/        # application.yml, application-dev.yml, db/migration/
│   ├── src/test/java/             # MockMvc controller & isolated Flyway tests
│   ├── pom.xml                    # Spring Boot 4.1.1 configuration
│   └── Dockerfile                 # Multi-stage production container build
├── frontend/                      # React 18, TypeScript, Vite SPA
│   ├── src/
│   │   ├── components/            # UI components (discovery, layout, common)
│   │   │   ├── discovery/         # HeroSection, FilterBar, DestinationCard, Modal
│   │   │   ├── layout/            # Navbar with live health ping and search
│   │   │   └── common/            # Button, Badge, SkeletonCard, EmptyState
│   │   ├── data/                  # Mock destination datasets with honest labels
│   │   ├── types/                 # TypeScript entity and provenance definitions
│   │   ├── App.tsx                # Discovery prototype shell
│   │   ├── index.css              # Custom design tokens and micro-interactions
│   │   └── main.tsx               # Application mount point
│   ├── package.json               # Vite, Tailwind CSS 3.4, Vitest, Lucide icons
│   ├── tailwind.config.js         # Curated Voyager Navy & Sunset Amber tokens
│   └── vite.config.ts             # Vite server & Vitest configuration
├── tests/                         # Centralized Automated Testing Workspace
│   ├── config/                    # test.properties with local base URLs
│   ├── database/                  # Database testing isolation documentation
│   ├── fixtures/                  # Reusable mock payloads (mock-weather.json)
│   ├── scripts/                   # Specialized test runner scripts
│   ├── src/test/java/             # Java Playwright browser & black-box API suites
│   │   └── com/voyageai/tests/
│   │       ├── base/              # BasePlaywrightTest with tracing & screenshots
│   │       ├── browser/           # Navigation, Discovery, Modal, Responsive tests
│   │       ├── e2e/               # Deterministic weather E2E & @smoke scenarios
│   │       └── api/               # Black-box /api/v1/health & weather API tests
│   ├── pom.xml                    # Playwright for Java 1.49.0 & JUnit 5 POM
│   └── README.md                  # Centralized testing guide
├── scripts/                       # Root-level unified test execution scripts
│   ├── run-all-tests.ps1          # Unified PowerShell runner (Backend + Frontend + Tests)
│   ├── run-all-tests.sh           # Unified POSIX/Bash runner
│   └── run-smoke-tests.ps1        # Real-provider integration smoke test runner
├── infra/                         # Local orchestration & database bootstrap
│   ├── docker-compose.yml         # MySQL 8.0 (3307:3306) & backend container
│   └── init-db.sql                # Safe idempotency database creation script
├── docs/                          # Specifications, architecture, & audit reports
│   ├── PRD.md / FRD.md / SRS.md / NFRD.md / TDD.md
│   ├── DATA_AUTHENTICITY_AND_PROVIDER_PLAN.md  # Official provider terms & quotas
│   ├── TEST_STRATEGY.md           # Multi-layered test architecture and safety rules
│   ├── architecture/              # Security & JWT specifications
│   ├── audit/                     # Pre-implementation audit reports
│   └── screenshots/               # Verified prototype visual references
├── Architecture.md                # High-level architecture documentation
├── task.md                        # Design context, guidelines & review logs
├── .env.example                   # Environment variable template
└── .gitignore                     # Git ignore rules
```

---

## 7. Data Authenticity & Architecture Principles

1. **Zero-Fabrication Mandate:**
   - Voyage-AI never presents invented or hardcoded mock values as real-world facts.
   - All factual fields (weather, entry fees, operational hours) use verified provider integrations or structured field-level verification envelopes (`UNVERIFIED`, `VERIFIED`, `ESTIMATED`).
   - Illustrative prototype estimates are transparently labeled with visible data provenance notices.
2. **Database Testing Isolation:**
   - Routine database integration tests run exclusively in a transient in-memory H2 schema (`jdbc:h2:mem:voyage_flyway_test;MODE=MySQL`).
   - Safety guard assertions actively verify that tests never execute against development MySQL databases or the developer's existing `pat` database.
3. **Graceful Provider Degradation:**
   - If an upstream provider (such as Open-Meteo or future Gemini AI adapters) is unavailable or encounters a network timeout, the application emits explicit HTTP 503 error payloads rather than hallucinating synthetic fallback values.
