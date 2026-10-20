# Voyage-AI — Technical Design Document (TDD)

**Version:** 0.1 · **Status:** Proposed baseline

## 1. Architecture overview

Use a **modular monolith backend** and a separate React frontend. This keeps the system understandable for a student team, reduces deployment complexity, and still provides clean boundaries.

```text
Browser
  React + TypeScript + Vite
  Tailwind + custom design system
        |
        | HTTPS/JSON, /api/v1
        v
Spring Boot application
  Security / Controllers / DTO validation
  Services / Domain rules
  Repositories / JPA / Hibernate
  AI adapter / Travel-data adapter
        |
        +------ MySQL
        |
        +------ Optional AI provider
        |
        +------ Optional travel/map provider
```

Do not split into microservices solely to claim microservices. A modular monolith is the baseline; service separation can be considered only if justified.

## 2. Repository layout

```text
voyage-ai/
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   ├── components/
│   │   ├── features/
│   │   │   ├── auth/
│   │   │   ├── discovery/
│   │   │   ├── listings/
│   │   │   ├── trips/
│   │   │   ├── itinerary/
│   │   │   └── profile/
│   │   ├── lib/              # API client, utilities
│   │   ├── types/
│   │   └── styles/
│   ├── public/
│   ├── package.json
│   └── vite.config.ts
├── backend/
│   ├── src/main/java/com/voyageai/
│   │   ├── VoyageAiApplication.java
│   │   ├── config/
│   │   ├── security/
│   │   ├── common/            # errors, DTO helpers, validation
│   │   ├── auth/
│   │   ├── user/
│   │   ├── destination/
│   │   ├── listing/
│   │   ├── trip/
│   │   ├── itinerary/
│   │   ├── recommendation/
│   │   └── integration/
│   │       ├── ai/
│   │       ├── traveldata/
│   │       └── maps/
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── db/migration/
│   ├── src/test/
│   ├── pom.xml
│   └── Dockerfile
├── infra/
│   └── docker-compose.yml
├── docs/
└── README.md
```

This is a suggested structure, not a requirement to create empty packages. Add modules when their first real feature is implemented.

## 3. Frontend design and implementation

- React + TypeScript + Vite.
- Tailwind CSS with a small, explicit design-token system.
- Use the initialized Impeccable and Emil Kowalski design skills to guide design critique and implementation.
- Establish typography, spacing, color, border, elevation, responsive breakpoints, focus states, and motion rules before building all pages.
- Build shared components for navigation, buttons, inputs, cards, badges, dialogs, toasts, loading states, empty states, and error states.
- Keep feature pages separate: Discover, Destination Detail, Stays, Restaurants, Attractions, Trip Builder, Itinerary, Saved Trips, Profile.
- Use a typed API client and centralized error handling.
- Avoid putting secrets in `VITE_*` environment variables; client variables are public at build time.
- Use a frontend test framework selected during setup (for example, Vitest and React Testing Library).

## 4. Backend layering

- **Controller:** HTTP mapping, DTO validation, status codes; no large business logic.
- **Service:** business rules, orchestration, transactions.
- **Repository:** persistence access via Spring Data JPA.
- **Entity:** database model and relationships.
- **DTO:** API request/response contracts; do not expose JPA entities directly by default.
- **Mapper:** explicit entity/DTO conversion.
- **Integration adapter:** isolates provider-specific API calls.
- **Exception handler:** maps known exceptions to a consistent safe API error format.

Use constructor injection. Keep transaction boundaries in service methods where a multi-step operation needs atomicity.

## 5. Initial data model and relationships

Proposed relationships:
- User 1 — many Trips.
- Trip 1 — many ItineraryDays.
- ItineraryDay 1 — many ItineraryItems.
- Destination 1 — many associated listings.
- ItineraryItem 0..1 — 1 Listing (custom activities may not reference a listing).
- User 1 — many SavedItems if shortlist is included.

Use stable primary keys, foreign keys, appropriate indexes, timestamps, and validation. Choose cascade/orphan behavior deliberately; do not cascade deletes from shared destination/listing records merely because a user deletes a trip.

## 6. API conventions

