# Voyage-AI — Pre-Implementation Environment Audit & Product Readiness Report

**Document Version:** 1.0  
**Audit Date:** 2026-10-20  
**Auditor:** Antigravity Coding Agent (Pair Programming Environment)  
**Target Project:** Voyage-AI (`g:\Projects\JFS\Voyage-AI`)  
**Verdict:** **READY AFTER MINOR REMEDIATION**

---

## 1. Executive Summary

This pre-implementation audit evaluates the host operating environment against the complete product specification for **Voyage-AI** (PRD, FRD, SRS, NFRD, TDD, UI/UX Spec, Traceability Matrix, Implementation Plan, and Open Decisions).

Voyage-AI is an AI-powered, all-in-one travel discovery and itinerary planning application featuring a **React + TypeScript + Vite** frontend, a **Java 17 + Spring Boot** modular monolith REST backend, and a **MySQL 8.0** persistence layer with Docker Compose support.

### Key Audit Findings
1. **Core Development Tooling is High-Grade & Ready:**
   - **Java/JDK:** Oracle JDK 17.0.17 LTS is active, `$env:JAVA_HOME` is set (`D:\Applications\Java 17`), `javac` and runtime execution pass verification.
   - **Build Tool:** Apache Maven 3.9.15 is installed and verified. (Gradle is absent, but Maven is the primary specified build system).
   - **Node.js & npm:** Node.js v24.13.1 and npm 11.8.0 / npx 11.8.0 are installed and operational.
   - **Git:** Git 2.52.0.windows.1 is active on branch `main` with a clean working tree.
   - **Docker & Docker Compose:** Docker Desktop 29.6.2 (Linux container engine on WSL2 kernel 6.6.87.2) and Docker Compose v5.3.1 are operational.
2. **Critical Host Port Contention (P0 Risk):**
   - A native Windows service `MySQL80` (MySQL Server 8.0.44 Community) is **already running and actively listening on port 3306** (`0.0.0.0:3306`).
   - If Docker Compose attempts to map `3306:3306` on the host, startup will fail with `bind: address already in use`. A host port offset (e.g., `3307:3306`) or dedicated configuration strategy is required.
3. **No External API Keys Configured (P1 / Working by Design):**
   - Zero external AI or travel API keys exist in the environment (`OPENAI_API_KEY`, `GEMINI_API_KEY`, `MAPBOX_ACCESS_TOKEN`, etc. are all unset).
   - This aligns with the product specification: Voyage-AI's MVP is explicitly designed to be developed and demonstrated using a **deterministic mock/seed data provider** without incurring costs or blocking on third-party approvals.
4. **Design Skills Operational:**
   - The **Emil Kowalski Design Engineering skill** (`emil-design-eng`) is installed and verified in `.agents/skills/`.
   - The **Impeccable Design Suite** workflows (`/teach-impeccable`, `/audit`, `/critique`, `/polish`, etc.) are installed and verified in `.agents/workflows/`.
5. **Readiness Verdict:**
   - **READY AFTER MINOR REMEDIATION**.
   - No missing fundamental runtimes or compilers block project setup. Resolving the host MySQL port mapping strategy, establishing the project skeleton (`frontend/`, `backend/`, `infra/`), and executing `/teach-impeccable` will allow Phase 0/Phase 1 implementation to commence smoothly.

---

## 2. Audit Scope, Environment, Date, and Methodology

### 2.1 Audit Scope
- **Operating System & Shell:** Host Windows 11 environment, PowerShell execution.
- **Runtimes & SDKs:** Java Development Kit, Node.js, Python.
- **Build & Dependency Systems:** Maven, Gradle, npm, npx.
- **Data Stores & Services:** Local MySQL services, network listeners, Docker daemon.
- **Containerization:** Docker Desktop, Docker Compose, engine capabilities.
- **API & Testing Tools:** Postman desktop/CLI, curl, JUnit, frontend test runners.
- **Product Specification Integrity:** 10 core documents in `docs/` (`README.md`, `PRD.md`, `FRD.md`, `SRS.md`, `NFRD.md`, `TDD.md`, `UI_UX_SPEC.md`, `TRACEABILITY_MATRIX.md`, `IMPLEMENTATION_PLAN.md`, `OPEN_DECISIONS.md`).
- **Design & Agent Capabilities:** Native agent tools, browser automation, MCP servers, and installed design skills.

