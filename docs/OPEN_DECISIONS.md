# Voyage-AI — Open Decisions and Assumptions

**Version:** 0.1

These decisions are intentionally left configurable so the team can begin building the core product without waiting for every third-party integration to be finalized.

## Defaults for starting implementation

| Decision | Starting default | When to revisit |
|---|---|---|
| Architecture | React client + Spring Boot modular monolith | Only if a demonstrated scaling/team need emerges |
| Frontend | React + TypeScript + Vite | If course/team constraints change |
| Styling | Tailwind + custom design system | After design-skill review |
| Persistence | MySQL 8.0 + Spring Data JPA/Hibernate (Docker container on host port 3307) | Resolved: Docker MySQL mapped to 3307:3306 to coexist with host MySQL service |
| Schema migrations | Flyway (baseline established in V0__baseline.sql) | Resolved: Flyway 10 selected with Spring Boot 3 |
| Authentication | JWT Bearer authentication (in-memory token transport) | Resolved: Approved in Phase 0; see docs/architecture/JWT_AUTH_DESIGN.md |
| Travel data | Curated/seeded data first; provider adapter later | Resolved (Academic v2.1): (1) Real destinations/POIs from attributable OpenStreetMap extracts & official ASI Gazette records in MySQL, (2) Wikimedia source-attributed media, (3) Open-Meteo forecasts. OpenTripMap designated as optional candidate only pending vendor terms verification |
| AI provider | Configurable adapter + mock/demo implementation | Resolved (Academic v2.1): Google Gemini 1.5 Flash via AI Studio Free Tier (15 RPM, no card required) with grounded RAG entity containment on database IDs; zero user PII transmitted in prompts (human-review disclosure); falls back to deterministic local sequencer |
| Maps/routing | Optional; defer until core workflow works | Resolved (Academic v2.1): Leaflet.js with OSM standard tiles (strictly following OSM tile usage policy, attribution, and no SLA). Routing via abstract `RoutingProvider` interface with OSRM demo server for development evaluation only |
| Booking | External links only | Resolved (Academic v2.1): Directory-only mode with verified contact links; live prices omitted unless sourced via official authorized feed |
| Budget | Estimates only; unknowns explicit | Resolved (Academic v2.1): Historical median benchmark ranges with explicit assumptions; zero fabricated quotes |
| Deployment | Docker Compose locally (MySQL 3307, Backend 8081); hosting selected later | Before final demo/deployment |

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
