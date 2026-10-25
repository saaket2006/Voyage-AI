# Graph Report - Voyage-AI  (2026-10-25)

## Corpus Check
- 247 files · ~326,510 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 3243 nodes · 5058 edges · 212 communities (173 shown, 39 thin omitted)
- Extraction: 88% EXTRACTED · 12% INFERRED · 0% AMBIGUOUS · INFERRED: 612 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `deb29078`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- App.tsx
- devDependencies
- Voyage-AI — Software Requirements Specification (SRS)
- UserApiTest.java
- Expo Animation Recipes
- Core Principles
- Animation Recipes
- Documentation Architect
- Capabilities
- Voyage-AI — Pre-Implementation Audit & Readiness Verification (v1.1)
- Worst-Case Catalog
- Animation Standards Reference
- Core Principles
- Animation Audit Playbook
- Capabilities
- Write Swift
- onboard.md
- GlobalExceptionHandler
- dependencies
- Voyage-AI — Functional Requirements Document (FRD)
- compilerOptions
- Apple Design
- The Fixes
- Prototyping Variants
- Delight Techniques
- API Design Principles Implementation Playbook
- Core Principles
- Implement Animations
- Glossary
- Introduce Color Strategically
- Design Critique
- AuthController
- Animation Performance
- ItineraryDayResponse
- 2. Token Lifecycle & Transport
- Voyage-AI — Product Requirements Document (PRD)
- Voyage-AI — UI/UX Specification
- Finding Animation Opportunities
- Polish Systematically
- compilerOptions
- Voyage-AI — All-in-One Travel Companion
- Implement Adaptations
- Improve Copy Systematically
- Simplify the Design
- Voyage-AI — Technical Design Document (TDD)
- Generate Comprehensive Report
- Amplify the Design
- Optimization Strategy
- Working With Sonner
- Frontend Aesthetics Guidelines
- Refine the Design
- Hardening Dimensions
- The list
- Voyage-AI — Implementation Plan
- Voyage-AI — Non-Functional Requirements Document (NFRD)
- mvnw
- WikimediaSummaryResponse
- Design Engineering
- docs/README.md
- API Design Principles
- Component Building Principles
- Step 2: Ask UX-Focused Questions
- Voyage-AI — High-Level System Architecture
- .get
- The Animation Decision Framework
- clip-path for Animation
- Performance Rules
- Gesture and Drag Interactions
- extract.md
- CorsConfig.java
- VoyageAiApplicationTests.java
- CSS Transform Mastery
- The Sonner Principles (Building Loved Components)
- Spring Animations
- Voyage-AI — Post-Implementation Verification Audit Report
- Core Philosophy
- Debugging Animations
- normalize.md
- DotenvEnvironmentPostProcessor.java
- Voyage-AI Database Migrations Directory
- TravelListing
- vite-env.d.ts
- BasePlaywrightTest
- rules/graphify.md
- workflows/graphify.md
- com.voyageai:voyage-ai-backend
- Voyage-AI — Data Authenticity, Provenance & Academic Provider Plan (v2.1)
- CurrentData
- CurrentWeatherDTO
- WeatherResponse
- DailyForecastDTO
- WeatherProvenanceDTO
- WeatherUnitsDTO
- DatabaseFlywayIntegrationTest
- 4. Execution Commands
- run-all-tests.sh script
- .getWeather
- ApiResponse
- Destination
- scripts
- TripResponse
- OpenMeteoWeatherService
- Voyage-AI — Centralized Automated Testing Workspace
- package.json
- TravelListingResponse
- Voyage-AI — Database Testing Strategy & Isolation
- @eslint/js
- eslint-plugin-react-hooks
- eslint-plugin-react-refresh
- postcss
- tailwindcss
- @testing-library/jest-dom
- @testing-library/react
- @types/node
- typescript-eslint
- vite
- @vitejs/plugin-react
- com.voyageai:voyage-ai-tests
- SecurityConfig
- DestinationSummaryResponse
- ListingType
- .createTrip
- ItineraryDay
- Voyage-AI — System Architecture & Technical Design Specification
- User
- UserProfileResponse
- ProvenanceDto
- GoogleGeminiService
- ItineraryService
- GeminiServiceTest
- Voyage-AI — Comprehensive Project Report
- AuthResponse
- JwtTokenProvider
- Voyage-AI — Engineering Methodology & Operational Workflows
- TripApiTest.java
- AiHealthResponse
- AiControllerTest.java
- DestinationService
- AiGenerateResponse
- DestinationIngestionService
- .syncProfile
- UserPrincipal
- Voyage-AI — Testing Strategy & Software Quality Assessment
- GeminiApiResponse
- .addItemToDay
- 3. Engineering Implementation Status & Roadmap
- DestinationRepository
- ItineraryItemResponse
- Voyage-AI — Documentation Suite Index & Navigation Guide
- .importDestinations
- generate_pdfs.py
- AuthService
- .importDestinations
- Voyage-AI — Post-Implementation Verification & Final Acceptance Report
- ContentUrls
- AdminDestinationControllerTest.java
- 3. Functional requirements
- TripRepository
- DestinationImportResult
- .updateTrip
- .updateItem
- AuthApiTest
- UserProfileApiTest.java
- DestinationImportRunner
- .getBackendBaseUrl
- DestinationsDiscoveryTest
- AiApiTest.java
- CustomAccessDeniedHandler
- CustomAuthenticationEntryPoint
- WeatherE2ETest.java
- DestinationIngestionServiceTest
- DatabaseMigrationConfig.java
- WeatherController.java
- HealthController.java
- ItineraryItem
- ResourceNotFoundException
- PersistenceIntegrationTest.java
- Design Context
- .onCreate
- .onUpdate
- DestinationIngestionApiTest.java
- PersistenceTripApiTest.java
- AiAssistantBrowserTest
- DestinationDetailModalTest
- HealthApiTest.java
- TravelListingService
- NavigationTest
- ResponsiveTest
- ErrorResponse
- .load
- Voyage-AI — Open Decisions and Assumptions
- .handleUnauthorizedException
- AiGenerateRequest
- 10. Prioritized Issues Requiring Changes & Recommended Fixes
- 11. Remediation and Confirmed Fixes Applied
- 3. Audit Area 1: Destination Source, Provenance, and Licensing
- 6.1 Inspection of `frontend/src/App.tsx`
- 7. Audit Area 5: Existing Feature Verification
- 5. Audit Area 3: MySQL and Flyway Verification
- 4. Audit Area 2: Administrative Endpoint Security
- 8. Summary of Tests Executed vs Not Executed

## God Nodes (most connected - your core abstractions)
1. `Destination` - 84 edges
2. `TravelListing` - 78 edges
3. `TravelListingResponse` - 67 edges
4. `ApiResponse` - 53 edges
5. `TripResponse` - 52 edges
6. `Trip` - 51 edges
7. `DestinationSummaryResponse` - 44 edges
8. `ItineraryItem` - 43 edges
9. `UserPrincipal` - 42 edges
10. `User` - 41 edges

