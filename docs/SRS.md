# Voyage-AI — Software Requirements Specification (SRS)

**Version:** 0.1 · **Status:** Baseline  
**Standard approach:** Structured, testable requirements; final institutional template may be applied later.

## 1. Introduction

### 1.1 Purpose
Specify the expected software behavior, interfaces, data, constraints, and acceptance requirements for the Voyage-AI MVP.

### 1.2 Scope
Voyage-AI is a browser-based travel discovery and planning application. It connects destination and travel-listing discovery with AI-assisted recommendations, editable itineraries, saved trips, and estimated budget tracking.

The MVP does not execute reservations, payments, ticket issuance, or booking confirmations.

### 1.3 Definitions
- **Trip:** User-owned planning record with dates, destination/context, travelers, preferences, and budget.
- **Listing:** Destination, accommodation, restaurant, attraction, or activity record.
- **Itinerary:** Ordered schedule organized into trip days.
- **Itinerary item:** A listing reference or custom activity placed in a schedule.
- **Estimate:** A non-guaranteed value based on available data.
- **Provider data:** Data returned by a third-party service.
- **AI proposal:** Generated content that requires validation and user review.

### 1.4 References
- ACSD30 Java Full Stack Development course syllabus provided by the team.
- Voyage-AI PRD v0.1.
- Voyage-AI FRD v0.1.
- Selected provider/API documentation, to be added after provider selection.

## 2. Overall description

### 2.1 Product perspective
The system will use a React web client communicating with a Java Spring Boot REST backend. The backend owns validation, authorization, business rules, persistence, and integration with AI/travel data providers. A relational database stores users, trips, itinerary data, and curated travel listings.

### 2.2 User classes
- Visitor: public discovery and account creation.
- Registered traveler: private trip and preference management.
- Maintainer: seed-data and environment maintenance; no broad public admin console is required in MVP.

### 2.3 Operating environment
- Modern browsers on desktop, tablet, and mobile.
- Node.js/npm-compatible frontend build environment.
- Supported JDK and Maven version finalized in project configuration.
- MySQL-compatible relational database.
- Docker-compatible local/deployment environment.

### 2.4 Constraints
- Frontend and backend are separate applications communicating over HTTP(S) JSON APIs.
- All private-resource authorization is enforced by the backend.
- AI/travel-provider credentials are never shipped to the browser.
- Provider usage, licensing, and rate limits constrain available data.
- Live information cannot be promised until provider coverage and freshness are verified.
- Initial architecture is a modular monolith; microservices are not a baseline requirement.

### 2.5 Assumptions
- A curated seed dataset can support local development and demonstration.
- The AI integration may be disabled in local/test profiles; manual trip planning remains available.
- Users can provide incomplete preferences; the system asks only for essential missing information or uses explicit defaults.
- External booking links are informational navigation, not in-app transactions.

## 3. System features and functional requirements

The FRD is the user-facing functional source of detail. The following requirements are system-level and testable.

### 3.1 Authentication and authorization
**SYS-F-001:** The system shall authenticate users using the selected Spring Security configuration.  
**SYS-F-002:** The system shall protect trip and profile APIs.  
**SYS-F-003:** Every private-resource operation shall verify the authenticated user's ownership/permissions.  
**SYS-F-004:** The system shall not store plaintext passwords. If framework-managed authentication is used, credentials shall be handled through the approved secure mechanism.  
**SYS-F-005:** Logout/session invalidation shall follow the chosen authentication mode.

### 3.2 Discovery and listing data
**SYS-F-010:** The backend shall expose paginated or bounded APIs for destinations and travel listings.  
**SYS-F-011:** Search and filtering shall operate only on supported, documented fields.  
**SYS-F-012:** Listing data shall distinguish required fields from optional/provider-dependent fields.  
**SYS-F-013:** External provider errors shall not crash unrelated core workflows.  
**SYS-F-014:** Seed data shall be available in a development/demo profile.

