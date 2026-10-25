# Voyage-AI — Technical Risk Register & Prioritized Engineering Roadmap

**Document Reference:** `docs/reports/risks-and-roadmap.pdf` / `docs/reports/risks-and-roadmap.md`  
**System Version:** 0.2.1 (Phase 2 Verified Baseline)  
**Author:** Senior Software Architect & Risk Analyst  
**Audit Date:** October 25, 2026  
**Status:** Implementation-Verified  

---

## 1. Technical Risk Register

The following risk register categorizes confirmed technical risks, architectural bottlenecks, and operational constraints based on empirical evidence gathered from the codebase and host runtime inspection:

| Risk ID | Risk / Issue Description | Supporting Evidence | Likelihood | Impact | Status | Mitigation Implemented | Dependencies | Verification Evidence |
|---|---|---|---|---|---|---|---|---|
| **RSK-01** | **Host Port Collision on Port 3306** | Windows service `MySQL80` bound to `0.0.0.0:3306` | High | High | **RESOLVED** | Enforced `3307:3306` port mapping in Docker Compose targeting canonical DB `voyage`; host DB untouched | Docker Compose config | `DatabaseFlywayIntegrationTest` verifies 0 connection to port 3306 |
| **RSK-02** | **External API Rate Limit (Open-Meteo)** | Relying on public non-commercial endpoint (10k/day) | Medium | Medium | **MONITORED** | In-memory coordinate caching and zero-fabrication fallback | Backend cache | `WeatherApiTest` (3/3 passed) |
| **RSK-03** | **Client Mock Data Divergence & Truncation** | Previously static `mockDestinations.ts` and 100-item ceiling | High | High | **RESOLVED** | Server-side pagination (`page`, `size=12`), search filters, and real Wikimedia ingestion | `DestinationController` | `DestinationsDiscoveryTest` (5/5 passed) |
| **RSK-04** | **Unauthenticated / Cross-User Access** | Private trips exposed without strict user boundaries | High | High | **RESOLVED** | Stateless JWT Bearer filter chain, `UserPrincipal`, and ownership check (`findByUserId`) | Security boundary | `PersistenceTripApiTest` verifies 401 unauth and 403 cross-user |
| **RSK-05** | **AI Free-Tier Human Review & PII Leakage** | Gemini free tier policy permits human review | Low | High | **RESOLVED** | Zero user PII transmitted in prompts; prompts constrained to internal POI metadata | AI Adapter | `AiControllerTest` & `AiApiTest` verify prompt isolation |
| **RSK-06** | **Administrative Ingestion Endpoint Exposure** | Ingestion endpoints open to non-admin users | High | High | **RESOLVED** | Enforced `@PreAuthorize("hasRole('ADMIN')")` with server-controlled admin email whitelist | Spring Security | `AdminDestinationControllerTest` (3/3 passed) |
| **RSK-07** | **Wikimedia Commons Media Licensing Deficit** | Generic attribution violating per-image CC terms | High | High | **RESOLVED** | MediaWiki Action API queries extracting photographer credits and explicit CC licenses | Ingestion engine | `DestinationIngestionServiceTest` (13/13 passed) |
| **RSK-08** | **OSRM Public Routing Server Instability** | Public demo server has no commercial SLA | High | Medium | **PLANNED** | Encapsulated behind `RoutingProvider` interface with Haversine direct distance fallback | Routing adapter | Phase 4 roadmap |
| **RSK-09** | **OpenStreetMap Tile Usage Policy** | Leaflet client tile requests must adhere to OSM policy | Medium | Low | **PLANNED** | Cache headers and visible attribution configured; CARTO / self-hosted evaluated | Frontend map | Phase 3 roadmap |

---

## 2. Prioritized Engineering Roadmap

```
+-----------------------------------------------------------------------------------------+
|                                PRIORITIZED ROADMAP MATRIX                               |
+-----------------------------------------------------------------------------------------+
| Priority Tier | Focus Area                               | Key Deliverables             |
|---------------|------------------------------------------|------------------------------|
| **P0 (Critical)**| Security & Persistence Foundation     | Flyway V0-V4, MySQL voyage,  |
|               | (COMPLETED & VERIFIED)                   | JWT auth, roles, profiles    |
| **P1 (High)** | Core Vertical Slice & Real Ingestion     | Paginated destination search,|
|               | (COMPLETED & VERIFIED)                   | Wikipedia/Wikimedia pipeline |
| **P2 (Medium)**| Itinerary, AI & Local Orchestration     | User trip CRUD, Gemini chat, |
|               | (COMPLETED & VERIFIED)                   | start.bat local orchestrator |
| **P3 (Roadmap)**| Spatial Visualization & Cloud Hosting  | Interactive Leaflet maps,    |
|               | (PHASES 3, 4, 7 TARGETS)                 | OSRM routing matrix, CI/CD   |
+-----------------------------------------------------------------------------------------+
```