### 2.2 System & Host Environment
- **Host OS:** Microsoft Windows 11 Home Single Language (NT 10.0.26300.0, 64-bit).
- **Workspace Location:** `g:\Projects\JFS\Voyage-AI` (Corpus: `saaket2006/Voyage-AI`).
- **Date & Timestamp:** 2026-10-20T14:12:00+05:30.
- **Execution Mode:** Read-only inspection. No source code was generated, no global software installed/uninstalled, no credentials modified, and no third-party network services billed.

---

## 3. Inventory of Tools, Versions, Paths, and Verification Evidence

| Category | Component / Tool | Detected Version | Executable Path / Location | Verification Evidence | Status |
|---|---|---|---|---|---|
| **OS** | Windows 11 64-bit | NT 10.0.26300.0 | N/A | CIM Win32_OperatingSystem verified | **Ready** |
| **JDK** | Oracle Java SE | 17.0.17+8-LTS-360 | `D:\Applications\Java 17\bin\java.exe` (shipped via Oracle javapath: `C:\Program Files\Common Files\Oracle\Java\javapath\java.exe`) | Compiled & executed `TestSanity.java` successfully | **Ready** |
| **Java Compiler** | javac | 17.0.17 | `C:\Program Files\Common Files\Oracle\Java\javapath\javac.exe` | Compiled test class to bytecode without error | **Ready** |
| **Java Home** | `$env:JAVA_HOME` | 17.0.17 | `D:\Applications\Java 17` | Verified present and non-empty | **Ready** |
| **Java Build** | Apache Maven | 3.9.15 | `D:\Applications\Maven\apache-maven-3.9.15\bin\mvn.cmd` | `mvn -v` executed, successfully points to JDK 17.0.17 | **Ready** |
| **Java Build** | Gradle | Absent | Not in PATH | Command not found; Maven is project standard | **Partially Ready** (Not needed) |
| **JavaScript** | Node.js | v24.13.1 | `D:\Applications\NodeJS\node.exe` | `node -e "console.log('Node OK:', process.version)"` passed | **Ready** |
| **Package Mgr** | npm & npx | 11.8.0 | `D:\Applications\NodeJS\npm.ps1`, `npx.ps1` | `npm -v`, `npx -v` passed; global packages listed | **Ready** |
| **Package Mgr** | pnpm, yarn, bun | Absent | Not in PATH | Not required; npm/npx standard for Vite | **N/A** |
| **VCS** | Git | 2.52.0.windows.1 | `C:\Program Files\Git\cmd\git.exe` | `git status` confirmed clean branch `main` | **Ready** |
| **Containers** | Docker Engine | 29.6.2 (build dfc4efb) | `C:\Program Files\Docker\Docker\resources\bin\docker.exe` | `docker info` verified WSL2 Linux daemon active | **Ready** |
| **Containers** | Docker Compose | v5.3.1 | `C:\Program Files\Docker\cli-plugins\docker-compose.exe` | `docker compose version` verified | **Ready** |
| **Local Database** | MySQL Server | 8.0.44 Community | `C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe` | Service `MySQL80` running on PID 7716, port 3306 | **Ready (with port warning)** |
| **Database UI** | MySQL Workbench | 8.0.44 | `C:\Program Files\MySQL\MySQL Workbench 8.0\mysql.exe` | Binary verified | **Ready** |
| **Python** | Python & pip | 3.11.0 / pip 26.1.2 | `D:\Applications\python 3.11\python.exe` | `python --version` verified | **Ready** |
| **API Client** | Postman Desktop | Detected in AppData | `C:\Users\ASUS\AppData\Roaming\Postman` | AppData folder present; CLI / newman not in PATH | **Partially Ready** |

---

## 4. Design-Skill and Coding-Agent Capability Audit

