# Voyage-AI — Automated Testing Strategy & Verification Guide

## 1. Overview & Test Architecture

Voyage-AI employs a multi-tiered, deterministic, offline-first automated testing pyramid. All routine tests execute locally without requiring internet connectivity, third-party credentials, or paid cloud services.

```
       ▲
      / \        Layer 3: Playwright Browser & Black-Box API Tests
     /   \       (Deterministic UI journeys + @smoke live test: 34 tests)
    /-----\
   /       \     Layer 2: API, Security & Database Integration Tests
  /         \    (Spring Boot MockMvc, Bounded MockRestServiceServer, Flyway V0-V4 on H2: 83 tests)
 /-----------\
/             \  Layer 1: Frontend Component Unit Tests
---------------  (Vitest + RTL for React, jsdom: 11 tests)
```

---

## 2. Test Suites Summary & Scope

| Layer | Framework | Scope / Coverage | Execution Target | Count | Network Dependency |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Backend Core, Domain & Ingestion** | JUnit 5, Mockito, Spring Boot MockMvc | `AuthApiTest`, `UserApiTest`, `DestinationApiTest`, `AdminDestinationControllerTest`, `DestinationIngestionServiceTest`, `TripApiTest`, `AiControllerTest`, `GeminiServiceTest`, `WeatherControllerTest`, `OpenMeteoWeatherServiceTest`, `HealthControllerTest` | JVM In-Memory | 76 tests | **None** (MockMvc / MockitoBean / MockRestServiceServer) |
| **Database Integration** | Flyway, Spring Boot Test, H2 (MySQL Mode) | `DatabaseFlywayIntegrationTest` (V0-V4 migrations, DB safety guard), `PersistenceIntegrationTest` (Foreign keys, cascades, timestamps) | Isolated in-memory H2 | 7 tests | **None** (Strictly isolated from host/Docker DBs) |
| **Frontend Unit & Component** | Vitest, React Testing Library, jsdom | `App.test.tsx`, `ImageWithFallback.test.tsx`, `AiTravelAssistant.test.tsx` | Node.js jsdom | 11 tests | **None** |
| **Playwright Browser E2E** | Java Playwright + Chromium | `AiAssistantBrowserTest` (Live Gemini UI generation), `DestinationDetailModalTest`, `DestinationsDiscoveryTest`, `NavigationTest`, `ResponsiveTest`, `WeatherE2ETest` | Local Vite dev server (`:5173`) | 20 tests | Local Vite + Backend |
| **Black-Box API Verification** | Java HttpClient + AssertJ | `PersistenceTripApiTest`, `DestinationIngestionApiTest`, `UserProfileApiTest`, `AiApiTest`, `WeatherApiTest`, `HealthApiTest` | Local backend (`:8081`) | 14 tests | Local backend |
| **Total Test Suite** | **All 3 Modules** | **Comprehensive Full System Verification** | **Local Environment** | **128 tests** | **100% Pass Rate (0 failures, 0 skipped)** |

---

## 3. Database Safety Mandate

1. **Strict Test Isolation:** Database integration tests ([DatabaseFlywayIntegrationTest.java](file:///g:/Projects/JFS/Voyage-AI/backend/src/test/java/com/voyageai/integration/db/DatabaseFlywayIntegrationTest.java)) execute against a transient in-memory H2 database (`jdbc:h2:mem:voyage_flyway_test;MODE=MySQL`).
2. **Zero Host Interference:** Automated tests **NEVER** point to or modify the host MySQL instance (`localhost:3306`), the Docker MySQL container (`3307`), the developer's canonical `voyage` database, or the existing `pat` database.
3. **Explicit Safety Guard Assertions:** [DatabaseFlywayIntegrationTest.java](file:///g:/Projects/JFS/Voyage-AI/backend/src/test/java/com/voyageai/integration/db/DatabaseFlywayIntegrationTest.java#L52-L70) includes programmatic safety assertions that verify the active database URL does not contain `localhost:3306/voyage`, `localhost:3306/pat`, or `:3307`.

---

## 4. Execution Commands

### Full Local Test Suite (All Layers)
```powershell
# Windows PowerShell
.\scripts\run-all-tests.ps1

# POSIX / Bash
./scripts/run-all-tests.sh
```

### Backend Unit, Controller & Database Tests
```powershell
cd backend
.\mvnw.cmd test
```

### Frontend Unit & Component Tests (Vitest)
```powershell
cd frontend
npm run test:unit
```

### Java Playwright Browser & E2E Tests (Deterministic Mode)
```powershell
cd tests
mvn test
```

### Real-Provider Smoke Test (@smoke)
```powershell
# Windows PowerShell
.\scripts\run-smoke-tests.ps1

# Direct Maven command
cd tests
mvn test -Psmoke
```

---

## 5. Prerequisites for Local Testing

1. **Frontend Dev Server:** Running on `http://localhost:5173` (`npm run dev` in `frontend/`). Playwright automatically reuses the existing dev server or spins one up on demand.
2. **Backend REST API:** Running on `http://localhost:8081` (via Docker Compose `infra/docker-compose.yml` or host Maven). Required only for `@smoke` tests and live verification; routine unit tests are fully mocked.
3. **Java & Node Environment:** Java 17+, Node.js v20+.
4. **Local Launcher (`start.bat`):** Run `start.bat` in the repository root to start MySQL (3307), Backend (8081), and Frontend (5173) in dependency order with automated readiness verification.