### 3.3 Trip and itinerary
**SYS-F-020:** The backend shall create, retrieve, update, and delete trips subject to ownership.  
**SYS-F-021:** The backend shall validate trip dates, required fields, and supported budget values.  
**SYS-F-022:** The backend shall create, retrieve, update, reorder, and remove itinerary items.  
**SYS-F-023:** Database transactions shall be used where a multi-record operation must remain atomic.  
**SYS-F-024:** The system shall persist itinerary modifications and return the persisted representation.  
**SYS-F-025:** The system shall define deletion behavior for related itinerary records.

### 3.4 AI orchestration
**SYS-F-030:** The backend shall build an AI request from validated trip data and relevant listing context.  
**SYS-F-031:** AI responses shall be parsed into a defined schema and validated before being offered as an itinerary proposal.  
**SYS-F-032:** The system shall enforce configured timeouts and bounded retry behavior.  
**SYS-F-033:** The system shall not expose provider credentials or raw internal exceptions to the client.  
**SYS-F-034:** AI failure shall return a consistent error and leave existing saved data unchanged.  
**SYS-F-035:** The system shall label generated recommendations as proposals and avoid representing unsupported data as verified facts.

### 3.5 Budget and source information
**SYS-F-040:** The system shall calculate displayed totals from known cost values according to documented currency and aggregation rules.  
**SYS-F-041:** Unknown values shall be represented distinctly from zero.  
**SYS-F-042:** Provider-sourced time-sensitive fields should carry source and retrieval/freshness metadata where feasible.

### 3.6 API behavior
**SYS-F-050:** APIs shall use documented JSON request and response schemas.  
**SYS-F-051:** APIs shall use appropriate HTTP status codes for success, validation failure, unauthenticated access, forbidden access, missing resources, conflict, rate limit, and unexpected errors.  
**SYS-F-052:** API errors shall use a consistent structure containing a safe message, stable error code, and optional field errors/correlation ID.  
**SYS-F-053:** Mutating endpoints shall validate request bodies and path parameters.  
**SYS-F-054:** API documentation shall be maintained alongside the implementation in OpenAPI 3.0 specification (`docs/openapi.yaml`).  
**SYS-F-055:** The global autocomplete search API (`GET /api/v1/search/suggestions`) shall provide debounced, real-time matching suggestions grouped strictly in order: Cities (`CITY`) -> Attractions (`ATTRACTION`) -> Dining (`DINING`), strictly sourcing authentic records without synthetic placeholders. Empty groups shall be explicitly retained.

## 4. External interface requirements

### 4.1 User interface
- React + TypeScript web client.
- Responsive layout with keyboard-accessible navigation and controls.
- Distinct loading, empty, validation, success, and failure states.
- No secret credentials embedded in client code.
- API base URL supplied through environment configuration.

### 4.2 Backend API
- HTTP(S), JSON, versioned prefix such as `/api/v1`.
- CORS restricted to configured frontend origins.
- Authentication mode documented and applied consistently.
- Input validation and server-side authorization on all protected operations.

### 4.3 Database
- MySQL relational database through Spring Data JPA/Hibernate.
- Schema changes managed through versioned migrations.
- Foreign keys and constraints used for referential integrity where applicable.
- No production secrets committed in repository files.

### 4.4 Third-party interfaces
- AI provider: configurable adapter/service; provider-specific SDK types should not leak through the domain model.
- Travel data provider: optional adapter for destination/listing data.
- Map/routing provider: optional MVP integration.
- Each provider integration must document credentials, limits, licensing, failure modes, and data attribution.

## 5. Logical data requirements

Initial logical entities:

### User
- `id`
- `displayName`
- `email` or authentication subject
- `createdAt`, `updatedAt`
- preferences as normalized fields or related records

### Destination
- `id`
- `name`
- `country`, `region`
- `description`
- optional coordinates, image references, tags, source metadata

### TravelListing
- `id`
- `destinationId` or geographic association
- `type` (accommodation, restaurant, attraction, activity)
- `name`, `description`
- optional address/coordinates, images, price information, currency, hours, rating, external URL, source metadata

