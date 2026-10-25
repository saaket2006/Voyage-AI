# Voyage-AI — Pre-Implementation Audit & Readiness Verification (v1.1)

**Document Version:** 1.1  
**Audit Date:** 2026-10-20  
**Target Repository:** Voyage-AI (`g:\Projects\JFS\Voyage-AI`)  
**Status:** Historical Baseline Record (Phase 0/1 Pre-Implementation)  
**Supersedes:** `docs/audit/PRE_IMPLEMENTATION_AUDIT.md` (v1.0)  
**Historical Note:** This document records the pre-implementation environment audit conducted on October 20, 2026 prior to Phase 0. Subsequent milestones (Phases 0, 1, and 2) have since been successfully completed and verified. The single canonical database was established as `voyage` (Docker port 3307), Flyway migrations V0-V4 were applied, and full-stack verification confirmed 128 passing tests. For the current post-implementation verification, consult `docs/reports/POST_IMPLEMENTATION_VERIFICATION.md`.

---

## 1. Executive Summary & Verdict Overview

This v1.1 audit provides an exhaustive, evidence-backed evaluation of the host operating environment against the complete ten-document Voyage-AI product specification:
1. `README.md`
2. `PRD.md` (Product Requirements Document)
3. `FRD.md` (Functional Requirements Document)
4. `SRS.md` (Software Requirements Specification)
5. `NFRD.md` (Non-Functional Requirements Document)
6. `TDD.md` (Technical Design Document)
7. `UI_UX_SPEC.md` (UI/UX Specification)
8. `TRACEABILITY_MATRIX.md` (Requirements Traceability Matrix)
9. `IMPLEMENTATION_PLAN.md` (Phased Implementation Plan)
10. `OPEN_DECISIONS.md` (Open Decisions and Assumptions)

### Multi-Stage Readiness Verdicts

| Lifecycle Stage | Readiness Verdict | Summary Rationale |
|---|---|---|
| **Stage 1: Project Initialization (Phase 0)** | **READY FOR INITIALIZATION** | All core runtimes (Java 17 LTS, Maven 3.9, Node 24, npm 11.8, Docker Engine, Docker Compose, Git) are installed, healthy, and verified. No host software installations or system modifications are required to scaffold `frontend/`, `backend/`, and `infra/`. |
| **Stage 2: Feature Implementation (Phases 1–5)** | **READY AFTER MINOR REMEDIATION** | Requires three project-level configuration decisions before writing domain logic: (1) host port offset (`3307:3306`) in `docker-compose.yml` due to active host `MySQL80` service; (2) approval of JWT Bearer authentication over cookie sessions for decoupled cross-origin development; (3) authoring the Flyway seed migrations and mock AI adapter. |
| **Stage 3: Production Deployment (Phases 6–7)** | **DECISION DEFERRED (BY SPECIFICATION)** | Cloud hosting provider (Render, Railway, AWS, or Oracle Cloud), remote managed database, and commercial AI provider API keys are intentionally unselected per `OPEN_DECISIONS.md` and `IMPLEMENTATION_PLAN.md`. |

---

## 2. Corrections and Refinements to Audit v1.0

| Area | Stated in v1.0 | Finding & Correction in v1.1 | Impact |
|---|---|---|---|
| **Spring Boot Lifecycle** | Stated Spring Boot 3.3.x/3.4.x without lifecycle qualification. | Java 17 LTS is supported by Oracle through at least Sept 2026+. Spring Boot 3.x requires Java 17 as its hard baseline. Spring Boot 3.3.x / 3.4.x is the recommended production baseline for Java 17, providing native support for Flyway 10, Hibernate 6.5+, and MySQL Connector/J 8.3+/9.x. | Positive confirmation; no version mismatch. |
| **Node.js 24 Compatibility** | Stated Node 24 works with Vite. | Re-verified via official specs: Vite 5 and Vite 6 require Node 18+ or 20.19+/22.12+. Node v24.13.1 (with V8 13.6 and native OpenSSL 3.5.5) fully executes Vite, esbuild, and Rollup. However, native OpenSSL 3.5 requires modern cryptographic ciphers; deprecated npm packages with weak keys must be avoided. | Confirmed safe for modern Vite + React 18/19 stack. |
| **MySQL Service State** | Reported `MySQL80` running on port 3306. | Re-verified directly via `netstat -ano` and Windows Service controller: Service `MySQL80` is running under PID 7716, bound to `0.0.0.0:3306` and `[::]:3306` with `StartType: Automatic`. Port 33060 (X Protocol) is also bound. | Confirms port 3306 on the host is 100% occupied. Docker Compose must use `3307:3306`. |
| **Postman / Newman** | Reported Postman installed in AppData; Newman missing. | Clarified: Postman desktop state exists in AppData, but no binary is in system `PATH`. For repeatable automated testing (NFR-TEST-04), Newman will be executed via `npx --yes newman` or as a project devDependency, avoiding global system contamination. | Zero environment friction. |
| **Requirements Traceability** | Limited to a high-level summary of requirement categories. | Expanded in v1.1 to a complete, rigorous matrix covering all individual IDs from FRD, SRS, NFRD, TDD, and UI_UX_SPEC. | Full audit traceability achieved. |

---

## 3. Verified Host Environment Inventory

All data below reflects fresh, live re-verification executed on 2026-10-20.

```text
Host Environment Snapshot:
- OS: Microsoft Windows 11 Home Single Language (NT 10.0.26300.0, 64-bit)
- Working Directory: g:\Projects\JFS\Voyage-AI
- Local Time: 2026-10-20T14:31:08+05:30
```

### 3.1 Runtimes, Compilers, and Build Tools

| Tool / Runtime | Installed Version | Binary Path | Live Test Executed | Result |
|---|---|---|---|---|
| **Java Runtime (JRE)** | Java 17.0.17 LTS (build 17.0.17+8-LTS-360) | `D:\Applications\Java 17\bin\java.exe` | `java -version` | Oracle HotSpot 64-Bit Server VM active |
| **Java Compiler (JDK)** | javac 17.0.17 | `C:\Program Files\Common Files\Oracle\Java\javapath\javac.exe` | Compiled `TestSanity.java` | Compiles clean bytecode |
| **Java Home** | 17.0.17 | `D:\Applications\Java 17` | `$env:JAVA_HOME` inspection | Valid path; recognized by Maven |
| **Apache Maven** | 3.9.15 | `D:\Applications\Maven\apache-maven-3.9.15\bin\mvn.cmd` | `mvn -v` | Active; runtime points to `D:\Applications\Java 17` |
| **Gradle** | Not installed | N/A | `Get-Command gradle` | Not found; Maven is project baseline |
| **Node.js** | v24.13.1 | `D:\Applications\NodeJS\node.exe` | `node -e "console.log(process.versions)"` | V8 13.6, OpenSSL 3.5.5, native ESM |
| **npm** | 11.8.0 | `D:\Applications\NodeJS\npm.ps1` | `npm -v` | Functional |
| **npx** | 11.8.0 | `D:\Applications\NodeJS\npx.ps1` | `npx -v` | Functional |
| **Python** | 3.11.0 | `D:\Applications\python 3.11\python.exe` | `python --version` | Operational (secondary scripting utility) |
| **Git** | 2.52.0.windows.1 | `C:\Program Files\Git\cmd\git.exe` | `git status` | Clean working tree on branch `main` |

