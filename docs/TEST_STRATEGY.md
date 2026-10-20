# Voyage-AI — Automated Testing Strategy & Verification Guide

## 1. Overview & Test Architecture

Voyage-AI employs a multi-tiered, deterministic, offline-first automated testing pyramid. All routine tests execute locally without requiring internet connectivity, third-party credentials, or paid cloud services.

```
       ▲
      / \        Layer 3: Playwright Browser & E2E Tests
     /   \       (Deterministic UI journeys + @smoke live test)
    /-----\
   /       \     Layer 2: API & Database Integration Tests
  /         \    (Spring Boot MockMvc, Bounded MockRestServiceServer, Flyway on H2)
 /-----------\
/             \  Layer 1: Unit & Component Tests
---------------  (Vitest + RTL for React, JUnit 5 + Mockito for Java)
```

---

## 2. Test Suites Summary

| Layer | Framework | Scope / Coverage | Execution Target | Network Dependency |
| :--- | :--- | :--- | :--- | :--- |
| **Backend Unit & Controller** | JUnit 5, Mockito, Spring Boot MockMvc | `HealthController`, `WeatherController`, `OpenMeteoWeatherService`, `WmoWeatherCode`, DTOs | JVM In-Memory | **None** (MockRestServiceServer / Mockito) |
| **Database Integration** | Flyway, Spring Boot Test, H2 (MySQL Mode) | `V0__baseline.sql` migration, `flyway_schema_history` verification, isolated connectivity | Isolated in-memory H2 | **None** (Strictly isolated from host/Docker DBs) |
| **Frontend Unit & Component** | Vitest, React Testing Library, jsdom | `App.tsx`, `ImageWithFallback.tsx`, common components | Node.js jsdom | **None** |
| **Browser UI Journeys** | Java Playwright + JUnit 5 | Navigation, filtering, search, modal focus/escape, responsive viewports | Local Vite dev server (`:5173`) | **None** (Network route interception) |
| **End-to-End Integration** | Java Playwright + JUnit 5 | Weather deterministic mocking & error handling | Local Vite dev server (`:5173`) | **None** (Network route interception) |
| **Real Integration Smoke** | Java Playwright & HttpClient (`@smoke`) | End-to-end live flow: `/api/v1/health` and `/api/v1/weather` for Jaipur | Local backend (`:8081`) | Requires local backend & Open-Meteo |

---

## 3. Database Safety Mandate

1. **Strict Test Isolation:** Database integration tests ([DatabaseFlywayIntegrationTest.java](file:///g:/Projects/JFS/Voyage-AI/backend/src/test/java/com/voyageai/integration/db/DatabaseFlywayIntegrationTest.java)) execute against a transient in-memory H2 database (`jdbc:h2:mem:voyage_flyway_test;MODE=MySQL`).
2. **Zero Host Interference:** Automated tests **NEVER** point to or modify the host MySQL instance (`localhost:3306`), the Docker MySQL container (`3307`), the developer's `voyage` database, or the existing `pat` database.
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

### Java Playwright Browser & E2E Tests (Deterministic Mocked Mode)
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
2. **Backend REST API:** Running on `http://localhost:8081` (via Docker Compose `infra/docker-compose.yml` or host Maven). Required only for `@smoke` tests and live manual verification; routine unit/e2e tests are fully mocked.
3. **Java & Node Environment:** Java 17+, Node.js v20+.