## Surprising Connections (you probably didn't know these)
- `AuthController` --references--> `AuthService`  [EXTRACTED]
  backend/src/main/java/com/voyageai/auth/AuthController.java → backend/src/main/java/com/voyageai/auth/AuthService.java
- `AuthService` --references--> `JwtTokenProvider`  [EXTRACTED]
  backend/src/main/java/com/voyageai/auth/AuthService.java → backend/src/main/java/com/voyageai/security/JwtTokenProvider.java
- `UserController` --references--> `AuthService`  [EXTRACTED]
  backend/src/main/java/com/voyageai/user/UserController.java → backend/src/main/java/com/voyageai/auth/AuthService.java
- `UserApiTest` --references--> `AuthService`  [EXTRACTED]
  backend/src/test/java/com/voyageai/auth/UserApiTest.java → backend/src/main/java/com/voyageai/auth/AuthService.java
- `AdminDestinationControllerTest` --references--> `AuthService`  [EXTRACTED]
  backend/src/test/java/com/voyageai/destination/AdminDestinationControllerTest.java → backend/src/main/java/com/voyageai/auth/AuthService.java

## Import Cycles
- None detected.

## Communities (212 total, 39 thin omitted)

### Community 0 - "App.tsx"
Cohesion: 0.07
Nodes (39): App(), AiTravelAssistant(), AiTravelAssistantProps, GenerateResponse, HealthResponse, SAMPLE_PROMPTS, Badge(), BadgeProps (+31 more)

### Community 1 - "devDependencies"
Cohesion: 0.12
Nodes (17): autoprefixer, eslint, devDependencies, autoprefixer, eslint, globals, jsdom, @types/react (+9 more)

### Community 2 - "Voyage-AI — Software Requirements Specification (SRS)"
Cohesion: 0.05
Nodes (37): 10. SRS acceptance criteria, 1.1 Purpose, 1.2 Scope, 1.3 Definitions, 1.4 References, 1. Introduction, 2.1 Product perspective, 2.2 User classes (+29 more)

### Community 3 - "UserApiTest.java"
Cohesion: 0.21
Nodes (8): ActiveProfiles, AutoConfigureMockMvc, DisplayName, MockMvc, ObjectMapper, SpringBootTest, Test, UserApiTest

### Community 4 - "Expo Animation Recipes"
Cohesion: 0.06
Nodes (33): Bottom sheet you can drag to dismiss, Collapsing header on scroll, Expo Animation Recipes, Firing something once at a threshold, Keyboard-synced UI, List entrances, Press feedback, Screen transitions (Expo Router) (+25 more)

### Community 5 - "Core Principles"
Cohesion: 0.06
Nodes (33): 10. Content and microcopy, 11. Feedback and error recovery, 12. Data-heavy interfaces, 13. Animation and interaction, 1. Understand, 1. Understand the user and the task, 2. Prioritize usability, 2. Structure (+25 more)

### Community 6 - "Animation Recipes"
Cohesion: 0.06
Nodes (31): Accordion / collapse, Animation Recipes, Button press, Drag to dismiss, Drawer / sheet, Dropdown, popover, menu, select, Hold to confirm, Masking a crossfade that won't settle (+23 more)

### Community 7 - "Documentation Architect"
Cohesion: 0.06
Nodes (32): 1. Documentation must reflect reality, 2. Optimize for usefulness, not length, 3. Start with the audience and purpose, 4. Use progressive disclosure, 5. Explain important "why", Architecture, Architecture Documentation, Code References (+24 more)

### Community 8 - "Capabilities"
Cohesion: 0.06
Nodes (31): API Contract & Documentation, API Design & Patterns, API Gateway & Load Balancing, Asynchronous Processing, Authentication & Authorization, Backend Architect, Behavioral Traits, Caching Strategies (+23 more)

### Community 9 - "Voyage-AI — Pre-Implementation Audit & Readiness Verification (v1.1)"
Cohesion: 0.06
Nodes (31): 1. Executive Summary & Verdict Overview, 2. Corrections and Refinements to Audit v1.0, 3.1 Runtimes, Compilers, and Build Tools, 3.2 Data Stores, Services, and Networking, 3.3 Design Skills & Agent Automation, 3. Verified Host Environment Inventory, 4.1 Functional Requirements (FRD: FR-001 through FR-084 & Business Rules), 4.2 System Functional Requirements (SRS: SYS-F-001 through SYS-F-054) (+23 more)

### Community 10 - "Worst-Case Catalog"
Cohesion: 0.06
Nodes (29): Collections, Emails, URLs, identifiers, Environment, Images and media, Labels, titles, and copy from data, Numbers and money, People and names, States (+21 more)

### Community 11 - "Animation Standards Reference"
Cohesion: 0.07
Nodes (26): Aggressive Escalation Triggers, Guidelines, Initial Response, Operating Posture, Part 1 — Findings table (REQUIRED), Part 2 — Verdict (REQUIRED), Remedial Preference Hierarchy, Required Output Format (+18 more)

### Community 12 - "Core Principles"
Cohesion: 0.08
Nodes (24): 1. Start with requirements, 2. Prefer simplicity, 3. Evaluate technology choices rationally, 4. Design for data integrity, 5. Design schemas around actual access patterns, 6. Treat migrations as production changes, 7. Scale only when justified, 8. Consider the whole system (+16 more)

### Community 13 - "Animation Audit Playbook"
Cohesion: 0.08
Nodes (22): 1. Purpose & frequency, 2. Easing & duration, 3. Physicality & origin, 4. Interruptibility, 5. Performance, 6. Accessibility, 7. Cohesion & tokens, 8. Missed opportunities (+14 more)

### Community 14 - "Capabilities"
Cohesion: 0.09
Nodes (22): Application Security Testing, Behavioral Traits, Capabilities, Cloud Security, Compliance & Governance, DevSecOps & Security Automation, Do not use this skill when, Emerging Security Technologies (+14 more)

### Community 15 - "Write Swift"
Cohesion: 0.09
Nodes (22): 10. ARC and object lifetime, 11. Testing — Swift Testing by default, 12. Macros, 13. Logging and debugging, 14. Unsafe code and interop, 15. Modern syntax you should be using, 16. Migrating an existing codebase to Swift 6, 1. Model data with value types (+14 more)

### Community 16 - "onboard.md"
Cohesion: 0.09
Nodes (22): Assess Onboarding Needs, Context Over Ceremony, Contextual Help, Design Onboarding Experiences, Documentation & Help, Empty State Design, Feature Discovery & Adoption, Guided Tours & Walkthroughs (+14 more)

### Community 17 - "GlobalExceptionHandler"
Cohesion: 0.17
Nodes (11): GlobalExceptionHandler, Logger, ResponseEntity, ConflictException, BadCredentialsException, ExceptionHandler, MethodArgumentNotValidException, MethodArgumentTypeMismatchException (+3 more)