### Trip
- `id`
- `ownerId`
- `title`
- destination/context fields
- `startDate`, `endDate`
- traveler count
- target budget and currency
- status, timestamps

### ItineraryDay
- `id`
- `tripId`
- date or day index
- title/notes
- display order

### ItineraryItem
- `id`
- `itineraryDayId`
- optional `listingId`
- title, category, notes
- optional start time, duration, estimated cost/currency
- display order
- origin/type indicator (listing/custom/AI proposal)

### SavedItem (optional MVP)
- `id`
- `ownerId`
- item type/reference
- creation timestamp

Entity names and normalization may change in the TDD after relationship and deletion rules are reviewed.

## 6. API resource outline

Exact request/response schemas and field validation must be specified before implementation.

| Resource | Example endpoints | Access |
|---|---|---|
| Auth | `/api/v1/auth/register`, `/login`, `/logout`, `/me` | Public or authenticated as appropriate |
| Destinations | `GET /api/v1/destinations`, `GET /api/v1/destinations/{id}` | Public |
| Listings | `GET /api/v1/listings`, `GET /api/v1/listings/{id}` | Public |
| Preferences | `GET/PUT /api/v1/me/preferences` | Authenticated |
| Trips | `GET/POST /api/v1/trips`, `GET/PUT/DELETE /api/v1/trips/{id}` | Owner |
| Itinerary | `GET/POST /api/v1/trips/{id}/days`, item mutation routes | Owner |
| AI planning | `POST /api/v1/trips/{id}/recommendations`, `POST /api/v1/trips/{id}/itinerary-proposals` | Owner |
| Budget | `GET /api/v1/trips/{id}/budget` | Owner |

Avoid building one giant endpoint that returns or mutates the entire application state. Use clear resource boundaries and validation.

## 7. Error handling

Recommended stable error categories:
- `VALIDATION_ERROR`
- `UNAUTHENTICATED`
- `FORBIDDEN`
- `NOT_FOUND`
- `CONFLICT`
- `RATE_LIMITED`
- `PROVIDER_UNAVAILABLE`
- `AI_OUTPUT_INVALID`
- `INTERNAL_ERROR`

Responses must not reveal stack traces, secrets, SQL details, or sensitive provider payloads. Detailed exceptions belong in controlled server logs.

## 8. Technology constraints and course coverage

- Java Collections, Maps/Sets/Lists, Streams, lambdas, functional interfaces, and exception handling should appear in appropriate backend logic.
- File I/O/serialization may support itinerary export/import or a clearly bounded feature; do not use Java serialization for untrusted web input.
- Session management must use a secure, documented approach appropriate to React + REST. Do not put session IDs or secrets in URLs.
- Spring IoC/DI, service beans, Spring MVC REST controllers, JPA/Hibernate relationships, and Spring Data repositories shall be used.
- Postman shall test core API flows.
- Docker shall package the backend and support services for repeatable local execution.

## 9. Verification requirements

- Unit tests for domain rules, validation, cost aggregation, and itinerary transformations.
- Integration tests for repository persistence and API behavior.
- Authorization tests proving users cannot access other users' trips.
- AI adapter tests using mocks/fakes so normal tests do not require a paid API call.
- Frontend tests for primary user flows and critical states.
- Postman collection for manual/reproducible API testing.
- Docker smoke test and documented startup procedure.

## 10. SRS acceptance criteria

The system is ready for demonstration when:
1. Build and startup instructions work from a clean checkout.
2. Core discovery APIs return seed/provider data and support documented filtering.
3. Authenticated users can create and persist trips and itinerary items.
4. Ownership checks prevent cross-user access.
5. AI output is schema-validated and AI failures degrade gracefully.
6. Automated tests pass for core rules and endpoints.
7. Postman collection covers the core workflows.
8. Docker Compose can start the documented services.
9. The UI is responsive and provides expected states.
10. Remaining provider-dependent limitations are documented.
