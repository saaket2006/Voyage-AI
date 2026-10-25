# Voyage-AI — High-Level System Architecture

## 1. Overview & Major Components

Voyage-AI is an AI-powered travel companion connecting destination discovery, accommodation/restaurant/attraction exploration, and personalized itinerary planning. The application uses a decoupled client-server architecture structured as a **modular monolith**:

- **Frontend Client (`frontend/`):** A single-page application built with React, TypeScript, and Vite, utilizing Tailwind CSS for styling. It manages user presentation, client-side routing, discovery browsing, trip manipulation, and interaction feedback.
- **Backend Service (`backend/`):** A Spring Boot REST application (Java 17, Spring Boot 4.1.x) organized into modular domain boundaries:
  - `auth` & `security`: Authentication, authorization, and ownership enforcement.
  - `destination` & `listing`: Public discovery catalog, destination details, listings, and data provenance.
  - `search`: Global real-time autocomplete suggestions service (`/api/v1/search/suggestions`), ordering matching results strictly into Cities -> Attractions -> Dining groups.
  - `trip` & `itinerary`: Private trip setup, day-by-day scheduling, and budget calculations.
  - `integration`: Isolated provider adapters for external travel data, weather, routing, and AI models.

```text
+-------------------------------------------------------------+
|                      React Web Client                       |
|                   (TypeScript, Vite, SPA)                   |
+-------------------------------------------------------------+
                              |
                              | HTTP(S) JSON /api/v1 (with Provenance DTOs)
                              v
+-------------------------------------------------------------+
|               Spring Boot Modular Monolith                  |
|                                                             |
|  [Security & Auth]  <-->  [Trip & Itinerary Management]      |
|                                     |                       |
|  [Destination & Listing Catalog]   |                       |
|         (Provenance Tracking)       v                       |
|                        [Integration Adapters Boundary]      |
+-------------------------------------------------------------+
          |                                  |
          v                                  v
+--------------------+        +--------------------------------+
|   MySQL Database   |        | External Authorized Providers  |
| (Relational Store) |        | - OpenTripMap / OSM (Heritage) |
| - Verified Seeds   |        | - Open-Meteo (Live Weather)    |
| - Provenance Cols  |        | - OSRM (Transit Distance)      |
| - User Trips/Auth  |        | - Wikimedia (Verified Media)   |
+--------------------+        | - Grounded LLM (Gemini / Groq) |
                              +--------------------------------+
```

## 2. Communication and Data Flows

1. **Client-to-Server Flow:** All client operations communicate with the backend via HTTP(S) RESTful JSON endpoints rooted at `/api/v1`. Factual travel responses include provenance metadata (`sourceProvider`, `providerRecordId`, `retrievedAt`, `staleAfter`, `attribution`, `verificationStatus`).
2. **Discovery & Catalog Flow:** Public visitors and authenticated users query destinations, attractions, and venues. Discovery search is serviced via indexed local relational database queries (or self-hosted extracts); public Nominatim is never used for search autocomplete or bulk extraction. If a provider fails or a factual field (e.g. entry fee, live room price) is unverified or missing, the system emits an explicit `null` or unavailable state—never a fabricated number.
3. **Trip & Itinerary Flow:** Authenticated users create and modify private trips and day schedules. Mutations are processed within transactional service boundaries and persisted atomically.
4. **Grounded AI Generation Flow (RAG):**
   - Client requests itinerary generation for a trip.
   - Backend retrieves verified candidate POIs and venues from the local database / active adapters.
   - Backend constructs an entity-constrained prompt injecting only retrieved verified records into the LLM adapter (Gemini/Groq or local fallback).
   - The LLM is restricted to sequencing and annotating only supplied verified entities; it is forbidden from hallucinating attractions, prices, or operating hours.
   - The backend validates all generated item IDs against verified records before persisting or returning the proposal as an unconfirmed draft.

## 3. Primary Data Stores & Persistence