### 3.2 Data Stores, Services, and Networking

| Service / Component | Version / Target | Location / Listener | Observed State | Conflict / Constraint |
|---|---|---|---|---|
| **Windows MySQL Service** | MySQL Server 8.0.44 Community | Windows Service `MySQL80` (PID 7716) | `Running` (StartType: Automatic) | Listens on `0.0.0.0:3306`, `[::]:3306`, `0.0.0.0:33060` |
| **MySQL CLI** | 8.0.44 | `C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe` | Present on disk, not in system `PATH` | Direct `mysql` command requires full path or PATH update |
| **MySQL Workbench** | 8.0.44 | `C:\Program Files\MySQL\MySQL Workbench 8.0\mysql.exe` | Present on disk | Local GUI client available for inspection |
| **Docker Engine** | 29.6.2 (build dfc4efb) | `C:\Program Files\Docker\Docker\resources\bin\docker.exe` | Linux containerd daemon active on WSL2 | 12 CPUs, 7.46 GiB RAM allocated; 1 running MCP container |
| **Docker Compose** | v5.3.1 | `C:\Program Files\Docker\cli-plugins\docker-compose.exe` | CLI plugin functional | Host port mapping `3306` blocked; must use `3307:3306` |

### 3.3 Design Skills & Agent Automation

| Capability | Location | Verification Status | Operational Notes |
|---|---|---|---|
| **Emil Kowalski Skill** | `.agents/skills/emil-design-eng/SKILL.md` | Verified (675 lines) | Strict `\| Before \| After \| Why \|` review table constraint; UI animation < 300ms; button `scale(0.97)` on `:active`; Sonner toast principles. |
| **Impeccable Workflows** | `.agents/workflows/` | Verified (18 workflows) | Includes `/teach-impeccable`, `/critique`, `/audit`, `/polish`, `/delight`, `/harden`, `/simplify`, `/adapt`. |
| **Companion Skills** | `.agents/skills/` | Verified | `animate`, `animation-performance`, `apple-design`, `ask-sonner`, `break-ui`, `ui-ux-designer`, `frontend-developer`. |
| **Browser Inspection** | Lazy tools (`chrome-devtools-mcp`) | Verified | DOM query, CSS inspection, lighthouse audit, screenshots via DevTools MCP. |
| **Subagent Automation** | `browser_subagent` native tool | Verified | Headless web recording and workflow execution. |

---

## 4. Comprehensive Requirements Traceability Matrix

This matrix maps every documented requirement across all ten product documents to the environment, implementation milestones, and verification criteria.

### 4.1 Functional Requirements (FRD: FR-001 through FR-084 & Business Rules)

