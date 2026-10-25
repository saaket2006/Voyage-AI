# Voyage-AI — Open Decisions and Assumptions

**Version:** 0.2.1 · **Status:** Implementation-Verified Decisions Baseline

## Resolved Architecture & Implementation Decisions

| Decision | Final Resolution | Status | Verification Reference |
|---|---|---|---|
| Architecture | React client + Spring Boot modular monolith | **Resolved & Implemented** | `backend/src/main/java/com/voyageai/`, `frontend/src/` |
| Frontend | React 18 + TypeScript + Vite | **Resolved & Implemented** | `frontend/package.json`, 11/11 Vitest tests passed |
| Styling | Tailwind CSS + Emil Kowalski tactile design system | **Resolved & Implemented** | `frontend/src/index.css`, `:active:scale(0.97)` |
| Canonical Persistence | MySQL 8.0 single canonical schema `voyage` (Docker port 3307); host port 3306 untouched; legacy `voyage_ai` inactive backup | **Resolved & Implemented** | `PersistenceIntegrationTest.java`, DB isolation guard verified |
| Schema migrations | Flyway versioned migrations V0-V4 (`V0__baseline`, `V1__init_schema`, `V2__seed_travel_data`, `V3__user_profile_and_destination_indexes`, `V4__add_user_roles_and_image_licensing`) | **Resolved & Implemented** | `DatabaseFlywayIntegrationTest.java` (4/4 passed) |
| Authentication & Roles | Stateless JWT Bearer tokens + BCrypt password hashing + Spring Security 6 filter chain + `ROLE_USER`/`ROLE_ADMIN` | **Resolved & Implemented** | `docs/JWT_AUTH_DESIGN.md`, `AuthApiTest.java` (10/10 passed), `AdminDestinationControllerTest.java` (3/3 passed) |
| User Profile Sync | User profile persistence (`/api/v1/users/me`), bio, avatar, preferences, role protection | **Resolved & Implemented** | `UserProfileApiTest.java`, `UserApiTest.java` |
| Destination Catalog | Authentic destinations & listings with full-catalog server pagination (`page`, `size=12`) | **Resolved & Implemented** | `DestinationController.java`, `DestinationsDiscoveryTest.java` (5/5 passed) |
| Destination Ingestion | Real Wikipedia summary & MediaWiki Action API extraction with CC licensing (`CC-BY-SA 4.0`, `CC0`, `Public Domain`) and photographer credits | **Resolved & Implemented** | `DestinationIngestionServiceTest.java` (13/13 passed), `DestinationIngestionApiTest.java` |
| AI provider | Google Gemini Developer API using `gemini-3.5-flash-lite` / `gemini-2.5-flash` model with Zero-PII | **Resolved & Implemented** | `GoogleGeminiService.java`, `AiAssistantBrowserTest.java` (HTTP 200, "New Delhi") |
| Weather integration | Open-Meteo numerical model forecast API with in-memory cache, WMO codes, & zero-fabrication fallback | **Resolved & Implemented** | `OpenMeteoWeatherService.java`, `WeatherApiTest.java` |
| Maps & Routing | Interactive Leaflet OSM map (Phase 3 Target); OSRM transit routing abstraction (Phase 4 Target) | **Roadmapped** | `docs/DATA_AUTHENTICITY_AND_PROVIDER_PLAN.md` |
| Budget estimation | Transparent median benchmark ranges with explicit currency formatting; zero fabricated quotes | **Resolved & Implemented** | `TripService.java`, `PersistenceTripApiTest.java` |
| Local Dev Orchestration | Windows single-command `start.bat` launcher with dependency ordering, health polling, browser launch, and service reuse | **Resolved & Implemented** | `start.bat` verified execution |
| Deployment | Local Docker Compose (MySQL 3307, Backend 8081, Frontend 5173); Cloud hosting in Phase 7 | **Resolved & Implemented** | `infra/docker-compose.yml`, `infra/init-db.sql` |

## Provider selection checklist

For each candidate AI/travel/map provider, record:
- Free-tier limits vs commercial production costs.
- Key/credential requirements (kept strictly server-side).
- Geographic coverage in the Indian subcontinent.
- Terms of use, attribution, and display restrictions (e.g. no autocomplete on public Nominatim).
- Rate limits, circuit breaking, and bounded timeouts.
- Data freshness, retrieval timestamps, and truthful labeling (e.g. model forecasts vs sensor readings).
- Whether the provider supports the specific required fields.
- Fallback plan: Honest unavailable states, zero synthetic fallbacks.

## Assumptions

- Development and testing operate on free non-commercial tiers and open data with $0 direct API costs; commercial production requirements and costs (e.g., Open-Meteo commercial Standard, self-hosted OSRM) are documented.
- Seed data must enforce non-nullable provenance columns and default `verification_status = 'UNVERIFIED'`.
- Test fixtures are strictly isolated in `src/test/` and never reachable in production runtime flows.
- Public Nominatim is never used for search autocomplete or bulk harvesting; discovery search runs against local database indexes.
- No feature should imply a reservation is confirmed unless an actual booking integration supports it.
- Users can always manually edit an itinerary if AI is unavailable.
- The reference repository is inspiration only; implementation starts from a clean repository.
- Final Java/Spring/Node versions are pinned in project files, not left floating.
