# Voyage-AI — Requirements Traceability Matrix

**Version:** 0.1 · **Status:** Initial mapping

This matrix links product capabilities to implementation areas, ACSD30 concepts, and verification. Update it as requirements and code evolve.

| Requirement group | Frontend area | Backend area | Data/integration | ACSD30 alignment | Verification |
|---|---|---|---|---|---|
| FR-001–005 Accounts/profile/ownership | Auth, Profile | Security, User | User records | Session management, DI, exception handling | Auth tests, cross-user authorization tests |
| FR-010–014 Destination discovery | Discover, Destination detail | Destination API/service | Destination seed/provider | Collections, Streams, REST, JPA | Search/filter API and UI tests |
| FR-020–025 Stays/restaurants/activities | Listing pages/cards | Listing API/service | Listing records/provider | Collections, Streams, REST, Hibernate | Listing API tests |
| FR-030–033 Trip setup | Create Trip, My Trips | Trip controller/service | Trip entity | Spring MVC, DI, JPA | Validation and CRUD tests |
| FR-040–046 AI recommendations | Recommendation flow | Recommendation service/adapter | AI provider, listing context | Exception handling, REST, service design | Mocked success/failure/invalid output tests |
| FR-050–057 Itinerary | Itinerary Builder | Itinerary service/controller | Days/items/listings | Collections, Streams, transactions, Hibernate | Add/edit/reorder/save/reload tests |
| FR-060–063 Budget | Budget summary | Budget service | Known estimated costs | Streams/reduce, domain logic | Currency/unknown-cost aggregation tests |
| FR-070–072 Map/export | Map/print view if included | Map/export service if included | Map provider/file output | I/O, REST | Provider mock/export tests |
| FR-080–084 Feedback/reliability | Shared states/toasts/forms | Error handler | N/A | Exception handling, REST | Error-state and integration tests |
| Docker/deployment | Build assets | Docker image | MySQL service | Docker module | Clean Compose smoke test |
| API testing | N/A | All public APIs | Postman environment | Postman module | Versioned collection execution |

## Course-outcome coverage notes

- **CO1:** Collections and Java 8 processing in listing and itinerary logic.
- **CO2:** Exception handling and I/O in robust services and optional itinerary export.
- **CO3:** Persistent entities, relationships, user sessions, and CRUD.
- **CO4:** Spring IoC/DI and modular Spring-based services.
- **CO5:** Spring Boot REST APIs, responsive UI, and JPA persistence.
- **CO6:** Postman verification and Dockerized application.

Coverage is planned, not proven, until corresponding code and passing tests exist.