### Community 18 - "dependencies"
Cohesion: 0.18
Nodes (11): clsx, dependencies, clsx, lucide-react, react, react-dom, tailwind-merge, lucide-react (+3 more)

### Community 19 - "Voyage-AI — Functional Requirements Document (FRD)"
Cohesion: 0.15
Nodes (12): 1. Purpose, 2. Roles, 4. Business rules, 5. Main use cases, 6. Acceptance tests for key workflows, 7. Dependencies, UC-01: Discover a destination, UC-02: Create a trip (+4 more)

### Community 20 - "compilerOptions"
Cohesion: 0.09
Nodes (22): compilerOptions, allowImportingTsExtensions, isolatedModules, jsx, lib, module, moduleDetection, moduleResolution (+14 more)

### Community 21 - "Apple Design"
Cohesion: 0.09
Nodes (21): 10. Gesture design details (the "feel" checklist), 11. Frame-level smoothness, 12. Materials & depth — translucency conveys hierarchy, 13. Multimodal feedback — motion + sound + haptics, 14. Reduced motion & accessibility, 15. Typography — optical sizing, tracking, leading, 16. Design foundations — the eight principles, 17. Process (+13 more)

### Community 22 - "The Fixes"
Cohesion: 0.09
Nodes (21): 10. Status bar color doesn't match, 11. Right in Chrome, wrong on phone, 1. Hover state stuck after tap, 2. Gray/blue flash on tap, 3. Layout has the wrong height, 4. Page zooms into the input, 5. Tap feels laggy, 6. Pull-to-refresh hijacks scroll (+13 more)

### Community 23 - "Prototyping Variants"
Cohesion: 0.10
Nodes (19): Behavior contract, Markup, Reference wiring, Rules, Styles, The Picker, Hard Rules, Initial Response (+11 more)

### Community 24 - "Delight Techniques"
Cohesion: 0.10
Nodes (20): Appropriate to Context, Assess Delight Opportunities, Celebration Moments, Compound Over Time, Context Gathering (Do This First), Delight Amplifies, Never Blocks, Delight Principles, Delight Techniques (+12 more)

### Community 25 - "API Design Principles Implementation Playbook"
Cohesion: 0.10
Nodes (19): 1. RESTful Design Principles, 2. GraphQL Design Principles, 3. API Versioning Strategies, API Design Principles Implementation Playbook, Best Practices, Common Pitfalls, Core Concepts, GraphQL APIs (+11 more)

### Community 26 - "Core Principles"
Cohesion: 0.10
Nodes (19): 10. Testing, 11. SEO, 1. Follow the existing project architecture, 2. Build for correctness first, 3. Component design, 4. State and data fetching, 5. Responsive design, 6. Accessibility (+11 more)

### Community 27 - "Implement Animations"
Cohesion: 0.10
Nodes (19): Accessibility, Assess Animation Opportunities, Context Gathering (Do This First), CSS Animations, Delight Moments, Entrance Animations, Feedback & Guidance, Implement Animations (+11 more)

### Community 28 - "Glossary"
Cohesion: 0.11
Nodes (18): Animation Vocabulary, Easing — how speed changes over an animation, Entrances & Exits — how elements appear and disappear, Examples, Feedback & Interaction — responding to the user's actions, Glossary, Initial Response, Instructions (+10 more)

### Community 29 - "Introduce Color Strategically"
Cohesion: 0.11
Nodes (18): Accent Color Application, Accessibility, Assess Color Opportunity, Background & Surfaces, Balance & Refinement, Borders & Accents, Cohesion, Context Gathering (Do This First) (+10 more)

### Community 30 - "Design Critique"
Cohesion: 0.11
Nodes (18): 10. Microcopy & Voice, 1. AI Slop Detection (CRITICAL), 2. Visual Hierarchy, 3. Information Architecture, 4. Emotional Resonance, 5. Discoverability & Affordance, 6. Composition & Balance, 7. Typography as Communication (+10 more)

### Community 31 - "AuthController"
Cohesion: 0.36
Nodes (5): AuthController, PostMapping, RequestMapping, ResponseEntity, RestController

### Community 32 - "Animation Performance"
Cohesion: 0.11
Nodes (17): Animation Performance, Hard Rules, Initial Response, Operating Posture, Phase 1: Find what animates, Phase 2: Diagnose, Phase 3: Report, React: Don't Animate Through State (+9 more)

### Community 33 - "ItineraryDayResponse"
Cohesion: 0.09
Nodes (3): ItineraryDayResponse, ItineraryMapper, TripMapper

### Community 34 - "2. Token Lifecycle & Transport"
Cohesion: 0.11
Nodes (17): 1. Overview & Strategy, 2.1 Token Issuance, 2.2 Client-Side Storage: In-Memory Only, 2.3 Token Transport, 2.4 Token Expiry & Expired Token Handling, 2.5 Logout & Client-Side Token Removal, 2.6 Server-Side Invalidation Expectations & Limitations, 2. Token Lifecycle & Transport (+9 more)

### Community 35 - "Voyage-AI — Product Requirements Document (PRD)"
Cohesion: 0.11
Nodes (18): 10. Success measures, 11. Assumptions and constraints, 12. Risks, 13. Release acceptance, 14. Next documents, 1. Executive summary, 2. Problem, 3. Vision (+10 more)

### Community 36 - "Voyage-AI — UI/UX Specification"
Cohesion: 0.11
Nodes (18): 1. Design objective, 2. Experience principles, 3. Proposed sitemap, 4. Key screen requirements, 5. Design system to define before page implementation, 6. Component inventory, 7. Interaction states, 8. Responsive behavior (+10 more)

### Community 37 - "Finding Animation Opportunities"
Cohesion: 0.12
Nodes (16): 1. Frequency — how often will a user see this?, 2. Purpose — why does this animate?, 3. Speed — can it stay inside budget?, 4. Function — does motion help or hinder here?, Finding Animation Opportunities, Hard Rules, Initial Response, Operating Posture (+8 more)

### Community 38 - "Polish Systematically"
Cohesion: 0.12
Nodes (16): Code Quality, Color & Contrast, Content & Copy, Edge Cases & Error States, Final Verification, Forms & Inputs, Icons & Images, Interaction States (+8 more)

### Community 39 - "compilerOptions"
Cohesion: 0.10
Nodes (20): compilerOptions, isolatedModules, lib, module, moduleDetection, moduleResolution, noEmit, noFallthroughCasesInSwitch (+12 more)

### Community 40 - "Voyage-AI — All-in-One Travel Companion"
Cohesion: 0.08
Nodes (23): 1. Prerequisites & Required Tools, 2. Port Allocation & Host Collision Avoidance, 3. Quick Start & Local Setup, 4. Automated Testing Framework, 5. Development & Build Commands, 6. Project Layout & Directory Structure, 7. Data Authenticity & Architecture Principles, Backend (`backend/`) (+15 more)