### 4.1 Coding Agent Tooling Verification
- **Terminal Execution:** Full Windows PowerShell execution through `run_command` with synchronous wait and asynchronous background task management (`manage_task`). Tested and functional.
- **File System Operations:** `view_file`, `write_to_file`, `replace_file_content`, `multi_replace_file_content`, `list_dir`, and `grep_search` verified.
- **Browser Automation:** `browser_subagent` and native `chrome-devtools-mcp` tools (click, evaluate_script, navigate_page, take_screenshot, lighthouse_audit) are available.
- **MCP Servers Available:**
  - `chrome-devtools-mcp` (Lazy tools for DOM inspection, CSS review, screenshots, lighthouse audits).
  - `github-mcp-server` (Running in container `gifted_lalande` on port 8082).
  - `cloudrun` & `firebase-mcp-server`.

### 4.2 Emil Kowalski Design Skill (`emil-design-eng`)
- **Location:** `.agents/skills/emil-design-eng/SKILL.md` (Total 675 lines).
- **Inspection Status:** Fully inspected and verified.
- **Invocation Protocol:**
  - When invoked without a specific question, replies with: `> I'm ready to help you build interfaces that feel right, my knowledge comes from Emil Kowalski's design engineering philosophy.`
  - Review table format constraint: When reviewing UI, MUST output a markdown table with `| Before | After | Why |` columns.
  - Core animation rules: UI animations under 300ms, custom easing (`--ease-out: cubic-bezier(0.23, 1, 0.32, 1)`), no `scale(0)` entry (use `scale(0.95)` with opacity), popovers origin-aware, responsive buttons (`transform: scale(0.97)` on `:active`).
  - Sonner principles: developer experience, zero friction, interruptible transitions, spring physics.

### 4.3 Impeccable Design Suite Workflows
- **Location:** `.agents/workflows/` (18 workflows detected).
- **Key Workflows Verified:**
  - `/teach-impeccable`: Scans project, collects UX/brand context, and persists a `## Design Context` block in `task.md`.
  - `/critique`: Evaluates UX hierarchy, typography, visual weight, emotional resonance.
  - `/audit`: Comprehensive check across accessibility (WCAG AA), responsive behavior, performance.
  - `/polish`: Final fit-and-finish pass for alignment, padding, token compliance.
  - `/delight`: Micro-interactions, tactile feedback, empty-state personality.
  - `/harden`: Error boundaries, edge-case data, network dropouts, overflow handling.
  - `/adapt`: Mobile, tablet, desktop responsive adaptations.

---

## 5. Requirements-to-Environment Matrix