- **Primary Relational Database:** **MySQL 8.0** with single canonical schema **`voyage`** (the legacy `voyage_ai` schema is deprecated, retained strictly as a non-active historical recovery backup, and never contacted by runtime services or migrations).
- **Data Access:** Spring Data JPA with Hibernate ORM (`ddl-auto: validate`).
- **Schema Management:** Versioned migrations using Flyway (`db/migration/V*`) applied exclusively against `voyage`:
  - `V0__baseline.sql`: Baseline schema definition.
  - `V1__init_schema.sql`: Core relational tables (`users`, `destinations`, `travel_listings`, `trips`, `itinerary_days`, `itinerary_items`).
  - `V2__seed_travel_data.sql`: Curated authentic seed records for heritage destinations.
  - `V3__user_profile_and_destination_indexes.sql`: Extended user profile persistence (`auth_provider`, `provider_subject`, `profile_image_url`) and performance indexes (`idx_users_provider`, `idx_destinations_provider`).
  - `V4__add_user_roles_and_image_licensing.sql`: User role authorization (`role` with `ROLE_USER` default and `ROLE_ADMIN`), destination image licensing (`image_attribution_text`, `image_license_type`, `image_license_url`, `image_source_url`), and destination curation status (`curation_status`).
  - `V6__seed_delhi_listings.sql`: Curated verified listings for Delhi from Archaeological Survey of India (Red Fort, Qutub Minar) and OpenStreetMap (Karim's).
  - `V7__seed_default_traveler.sql`: Seeds default authenticated traveler account (`traveler@voyageai.local`) with empty JSON preferences (`{"savedDestinationIds":[]}`) for automated workflows and multi-city trip planning.
- **Data Authenticity & Provenance:** Every factual travel entity table (`destinations`, `attractions`, `accommodations`, `dining_places`) enforces non-nullable provenance columns: `source_provider`, `provider_record_id`, `retrieved_at`, `stale_after`, `license_type`, `attribution_text`, and `verification_status` (defaulting to `'UNVERIFIED'`, never defaulting to verified). Volatile factual fields (entry fees, nightly rates, operational hours) use structured field-level verification envelopes. Image media items carry distinct attribution text, specific Creative Commons / Public Domain license types, license URLs, and source URLs.
- **Test Fixture Isolation:** Test fixtures are physically segregated in `src/test/` directories and never bundled, served, or fallen back to in production runtime flows.
- **Core Entities:** `User`, `Destination`, `TravelListing`, `Trip`, `ItineraryDay`, `ItineraryItem`.

## 4. Authentication & Authorization Boundaries

- **Public Zone:** Destination catalog with server-side pagination, travel listings, global autocomplete search, user registration, and sign-in endpoints (`/api/v1/auth/**`, `/api/v1/destinations/**`, `/api/v1/listings/**`, `/api/v1/search/**`, `/api/v1/weather/**`, `/api/v1/ai/**`).
- **Protected User Zone:** User profile management (`GET /api/v1/users/me`, `POST /api/v1/users/me/sync`, `PATCH /api/v1/users/me/profile`, `PUT /api/v1/users/me/preferences`), wishlist management (`GET /api/v1/users/me/wishlist`, `POST /api/v1/users/me/wishlist/{destinationId}`, `DELETE /api/v1/users/me/wishlist/{destinationId}`), trip creation and scoped trips (`/api/v1/users/me/trips/**`, `/api/v1/trips/**`).
- **Protected Administrator Zone:** Administrative operations (`POST /api/v1/admin/destinations/import`) secured via server-enforced `ROLE_ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`). Unauthenticated requests yield 401 Unauthorized; ordinary authenticated users (`ROLE_USER`) yield 403 Forbidden.
- **Administrative Role Provisioning:** Administrator privileges cannot be granted by client payloads, registration, profile updates, or preferences. Roles are assigned exclusively by server-controlled configuration (`app.security.admin-emails`) upon user creation or synchronization, and encoded into signed JWT claims.
- **Authorization Enforcement:** Server-side ownership verification ensures users can only read, update, or delete trips they own (returning 403 Forbidden on cross-user access attempts).
- **Security Boundary:** Stateless JWT Bearer tokens validated via Spring Security filter chains. All third-party provider keys reside exclusively in backend environment variables and are never leaked to frontend code, logs, or responses.

## 5. Major External Dependencies & Integrations