### Community 41 - "Implement Adaptations"
Cohesion: 0.13
Nodes (14): Assess Adaptation Challenge, Content Adaptation, Desktop Adaptation (Mobile → Desktop), Email Adaptation (Web → Email), Implement Adaptations, Layout Adaptation Techniques, Mobile Adaptation (Desktop → Mobile), Navigation Adaptation (+6 more)

### Community 42 - "Improve Copy Systematically"
Cohesion: 0.13
Nodes (14): Apply Clarity Principles, Assess Current Copy, Button & CTA Text, Confirmation Dialogs, Empty States, Error Messages, Form Labels & Instructions, Help Text & Tooltips (+6 more)

### Community 43 - "Simplify the Design"
Cohesion: 0.13
Nodes (14): Assess Current State, Code Simplification, Content Simplification, Context Gathering (Do This First), Document Removed Complexity, Information Architecture, Interaction Simplification, Layout Simplification (+6 more)

### Community 44 - "Voyage-AI — Technical Design Document (TDD)"
Cohesion: 0.13
Nodes (15): 10. Database and migrations, 11. Course requirements in implementation, 12. Testing strategy, 13. Docker and local setup, 14. Architecture decisions still open, 1. Architecture overview, 2. Repository layout, 3. Frontend design and implementation (+7 more)

### Community 45 - "Generate Comprehensive Report"
Cohesion: 0.14
Nodes (13): Anti-Patterns Verdict, Critical Issues, Detailed Findings by Severity, Diagnostic Scan, Executive Summary, Generate Comprehensive Report, High-Severity Issues, Low-Severity Issues (+5 more)

### Community 46 - "Amplify the Design"
Cohesion: 0.14
Nodes (13): Amplify the Design, Assess Current State, Color Intensification, Composition Boldness, Context Gathering (Do This First), MANDATORY PREPARATION, Motion & Animation, Plan Amplification (+5 more)

### Community 47 - "Optimization Strategy"
Cohesion: 0.14
Nodes (13): Animation Performance, Assess Performance Issues, Core Web Vitals Optimization, Cumulative Layout Shift (CLS < 0.1), First Input Delay (FID < 100ms) / INP (< 200ms), Largest Contentful Paint (LCP < 2.5s), Loading Performance, Network Optimization (+5 more)

### Community 48 - "Working With Sonner"
Cohesion: 0.15
Nodes (11): Functions, Sonner API Reference, `toast()` options, `<Toaster />`, Initial Response, Picking the right call, Recipes, Setup (+3 more)

### Community 49 - "Frontend Aesthetics Guidelines"
Cohesion: 0.15
Nodes (12): Color & Theme, Design Direction, Frontend Aesthetics Guidelines, Implementation Principles, Interaction, Layout & Space, Motion, Responsive (+4 more)

### Community 50 - "Refine the Design"
Cohesion: 0.15
Nodes (12): Assess Current State, Color Refinement, Composition Refinement, Context Gathering (Do This First), MANDATORY PREPARATION, Motion Reduction, Plan Refinement, Refine the Design (+4 more)

### Community 51 - "Hardening Dimensions"
Cohesion: 0.17
Nodes (11): Accessibility Resilience, Assess Hardening Needs, Edge Cases & Boundary Conditions, Error Handling, Hardening Dimensions, Input Validation & Sanitization, Internationalization (i18n), Performance Resilience (+3 more)

### Community 52 - "The list"
Cohesion: 0.18
Nodes (10): Charts, Common mismatches to catch, How to use this, Initial Response, Interaction & performance, Motion & visuals, Picking The Right Library, State & styling (+2 more)

### Community 53 - "Voyage-AI — Implementation Plan"
Cohesion: 0.18
Nodes (11): Agent execution rules, Guiding rule, Phase 0 — Repository and setup (COMPLETED & VERIFIED), Phase 1 — Design foundation (COMPLETED & VERIFIED), Phase 2 — Backend and persistence foundation (COMPLETED & VERIFIED), Phase 3 — First complete vertical slice (COMPLETED & VERIFIED), Phase 4 — Itinerary and budget (COMPLETED & VERIFIED), Phase 5 — AI integration (COMPLETED & VERIFIED) (+3 more)

### Community 54 - "Voyage-AI — Non-Functional Requirements Document (NFRD)"
Cohesion: 0.18
Nodes (11): 10. Initial acceptance targets, 1. Purpose, 2. Performance, 3. Security and privacy, 4. Reliability and resilience, 5. Usability and accessibility, 6. Maintainability, 7. Testability (+3 more)

### Community 55 - "mvnw"
Cohesion: 0.33
Nodes (6): mvnw script, clean(), die(), exec_maven(), set_java_home(), verbose()

### Community 56 - "WikimediaSummaryResponse"
Cohesion: 0.09
Nodes (4): Coordinates, Image, JsonIgnoreProperties, WikimediaSummaryResponse

### Community 57 - "Design Engineering"
Cohesion: 0.22
Nodes (8): Accessibility, Design Engineering, Initial Response, prefers-reduced-motion, Review Checklist, Review Format (Required), Stagger Animations, Touch device hover states

### Community 58 - "docs/README.md"
Cohesion: 0.12
Nodes (10): Core Specifications & Technical Plans, Implementation & Verification Reports (`docs/reports/`), Verified System Architecture, Voyage-AI — Project Documentation, 1. Status Classification Definitions, 2. Comprehensive Implementation Status Inventory, 3. Comprehensive Verification Summary, Voyage-AI — Implementation Status & Feature Inventory (+2 more)

### Community 59 - "API Design Principles"
Cohesion: 0.25
Nodes (7): API Design Principles, Description:, Do not use this skill when, Instructions, Name: api-design-principles, Resources, Use this skill when

### Community 60 - "Component Building Principles"
Cohesion: 0.25
Nodes (8): Animate enter states with @starting-style, Buttons must feel responsive, Component Building Principles, Make popovers origin-aware, Never animate from scale(0), Tooltips: skip delay on subsequent hovers, Use blur to mask imperfect transitions, Use CSS transitions over keyframes for interruptible UI

### Community 61 - "Step 2: Ask UX-Focused Questions"
Cohesion: 0.25
Nodes (7): Accessibility & Inclusion, Aesthetic Preferences, Brand & Personality, Step 1: Explore the Codebase, Step 2: Ask UX-Focused Questions, Step 3: Write Design Context, Users & Purpose

### Community 62 - "Voyage-AI — High-Level System Architecture"
Cohesion: 0.22
Nodes (8): 1. Overview & Major Components, 2. Communication and Data Flows, 3. Primary Data Stores & Persistence, 4. Authentication & Authorization Boundaries, 5. Major External Dependencies & Integrations, 6. Deployment Boundaries, 7. Automated Testing & Verification Architecture, Voyage-AI — High-Level System Architecture

### Community 63 - ".get"
Cohesion: 0.12
Nodes (20): HealthControllerTest, ActiveProfiles, AutoConfigureMockMvc, MockMvc, SpringBootTest, Test, DestinationApiTest, ActiveProfiles (+12 more)