| Requirement ID | Specific Documented Requirement | Source Document & Section | Required Capability / Tool | Environment Evidence | Status | Gap and Impact | Recommended Action | Priority |
|---|---|---|---|---|---|---|---|---|
| **REQ-ENV-01** | Java 17+ Spring Boot Backend Runtime | SRS §2.3, TDD §1, §2 | JDK 17+, `java`, `javac` | Oracle JDK 17.0.17 LTS verified at `D:\Applications\Java 17` | **Ready** | None for compilation and execution. | Ensure `pom.xml` sets `<java.version>17</java.version>`. | **P0** |
| **REQ-ENV-02** | Backend Build & Dependency Automation | SRS §2.3, TDD §2 | Maven 3.8+ (or Gradle) | Apache Maven 3.9.15 verified at `D:\Applications\Maven\apache-maven-3.9.15` | **Ready** | Gradle is missing, but Maven is the chosen project baseline. | Use standard Maven directory layout and `mvnw` wrapper. | **P0** |
| **REQ-ENV-03** | Frontend Build & Tooling | SRS §2.3, §4.1, TDD §3 | Node.js (LTS), npm, Vite | Node.js v24.13.1, npm 11.8.0, npx 11.8.0 verified | **Ready** | Node 24 is latest; modern Vite 5/6 works smoothly. | Verify React + Vite template initialization with `npx -y`. | **P0** |
| **REQ-ENV-04** | Relational Database & Engine | SRS §4.3, TDD §10 | MySQL 8.0 server | Windows service `MySQL80` (v8.0.44) running on port 3306 | **Partially Ready** | Port 3306 is occupied on the host by Windows service. Docker Compose mapping `3306:3306` will collide. | Map Docker MySQL to `3307:3306` in `docker-compose.yml`, or connect to local host MySQL. | **P0** |
| **REQ-ENV-05** | Database Client CLI | SRS §4.3, TDD §10 | `mysql.exe` client | Binary exists at `C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe`, but NOT in system PATH | **Partially Ready** | Direct CLI commands like `mysql -u root` fail unless fully qualified path is used. | Use fully qualified path or rely on Spring Data JPA / Flyway migrations. | **P1** |
| **REQ-ENV-06** | Database Schema Migration | SRS §4.3, TDD §10 | Flyway or Liquibase | Flyway proposed in TDD & Open Decisions | **Ready (Project-level)** | Not a global tool; must be defined as Maven plugin/dependency in `pom.xml`. | Add `flyway-core` and `flyway-mysql` to `backend/pom.xml`. | **P0** |
| **REQ-ENV-07** | Containerization & Multi-container Compose | SRS §4.3, §8, TDD §13 | Docker Engine & Docker Compose | Docker Desktop 29.6.2, Compose v5.3.1 active on WSL2 | **Ready** | Existing host port 3306 conflict must be addressed in compose file. | Configure host port mapping `3307:3306` in `infra/docker-compose.yml`. | **P0** |
| **REQ-ENV-08** | Automated API Testing Tooling | SRS §8, §9, Traceability Matrix | Postman / Newman | Postman AppData directory exists; `newman` not in PATH | **Partially Ready** | Automated headless execution of Postman collection requires Newman. | Add `newman` via local `npm install -D newman` or run via `npx --yes newman`. | **P1** |
| **REQ-ENV-09** | AI Orchestration & Adapter Fallback | FR-040–FR-046, SRS §3.4, TDD §8 | LLM API credentials or Mock Adapter | `OPENAI_API_KEY`, `GEMINI_API_KEY` are unset | **Ready (By Specification)** | No active third-party API key; MVP explicitly mandates mock/seed fallback first. | Implement `MockItineraryGenerationService` with realistic seeded JSON proposals. | **P1** |
| **REQ-ENV-010** | Seed Travel Data & Discovery | FR-010–FR-025, SRS §3.2, §5 | Relational seed dataset | MySQL ready, schema models drafted | **Ready** | Curated Indian destinations & listings need to be authored as idempotent SQL/migration scripts. | Author initial Flyway migration `V1__init_schema.sql` and `V2__seed_travel_data.sql`. | **P1** |
| **REQ-ENV-011** | Responsive UI & Design Engineering System | UI/UX Spec §5, §6 | Tailwind CSS, Lucide icons, Design skills | Installed `emil-design-eng` skill and Impeccable workflows | **Ready** | Design tokens need to be codified in `tailwind.config.js` and `index.css`. | Execute `/teach-impeccable` workflow at Phase 1 start. | **P1** |
| **REQ-ENV-012** | Browser Inspection & E2E Testing | NFR-UX-01, NFR-UX-03 | Headless browser / DevTools | `chrome-devtools-mcp` and `browser_subagent` active | **Ready** | Can inspect rendered DOM, verify contrast, simulate mobile viewports, take screenshots. | Use MCP DevTools during UI verification milestones. | **P2** |

---

## 6. Product Scope and Specification Completeness

The documentation provides an exceptionally clear baseline for an ACSD30 full-stack project. Below is an audit of each functional domain against the supplied specifications:

### 6.1 Classification of Product Features