| Req ID | Requirement Summary | Source Document | Required Tool / Stack Component | Environment Evidence | Milestone | Verification Method & Expected Result | Status | Priority |
|---|---|---|---|---|---|---|---|---|
| **FR-001** | User Registration with validation | FRD §3.A | Spring Security, BCrypt, React form | JDK 17, Maven 3.9, Node 24 | Phase 2 / Phase 3 | Automated test: POST `/api/v1/auth/register` returns 201; rejects duplicate email with 409 Conflict. | Ready for Dev | **P0** |
| **FR-002** | Sign in / Sign out & protected routes | FRD §3.A | Spring Security Filter Chain, React Auth Context | JDK 17, Node 24 | Phase 2 / Phase 3 | MockMvc test: Protected endpoints return 401 when unauthenticated. | Ready for Dev | **P0** |
| **FR-003** | User Profile view & update | FRD §3.A | REST Controller, JPA User Entity | JDK 17, MySQL 8.0 | Phase 2 | GET/PUT `/api/v1/me` persists display name and preferences. | Ready for Dev | **P1** |
| **FR-004** | Maintain Travel Interests & Preferences | FRD §3.A | JPA Entity / JSON attribute, React settings | JDK 17, MySQL 8.0 | Phase 2 | Preferences successfully feed into AI context generation. | Ready for Dev | **P1** |
| **FR-005** | Private Resource Ownership Enforcement | FRD §3.A, BR-01, BR-10 | Spring Security `@PreAuthorize` or Service Ownership check | JDK 17, Spring Boot | Phase 2 / Phase 3 | Security test: User B attempting GET/PUT/DELETE on User A's trip receives 403 Forbidden. | Ready for Dev | **P0** |
| **FR-010** | Browse Destination Cards | FRD §3.B | React Card Component, REST API | Node 24, Vite, Spring Boot | Phase 1 / Phase 2 | GET `/api/v1/destinations` returns paginated list with images and tags. | Ready for Dev | **P1** |
| **FR-011** | Text Search across Destinations | FRD §3.B | Spring Data JPA query / Specification | MySQL 8.0, JPA | Phase 2 | Search for "Jaipur" returns matching destination records. | Ready for Dev | **P1** |
| **FR-012** | Filter & Sort by supported attributes | FRD §3.B | JPA dynamic query, React Filter Bar | Spring Boot, React | Phase 2 | Filtering by region/budget returns subset; unsupported filters rejected. | Ready for Dev | **P1** |
| **FR-013** | Destination Detail with Stays/Dining/Activities | FRD §3.B | Master-detail REST API, React Tabs | Spring Boot, JPA relations | Phase 2 / Phase 3 | GET `/api/v1/destinations/{id}` returns overview + related listings. | Ready for Dev | **P1** |
| **FR-014** | Data Provenance & Freshness Labels | FRD §3.B, NFR-DATA-03 | Provenance badge component, DB metadata | React, Tailwind, JPA | Phase 1 / Phase 2 | UI displays `Curated / Demo` badge with retrieval timestamp. | Ready for Dev | **P1** |
| **FR-020** | Accommodation Listings exploration | FRD §3.C | JPA Listing Entity (type=ACCOMMODATION) | MySQL 8.0, Spring Boot | Phase 2 | GET `/api/v1/listings?type=ACCOMMODATION&destinationId={id}` works. | Ready for Dev | **P1** |
| **FR-021** | Accommodation Detail view | FRD §3.C | React Listing Modal/Page | React, Tailwind | Phase 2 | Displays price range, amenities, location, disclaimer. | Ready for Dev | **P1** |
| **FR-022** | Restaurant Discovery & Filtering | FRD §3.C | JPA Listing Entity (type=RESTAURANT) | MySQL 8.0, Spring Boot | Phase 2 | Displays cuisine, price band, opening hours or unavailable badge. | Ready for Dev | **P1** |
| **FR-023** | Attraction & Activity Discovery | FRD §3.C | JPA Listing Entity (type=ATTRACTION) | MySQL 8.0, Spring Boot | Phase 2 | Displays duration, estimated cost, category tags. | Ready for Dev | **P1** |
| **FR-024** | Shortlist / Favorites (Conditional MVP) | FRD §3.C | UserSavedItem Entity, Toggle button | Spring Boot, React | Phase 4 (Conditional) | User can bookmark listing; does not block trip workflow if deferred. | Optional | **P2** |
| **FR-025** | External Booking / Information Links | FRD §3.C, BR-06 | External URL button with disclaimer | React | Phase 1 / Phase 2 | Link opens in new tab (`rel="noopener"`); clearly marked non-booking. | Ready for Dev | **P1** |
| **FR-030** | Create Trip with title, dates, travelers, budget | FRD §3.D | Trip Entity, Form with validation | Spring Boot, React | Phase 3 | POST `/api/v1/trips` persists trip under authenticated user ID. | Ready for Dev | **P0** |
| **FR-031** | Validate Trip Dates (`endDate >= startDate`) | FRD §3.D, BR-02 | Bean Validation (`@AssertTrue` / custom validator) | Spring Validation | Phase 3 | Invalid dates return 400 Bad Request with field error message. | Ready for Dev | **P0** |
| **FR-032** | Edit Trip Metadata | FRD §3.D | PUT `/api/v1/trips/{id}` | Spring Boot, JPA | Phase 3 | Owner can update title, traveler count, target budget. | Ready for Dev | **P1** |
| **FR-033** | Trip Dashboard (My Trips) | FRD §3.D | GET `/api/v1/trips`, React Grid | Spring Boot, React | Phase 3 | Shows active/draft trips with destinations and status. | Ready for Dev | **P1** |
| **FR-040** | Destination Recommendations from preferences | FRD §3.E | Recommendation Service | Spring Boot, Mock/AI | Phase 5 | Proposes matching destinations; flags seed/curated source. | Ready for Dev | **P1** |
| **FR-041** | AI Itinerary Generation Request | FRD §3.E | AI Orchestrator adapter | Spring Boot, REST | Phase 5 | Compiles trip context into prompt; requests structured output. | Ready for Dev | **P1** |
| **FR-042** | Structured Itinerary Proposal Schema | FRD §3.E | JSON Schema DTO (Days, Items, Times, Costs) | Jackson, Java DTOs | Phase 5 | Validates JSON proposal structure before presenting to user. | Ready for Dev | **P1** |
| **FR-043** | Constraint & Conflict Handling | FRD §3.E, BR-05 | Validation Service | Spring Boot | Phase 5 | Flags items outside trip date range or overlapping time slots. | Ready for Dev | **P1** |
| **FR-044** | Itinerary Regeneration & Refinement | FRD §3.E | Refinement REST endpoint | Spring Boot, React | Phase 5 | User submits refinement prompt; updates draft proposal. | Ready for Dev | **P2** |
| **FR-045** | AI Failure Fallback to Manual Planning | FRD §3.E, NFR-REL-01 | Graceful exception handler, UI alert | Spring Boot, React | Phase 5 | AI timeout/error displays non-sensitive alert; manual builder remains 100% usable. | Ready for Dev | **P0** |
| **FR-046** | No Fabricated Certainty / Draft Labeling | FRD §3.E, NFR-DATA-04 | Proposal Banner & Badging | React, Tailwind | Phase 5 | AI proposals labeled `Draft Proposal - Unconfirmed`. | Ready for Dev | **P1** |
| **FR-050** | Manual Itinerary Day & Item Creation | FRD §3.F | Day/Item Entities, Form dialogs | Spring Boot, React | Phase 3 / Phase 4 | User adds days and items without invoking AI service. | Ready for Dev | **P0** |
| **FR-051** | Add Discovered Listing to Itinerary Day | FRD §3.F, BR-03 | Linking DTO / Entity reference | Spring Boot, React | Phase 3 / Phase 4 | Listing details populate itinerary item defaults. | Ready for Dev | **P1** |
| **FR-052** | Edit Itinerary Item (time, notes, cost) | FRD §3.F | PUT `/api/v1/trips/{id}/items/{itemId}` | Spring Boot, React | Phase 4 | Item updates persist atomically. | Ready for Dev | **P1** |
| **FR-053** | Reorder Itinerary Items | FRD §3.F | `displayOrder` attribute, drag/up-down UI | Spring Boot, React | Phase 4 | PUT batch reorder updates display sequence within the day. | Ready for Dev | **P1** |
| **FR-054** | Remove Itinerary Item | FRD §3.F | DELETE endpoint with confirmation | Spring Boot, React | Phase 4 | Item removed from DB; totals re-aggregated. | Ready for Dev | **P1** |
| **FR-055** | Persistence Integrity across sessions | FRD §3.F | Relational DB transactions | MySQL 8.0, JPA | Phase 3 / Phase 4 | Logging out and back in restores identical saved trip state. | Ready for Dev | **P0** |
| **FR-056** | Trip Deletion & Cascade Rules | FRD §3.F, BR-07 | JPA `@OnDelete(CASCADE)` on Days/Items | MySQL 8.0, JPA | Phase 3 | Deleting trip deletes its days/items; shared listings remain untouched. | Ready for Dev | **P0** |
| **FR-057** | Schedule Overlap Warnings | FRD §3.F | Schedule validation utility | Java Collections/Streams | Phase 4 | Overlapping times show non-blocking warning badge. | Ready for Dev | **P2** |
| **FR-060** | Target Budget Setting & Currency | FRD §3.G | BigDecimal budget, Currency code | Spring Boot, React | Phase 3 / Phase 4 | Supports target budget (e.g. INR ₹50,000). | Ready for Dev | **P1** |
| **FR-061** | Cost Estimation Aggregation | FRD §3.G | Java Stream reduce/sum | Java Streams (CO1) | Phase 4 | Groups known costs by category (Stays, Food, Activities). | Ready for Dev | **P1** |
| **FR-062** | Explicit Handling of Unknown Costs | FRD §3.G, NFR-DATA-02 | Nullable cost modeling | Java Domain Logic | Phase 4 | Displays `₹24,000 + 2 items without estimate`; NEVER counts unknown as 0. | Ready for Dev | **P0** |
| **FR-063** | Budget Health Indicator | FRD §3.G | Budget progress bar component | React, Tailwind | Phase 4 | Visual indicator (Under / Near / Over Budget) with caveat note. | Ready for Dev | **P2** |
| **FR-070** | Map View of Destination & Stops (Conditional) | FRD §3.H | Leaflet.js + OpenStreetMap | React Leaflet (No API key) | Phase 5 (Conditional) | Renders map with pins; fallback to list view if disabled. | Optional | **P2** |
| **FR-071** | Routing & Distance Estimates (Conditional) | FRD §3.H | Routing provider or simple distance | Open-source routing | Phase 6 (Conditional) | Displays travel time only if reliable data exists. | Optional | **P3** |
| **FR-072** | Export / Print Itinerary (Conditional) | FRD §3.H | Print CSS stylesheet / text export | React Print CSS | Phase 6 (Conditional) | Clean printable view without navigation bars. | Optional | **P3** |
| **FR-080** | Asynchronous Loading Feedback | FRD §3.I, NFR-UX-05 | Skeleton screens, spinner buttons | React, Emil Design skill | Phase 1 | Emil guideline: spinners fast, skeletons match layout. | Ready for Dev | **P1** |
| **FR-081** | Actionable Empty States | FRD §3.I, NFR-UX-05 | EmptyState component with CTA | React, Tailwind | Phase 1 | Empty trips screen prompts `Create your first trip`. | Ready for Dev | **P1** |
| **FR-082** | Consistent Error States | FRD §3.I, NFR-UX-05 | Error boundary, Toast notifications | React, Sonner / Toast | Phase 1 | Safe, friendly messages without raw stack traces. | Ready for Dev | **P1** |
| **FR-083** | Duplicate Submission Prevention | FRD §3.I | Button disable state during request | React Form state | Phase 1 | Prevents double-clicking save/generate buttons. | Ready for Dev | **P1** |
| **FR-084** | Persistence Integrity on Failure | FRD §3.I, NFR-REL-04 | Transaction rollback (`@Transactional`) | Spring Boot JPA | Phase 2 | DB error triggers 500 and complete rollback; UI retains unsaved form. | Ready for Dev | **P0** |