### Community 64 - "The Animation Decision Framework"
Cohesion: 0.33
Nodes (6): 1. Should this animate at all?, 2. What is the purpose?, 3. What easing should it use?, 4. How fast should it be?, Perceived performance, The Animation Decision Framework

### Community 65 - "clip-path for Animation"
Cohesion: 0.33
Nodes (6): clip-path for Animation, Comparison sliders, Hold-to-delete pattern, Image reveals on scroll, Tabs with perfect color transitions, The inset shape

### Community 66 - "Performance Rules"
Cohesion: 0.33
Nodes (6): CSS animations beat JS under load, CSS variables are inheritable, Framer Motion hardware acceleration caveat, Only animate transform and opacity, Performance Rules, Use WAAPI for programmatic CSS animations

### Community 67 - "Gesture and Drag Interactions"
Cohesion: 0.33
Nodes (6): Damping at boundaries, Friction instead of hard stops, Gesture and Drag Interactions, Momentum-based dismissal, Multi-touch protection, Pointer capture for drag

### Community 68 - "extract.md"
Cohesion: 0.33
Nodes (5): Discover, Document, Extract & Enrich, Migrate, Plan Extraction

### Community 69 - "CorsConfig.java"
Cohesion: 0.53
Nodes (4): CorsConfig, Bean, Configuration, WebMvcConfigurer

### Community 70 - "VoyageAiApplicationTests.java"
Cohesion: 0.53
Nodes (4): ActiveProfiles, SpringBootTest, Test, VoyageAiApplicationTests

### Community 71 - "CSS Transform Mastery"
Cohesion: 0.40
Nodes (5): 3D transforms for depth, CSS Transform Mastery, scale() scales children too, transform-origin, translateY with percentages

### Community 72 - "The Sonner Principles (Building Loved Components)"
Cohesion: 0.40
Nodes (5): Asymmetric enter/exit timing, Cohesion matters, Review your work the next day, The opacity + height combination, The Sonner Principles (Building Loved Components)

### Community 73 - "Spring Animations"
Cohesion: 0.40
Nodes (5): Interruptibility advantage, Spring Animations, Spring-based mouse interactions, Spring configuration, When to use springs

### Community 74 - "Voyage-AI — Post-Implementation Verification Audit Report"
Cohesion: 0.25
Nodes (7): 1. Executive Summary, 2.1 Repository Architecture Overview, 2.2 Active Configuration Inspection, 2. Repository & Configuration Evidence, 9. Claims Verification Matrix, Findings Breakdown by Severity, Voyage-AI — Post-Implementation Verification Audit Report

### Community 75 - "Core Philosophy"
Cohesion: 0.50
Nodes (4): Beauty is leverage, Core Philosophy, Taste is trained, not innate, Unseen details compound

### Community 76 - "Debugging Animations"
Cohesion: 0.50
Nodes (4): Debugging Animations, Frame-by-frame inspection, Slow motion testing, Test on real devices

### Community 77 - "normalize.md"
Cohesion: 0.50
Nodes (3): Clean Up, Execute, Plan

### Community 78 - "DotenvEnvironmentPostProcessor.java"
Cohesion: 0.23
Nodes (8): DotenvEnvironmentPostProcessor, Override, VoyageAiApplication, ConfigurableEnvironment, EnvironmentPostProcessor, Ordered, SpringApplication, SpringBootApplication

### Community 79 - "Voyage-AI Database Migrations Directory"
Cohesion: 0.50
Nodes (3): Migration Naming Convention, Phase Boundaries, Voyage-AI Database Migrations Directory

### Community 80 - "TravelListing"
Cohesion: 0.04
Nodes (5): Entity, PrePersist, PreUpdate, Table, TravelListing

### Community 82 - "BasePlaywrightTest"
Cohesion: 0.13
Nodes (12): AfterAll, AfterEach, Browser, BrowserContext, Playwright, TestInfo, BasePlaywrightTest, BeforeAll (+4 more)

### Community 92 - "Voyage-AI — Data Authenticity, Provenance & Academic Provider Plan (v2.1)"
Cohesion: 0.10
Nodes (20): 1. Executive Summary & Non-Negotiable Rules, 2.1 Core Component Strategy, 2.2 First Integration Scope (Academic Prototype), 2. Approved Architecture Direction & Scope Prioritization, 3.1 OpenStreetMap (OSM) Tiles & Leaflet, 3.2 Open-Meteo Weather API, 3.3 Google Gemini API (Google AI Studio Free Tier), 3.4 Wikimedia Commons REST API (+12 more)

### Community 93 - "CurrentData"
Cohesion: 0.06
Nodes (5): CurrentData, DailyData, JsonIgnoreProperties, OpenMeteoApiResponse, WmoWeatherCode

### Community 99 - "DatabaseFlywayIntegrationTest"
Cohesion: 0.25
Nodes (10): DatabaseFlywayIntegrationTest, ActiveProfiles, BeforeEach, DataSource, DisplayName, Flyway, SpringBootTest, Test (+2 more)

### Community 100 - "4. Execution Commands"
Cohesion: 0.17
Nodes (11): 1. Overview & Test Architecture, 2. Test Suites Summary & Scope, 3. Database Safety Mandate, 4. Execution Commands, 5. Prerequisites for Local Testing, Backend Unit, Controller & Database Tests, Frontend Unit & Component Tests (Vitest), Full Local Test Suite (All Layers) (+3 more)

### Community 102 - ".getWeather"
Cohesion: 0.22
Nodes (7): Override, HttpStatus, WeatherServiceException, DisplayName, MockRestServiceServer, Test, OpenMeteoWeatherServiceTest

### Community 103 - "ApiResponse"
Cohesion: 0.11
Nodes (15): ApiResponse, GetMapping, ResponseEntity, PostMapping, GetMapping, ResponseEntity, DeleteMapping, GetMapping (+7 more)

### Community 104 - "Destination"
Cohesion: 0.03
Nodes (3): Destination, Entity, Table

### Community 105 - "scripts"
Cohesion: 0.25
Nodes (8): scripts, build, dev, lint, preview, test, test:unit, typecheck

### Community 106 - "TripResponse"
Cohesion: 0.05
Nodes (6): TripResponse, Entity, PrePersist, PreUpdate, Table, Trip

### Community 107 - "OpenMeteoWeatherService"
Cohesion: 0.33
Nodes (6): Autowired, Logger, RestClient, Service, OpenMeteoWeatherService, BeforeEach

### Community 110 - "Voyage-AI — Centralized Automated Testing Workspace"
Cohesion: 0.33
Nodes (5): Directory Structure, Failure Artifacts, Running Tests, Test Suites & Responsibilities, Voyage-AI — Centralized Automated Testing Workspace

### Community 111 - "package.json"
Cohesion: 0.40
Nodes (4): name, private, type, version

### Community 128 - "SecurityConfig"
Cohesion: 0.21
Nodes (12): AuthenticationConfiguration, AuthenticationManager, Bean, Configuration, ObjectMapper, PasswordEncoder, SecurityConfig, CorsConfigurationSource (+4 more)