| Domain / Feature | Classification | Document Reference | Completeness Analysis & MVP Boundary |
|---|---|---|---|
| **User Authentication & Authorization** | Explicitly specified | FR-001–FR-005, SYS-F-001–SYS-F-005 | Form-based registration, login, logout, password hashing (BCrypt), owner-only authorization checks on all `/api/v1/trips/**` endpoints. |
| **Destination Discovery** | Explicitly specified | FR-010–FR-014, SYS-F-010–SYS-F-014 | Browse cards, search by name/region, filter by category/season/budget, destination details with associated stays/restaurants/attractions. |
| **Travel Listings (Stays, Dining, Activities)** | Explicitly specified | FR-020–FR-023, SYS-F-010 | Sub-categories linked by foreign key to destination; pricing estimates, hours, amenities, external informational link. |
| **Trip Setup & Management** | Explicitly specified | FR-030–FR-033, SYS-F-020–SYS-F-025 | Create trip with title, dates, traveler count, budget, interests; validate `endDate >= startDate`; My Trips dashboard. |
| **AI Recommendation & Itinerary Proposal** | Explicitly specified | FR-040–FR-046, SYS-F-030–SYS-F-035 | Prompt builder using trip context + listing records; strict schema validation; graceful fallback to manual planning on provider timeout/error. |
| **Manual Itinerary Builder** | Explicitly specified | FR-050–FR-057, SYS-F-022–SYS-F-024 | Multi-day schedule; add items from listings or custom activities; inline editing; drag-and-drop / manual reordering; item deletion. |
| **Estimated Budget Calculator** | Explicitly specified | FR-060–FR-063, SYS-F-040–SYS-F-041 | Sums known costs; **unknown costs are explicitly flagged and never treated as zero**; budget status indicator. |
| **Shortlist / Favorites** | Optional enhancement | FR-024, SRS §5 | Deferrable; does not block the primary trip creation flow. |
| **Interactive Map & Routing** | Optional enhancement | FR-070–FR-071, TDD §14 | Deferrable; conditional on free provider availability (e.g. OpenStreetMap / Leaflet). |
| **Itinerary Export / Print** | Optional enhancement | FR-072, TDD §14 | Deferrable to Phase 4 / Phase 6. |
| **Direct Booking & Payments** | Explicitly Excluded | PRD §8, FR-025, SRS §1.2 | **OUT OF SCOPE**. External informational links only. No booking confirmation or credit card handling. |
| **Live Flight Tracking & Availability** | Explicitly Excluded | PRD §8, SRS §1.2 | **OUT OF SCOPE**. No GDS or real-time flight integrations. |

### 6.2 Boundaries Between Real, Seeded, and Estimated Data
The documentation establishes strict integrity principles:
1. **Never fabricate certainty (FR-046, NFR-DATA-05):** The UI must never display unverified prices, opening hours, or ratings as live facts.
2. **Provenance Labels (FR-014, NFR-DATA-03):** Display badges: `Curated / Demo`, `Estimated Cost`, or `Hours Unavailable`.
3. **Budget Honesty (FR-062, NFR-DATA-02):** If 3 out of 7 itinerary items have unknown costs, the budget summary must state `₹14,500 + 3 unestimated items`, never `₹14,500 total`.

---

## 7. Technical Consistency and Compatibility

### 7.1 Java, Spring Boot, and Maven Compatibility
- **Installed JDK:** 17.0.17 LTS.
- **Maven:** 3.9.15.
- **Spring Boot Baseline:** Spring Boot 3.3.x or 3.4.x.
  - Spring Boot 3.x requires Java 17 as its minimum baseline and runs smoothly on JDK 17.0.17.
  - Maven 3.9.15 is fully compatible with Spring Boot 3 plugins (`spring-boot-maven-plugin`).
- **Conclusion:** 100% compatible.

### 7.2 Frontend Stack Compatibility
- **Installed Node.js:** v24.13.1.
- **Package Manager:** npm 11.8.0.
- **Frontend Stack:** React 18/19 + TypeScript + Vite 5/6 + Tailwind CSS.
- **Compatibility:** Node 24 is compatible with Vite 5.x and 6.x. Vitest and React Testing Library run natively on this runtime.

### 7.3 MySQL Port Conflict & Multi-Environment Strategy
- **Conflict:** Native service `MySQL80` (PID 7716) listens on host port 3306.
- **Docker Compose impact:**
  ```yaml
  # FAILS on this machine:
  ports:
    - "3306:3306"
  # SUCCEEDS on this machine:
  ports:
    - "3307:3306"
  ```
