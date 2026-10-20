# Voyage-AI — Implementation Plan

**Version:** 0.1 · **Status:** Proposed sequence

## Guiding rule

Build a working end-to-end vertical slice early. Do not spend the entire schedule on documents or disconnected UI mockups. The documentation is a baseline that can be revised as the implementation reveals real constraints.

## Phase 0 — Repository and setup
- Create a new `voyage-ai` repository; do not copy the reference repo.
- Add docs, frontend, backend, and infra directories.
- Choose JDK/Spring Boot/Node versions compatible with dependencies.
- Add `.gitignore`, `.env.example`, formatter/linter, and README.
- Establish a basic CI/local verification script.

**Exit gate:** frontend and backend both build; backend health endpoint responds.

## Phase 1 — Design foundation
- Read initialized design-skill instructions.
- Establish product visual direction and tokens.
- Build app shell, responsive navigation, buttons, form controls, cards, loading/empty/error states.
- Create static/seed-data discovery pages.

**Exit gate:** responsive discovery prototype with coherent design system.

## Phase 2 — Backend and persistence foundation
- Configure Spring Boot, validation, exception mapping, security, JPA/Hibernate, MySQL, and migrations.
- Create User, Destination, TravelListing, Trip, ItineraryDay, and ItineraryItem models as needed.
- Seed deterministic demo destinations and listings.
- Implement basic discovery and trip CRUD APIs.

**Exit gate:** API tests prove search and trip CRUD with persistence.

## Phase 3 — First complete vertical slice
- Frontend can browse a destination.
- User can create a trip, add a listing/custom activity, save, and reload.
- Enforce ownership and validation.
- Add integration and frontend tests.

**Exit gate:** complete manual planning flow works without AI.

## Phase 4 — Itinerary and budget
- Add day management, item ordering/editing/removal.
- Add cost aggregation and unknown-cost handling.
- Add schedule warnings only where supporting data exists.
- Add optional print/export if schedule permits.

**Exit gate:** saved itinerary and estimates work reliably.

## Phase 5 — AI integration
- Choose an affordable provider after reviewing limits and terms.
- Implement an adapter and structured response schema.
- Validate output and handle timeouts/provider errors.
- Add mocked tests and deterministic demo fallback.
- Add generation and refinement UI.

**Exit gate:** AI proposal can be generated or gracefully falls back without corrupting saved plans.

## Phase 6 — Polish and verification
- Complete mobile/responsive behavior and interaction states.
- Run lint, type checks, unit/integration tests, production builds.
- Verify access-control cases.
- Create Postman collection and environment.
- Test Docker Compose from a clean state.
- Update docs and traceability matrix.

**Exit gate:** documented demonstration workflow passes.

## Phase 7 — Deployment and demonstration
- Select a hosting setup based on cost and available accounts.
- Configure environment variables and database persistence.
- Deploy only after secrets, CORS, HTTPS, and migrations are checked.
- Prepare a short demo scenario and known-limitations section.

## Agent execution rules
- Inspect current files before editing; do not overwrite unrelated work.
- Implement in small, buildable milestones.
- Do not mark features complete without tests or manual verification evidence.
- Never hardcode API secrets or fabricate external data.
- Prefer free-tier options and ask before adding paid dependencies.
- Run relevant checks after each milestone and summarize changed files, tests, and remaining issues.
