# Voyage-AI — Open Decisions and Assumptions

**Version:** 0.1

These decisions are intentionally left configurable so the team can begin building the core product without waiting for every third-party integration to be finalized.

## Defaults for starting implementation

| Decision | Starting default | When to revisit |
|---|---|---|
| Architecture | React client + Spring Boot modular monolith | Only if a demonstrated scaling/team need emerges |
| Frontend | React + TypeScript + Vite | If course/team constraints change |
| Styling | Tailwind + custom design system | After design-skill review |
| Persistence | MySQL + Spring Data JPA/Hibernate | If hosting or course requirements require another relational DB |
| Schema migrations | Flyway proposed | Confirm dependency/version compatibility during setup |
| Authentication | Secure backend-managed session is a reasonable default for same-site deployment | Before implementing auth; depends on hosting topology and faculty expectations |
| Travel data | Curated/seeded data first; provider adapter later | Before showing live prices, ratings, opening hours, or availability |
| AI provider | Configurable adapter + mock/demo implementation | Before AI implementation; compare cost, quality, rate limits, terms |
| Maps/routing | Optional; defer until core workflow works | If reliable free/affordable provider and time are available |
| Booking | External links only | Direct booking requires partner/API agreements and additional security/operations |
| Budget | Estimates only; unknowns explicit | After cost source and currency rules are defined |
| Deployment | Docker Compose locally; hosting selected later | Before final demo/deployment |

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
