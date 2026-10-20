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
| Travel data | Curated/seeded data first; provider adapter later | Before showing live prices, ratings, opening hours, or availability |
| AI provider | Configurable adapter + mock/demo implementation | Before AI implementation; compare cost, quality, rate limits, terms |
| Maps/routing | Optional; defer until core workflow works | If reliable free/affordable provider and time are available |
| Booking | External links only | Direct booking requires partner/API agreements and additional security/operations |
| Budget | Estimates only; unknowns explicit | After cost source and currency rules are defined |
| Deployment | Docker Compose locally (MySQL 3307, Backend 8081); hosting selected later | Before final demo/deployment |

## Provider selection checklist

For each candidate AI/travel/map provider, record:
- Free-tier limits and expected cost.
- Key/credential requirements.
- Geographic coverage and data completeness.
- Terms of use, attribution, and display restrictions.
- Rate limits and failure behavior.
- Data freshness and reliability.
- Whether the provider supports the specific required fields.
- Fallback plan.

## Assumptions

- MVP should be demonstrable without paid API access.
- Seed data should be clearly identified as demo/curated content.
- No feature should imply a reservation is confirmed unless an actual booking integration supports it.
- Users can always manually edit an itinerary if AI is unavailable.
- The reference repository is inspiration only; implementation starts from a clean repository.
- Final Java/Spring/Node versions will be pinned in project files, not left floating.