- **Resolution:**
  - Option A (Recommended for Docker): In `infra/docker-compose.yml`, expose MySQL container on host port `3307` (`3307:3306`). The Spring Boot container within the Docker network communicates over `mysql:3306` (unaffected by host binding).
  - Option B (Alternative for local direct dev): Run Spring Boot locally targeting the existing host MySQL service at `localhost:3306` using a dedicated schema `voyage_ai_dev`.

### 7.4 Authentication & Session Strategy
- In `docs/OPEN_DECISIONS.md`, the auth mechanism was left open between **Secure HttpOnly Cookie Sessions** vs **JWT Bearer Tokens**.
- **Assessment:**
  - In an ACSD30 course context, demonstrating Spring Security with **secure HttpOnly session cookies** and CSRF protection is idiomatic for a monolith/same-origin reverse proxy.
  - Alternatively, **JWT stateless tokens** stored in memory / secure storage with an `Authorization: Bearer <token>` header provides clean separation between Vite dev server (`localhost:5173`) and Spring Boot (`localhost:8080`) during development without complex cookie cross-origin domain sharing.
  - Recommendation: Use **JWT authentication with Spring Security Filter Chain** for seamless cross-port local development (`localhost:5173` -> `localhost:8080`).

---

## 8. Missing Tools, Configuration, Integrations, and Decisions

### 8.1 Missing Tooling on Host
1. **`mysql.exe` in PATH:** While MySQL Server is installed at `C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe`, its `bin` folder is not in Windows system `PATH`.
2. **`newman` CLI:** Not installed globally in npm. (Easily run via `npx --yes newman` without modifying global state).
3. **Gradle:** Not installed. (Irrelevant, as Maven is the selected project tool).