### Community 130 - "ListingType"
Cohesion: 0.16
Nodes (6): ListingType, ACCOMMODATION, ACTIVITY, ATTRACTION, RESTAURANT, DisplayName

### Community 131 - ".createTrip"
Cohesion: 0.09
Nodes (10): CreateTripRequest, DeleteMapping, GetMapping, PostMapping, RequestMapping, ResponseEntity, RestController, TripController (+2 more)

### Community 132 - "ItineraryDay"
Cohesion: 0.07
Nodes (6): CreateItineraryDayRequest, ItineraryDay, Entity, PrePersist, PreUpdate, Table

### Community 133 - "Voyage-AI — System Architecture & Technical Design Specification"
Cohesion: 0.06
Nodes (30): 1.1 Project Purpose & System Boundaries, 1.2 Core Architectural Decisions & Trade-Offs, 1. Executive Architecture Overview, 2. Verified Technology Stack, 3.1 Backend Module Architecture (`com.voyageai`), 3.2 Frontend Component Architecture (`frontend/src`), 3. Component Architecture & Domain Boundaries, 4.1 System Context Diagram (+22 more)

### Community 134 - "User"
Cohesion: 0.08
Nodes (6): LoginRequest, Entity, PrePersist, PreUpdate, Table, User

### Community 137 - "GoogleGeminiService"
Cohesion: 0.13
Nodes (14): DotenvLoader, Logger, GoogleGeminiService, Autowired, Logger, RestClient, Service, GeminiApiIntegrationTest (+6 more)

### Community 138 - "ItineraryService"
Cohesion: 0.20
Nodes (9): ItineraryDayRepository, Repository, ItineraryItemRepository, Repository, ItineraryService, Service, Repository, TravelListingRepository (+1 more)

### Community 139 - "GeminiServiceTest"
Cohesion: 0.33
Nodes (5): GeminiServiceException, HttpStatus, GeminiServiceTest, DisplayName, Test

### Community 140 - "Voyage-AI — Comprehensive Project Report"
Cohesion: 0.10
Nodes (20): 1. Executive Summary, 2.1 The Crisis of Modern Travel Planning, 2.2 Core Project Objectives, 2. Problem Statement & Strategic Objectives, 3. Project Scope & Deliverables, 4.1 High-Level Architecture, 4.2 Verified Technology Summary, 4. System Overview & Technology Stack (+12 more)

### Community 141 - "AuthResponse"
Cohesion: 0.09
Nodes (5): AuthResponse, UserSummaryDto, BeforeEach, BeforeEach, BeforeEach

### Community 142 - "JwtTokenProvider"
Cohesion: 0.14
Nodes (11): Component, HttpServletRequest, HttpServletResponse, Override, JwtAuthenticationFilter, Component, Logger, ObjectMapper (+3 more)

### Community 143 - "Voyage-AI — Engineering Methodology & Operational Workflows"
Cohesion: 0.11
Nodes (18): 1.1 Evidenced Engineering Process, 1.2 Module Implementation Sequence, 1.3 Version Control & Branching Strategy, 1. Development Methodology & Engineering Lifecycle, 2.1 Workflow 1: Destination Discovery & Multi-Criteria Filtering, 2.2 Workflow 2: Weather Forecast Retrieval & Provenance Tracking, 2.3 Workflow 3: Live Backend Health Monitoring, 2. End-to-End Operational Workflows (+10 more)

### Community 144 - "TripApiTest.java"
Cohesion: 0.23
Nodes (8): ActiveProfiles, AutoConfigureMockMvc, DisplayName, MockMvc, ObjectMapper, SpringBootTest, Test, TripApiTest

### Community 146 - "AiHealthResponse"
Cohesion: 0.16
Nodes (4): GetMapping, AiHealthResponse, Override, MockRestServiceServer

### Community 147 - "AiControllerTest.java"
Cohesion: 0.26
Nodes (7): AiControllerTest, ActiveProfiles, AutoConfigureMockMvc, DisplayName, MockMvc, SpringBootTest, Test

### Community 148 - "DestinationService"
Cohesion: 0.22
Nodes (8): DestinationController, RequestMapping, RestController, DestinationService, Page, Pageable, Service, Transactional

### Community 150 - "DestinationIngestionService"
Cohesion: 0.21
Nodes (8): DestinationIngestionService, DestinationMetadata, Autowired, Logger, ObjectMapper, RestClient, Service, Transactional

### Community 152 - "UserPrincipal"
Cohesion: 0.11
Nodes (11): ItineraryController, DeleteMapping, PostMapping, PutMapping, RequestMapping, ResponseEntity, RestController, Override (+3 more)

### Community 153 - "Voyage-AI — Testing Strategy & Software Quality Assessment"
Cohesion: 0.13
Nodes (14): 1. Testing Frameworks, Tools & Quality Architecture, 2. Test Directory Structure & Organization, 3.1 Suite Summary Breakdown, 3.2 Backend Test Execution Audit (`mvn test` in `backend/`), 3.3 Frontend Test Execution Audit (`npm run test:unit` in `frontend/`), 3.4 End-to-End Browser & Black-Box API Audit (`mvn test "-Dtest.excludedGroups="` in `tests/`), 3. Verified Test Execution Results, 4.1 Strict Test Database Isolation Guard (+6 more)

### Community 154 - "GeminiApiResponse"
Cohesion: 0.30
Nodes (9): Content, GeminiApiRequest, Part, Candidate, Content, GeminiApiResponse, JsonIgnoreProperties, Part (+1 more)

### Community 156 - "3. Engineering Implementation Status & Roadmap"
Cohesion: 0.15
Nodes (12): 1. Technical Risk Register, 2. Prioritized Engineering Roadmap, 3.1 Task 1 (P0): Relational Schema & Persistence Foundation — **COMPLETED & VERIFIED**, 3.2 Task 2 (P0): Stateless JWT Security & Ownership Enforcement — **COMPLETED & VERIFIED**, 3.3 Task 3 (P1): Discovery & Listing Catalog REST APIs — **COMPLETED & VERIFIED**, 3.4 Task 4 (P2): Google Gemini AI Travel Concierge — **COMPLETED & VERIFIED**, 3.5 Task 5 (P3): Interactive Leaflet Maps (Phase 3 Target), 3.6 Task 6 (P3): Transit Distance & Routing Matrix (Phase 4 Target) (+4 more)

### Community 157 - "DestinationRepository"
Cohesion: 0.29
Nodes (5): DestinationRepository, Page, Pageable, Query, Repository

### Community 159 - "Voyage-AI — Documentation Suite Index & Navigation Guide"
Cohesion: 0.22
Nodes (8): 1. Overview & Purpose of the Documentation Suite, 2. Master Report Directory & Reading Order, 3.1 Implemented & Verified Baseline (Phase 2), 3.2 Status Classification Key, 3. Current Project State & Documentation Scope, 4. Canonical Repository Documentation References, Recommended Reading Sequences:, Voyage-AI — Documentation Suite Index & Navigation Guide

