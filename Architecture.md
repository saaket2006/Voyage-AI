# Voyage-AI — High-Level System Architecture

## 1. Overview & Major Components

Voyage-AI is an AI-powered travel companion connecting destination discovery, accommodation/restaurant/attraction exploration, and personalized itinerary planning. The application uses a decoupled client-server architecture structured as a **modular monolith**:

- **Frontend Client (`frontend/`):** A single-page application built with React, TypeScript, and Vite, utilizing Tailwind CSS for styling. It manages user presentation, client-side routing, discovery browsing, trip manipulation, and interaction feedback.
- **Backend Service (`backend/`):** A Spring Boot REST application (Java 17) organized into modular domain boundaries:
  - `auth` & `security`: Authentication, authorization, and ownership enforcement.
  - `destination` & `listing`: Public discovery search, filters, and catalog details.
  - `trip` & `itinerary`: Private trip setup, day-by-day scheduling, and budget calculations.
  - `integration`: Adapters isolating third-party AI, travel data, and map services.

```text
+-------------------------------------------------------------+
|                      React Web Client                       |
|                   (TypeScript, Vite, SPA)                   |
+-------------------------------------------------------------+
                              |
                              | HTTP(S) JSON /api/v1
                              v
+-------------------------------------------------------------+
|               Spring Boot Modular Monolith                  |
|                                                             |
|  [Security & Auth]  <-->  [Trip & Itinerary Management]      |
|                                     |                       |
|  [Destination & Listing Catalog]   |                       |
|                                     v                       |
|                        [Integration Adapters]               |
+-------------------------------------------------------------+
          |                                  |
          v                                  v
+--------------------+        +------------------------------+
|   MySQL Database   |        | Optional External Services   |
| (Relational Store) |        | - AI Provider (Mock / LLM)   |
|                    |        | - Map/Routing (OSM/Leaflet)  |
+--------------------+        +------------------------------+
```

## 2. Communication and Data Flows

1. **Client-to-Server Flow:** All client operations communicate with the backend via HTTP(S) RESTful JSON endpoints rooted at `/api/v1`.
2. **Discovery Flow:** Public visitors and authenticated users query destinations and listings via paginated read-only endpoints. Data is served directly from the relational database (seeded catalog or synchronized provider data).
3. **Trip & Itinerary Flow:** Authenticated users create and modify private trips and day schedules. Mutations are processed within transactional service boundaries and persisted atomically.
4. **AI Generation Flow:**
   - Client requests itinerary generation for a trip.
   - Backend validates trip constraints and compiles relevant listing context.
   - Backend invokes the AI integration adapter (or deterministic mock service).
   - Generated proposals undergo schema validation and are returned to the client as an unconfirmed draft for user review.

## 3. Primary Data Stores & Persistence

- **Primary Relational Database:** **MySQL 8.0**.
- **Data Access:** Spring Data JPA with Hibernate ORM.
- **Schema Management:** Versioned migrations using Flyway (`db/migration/V*`).
- **Core Entities:** `User`, `Destination`, `TravelListing`, `Trip`, `ItineraryDay`, `ItineraryItem`.

## 4. Authentication & Authorization Boundaries

- **Public Zone:** Destination catalog, travel listings, user registration, and sign-in endpoints.
- **Protected Zone:** User profile, trip creation, trip retrieval, itinerary editing, and AI proposal endpoints.
- **Authorization Enforcement:** Server-side ownership verification ensures users can only read, update, or delete trips they own.
- **Security Boundary:** Stateless/session authentication tokens are validated via Spring Security filter chains. Third-party API keys and database credentials reside exclusively on the server and are never exposed to the client.

## 5. Major External Dependencies & Integrations

- **AI Provider Adapter:** Pluggable adapter supporting structured schema generation with fallback to deterministic mock proposals (e.g., Google Gemini / Groq / OpenAI or local mock).
- **Travel Data Source:** Curated local database seed data as the baseline, with optional external adapter (e.g., OpenTripMap).
- **Geographic & Map Services:** Open-source Leaflet.js with OpenStreetMap tiles (no paid API dependencies).

## 6. Deployment Boundaries

- **Development:** Frontend served via Vite dev server (`http://localhost:5173`); backend served via Spring Boot (`http://localhost:8081`, avoiding host port 8080 collision with AgentService); database hosted via Docker MySQL on port `3307` or local MySQL.
- **Containerization:** Docker Compose manages multi-container local orchestration:
  - `mysql` container (mapped to host port `3307` to avoid port 3306 collision).
  - `backend` container running the compiled runnable Spring Boot JAR (exposed on host port `8081`).
  - Optional containerized frontend reverse-proxy.