- Prefix: `/api/v1`.
- JSON request/response bodies.
- DTO validation at the API boundary.
- Consistent error response, e.g. `{ "code": "VALIDATION_ERROR", "message": "Check the highlighted fields.", "fieldErrors": {} }`.
- Appropriate HTTP status codes.
- Pagination for potentially large lists.
- CORS allowlist from environment configuration.
- API documentation through OpenAPI/Springdoc if compatible with the selected Spring Boot version.
- Authentication/authorization documented per endpoint.

## 7. Authentication decision

React + Spring Boot can use either:
1. Cookie-based server session, with appropriate CSRF and cookie configuration; or
2. Token-based authentication with carefully managed token storage and refresh/revocation strategy.

For a same-site deployed web application, secure HttpOnly cookie-based sessions are a reasonable default and naturally demonstrate session management. Finalize based on hosting topology and instructor requirements. Do not place session IDs or tokens in URLs or localStorage by default.

## 8. AI integration

- Define an application-level interface such as `ItineraryGenerationService`.
- Implement a provider adapter behind the interface.
- Send only necessary trip and listing context.
- Require structured output and validate it before displaying/saving.
- Apply timeouts, bounded retries, rate limits, and safe error handling.
- Keep API keys in backend environment variables.
- Provide a mock/fake implementation for tests and a deterministic demo mode.
- Do not rely on AI to invent current opening hours, prices, or route durations.
- If output is invalid, ask for regeneration or fall back to manual planning without modifying existing saved trips.

## 9. Travel data integration

Provider choice is unresolved. Evaluate candidates based on:
- Geographic coverage.
- Terms of use and display/attribution rules.
- Free-tier availability, rate limits, and cost.
- Access to accommodation, restaurant, and attraction data.
- Image licensing and caching restrictions.
- Availability/freshness guarantees.

Create an adapter boundary so provider changes do not require rewriting the domain model. Use curated seed records in development/demo. Avoid scraping services unless explicitly permitted by their terms.

## 10. Database and migrations

- MySQL for relational persistence.
- Spring Data JPA/Hibernate.
- Use Flyway or Liquibase for versioned schema migrations; choose one at setup.
- Keep schema migration history in source control.
- Use separate configuration profiles for local/test/demo/deployment.
- Do not use production schema auto-update as a migration strategy.
- Seed demo data reproducibly and idempotently.

## 11. Course requirements in implementation

- Collections/Streams: listing filters, grouping costs, ordering itinerary items.
- Lambdas/functional interfaces: comparators, predicates, transformation functions.
- Exception handling: domain exceptions and centralized REST exception mapping.
- I/O: itinerary export/print feature if included; never deserialize arbitrary untrusted Java objects.
- Session management: secure server session or documented token approach; no URL session IDs.
- Hibernate: annotated entities and meaningful relationships.
- Spring: IoC/DI, bean configuration, service layer.
- Spring MVC: REST controllers.
- JPA: repositories and transactions.
- Postman: collection/environment for auth, discovery, trips, itinerary, error cases.
- Docker: backend image and Compose configuration with database.

## 12. Testing strategy

- Unit tests: validation, date rules, budget aggregation, itinerary ordering and mapping.
- Repository tests: entity relationships, constraints, migrations.
- API integration tests: status codes, DTO validation, persistence.
- Security tests: anonymous access and cross-user ownership.
- AI adapter tests: mocked provider success, timeout, invalid output.
- Frontend tests: forms, search/filter, trip creation/editing, error states.
- End-to-end smoke flow: discover → create trip → generate/mock proposal → edit → save → reload.
- Postman collection: positive and negative cases.
- Docker smoke test: clean build, service health, main API endpoint.

## 13. Docker and local setup

Use Docker Compose for MySQL and the backend; frontend can run locally during development or be containerized for the final demo. Keep secrets in `.env` files excluded from version control and provide `.env.example` with placeholder values. Add health checks and service readiness handling.

## 14. Architecture decisions still open

- Exact JDK/Spring Boot versions.
- Authentication mode and deployment origin topology.
- Travel-data, map/routing, and AI providers.
- Flyway versus Liquibase.
- Whether map view/export/shortlist make the MVP cut.
- Hosting and any production database choice.
