# Voyage-AI — Project Documentation

**Version:** 0.1 · **Status:** Initial baseline for team review

Voyage-AI is an AI-powered, all-in-one travel companion for discovering destinations, exploring stays, restaurants and attractions, and organizing selected options into an editable trip itinerary.

## Documents

1. [PRD — Product Requirements Document](PRD.md)
2. [FRD — Functional Requirements Document](FRD.md)
3. [SRS — Software Requirements Specification](SRS.md)
4. [NFRD — Non-Functional Requirements Document](NFRD.md)
5. [TDD — Technical Design Document](TDD.md)
6. [UI/UX Specification](UI_UX_SPEC.md)
7. [Requirements Traceability Matrix](TRACEABILITY_MATRIX.md)
8. [Implementation Plan](IMPLEMENTATION_PLAN.md)
9. [Open Decisions and Assumptions](OPEN_DECISIONS.md)

## Baseline decisions

- Build from scratch; the attached itinerary-planner repository is inspiration/reference only. No code or architecture reuse is assumed.
- Frontend: React + TypeScript + Vite.
- Backend: Java + Spring Boot REST API.
- Persistence: Spring Data JPA + Hibernate with MySQL.
- Styling: Tailwind CSS with a custom design system.
- Testing: JUnit/Spring Boot Test, frontend tests, Postman.
- Packaging: Docker and Docker Compose.
- MVP focuses on discovery, recommendations, editable/saved itineraries, and estimated budget—not direct bookings or payments.
- Use a modular monolith initially, not premature microservices.
- External data and AI providers are not yet selected. The application must support curated/demo data and clearly label estimates or unavailable live information.

## How to use this pack

Treat these documents as a practical v0.1 baseline. The PRD describes why and what; FRD describes user-visible behavior; SRS describes system requirements and interfaces; NFRD describes quality/security constraints; TDD describes the proposed implementation structure. Resolve items in `OPEN_DECISIONS.md` during implementation planning rather than blocking the entire project on them.