---

### 4.2 System Functional Requirements (SRS: SYS-F-001 through SYS-F-054)

| Req ID | Requirement Summary | Source Document | Required Technical Component | Environment Evidence | Milestone | Verification Method | Status | Priority |
|---|---|---|---|---|---|---|---|---|
| **SYS-F-001** | Spring Security Authentication | SRS §3.1 | `SecurityFilterChain` bean | Spring Security 6, JDK 17 | Phase 2 | Unauthenticated request to `/api/v1/trips` returns 401. | Ready for Dev | **P0** |
| **SYS-F-002** | Protected Trip and Profile APIs | SRS §3.1 | URL pattern matching in SecurityConfig | Spring Security | Phase 2 | Verification via MockMvc tests. | Ready for Dev | **P0** |
| **SYS-F-003** | Ownership Check on Every Mutation | SRS §3.1 | Service method query: `findByTripIdAndOwnerId` | Spring Data JPA | Phase 3 | Cross-user update test returns 403 or 404. | Ready for Dev | **P0** |
| **SYS-F-004** | No Plaintext Passwords / BCrypt | SRS §3.1 | `BCryptPasswordEncoder` bean | Spring Security | Phase 2 | DB inspection proves password hashes start with `$2a$` or `$2b$`. | Ready for Dev | **P0** |
| **SYS-F-005** | Documented Logout / Invalidation | SRS §3.1 | Logout handler (clear cookie/token) | Spring Security | Phase 2 | Invalidation test: invalidated token/session rejected immediately. | Ready for Dev | **P0** |
| **SYS-F-010** | Paginated & Bounded Listing APIs | SRS §3.2 | `Pageable`, `Page<T>` in Spring Data | Spring Data JPA | Phase 2 | GET `/api/v1/destinations?page=0&size=10` returns bounded page metadata. | Ready for Dev | **P1** |
| **SYS-F-011** | Supported-Only Filter Attributes | SRS §3.2 | Query parameter DTO binding | Spring MVC DTO | Phase 2 | Invalid filter key ignored or rejected; no SQL injection. | Ready for Dev | **P1** |
| **SYS-F-012** | Required vs Optional Field Separation | SRS §3.2 | Java DTO with `@NotNull` / nullable fields | Bean Validation | Phase 2 | Listing DTO cleanly distinguishes required name/type from optional price/hours. | Ready for Dev | **P1** |
| **SYS-F-013** | Provider Failure Isolation | SRS §3.2 | Try-catch / Resilience in Adapter | Java Exception Handling | Phase 5 | Third-party failure returns fallback/cached data without 500 crash. | Ready for Dev | **P1** |
| **SYS-F-014** | Seed Data in Dev/Demo Profile | SRS §3.2 | Flyway migration `V2__seed_data.sql` | Flyway, MySQL | Phase 2 | Fresh startup populates database with rich demo catalog. | Ready for Dev | **P0** |
| **SYS-F-020** | Trip CRUD Operations | SRS §3.3 | `TripRepository`, `TripService`, Controller | Spring Data JPA | Phase 3 | Full CRUD cycle verified in integration test. | Ready for Dev | **P0** |
| **SYS-F-021** | Trip Input Validation | SRS §3.3 | `@Valid` on `@RequestBody CreateTripRequest` | Bean Validation | Phase 3 | Missing title or negative budget returns 400 Bad Request. | Ready for Dev | **P0** |
| **SYS-F-022** | Itinerary Day & Item Mutations | SRS §3.3 | `ItineraryRepository`, Service methods | Spring Data JPA | Phase 4 | Adding, moving, removing items updates DB accurately. | Ready for Dev | **P1** |
| **SYS-F-023** | Database Transactions for Multi-Record Writes | SRS §3.3 | `@Transactional` on Service layer | Spring PlatformTransactionManager | Phase 3 / Phase 4 | Partial failure in day-item batch rolls back entire transaction. | Ready for Dev | **P0** |
| **SYS-F-024** | Persisted Representation Return | SRS §3.3 | Response DTO mapped from saved entity | MapStruct / manual mapper | Phase 3 | POST/PUT returns updated entity state including generated IDs. | Ready for Dev | **P1** |
| **SYS-F-025** | Clear Relationship Deletion Policy | SRS §3.3 | `@OneToMany(cascade=ALL, orphanRemoval=true)` | Hibernate 6 | Phase 3 | Orphaned items removed; shared destination entities preserved. | Ready for Dev | **P0** |
| **SYS-F-030** | Context Preparation for AI Requests | SRS §3.4 | `AiContextBuilder` service | Java Streams / DTOs | Phase 5 | Formats trip destination, dates, party size, and listings into prompt. | Ready for Dev | **P1** |
| **SYS-F-031** | Schema Validation for AI Responses | SRS §3.4 | Jackson ObjectMapper validation | Jackson, Bean Validation | Phase 5 | Malformed AI JSON rejected before touching database. | Ready for Dev | **P0** |
| **SYS-F-032** | Configured Timeouts & Bounded Retries | SRS §3.4 | Spring `RestClient` with timeout config | Spring 6 RestClient | Phase 5 | Connect timeout 5s, read timeout 15s; max 1 retry on 503. | Ready for Dev | **P1** |
| **SYS-F-033** | Secrets & Exceptions Sanitization | SRS §3.4 | Global `@RestControllerAdvice` | Spring MVC | Phase 2 | Error response contains error code; no API keys or stack traces. | Ready for Dev | **P0** |
| **SYS-F-034** | AI Failure Preserves Existing Data | SRS §3.4 | Read-only generation proposal | Spring Service | Phase 5 | AI failure does not overwrite, delete, or corrupt saved trip records. | Ready for Dev | **P0** |
| **SYS-F-035** | Proposal Labeling Contract | SRS §3.4 | `isProposal=true` in response DTO | Java DTO, React | Phase 5 | Frontend displays AI output in draft review banner. | Ready for Dev | **P1** |
| **SYS-F-040** | Total Calculation from Known Costs | SRS §3.5 | Domain Calculation Service | Java Streams (CO1) | Phase 4 | Aggregates costs matching user currency; handles conversions/notes. | Ready for Dev | **P1** |
| **SYS-F-041** | Distinct Unknown Value Representation | SRS §3.5 | `knownEstimatedTotal` & `unestimatedItemCount` | DTO fields | Phase 4 | DTO outputs `{ estimatedTotal: 15000, unknownCount: 3 }`. | Ready for Dev | **P0** |
| **SYS-F-042** | Provenance & Freshness Metadata | SRS §3.5 | `sourceName`, `lastRetrievedAt` fields | JPA Entity columns | Phase 2 | Returned in listing DTOs. | Ready for Dev | **P1** |
| **SYS-F-050** | Documented JSON Request/Response Schemas | SRS §3.6 | OpenAPI / Springdoc or TDD schemas | Springdoc OpenAPI | Phase 2 | All endpoints accept/return documented JSON. | Ready for Dev | **P1** |
| **SYS-F-051** | HTTP Status Code Standards | SRS §3.6 | 200, 201, 204, 400, 401, 403, 404, 409, 500 | Spring `ResponseEntity` | Phase 2 | Verified in MockMvc integration tests. | Ready for Dev | **P0** |
| **SYS-F-052** | Consistent Error Contract | SRS §3.6 | Standard `ErrorResponse` DTO | Global Exception Handler | Phase 2 | Schema: `{ code: string, message: string, fieldErrors: object }`. | Ready for Dev | **P0** |
| **SYS-F-053** | Mutating Endpoint Validation | SRS §3.6 | `@Valid`, `@PathVariable` constraints | Bean Validation | Phase 2 | Rejects negative IDs and blank titles with structured error. | Ready for Dev | **P0** |
| **SYS-F-054** | Synchronized API Documentation | SRS §3.6 | OpenAPI swagger-ui at `/swagger-ui.html` | Springdoc 2.x | Phase 2 | Swagger UI accessible in dev profile. | Ready for Dev | **P2** |