---

## 3. Engineering Implementation Status & Completed Milestones

### 3.1 Task 1 (P0): Relational Schema & Persistence Foundation — **COMPLETED & VERIFIED**
- **Implemented:** Flyway migrations `V0` through `V4` applied to canonical database `voyage` on Docker port 3307. JPA entities (`User`, `Destination`, `TravelListing`, `Trip`, `TripDay`, `ItineraryItem`) with mandatory provenance columns.
- **Verification Evidence:** `DatabaseFlywayIntegrationTest.java` (4/4 passed) and `PersistenceIntegrationTest.java` (3/3 passed).

### 3.2 Task 2 (P0): Stateless JWT Security & Role Authorization — **COMPLETED & VERIFIED**
- **Implemented:** Stateless JWT Bearer authentication, user profile management (`/api/v1/users/me`), BCrypt hashing, roles (`ROLE_USER`, `ROLE_ADMIN`), and administrative authorization (`@PreAuthorize("hasRole('ADMIN')")`).
- **Verification Evidence:** `AuthApiTest.java` (10/10 passed), `AdminDestinationControllerTest.java` (3/3 passed), `UserProfileApiTest.java` (2/2 passed).

### 3.3 Task 3 (P1): Full-Catalog Server Pagination & Search — **COMPLETED & VERIFIED**
- **Implemented:** `DestinationController` with server-side pagination (`page`, `size=12`), region, style, and budget filter predicates, and pagination headers (`X-Total-Count`, `X-Total-Pages`).
- **Verification Evidence:** `DestinationApiTest.java` (9/9 passed) and `DestinationsDiscoveryTest.java` (5/5 passed).

### 3.4 Task 4 (P1): Real Wikimedia Ingestion Pipeline — **COMPLETED & VERIFIED**
- **Implemented:** `DestinationIngestionService` extracting real Wikipedia summaries and MediaWiki Action API photographer credits and CC licenses (`CC-BY-SA 4.0`, `CC0`, `Public Domain`), rendered on cards and modals.
- **Verification Evidence:** `DestinationIngestionServiceTest.java` (13/13 passed) and `DestinationIngestionApiTest.java` (1/1 passed).

### 3.5 Task 5 (P2): Google Gemini AI Travel Concierge — **COMPLETED & VERIFIED**
- **Implemented:** `GoogleGeminiService` integrating Google Gemini `gemini-3.5-flash-lite` model with structured error codes, zero user PII transmission, and frontend `AiTravelAssistant` UI.
- **Verification Evidence:** `AiApiTest.java` (3/3 passed) and `AiAssistantBrowserTest.java` (3/3 passed, verified response to "What's capital of India?" in Chromium DOM).

### 3.6 Task 6 (P2): Local Development Orchestrator (`start.bat`) — **COMPLETED & VERIFIED**
- **Implemented:** Windows single-command launcher checking MySQL (3307), Backend (8081), Frontend (5173), waiting for readiness, opening default browser, and safely reusing running services without data resets.
- **Verification Evidence:** Validated via Windows `cmd.exe` execution.

### 3.7 Remaining Roadmap Milestones
- **Task 7 (P3): Interactive Leaflet Maps (Phase 3 Target):** Leaflet.js widget rendering OSM tiles with strict policy compliance and spatial destination plotting.
- **Task 8 (P3): Transit Distance & Routing Matrix (Phase 4 Target):** `RoutingProvider` adapter interface integrating OSRM with automatic Haversine fallback.
- **Task 9 (P3): Production Deployment & Cloud Hosting (Phase 7 Target):** Cloud hosting configuration (Render, Railway, or AWS ECS), SSL/TLS termination, and cloud KMS secrets custody.

---

## 4. Verification Milestones & Quality Gates

- **Milestone 1 (Persistence & Security Gate):** PASSED — Flyway migrations V0-V4 applied cleanly; JPA repositories verified; all `/api/v1/trips/**` endpoints reject unauthenticated requests with HTTP 401.
- **Milestone 2 (Dynamic Vertical Slice Gate):** PASSED — React client renders paginated destinations fetched from `/api/v1/destinations` with real Wikimedia CC licensing.
- **Milestone 3 (AI & Meteorological Gate):** PASSED — Gemini AI adapter and Open-Meteo weather service verified live without mocks.
- **Milestone 4 (Full System Quality Gate):** PASSED — 128 automated tests passing (83 backend, 11 frontend, 34 Playwright) with 100% pass rate. Single canonical database `voyage` enforced on port 3307. Single-command orchestrator `start.bat` verified.