### Community 160 - ".importDestinations"
Cohesion: 0.38
Nodes (3): Override, DisplayName, Test

### Community 161 - "generate_pdfs.py"
Cohesion: 0.36
Nodes (7): generate_pdf_via_edge(), main(), Converts Markdown text to full HTML5 with custom typography and CSS., Invokes Microsoft Edge Chromium in headless mode to render HTML to PDF., Validates the PDF using PyMuPDF (fitz). Returns dict with page count, size, and…, render_markdown_to_html(), validate_pdf()

### Community 162 - "AuthService"
Cohesion: 0.24
Nodes (6): AuthService, PasswordEncoder, Service, Repository, UserRepository, Test

### Community 163 - ".importDestinations"
Cohesion: 0.16
Nodes (7): AdminDestinationController, PostMapping, RequestMapping, ResponseEntity, RestController, DestinationImportRequest, PreAuthorize

### Community 164 - "Voyage-AI — Post-Implementation Verification & Final Acceptance Report"
Cohesion: 0.12
Nodes (16): 1. Executive Summary, 2. Requirement-to-Implementation Traceability Matrix, 3.1 Test Suite Summary Breakdown, 3.2 Individual Test Suite Results, 3. Comprehensive Test Execution Evidence, 4.1 Google Gemini AI Verification ("Capital of India"), 4.2 Open-Meteo Weather Verification (New Delhi), 4.3 Wikimedia Commons Media Licensing (+8 more)

### Community 166 - "AdminDestinationControllerTest.java"
Cohesion: 0.16
Nodes (9): RegisterRequest, AdminDestinationControllerTest, ActiveProfiles, AutoConfigureMockMvc, DisplayName, MockMvc, ObjectMapper, SpringBootTest (+1 more)

### Community 167 - "3. Functional requirements"
Cohesion: 0.20
Nodes (10): 3. Functional requirements, A. Authentication and profile, B. Destination discovery, C. Travel listings, D. Trip setup, E. Recommendations and AI, F. Itinerary management, G. Budget (+2 more)

### Community 168 - "TripRepository"
Cohesion: 0.32
Nodes (3): Query, Repository, TripRepository

### Community 170 - ".updateTrip"
Cohesion: 0.10
Nodes (3): UpdateTripRequest, PatchMapping, PutMapping

### Community 172 - "AuthApiTest"
Cohesion: 0.22
Nodes (9): AuthApiTest, ActiveProfiles, AutoConfigureMockMvc, BeforeEach, DisplayName, MockMvc, ObjectMapper, SpringBootTest (+1 more)

### Community 173 - "UserProfileApiTest.java"
Cohesion: 0.40
Nodes (7): AuthInfo, DisplayName, HttpClient, ObjectMapper, Tag, Test, UserProfileApiTest

### Community 174 - "DestinationImportRunner"
Cohesion: 0.48
Nodes (5): DestinationImportRunner, Component, Logger, CommandLineRunner, ConditionalOnProperty

### Community 175 - ".getBackendBaseUrl"
Cohesion: 0.40
Nodes (6): DisplayName, HttpClient, ObjectMapper, Tag, Test, WeatherApiTest

### Community 176 - "DestinationsDiscoveryTest"
Cohesion: 0.40
Nodes (4): DestinationsDiscoveryTest, BeforeEach, DisplayName, Test

### Community 177 - "AiApiTest.java"
Cohesion: 0.40
Nodes (7): AiApiTest, DisplayName, HttpClient, Logger, ObjectMapper, Tag, Test

### Community 178 - "CustomAccessDeniedHandler"
Cohesion: 0.30
Nodes (8): AccessDeniedException, AccessDeniedHandler, CustomAccessDeniedHandler, Component, HttpServletRequest, HttpServletResponse, ObjectMapper, Override

### Community 179 - "CustomAuthenticationEntryPoint"
Cohesion: 0.27
Nodes (8): AuthenticationEntryPoint, AuthenticationException, CustomAuthenticationEntryPoint, Component, HttpServletRequest, HttpServletResponse, ObjectMapper, Override

### Community 180 - "WeatherE2ETest.java"
Cohesion: 0.40
Nodes (5): DisplayName, ObjectMapper, Tag, Test, WeatherE2ETest

### Community 181 - "DestinationIngestionServiceTest"
Cohesion: 0.36
Nodes (6): DestinationIngestionServiceTest, ActiveProfiles, BeforeEach, MockRestServiceServer, SpringBootTest, Transactional

### Community 182 - "DatabaseMigrationConfig.java"
Cohesion: 0.36
Nodes (7): DatabaseMigrationConfig, Bean, Configuration, DataSource, Flyway, Logger, BeanFactoryPostProcessor

### Community 183 - "WeatherController.java"
Cohesion: 0.31
Nodes (6): GetMapping, RequestMapping, ResponseEntity, RestController, WeatherController, WeatherProvider

### Community 184 - "HealthController.java"
Cohesion: 0.48
Nodes (5): HealthController, GetMapping, RequestMapping, ResponseEntity, RestController

### Community 185 - "ItineraryItem"
Cohesion: 0.10
Nodes (5): ItineraryItem, Entity, PrePersist, PreUpdate, Table

### Community 186 - "ResourceNotFoundException"
Cohesion: 0.12
Nodes (4): Transactional, UpdatePreferencesRequest, ResourceNotFoundException, UpdateProfileRequest

### Community 187 - "PersistenceIntegrationTest.java"
Cohesion: 0.20
Nodes (6): ForbiddenException, Service, ActiveProfiles, SpringBootTest, Transactional, PersistenceIntegrationTest

### Community 188 - "Design Context"
Cohesion: 0.20
Nodes (9): 1. Component Inventory, 2. Design Review Table (Emil Kowalski Philosophy Alignment), Aesthetic & Token Specifications, Brand Personality, Design Context, Design Principles (Impeccable & Emil Kowalski Alignment), Phase 1 Implementation & Design Decisions, Users & Purpose (+1 more)

### Community 191 - "DestinationIngestionApiTest.java"
Cohesion: 0.38
Nodes (6): DestinationIngestionApiTest, DisplayName, HttpClient, ObjectMapper, Tag, Test

### Community 192 - "PersistenceTripApiTest.java"
Cohesion: 0.44
Nodes (6): DisplayName, HttpClient, ObjectMapper, Tag, Test, PersistenceTripApiTest

### Community 193 - "AiAssistantBrowserTest"
Cohesion: 0.42
Nodes (6): AiAssistantBrowserTest, DisplayName, Logger, ObjectMapper, Tag, Test

### Community 194 - "DestinationDetailModalTest"
Cohesion: 0.42
Nodes (4): DestinationDetailModalTest, BeforeEach, DisplayName, Test