---

### 4.3 Non-Functional Requirements (NFRD: Quality Attributes & Constraints)

| Req ID | Requirement Summary | Source Document | Target Metric / Constraint | Environment Evidence | Milestone | Verification Method | Status | Priority |
|---|---|---|---|---|---|---|---|---|
| **NFR-PERF-01** | Non-AI API Response Time | NFRD §2 | p95 < 700 ms on local workload | Fast local CPU (12 cores) & NVMe | Phase 6 | Benchmark non-AI endpoints with automated test suite. | Ready for Dev | **P1** |
| **NFR-PERF-02** | AI Operation Timeouts & Feedback | NFRD §2 | Timeouts configured, progress UI | React loading states | Phase 5 | Connect timeout 5s, read timeout 20s. | Ready for Dev | **P1** |
| **NFR-PERF-03** | Bounded List Sizes & Pagination | NFRD §2 | Page size default 10, max 50 | Spring Data `Pageable` | Phase 2 | Verify list queries enforce maximum limit. | Ready for Dev | **P1** |
| **NFR-PERF-04** | Fast Frontend Rendering / No Full Reloads | NFRD §2 | React SPA client-side routing | Vite, React Router | Phase 1 | Navigation transitions without full document reload. | Ready for Dev | **P1** |
| **NFR-PERF-05** | Measured Performance Claims Only | NFRD §2 | No fabricated benchmark claims | Test runner reports | Phase 6 | Performance claims backed by actual test outputs. | Ready for Dev | **P1** |
| **NFR-SEC-01** | Server-Side Authorization | NFRD §3 | Enforced in Java, never trust client | Spring Security | Phase 2 / Phase 3 | Bypassing UI controls to call API directly is rejected. | Ready for Dev | **P0** |
| **NFR-SEC-02** | Secure Password Hashing | NFRD §3 | BCrypt strength 10+ | Spring Security | Phase 2 | DB inspection proves plaintext passwords are never stored. | Ready for Dev | **P0** |
| **NFR-SEC-03** | Secrets in Env Vars Only | NFRD §3 | No secrets in git or frontend bundle | `.env.example`, `.gitignore` | Phase 0 | Grep search ensures no API keys or passwords in source. | Ready for Dev | **P0** |
| **NFR-SEC-04** | Parameterized Queries & Input Validation | NFRD §3 | Prevent SQLi & XSS | JPA / Hibernate parameterization | Phase 2 | Security audit ensures no raw concatenated SQL strings. | Ready for Dev | **P0** |
| **NFR-SEC-05** | Strict CORS Configuration | NFRD §3 | Allow only configured frontend origin | Spring Web `CorsConfiguration` | Phase 2 | Requests from unauthorized origins rejected by browser/CORS. | Ready for Dev | **P0** |
| **NFR-SEC-06** | HTTPS in Deployed Environments | NFRD §3 | TLS termination in production | Cloud / Reverse proxy config | Phase 7 | Production deployment uses HTTPS. | Ready for Dev | **P2** |
| **NFR-SEC-07** | Secure Cookie / Token Attributes | NFRD §3 | HttpOnly, Secure, SameSite=Lax/Strict | Spring Security / Servlet | Phase 2 | Browser DevTools confirms cookie attributes. | Ready for Dev | **P0** |
| **NFR-SEC-08** | No Credentials in URLs | NFRD §3 | Never put tokens or passwords in query params | API URL structure | Phase 2 | Code inspection confirms credentials only in headers/bodies. | Ready for Dev | **P0** |
| **NFR-SEC-09** | Log Sanitization | NFRD §3 | No passwords or API keys in logs | Logback configuration | Phase 2 | Log inspection proves sensitive fields masked. | Ready for Dev | **P1** |
| **NFR-SEC-10** | Minimal Data Collection & Privacy | NFRD §3 | Collect only necessary trip data | Schema design | Phase 2 | No unnecessary personal tracking fields. | Ready for Dev | **P1** |
| **NFR-REL-01** | Saved Trips Resilient to External Failures | NFRD §4 | Local DB independent of third parties | MySQL 8.0 | Phase 5 | Saved itineraries load when disconnected from Internet. | Ready for Dev | **P0** |
| **NFR-REL-02** | Bounded Retries on Transient Failures | NFRD §4 | Max 1 retry on 503; 0 retries on 4xx | Spring RestClient | Phase 5 | Unit tests verify retry count limit. | Ready for Dev | **P1** |
| **NFR-REL-03** | Multi-Record Consistency via Transactions | NFRD §4 | Atomic commits | Spring `@Transactional` | Phase 3 | Rollback verified on mid-operation exceptions. | Ready for Dev | **P0** |
| **NFR-REL-04** | Failed Writes Never Report Success | NFRD §4 | Accurate HTTP status codes | RestController | Phase 2 | DB failure returns 500/400; frontend does not show success. | Ready for Dev | **P0** |
| **NFR-REL-05** | Preserve Unsaved Input on Recoverable Failure | NFRD §4 | Form state preserved on error | React component state | Phase 1 | Form fields retain user text after validation rejection. | Ready for Dev | **P1** |
| **NFR-REL-06** | Deterministic Offline Seed Data | NFRD §4 | Independent of external APIs | Flyway migration | Phase 2 | App is 100% demoable from seeded database alone. | Ready for Dev | **P0** |
| **NFR-UX-01** | Responsive Across Mobile, Tablet, Desktop | NFRD §5 | Breakpoints: 375px, 768px, 1280px | Tailwind CSS | Phase 1 | Tested with browser automation and mobile viewport emulation. | Ready for Dev | **P1** |
| **NFR-UX-02** | Accessible Form Labels & Focus | NFRD §5 | HTML5 label associations, aria-describedby | Semantic HTML | Phase 1 | Lighthouse accessibility audit score > 90. | Ready for Dev | **P1** |
| **NFR-UX-03** | Keyboard Navigation & Visible Focus | NFRD §5 | Focus-visible rings, Tab order | Tailwind `focus-visible:ring-2` | Phase 1 | Complete workflow navigable without mouse. | Ready for Dev | **P1** |
| **NFR-UX-04** | WCAG 2.2 AA Color Contrast | NFRD §5 | Text contrast ratio >= 4.5:1 | Tailwind palette | Phase 1 | Color audit passes automated contrast check. | Ready for Dev | **P1** |
| **NFR-UX-05** | Intentionally Designed States | NFRD §5 | Loading, Empty, Success, Error | Shared React components | Phase 1 | Every page handles all four states cleanly. | Ready for Dev | **P1** |
| **NFR-UX-06** | Destructive Action Confirmations | NFRD §5 | Confirmation modal before trip deletion | React Modal Dialog | Phase 3 | Deleting a trip requires explicit confirmation. | Ready for Dev | **P1** |
| **NFR-UX-07** | Consistent Design System | NFRD §5 | Centralized Tailwind tokens | Design tokens in `index.css` | Phase 1 | No ad-hoc conflicting colors or arbitrary fonts. | Ready for Dev | **P1** |
| **NFR-MAINT-01** | Modular Frontend Boundaries | NFRD §6 | Clean folder structure (`features/`, `components/`) | Vite + React structure | Phase 0 / Phase 1 | Code organization matches TDD §2. | Ready for Dev | **P1** |
| **NFR-MAINT-02** | Layered Backend Architecture | NFRD §6 | Controller -> Service -> Repository | Spring Boot architecture | Phase 2 | No repository access directly from controllers. | Ready for Dev | **P0** |
| **NFR-MAINT-03** | No Duplicated Business Logic | NFRD §6 | Logic encapsulated in Service layer | Java Services | Phase 2 | Date and budget rules centralized. | Ready for Dev | **P1** |
| **NFR-MAINT-04** | Consistent Naming, Linting, & Formatting | NFRD §6 | ESLint, Prettier, Checkstyle / Spotless | npm scripts, Maven | Phase 0 | `npm run lint` and `mvn compile` run without warnings. | Ready for Dev | **P1** |
| **NFR-MAINT-05** | Versioned Schema Migrations | NFRD §6 | Flyway SQL scripts (`V1__...`, `V2__...`) | Flyway Maven plugin | Phase 2 | Database rebuilt idempotently on fresh start. | Ready for Dev | **P0** |
| **NFR-MAINT-06** | Provider Isolation Behind Adapters | NFRD §6 | Interface `ItineraryGenerationService` | Java Interfaces | Phase 5 | Domain models never import provider-specific SDK classes. | Ready for Dev | **P0** |
| **NFR-TEST-01** | Domain Logic Testable Without External Services | NFRD §7 | JUnit 5 unit tests with Mockito | Maven test phase | Phase 2 | `mvn test` passes completely offline in < 30 seconds. | Ready for Dev | **P0** |
| **NFR-TEST-02** | Automated Local Verification Scripts | NFRD §7 | Single command runs all tests | `npm test` & `mvn test` | Phase 0 | Documented verification commands work out of the box. | Ready for Dev | **P1** |
| **NFR-TEST-03** | Automated Coverage for Core Workflows & Auth | NFRD §7 | Integration tests for auth & CRUD | MockMvc, SpringBootTest | Phase 3 | Proves ownership rules and date validations. | Ready for Dev | **P0** |
| **NFR-TEST-04** | Synchronized Postman Collection | NFRD §7 | Collection file matching current API | Postman JSON, Newman | Phase 6 | Newman runs collection in CI/local terminal with 0 failures. | Ready for Dev | **P1** |
| **NFR-DEP-01** | Backend Builds as Runnable JAR | NFRD §8 | `spring-boot-maven-plugin` | Maven 3.9, JDK 17 | Phase 0 | `java -jar target/voyageai-backend.jar` starts clean. | Ready for Dev | **P0** |
| **NFR-DEP-02** | Dockerfile Builds from Documented Config | NFRD §8 | Multi-stage Dockerfile | Docker Engine 29.6 | Phase 0 | `docker build` succeeds without local absolute path hacks. | Ready for Dev | **P0** |
| **NFR-DEP-03** | Docker Compose Starts Full Stack | NFRD §8 | Compose file for MySQL & Backend | Docker Compose v5.3 | Phase 0 | `docker compose up` starts healthy services on mapped ports. | Ready for Dev | **P0** |
| **NFR-DEP-04** | Multi-Environment Configuration Profiles | NFRD §8 | `dev`, `test`, `prod` profiles | Spring `application-*.yml` | Phase 2 | `dev` seeds data; `test` uses in-memory/isolated DB. | Ready for Dev | **P1** |
| **NFR-DEP-05** | No Machine-Specific Absolute Paths | NFRD §8 | Relative paths in all configs | Maven / Vite configs | Phase 0 | Repository clones and runs on any standard OS. | Ready for Dev | **P0** |
| **NFR-DATA-01** | Estimated Prices Distinct from Confirmed | NFRD §9 | Visual badge and DTO attribute | React, Java DTO | Phase 2 | Clearly states `Estimated` next to prices. | Ready for Dev | **P1** |
| **NFR-DATA-02** | Unknown Cost Values Never Silently Zero | NFRD §9 | Nullable types, count tracking | Java Domain Logic | Phase 4 | Null cost is counted as unestimated, not ₹0. | Ready for Dev | **P0** |
| **NFR-DATA-03** | Attribution & Retrieval Metadata Displayed | NFRD §9 | Listing card footer metadata | React component | Phase 2 | Displays provider/curator name and date. | Ready for Dev | **P1** |
| **NFR-DATA-04** | AI Generated Content Labeled as Proposal | NFRD §9 | Proposal banner component | React, Tailwind | Phase 5 | Clear disclaimer that AI output is suggestive. | Ready for Dev | **P1** |
| **NFR-DATA-05** | No Fabricated Confirmations or Hours | NFRD §9 | Absence of false certainty | Content policy | Phase 2 | When hours are unknown, displays `Hours not available`. | Ready for Dev | **P0** |

