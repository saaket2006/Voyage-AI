# Voyage-AI — Project Documentation

**Version:** 0.2.1 (Phase 2 Verified Baseline) · **Status:** Implementation-Verified Documentation Suite

Voyage-AI is an AI-powered, all-in-one travel companion for discovering destinations, exploring stays, restaurants and attractions, and organizing selected options into an editable trip itinerary with persistent multi-day scheduling, authentic Wikimedia media licensing, and real Google Gemini intelligence.

## Core Specifications & Technical Plans

1. [PRD — Product Requirements Document](PRD.md)
2. [FRD — Functional Requirements Document](FRD.md)
3. [SRS — Software Requirements Specification](SRS.md)
4. [NFRD — Non-Functional Requirements Document](NFRD.md)
5. [TDD — Technical Design Document](TDD.md)
6. [UI/UX Specification](UI_UX_SPEC.md)
7. [Requirements Traceability Matrix](TRACEABILITY_MATRIX.md) *(Verified against 128 automated tests)*
8. [Implementation Plan](IMPLEMENTATION_PLAN.md) *(Phases 0–6 completed and verified)*
9. [Resolved Decisions and Assumptions](OPEN_DECISIONS.md)
10. [Data Authenticity and Provider Plan](DATA_AUTHENTICITY_AND_PROVIDER_PLAN.md)
11. [JWT Authentication Design](JWT_AUTH_DESIGN.md)
12. [Automated Testing Strategy & Verification Guide](TEST_STRATEGY.md)

## Implementation & Verification Reports (`docs/reports/`)

- [Post-Implementation Verification & Final Acceptance Report](reports/POST_IMPLEMENTATION_VERIFICATION.md) *(128/128 tests passed, 100% pass rate)*
- [Verified Implementation Status](reports/implementation-status.md)
- [System Architecture & Technical Design](reports/architecture.md)
- [Testing Strategy & Software Quality Assessment](reports/testing-and-quality.md)
- [Technical Risk Register & Prioritized Engineering Roadmap](reports/risks-and-roadmap.md)
- [Comprehensive Project Report](reports/project-report.md)
- [Engineering Methodology & Workflows](reports/methodology.md)
- [Documentation Suite Index](reports/README.md)
- [Post-Implementation Audit Report](reports/POST_IMPLEMENTATION_AUDIT.md)

## Verified System Architecture

- **Frontend:** React 18 + TypeScript + Vite + Tailwind CSS (`frontend/`) with server-side catalog pagination and CC-licensed media attribution.
- **Backend:** Java 17 LTS + Spring Boot 4.1.x Modular Monolith REST API (`backend/`).
- **Persistence:** Spring Data JPA + Hibernate 7.4.5 + MySQL 8.0 on Docker port `3307` targeting single canonical schema **`voyage`** (host port 3306 untouched; legacy `voyage_ai` inactive backup).
- **Flyway Migrations:** Versioned migrations V0 through V4 (`V0__baseline.sql`, `V1__init_schema.sql`, `V2__seed_travel_data.sql`, `V3__user_profile_and_destination_indexes.sql`, `V4__add_user_roles_and_image_licensing.sql`).
- **Security:** Stateless JWT Bearer tokens, BCrypt hashing, Spring Security 6 filter chain, user roles (`ROLE_USER`, `ROLE_ADMIN`), user profile management (`/api/v1/users/me`), and multi-tenant trip ownership boundaries.
- **Destination Ingestion:** Real Wikipedia summary and Wikimedia Commons Action API extraction (`POST /api/v1/admin/destinations/import` guarded by `ROLE_ADMIN`) with explicit CC licensing (`CC-BY-SA 4.0`, `CC0`, `Public Domain`) and photographer credits.
- **AI Reasoning:** Official Google Gemini API (`gemini-3.5-flash-lite`) with structured error mapping and Zero-PII transmission (verified live: "Capital of India" -> "New Delhi").
- **Weather:** Open-Meteo REST service with coordinate caching, WMO weather codes, and CC-BY 4.0 provenance.
- **Testing:** 128 automated tests across backend unit/integration, frontend Vitest, and Playwright Chromium browser automation (100% pass rate).
- **Packaging & Orchestration:** Multi-container Docker Compose (`infra/docker-compose.yml`) and single-command local developer launcher [`start.bat`](file:///g:/Projects/JFS/Voyage-AI/start.bat).