### Community 195 - "HealthApiTest.java"
Cohesion: 0.50
Nodes (5): HealthApiTest, DisplayName, HttpClient, ObjectMapper, Test

### Community 196 - "TravelListingService"
Cohesion: 0.27
Nodes (6): RequestMapping, RestController, TravelListingController, Service, Transactional, TravelListingService

### Community 197 - "NavigationTest"
Cohesion: 0.62
Nodes (3): DisplayName, Test, NavigationTest

### Community 198 - "ResponsiveTest"
Cohesion: 0.67
Nodes (3): DisplayName, Test, ResponsiveTest

### Community 201 - "Voyage-AI — Open Decisions and Assumptions"
Cohesion: 0.40
Nodes (4): Assumptions, Provider selection checklist, Resolved Architecture & Implementation Decisions, Voyage-AI — Open Decisions and Assumptions

### Community 205 - "AiGenerateRequest"
Cohesion: 0.18
Nodes (6): AiController, RequestMapping, ResponseEntity, RestController, AiGenerateRequest, GeminiService

### Community 206 - "10. Prioritized Issues Requiring Changes & Recommended Fixes"
Cohesion: 0.33
Nodes (6): 10. Prioritized Issues Requiring Changes & Recommended Fixes, Issue 1: Administrative Endpoint Lacks Role Check (Severity: High), Issue 2: Wikimedia Commons Image Licensing Violation (Severity: High), Issue 3: Ingestion Falls Back to Synthetic Travel Metadata (Severity: Medium), Issue 4: Frontend Catalog Truncation and In-Memory Search (Severity: Medium), Issue 5: Automated Tests Lack Real MySQL CI Profile (Severity: Medium)

### Community 207 - "11. Remediation and Confirmed Fixes Applied"
Cohesion: 0.33
Nodes (6): 11.1 Remediation of Issue 1: Administrative Authorization Enforcement, 11.2 Remediation of Issue 2: Destination Provenance & Wikimedia Image Licensing, 11.3 Remediation of Issue 3: Elimination of Synthetic Travel Plans for Uncurated Cities, 11.4 Remediation of Issue 4: Full-Catalog Server-Side Search and Pagination, 11.5 Remediation of Issue 5: MySQL 8.0 Persistence & Isolation Verification, 11. Remediation and Confirmed Fixes Applied

### Community 208 - "3. Audit Area 1: Destination Source, Provenance, and Licensing"
Cohesion: 0.33
Nodes (6): 3.1 Provider Identification & API Inspected, 3.2 Provenance, Content Mapping & Licensing Findings, 3.3 Validation, Idempotency, and Rate Limiting, 3. Audit Area 1: Destination Source, Provenance, and Licensing, Detailed Finding 1.1: Wikimedia Commons Media Licensing Deficit (Severity: High), Detailed Finding 1.2: Synthetic Travel Metadata Fallbacks (Severity: Medium)

### Community 209 - "6.1 Inspection of `frontend/src/App.tsx`"
Cohesion: 0.33
Nodes (6): 6.1 Inspection of `frontend/src/App.tsx`, 6.2 Browser Test Verification Summary, 6. Audit Area 4: Destination Search and Frontend Behavior, Detailed Finding 4.1: Silent Catalog Truncation via `size=100` (Severity: Medium), Detailed Finding 4.2: Client-Only In-Memory Filtering (Severity: Medium), Detailed Finding 4.3: Missing Image Credits and Licensing Display in UI (Severity: Low)

### Community 210 - "7. Audit Area 5: Existing Feature Verification"
Cohesion: 0.33
Nodes (6): 7.1 User Profile Endpoints, 7.2 Trip Planning CRUD & Cross-User Security Isolation, 7.3 Real Google Gemini AI Integration (Zero Mocks), 7.4 Real Open-Meteo Weather Integration (Zero Mocks), 7.5 Destination Ingestion and Duplicate Handling, 7. Audit Area 5: Existing Feature Verification

### Community 212 - "5. Audit Area 3: MySQL and Flyway Verification"
Cohesion: 0.40
Nodes (5): 5.1 Flyway Migrations Compatibility, 5.2 Isolated Live MySQL 8.0 Verification, 5.3 Data Persistence & Application Restart Survival Verification, 5.4 Detailed Finding 3.1: Automated Tests Run Exclusively on H2 (Severity: Medium), 5. Audit Area 3: MySQL and Flyway Verification

### Community 213 - "4. Audit Area 2: Administrative Endpoint Security"
Cohesion: 0.50
Nodes (4): 4.1 Endpoint Security Configuration Inspection, 4.2 Detailed Finding 2.1: Missing Administrative Role Enforcement (Severity: High), 4.3 Startup Ingestion Guard Evaluation (Severity: Informational), 4. Audit Area 2: Administrative Endpoint Security

### Community 214 - "8. Summary of Tests Executed vs Not Executed"
Cohesion: 0.67
Nodes (3): 8.1 Tests Actually Executed by the Auditor, 8.2 Tests Not Executed and Technical Rationale, 8. Summary of Tests Executed vs Not Executed

## Knowledge Gaps
- **1124 isolated node(s):** `com.voyageai:voyage-ai-backend`, `ACCOMMODATION`, `RESTAURANT`, `ATTRACTION`, `ACTIVITY` (+1119 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **39 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ApiResponse` connect `ApiResponse` to `.importDestinations`, `.createTrip`, `.updateTrip`, `AiGenerateRequest`, `AiHealthResponse`, `WeatherE2ETest.java`, `WeatherController.java`, `HealthController.java`, `UserPrincipal`, `AuthController`?**
  _High betweenness centrality (0.094) - this node is a cross-community bridge._
- **Why does `WeatherResponse` connect `WeatherResponse` to `DailyForecastDTO`, `WeatherProvenanceDTO`, `WeatherUnitsDTO`, `.getWeather`, `WeatherController.java`, `CurrentData`, `CurrentWeatherDTO`, `.get`?**
  _High betweenness centrality (0.055) - this node is a cross-community bridge._
- **Why does `Destination` connect `Destination` to `DestinationSummaryResponse`, `ItineraryService`, `TravelListing`, `TravelListingResponse`, `DestinationRepository`, `DestinationIngestionService`, `WikimediaSummaryResponse`, `PersistenceIntegrationTest.java`, `.onCreate`, `.onUpdate`?**
  _High betweenness centrality (0.033) - this node is a cross-community bridge._
- **What connects `com.voyageai:voyage-ai-backend`, `ACCOMMODATION`, `RESTAURANT` to the rest of the system?**
  _1124 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `App.tsx` be split into smaller, more focused modules?**
  _Cohesion score 0.06766917293233082 - nodes in this community are weakly interconnected._
- **Should `devDependencies` be split into smaller, more focused modules?**
  _Cohesion score 0.11764705882352941 - nodes in this community are weakly interconnected._
- **Should `Voyage-AI — Software Requirements Specification (SRS)` be split into smaller, more focused modules?**
  _Cohesion score 0.05263157894736842 - nodes in this community are weakly interconnected._