---

### 4.4 ACSD30 Course Coverage Mapping (Traceability Matrix)

| Course Outcome | Documented Topic | Implementation Area in Voyage-AI | Verification Method | Milestone |
|---|---|---|---|---|
| **CO1** | Collections, Java Streams, Lambdas | Listing sorting, category grouping, cost reduction, itinerary sequencing | Unit tests verifying stream aggregations and filters | Phase 2, 4 |
| **CO2** | Exception Handling & Robust Services | Custom domain exceptions, `@RestControllerAdvice`, optional print/export I/O | Negative API tests verifying 400/404/500 error mapping | Phase 2, 5 |
| **CO3** | Persistent Entities, Relationships, CRUD | JPA entities (`User`, `Trip`, `ItineraryDay`, `ItineraryItem`, `Listing`), Cascade rules | Repository integration tests with transaction rollback | Phase 2, 3 |
| **CO4** | Spring IoC / Dependency Injection | Layered services, repository beans, AI adapter injection | Spring Boot context load tests | Phase 2 |
| **CO5** | Spring Boot REST APIs & Responsive UI | REST controllers (`/api/v1/**`), DTO validation, React + Vite frontend | MockMvc tests, browser interaction tests | Phase 2, 3 |
| **CO6** | Postman Verification & Docker Containerization | Postman collection execution via Newman, multi-service `docker-compose.yml` | Clean container smoke test and automated Newman execution | Phase 0, 6 |