- **Integration Adapter Architecture (`com.voyageai.integration`):** Decouples product domain services from specific third-party APIs via common Java interfaces with bounded timeouts (3s connect, 5s read) and circuit breaking.
- **Interactive Map Rendering:** Leaflet.js with standard OpenStreetMap tile layer, adhering strictly to OSM Tile Usage Policies (visible attribution, HTTP cache headers respected, bulk downloading prohibited, no commercial SLA).
- **Destinations & Heritage Data:** Attributable OpenStreetMap extracts and official Archaeological Survey of India (ASI) Gazette records stored in MySQL under ODbL; OpenTripMap designated as an optional candidate only pending formal terms confirmation.
- **Live Destination Ingestion Pipeline (`com.voyageai.destination.ingestion`):** Automated real-data ingestion pipeline leveraging the official Wikipedia REST API (`https://en.wikipedia.org/api/rest_v1/page/summary/{title}`) for encyclopedic text and summaries under CC-BY-SA 4.0, and the Wikimedia Commons imageinfo API (`https://commons.wikimedia.org/w/api.php`) for verified photographer attribution, specific license types (CC-BY-SA 4.0, CC-BY 2.0, CC0, Public Domain), and license documentation URLs. Ingests authentic geographical coordinates (validated within [-90, +90] latitude and [-180, +180] longitude), authentic Wikibase identifiers (`wikibase_item`), and original photography. Deduplication logic enforces uniqueness by provider record ID and slug identifiers to eliminate duplicate entries. Uncurated ingested cities are flagged with `curationStatus = 'COMMUNITY_INGESTED'`, `verificationStatus = 'UNVERIFIED_SOURCE'`, and `recommendedDurationDays = 0`, eliminating synthetic travel plans. Exposed operationally via `POST /api/v1/admin/destinations/import` (requiring `ROLE_ADMIN`) and an optional startup `CommandLineRunner` (`app.destinations.import.enabled`). Truthful degradation ensures missing or malformed entities return structured error entries without synthetic fabrication.
- **Weather & Climate:** Open-Meteo free non-commercial API (up to 10,000 calls/day) with 60-minute in-memory caching, strictly labeled as numerical model forecasts.
- **Transit & Routing:** Abstract `RoutingProvider` interface with Open Source Routing Machine (OSRM) public demo server used only for controlled development evaluation, falling back to haversine direct distance.
- **Verified Media:** Wikimedia Commons REST API (public domain and Creative Commons photography with per-image author, license, and page attribution).
- **AI / LLM Reasoning (`com.voyageai.integration.ai`):** Google Gemini Developer API using the official `gemini-3.5-flash-lite` model. The service is encapsulated in `GoogleGeminiService` using Spring's `RestClient` with externalized configuration (`GEMINI_API_KEY`, `GEMINI_MODEL=gemini-3.5-flash-lite`), configurable connection (5s) and read (15s) timeouts, and structured exception handling (`GEMINI_CONFIG_MISSING`, `GEMINI_AUTHENTICATION_FAILED`, `GEMINI_RATE_LIMITED`, `GEMINI_TIMEOUT`, `GEMINI_UPSTREAM_ERROR`, `GEMINI_EMPTY_RESPONSE`). Exposed via `POST /api/v1/ai/generate` and separate live health connectivity probe `GET /api/v1/ai/health`. Live API verification is encapsulated in `GeminiApiIntegrationTest` (tagged `@Tag("live-api")`), separated from deterministic mocked unit tests.
- **Zero-Fabrication Degradation Rule:** When an external provider (Gemini or Open-Meteo) is unreachable, unconfigured, or quota-exhausted, the adapter returns an explicit unavailable state or structured error code without synthetic or mock fallback data.

## 6. Deployment Boundaries

- **Development:** Frontend served via Vite dev server (`http://localhost:5173`); backend served via Spring Boot in Docker (`http://localhost:8081`, avoiding host port 8080 collision with AgentService); database hosted via Docker MySQL container on host port `3307` targeting canonical schema `voyage`.
- **Containerization:** Docker Compose manages multi-container local orchestration (`infra/docker-compose.yml`):
  - `mysql` container (`voyage_ai_mysql`) exposing internal port `3306` mapped to host port `3307` with canonical database `voyage`.
  - `backend` container (`voyage_ai_backend`) running the compiled Spring Boot application connected to `jdbc:mysql://mysql:3306/voyage` (exposed on host port `8081`).
  - Host development tools (Spring Boot CLI, MySQL Workbench) connect to the container database via `127.0.0.1:3307/voyage`.
  - Host-side MySQL service on port `3306` remains completely isolated and untouched.

## 7. Automated Testing & Verification Architecture

- **Backend Unit & Controller Tier:** Spring Boot MockMvc / Mockito suites (`src/test/`) verify endpoints, input validation, and upstream error mapping using mocked HTTP boundaries (no live network or Gemini/Open-Meteo external calls required).
- **Database Integration Tier:** Dedicated Flyway integration tests run against an isolated in-memory test database schema (`jdbc:h2:mem:voyage_test`). Tests strictly assert that the production database (`voyage`), legacy schema (`voyage_ai`), and host developer databases (`pat`) are never contacted or mutated.
- **Frontend Unit Tier:** Vitest with React Testing Library verifies component rendering, accessibility, and fallback states.
- **End-to-End Browser Tier:** Playwright runs multi-viewport browser tests (Desktop & Mobile Chrome) against local development servers (`http://localhost:5173`, `http://localhost:8081`). Upstream external API routes are intercepted deterministically with labelled test fixtures; real provider integration is isolated to separately invoked `@smoke` tests.


