# Voyage-AI — Implementation Plan

**Version:** 0.2.1 · **Status:** Implementation-Verified

## Guiding rule

Build a working end-to-end vertical slice early. Ground every milestone in working source code, deterministic automated test suites, and empirical host verification.

## Phase 0 — Repository and setup (COMPLETED & VERIFIED)
- [x] Create a clean `voyage-ai` repository structure.
- [x] Add docs, frontend, backend, and infra directories.
- [x] Choose JDK/Spring Boot/Node versions compatible with dependencies (Java 17 LTS, Spring Boot 4.1.x, Node 24).
- [x] Add `.gitignore`, `.env.example`, formatter/linter, and README.
- [x] Establish a basic CI/local verification script.
- **Exit gate:** PASSED — Frontend and backend both build cleanly; health endpoints (`/api/v1/health`, `/actuator/health`) respond with HTTP 200.

## Phase 1 — Design foundation (COMPLETED & VERIFIED)
- [x] Read initialized design-skill instructions (`emil-design-eng`).
- [x] Establish product visual direction and tokens (editorial warm modernism, tactile feedback).
- [x] Build app shell, responsive navigation, buttons, form controls, cards, loading/empty/error states.
- [x] Create static/seed-data discovery pages with 5-tab detail modals across authentic destinations (Jaipur, Varanasi, Munnar, Hampi).
- **Exit gate:** PASSED — Responsive discovery prototype with coherent design system; verified via `npm run test:unit` (11/11 passed).

## Phase 2 — Backend, persistence & security foundation (COMPLETED & VERIFIED)
- [x] Configure Spring Boot 4.1.1, validation, exception mapping, security, JPA/Hibernate 7.4.5, MySQL 8.0, and Flyway.
- [x] Standardize on single canonical database **`voyage`** on Docker port 3307 (preserving host port 3306 untouched; legacy `voyage_ai` inactive backup).
- [x] Create User, Destination, TravelListing, Trip, ItineraryDay, and ItineraryItem JPA models and repositories.
- [x] Author Flyway migrations `V0` through `V4` with mandatory provenance columns, user profiles, composite indexes, roles, and media licensing.
- [x] Implement stateless JWT Bearer authentication (`/api/v1/auth/register`, `/login`, `/logout`, `/me`) and BCrypt password hashing.
- [x] Implement user profile persistence (`/api/v1/users/me`) and role authorization (`ROLE_USER`, `ROLE_ADMIN`).
- [x] Implement server-side catalog pagination (`page`, `size=12`), search, and region/style/budget filter predicates.
- [x] Implement real Wikimedia destination ingestion pipeline (`POST /api/v1/admin/destinations/import` guarded by `ROLE_ADMIN`) with MediaWiki Action API Creative Commons licensing (`CC-BY-SA 4.0`, `CC0`, `Public Domain`) and photographer credits.
- [x] Implement discovery and user-scoped trip CRUD APIs with multi-tenant ownership isolation (`403 Forbidden` on cross-user access).
- [x] Provide Windows single-command development orchestrator [`start.bat`](file:///g:/Projects/JFS/Voyage-AI/start.bat) ensuring non-destructive service reuse.
- **Exit gate:** PASSED — 83 backend tests passing (`mvn test`), including `DatabaseFlywayIntegrationTest`, `PersistenceIntegrationTest`, `AuthApiTest`, `AdminDestinationControllerTest`, `DestinationIngestionServiceTest`, and `TripApiTest`.

## Phase 3 — First complete vertical slice (COMPLETED & VERIFIED)
- [x] Frontend can browse destinations with live category, region, budget, and search filters backed by server pagination.
- [x] User can register, authenticate via JWT, create a trip, add listings/custom activities, save, and reload.
- [x] Enforce server-side ownership (cross-user access returns HTTP 403 Forbidden; unauthenticated returns 401 Unauthorized).
- [x] Add automated integration and frontend tests.
- **Exit gate:** PASSED — Complete planning and persistence flow verified via `PersistenceTripApiTest.java`.

## Phase 4 — Itinerary and budget (COMPLETED & VERIFIED)
- [x] Add day management (auto-generating days matching trip date range).
- [x] Add item scheduling, activity categories, suggested times, durations, and estimated costs.
- [x] Add cost aggregation and transparent currency formatting.
- [x] Add date range validation (`startDate` <= `endDate`, returning 400 Bad Request on inverted ranges).
- **Exit gate:** PASSED — Saved itineraries, scheduling items, and budget summaries verified in `PersistenceTripApiTest.java` and `TripService.java`.

## Phase 5 — AI & Meteorological integrations (COMPLETED & VERIFIED)
- [x] Integrate Google Gemini Developer API using the official `gemini-3.5-flash-lite` model.
- [x] Implement resilient adapter (`GoogleGeminiService`) with structured error mapping (`GEMINI_CONFIG_MISSING`, `GEMINI_TIMEOUT`, etc.).
- [x] Enforce Zero User PII transmission and Grounded RAG entity-constrained prompt design.
- [x] Provide dedicated health probe at `/api/v1/ai/health`.
- [x] Integrate AI Travel Concierge component in frontend UI with live generation and loading state.
- [x] Integrate Open-Meteo meteorological REST API with in-memory caching and CC-BY 4.0 provenance DTOs.
- **Exit gate:** PASSED — Real Gemini API generation verified via direct API (`AiApiTest`) and end-to-end browser automation (`AiAssistantBrowserTest`) returning "New Delhi" for "What's capital of India?". Real Open-Meteo verified live for New Delhi (`29.3°C`, `Clear sky`).

## Phase 6 — Polish and comprehensive verification (COMPLETED & VERIFIED)
- [x] Complete mobile/responsive behavior and interaction states across Desktop (1280x800) and Mobile (390x844).
- [x] Run lint, type checks, unit/integration tests across all layers (128/128 tests passing, 100% pass rate).
- [x] Verify multi-tenant access control and database isolation safety guards.
- [x] Author comprehensive verification report (`POST_IMPLEMENTATION_VERIFICATION.md`).
- **Exit gate:** PASSED — 100% test pass rate across backend (83), frontend (11), and Playwright (34) test suites.

## Phase 7 — Deployment and demonstration (NEXT PHASE / ROADMAP)
- [ ] Select a hosting setup based on cost and available accounts (Render, Railway, AWS, or Docker host).
- [ ] Configure environment variables and remote database persistence.
- [ ] Deploy only after secrets, CORS, HTTPS, and migrations are checked.
- [ ] Prepare a short demo scenario and known-limitations section.

## Agent execution rules
- Inspect current files before editing; do not overwrite unrelated work.
- Implement in small, buildable milestones.
- Do not mark features complete without tests or manual verification evidence.
- Never hardcode API secrets or fabricate external data.
- Prefer free-tier options and ask before adding paid dependencies.
- Run relevant checks after each milestone and summarize changed files, tests, and remaining issues.