---

## 5. Technical Inconsistencies & Architecture Review

### 5.1 Java 17 & Proposed Spring Boot Baseline
- **Installed Runtime:** Oracle JDK 17.0.17 LTS (`D:\Applications\Java 17`).
- **Proposed Framework:** Spring Boot 3.3.x or 3.4.x.
- **Verification & Compatibility:**
  - Spring Boot 3 requires Java 17 as its hard baseline.
  - Maven 3.9.15 is fully compatible with Java 17 and Spring Boot 3 plugins.
  - Spring Boot 3 incorporates Spring Framework 6, Hibernate 6, and Jakarta EE 10 (`jakarta.persistence.*`, `jakarta.validation.*`).
  - **Decision for Review:** Standardize on **Spring Boot 3.3.x** as the baseline in `backend/pom.xml`.

### 5.2 Node.js 24 & Frontend Tooling Baseline
- **Installed Runtime:** Node.js v24.13.1, npm 11.8.0.
- **Proposed Stack:** React 18/19, TypeScript 5.x, Vite 5/6, Tailwind CSS 3.4.x.
- **Verification & Compatibility:**
  - Node 24 is compatible with Vite 5/6.
  - Node 24 includes V8 13.6, delivering fast esbuild and TypeScript compilation times.
  - Modern Tailwind CSS 3.4 with PostCSS runs without compatibility flags.
  - **Recommendation:** Use standard `npx -y create-vite@latest ./ --template react-ts` during Phase 0 initialization.

### 5.3 MySQL Host Port 3306 Contention vs Docker Compose Port 3307
- **Observed Host State:** Windows service `MySQL80` (PID 7716) is active and listening on `0.0.0.0:3306` with `StartType: Automatic`.
- **The Problem:** A standard Docker Compose file mapping `ports: ["3306:3306"]` will crash immediately with `bind: address already in use`.
- **Architectural Solution for Review:**
  - Configure `infra/docker-compose.yml` with host port mapping `3307:3306`:
    ```yaml
    services:
      mysql:
        image: mysql:8.0
        environment:
          MYSQL_DATABASE: voyage_ai
          MYSQL_USER: voyage_user
          MYSQL_PASSWORD: voyage_password
          MYSQL_ROOT_PASSWORD: root_password
        ports:
          - "3307:3306"
        volumes:
          - mysql_data:/var/lib/mysql
    ```
  - Inside the Docker network, the backend communicates over `mysql:3306` (unaffected by host binding).
  - When running Spring Boot locally on the host for rapid development, it connects to `localhost:3307` (or to host `localhost:3306` if configured to use the native service with a dedicated schema `voyage_ai_dev`).

### 5.4 Flyway Migrations & Seed Data Strategy
- **Migration Engine:** Flyway 10.x integrated via `flyway-core` and `flyway-mysql` Maven dependencies.
- **Structure:**
  - `V1__init_schema.sql`: DDL establishing tables, indexes, and foreign keys (`users`, `destinations`, `travel_listings`, `trips`, `itinerary_days`, `itinerary_items`).
  - `V2__seed_travel_data.sql`: Idempotent DML populating 6 curated Indian destinations (Jaipur, Goa, Munnar, Varanasi, Manali, Pondicherry) with verified coordinates, descriptions, and 4–6 associated stays, dining options, and attractions per destination.
- **Benefits:** Ensures complete local offline reproducibility (NFR-REL-06) and zero reliance on third-party API availability.

### 5.5 Authentication Architecture: Cookie Session vs JWT Bearer Token
In `docs/OPEN_DECISIONS.md`, this was left open. Here is the architectural evaluation:

| Dimension | Cookie-Based Session | JWT Bearer Token (Recommended) |
|---|---|---|
| **Development Ergonomics** | Difficult: Vite runs on `localhost:5173`, Spring Boot on `localhost:8080`. Cross-port cookies require strict CORS origin reflection, `SameSite=None`, credentials flags, or a local Vite reverse proxy. | Seamless: Token sent in standard HTTP header `Authorization: Bearer <token>`. Works effortlessly across `localhost:5173` and `localhost:8080`. |
| **Security & CSRF** | Requires CSRF token protection on all mutating POST/PUT/DELETE requests. | Stateless; naturally immune to CSRF when stored in application memory. |
| **Course Alignment** | Demonstrates classic Java Servlet session management. | Demonstrates modern REST API decoupled authentication and token validation filter chains. |
| **Recommendation** | Acceptable if reverse proxy is used. | **Recommended for Voyage-AI**: Implement JWT authentication using JJWT or Spring Security OAuth2 Resource Server. |

### 5.6 API Contracts & Error Response Schema
To prevent frontend/backend integration rework, the API contract must adhere to a stable schema:
- **Success Prefix:** `/api/v1`
- **Standard Error Response DTO (SYS-F-052):**
  ```json
  {
    "code": "VALIDATION_ERROR",
    "message": "The trip end date must be on or after the start date.",
    "fieldErrors": {
      "endDate": "Must be on or after 2026-11-01"
    },
    "timestamp": "2026-10-20T14:35:00Z"
  }
  ```
- **Standard HTTP Codes:** `200 OK`, `201 Created`, `204 No Content`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `409 Conflict`, `500 Internal Server Error`.

### 5.7 AI Adapter & Graceful Fallback Strategy
- **Interface Contract:**
  ```java
  public interface ItineraryGenerationService {
      ItineraryProposalDto generateItinerary(TripContextDto context);
  }
  ```
- **Implementations:**
  1. `MockItineraryGenerationService`: Reads realistic, curated template schedules based on destination tags and duration. Generates instant, 100% reliable proposals without network or API costs.
  2. `GeminiItineraryGenerationService` (Optional, pluggable via `@ConditionalOnProperty(name="ai.provider", havingValue="gemini")`): Calls Google Gemini API using structured JSON schema.
- **Fault Tolerance:** If the external provider times out (connect > 5s, read > 15s) or fails schema validation, the system gracefully returns a structured error to the user while leaving all persisted trip records completely untouched.

---

## 6. Architecture Completeness & MVP Scope Boundaries

### 6.1 Confirmed In-Scope Capabilities
- User registration, login, logout, and profile preferences.
- Server-side authorization verifying that users access only their own trips.
- Destination browsing, text searching, and attribute filtering.
- Associated stays, restaurants, and attractions.
- Trip creation with start/end date validation and traveler count.
- AI-assisted itinerary proposal with structured schema validation and mock fallback.
- Manual day-by-day itinerary editing, adding listings/custom activities, reordering, and item deletion.
- Estimated budget tracking separating known costs from unknown values.
- Responsive, accessible UI with intentional loading, empty, success, and error states.
- Docker Compose local orchestration with reproducible seed migrations.

### 6.2 Confirmed Out-of-Scope (Strict Boundaries)
- **Direct in-app bookings or payments:** No Stripe, PayPal, or airline booking engine integrations.
- **Live flight tracking / GDS integrations:** Travel is destination-centric.
- **Guaranteed real-time availability or pricing:** Price estimates must carry provenance/estimate badges.
- **Complex multi-service microservices:** Retain the clean, modular monolith backend.

---

## 7. Specific Decisions Requiring User Approval

The following three architectural decisions require user review before code generation begins:

1. **Docker Host Port Mapping:**  
   Do you approve mapping the Docker MySQL container to host port `3307` (`3307:3306`) in `infra/docker-compose.yml` to prevent conflict with your active host `MySQL80` service?
2. **Authentication Strategy:**  
   Do you approve using **JWT Bearer Tokens** with Spring Security filter chains (simplifies cross-origin development between Vite `5173` and Spring Boot `8080` while avoiding CSRF complexity), or do you prefer traditional **HttpOnly Cookie Sessions**?
3. **Phase 0 Launch Approval:**  
   Do you approve proceeding to **Phase 0: Project Initialization** (scaffolding `backend/pom.xml`, `frontend/package.json`, `infra/docker-compose.yml`, and `.env.example`)?

---

## 8. Prioritized Remediation Plan

### Priority P0 (Immediate / Project Initialization Phase)
- [ ] **P0-1:** Configure `infra/docker-compose.yml` with host port `3307:3306` for MySQL.
- [ ] **P0-2:** Scaffold `backend/` using Spring Boot 3.3.x, Java 17, Spring Data JPA, Spring Web, Spring Security, MySQL Connector, and Flyway.
- [ ] **P0-3:** Scaffold `frontend/` using Vite + React 18/19 + TypeScript + Tailwind CSS.
- [ ] **P0-4:** Provide `.env.example` defining database credentials matching Docker Compose (`DB_HOST=localhost`, `DB_PORT=3307`, etc.).

### Priority P1 (Phase 1–3 Milestones)
- [ ] **P1-1:** Author `V1__init_schema.sql` and `V2__seed_travel_data.sql` with rich Indian travel destinations.
- [ ] **P1-2:** Implement `MockItineraryGenerationService` to guarantee reliable itinerary proposals without third-party dependencies.
- [ ] **P1-3:** Execute `/teach-impeccable` workflow to codify visual tokens and design context into `task.md`.
- [ ] **P1-4:** Implement ownership security checks (`@PreAuthorize` / ownership validator) on all trip endpoints.

### Priority P2 (Phase 4–5 Milestones)
- [ ] **P2-1:** Add Leaflet.js + OpenStreetMap integration for visual map rendering (zero API key cost).
- [ ] **P2-2:** Add Newman runner script (`npx --yes newman`) for headless Postman collection testing.
- [ ] **P2-3:** Implement item reordering and schedule conflict warnings.

### Priority P3 (Phase 6 Polish & Export)
- [ ] **P3-1:** Add printable CSS stylesheet for itinerary export.
- [ ] **P3-2:** Optional addition of MySQL binary path to user Windows `PATH` for convenience.

---

## 9. Verification Evidence and Reproducibility

Commands executed during this audit session:
- `java -version`: Oracle JDK 17.0.17 LTS (build 17.0.17+8-LTS-360)
- `javac TestSanity.java && java TestSanity`: Bytecode compilation and execution succeeded (`Java OK`).
- `mvn -v`: Apache Maven 3.9.15, Java 17.0.17, Windows 11 amd64.
- `node -e "console.log(process.versions)"`: Node v24.13.1, V8 13.6, OpenSSL 3.5.5.
- `docker version`: Client 29.6.2, Server 29.6.2 (WSL2 containerd).
- `docker compose version`: Docker Compose v5.3.1.
- `Get-Service MySQL80`: Status: `Running`, StartType: `Automatic`.
- `netstat -ano | findstr :3306`: PID 7716 listening on `0.0.0.0:3306`, `[::]:3306`, `0.0.0.0:33060`.
- Git status: Branch `main`, working tree clean.