### 8.2 Missing Project Configuration (Pending Phase 0 Initialization)
1. **No Project Directories:** `frontend/`, `backend/`, and `infra/` have not yet been created (per strict instructions not to initialize early).
2. **Missing `.env.example`:** Need standard template defining `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `AI_PROVIDER`, etc.
3. **Unselected AI Provider:** No API key configured.

---

## 9. Recommended Free-First Solutions and Alternatives

To ensure zero financial cost, zero vendor lock-in, and reliable local evaluation:

1. **AI Service:**
   - **Primary MVP:** `MockItineraryGenerationService` returning curated, realistic structured JSON responses based on destination and budget.
   - **Free External Option:** Google Gemini Developer API (free tier) or Groq API (free tier with Llama 3 / Mistral), configured via an environment variable (`AI_PROVIDER=gemini|groq|mock`).
2. **Travel Data Source:**
   - **Primary MVP:** Curated seed dataset embedded in database migrations (`V2__seed_travel_data.sql`), covering 6–8 diverse Indian travel destinations (e.g., Jaipur, Goa, Munnar, Varanasi, Manali, Pondicherry) with 4–6 stays, restaurants, and attractions per destination.
   - **External Option:** OpenTripMap API (generous free tier) or Overpass/OpenStreetMap API.
3. **Map & Geolocation:**
   - **Primary MVP:** **Leaflet.js + OpenStreetMap (OSM)** tiles. 100% free, open-source, requires zero API keys or credit card registration.
4. **Automated API Testing:**
   - **Primary MVP:** Newman invoked via `npx --yes newman run docs/postman/VoyageAI_Collection.json` within automated scripts.

---

## 10. Prioritized Remediation Checklist

### Priority P0 (Must be resolved during Phase 0 initialization)
- [ ] **P0-1: Docker Compose MySQL Port Offset:** Set host port to `3307:3306` in `infra/docker-compose.yml` to prevent collision with the running Windows `MySQL80` service on port 3306.
- [ ] **P0-2: Scaffold Clean Repository Structure:** Initialize `frontend/` (Vite + React + TS), `backend/` (Spring Boot 3 + Maven), and `infra/` without reusing reference repository code.
- [ ] **P0-3: Configure Backend Maven Dependencies:** Pin Java 17, Spring Boot 3.3.x, Spring Web, Spring Security, Spring Data JPA, MySQL Connector/J, Flyway, and Lombok in `backend/pom.xml`.
- [ ] **P0-4: Create `.env.example`:** Supply explicit database and JWT defaults matching the Docker Compose configuration.

### Priority P1 (Before Feature Implementation / Phase 1–3)
- [ ] **P1-1: Finalize Auth Topology Decision:** Confirm JWT bearer authentication for seamless cross-origin development between Vite (`localhost:5173`) and Spring Boot (`localhost:8080`).
- [ ] **P1-2: Author Idempotent Seed Migration:** Write `V1__init_schema.sql` and `V2__seed_data.sql` with rich, realistic Indian destinations and listings.
- [ ] **P1-3: Implement AI Mock Adapter:** Build `MockItineraryGenerationService` to guarantee complete itinerary generation and testing without requiring third-party API keys.
- [ ] **P1-4: Run Design Initialization:** Execute `/teach-impeccable` workflow to codify typography, palette tokens, and motion guidelines into `task.md`.

### Priority P2 (Mid-Project / Phase 4–5)
- [ ] **P2-1: Newman Test Runner Integration:** Add `npm run test:api` script to run the Postman collection via `npx --yes newman`.
- [ ] **P2-2: Free Leaflet Map Integration:** Add optional Leaflet map component for destination coordinates and day-trip stops.
- [ ] **P2-3: Schedule Conflict Logic:** Implement time-overlap detection for itinerary items within the same day.

### Priority P3 (Optional Polish / Phase 6)
- [ ] **P3-1: Add `mysql.exe` to Host PATH:** Optional convenience for manual command-line inspection of MySQL.
- [ ] **P3-2: Itinerary PDF/Print Export:** Format a print-friendly CSS view for saved itineraries.

---

## 11. Implementation-Readiness Verdict

# **VERDICT: READY AFTER MINOR REMEDIATION**

### Justification:
The host development environment contains all essential, heavyweight developer tooling in healthy, tested condition:
- Java 17 LTS compiler and runtime verified.
- Apache Maven 3.9.15 verified.
- Node.js v24.13.1 and npm 11.8.0 verified.
- Git 2.52.0 verified.
- Docker Desktop with WSL2 engine and Docker Compose v5.3.1 verified.
- Local MySQL 8.0.44 server verified.
- Required design skills and workflows inspected and operational.

There are **zero missing runtimes or compilers**. The only required remediations are project-level setup configurations:
1. Offsetting the Docker MySQL host port to `3307` (due to active host `MySQL80` service).
2. Generating the standard project skeletons (`backend/`, `frontend/`, `infra/`).
3. Approving the JWT authentication decision.

Once the user approves these minor setup parameters, Phase 0 (Repository & Setup) can begin immediately.

---

## 12. Evidence, Limitations, and Items Unverified

### 12.1 Verification Evidence Summary
- `java -version`: Oracle JDK 17.0.17 LTS (mixed mode, sharing).
- `javac TestSanity.java && java TestSanity`: Output `Java OK`.
- `mvn -v`: Apache Maven 3.9.15, Java 17.0.17, Windows 11 amd64.
- `node -e "..."`: Node OK: v24.13.1.
- `docker info`: Docker 29.6.2, 12 CPUs, 7.46 GiB memory, Linux containerd on WSL2.
- `Get-Service MySQL80`: Running on PID 7716, listening on port 3306.
- `Get-Command git`: `C:\Program Files\Git\cmd\git.exe`, v2.52.0.

### 12.2 Limitations & Unverified Items
- **Local MySQL Root Password:** In adherence to security rules, no connection attempt or password trial was made against the host `MySQL80` database. Credentials remain uninspected.
- **Third-Party AI & Travel API Live Calls:** Skipped intentionally as no API keys are present in the environment and no paid/external requests were authorized.
- **Production Cloud Deployment Target:** Hosting provider (Render, Railway, AWS, Oracle Cloud) remains unselected, as planned in `docs/OPEN_DECISIONS.md`.
