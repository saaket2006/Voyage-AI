# Graph Report - Voyage-AI  (2026-10-25)

## Corpus Check
- 233 files · ~318,308 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 2984 nodes · 4425 edges · 201 communities (156 shown, 45 thin omitted)
- Extraction: 89% EXTRACTED · 11% INFERRED · 0% AMBIGUOUS · INFERRED: 487 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `34b27627`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- App.tsx
- devDependencies
- Voyage-AI — Software Requirements Specification (SRS)
- Voyage-AI — Pre-Implementation Environment Audit & Product Readiness Report
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
- ApiResponse
- Animation Performance
- 2. Token Lifecycle & Transport
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
- Design Context
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
- Voyage-AI — Open Decisions and Assumptions
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
- DestinationsDiscoveryTest
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
- ItineraryItem
- Trip
- ItineraryDay
- Voyage-AI — System Architecture & Technical Design Specification
- User
- UserProfileResponse
- ProvenanceDto
- GoogleGeminiService
- ListingType
- GeminiServiceTest
- Voyage-AI — Comprehensive Project Report
- TripApiTest.java
- UserPrincipal
- Voyage-AI — Engineering Methodology & Operational Workflows
- LoginRequest
- AiGenerateRequest
- AiHealthResponse
- AiControllerTest.java
- DestinationService
- AiGenerateResponse
- .register
- UserRepository
- JwtTokenProvider
- Voyage-AI — Testing Strategy & Software Quality Assessment
- GeminiApiResponse
- 3. Functional requirements
- 3. Actionable Implementation Plan
- DestinationRepository
- ItineraryItemResponse
- Voyage-AI — Documentation Suite Index & Navigation Guide
- 4. Key screen requirements
- generate_pdfs.py
- TripRepository
- Voyage-AI — Implementation Status & Feature Inventory
- Voyage-AI — Post-Implementation Verification & Final Acceptance Report
- .updateItem
- GeminiServiceException
- TripService
- .onCreate
- .onUpdate
- .updateTrip
- .addItemToDay
- AuthApiTest
- .createTrip
- .addDayToTrip
- ErrorResponse
- PersistenceIntegrationTest.java
- .getBackendBaseUrl
- CustomAccessDeniedHandler
- CustomAuthenticationEntryPoint
- JwtAuthenticationFilter
- WeatherE2ETest.java
- DatabaseMigrationConfig.java
- WeatherController.java
- DestinationDetailModalTest
- .getId
- TestConfig
- HealthController.java
- ResourceNotFoundException
- NavigationTest
- AiApiTest.java
- ResponsiveTest
- ItineraryMapper.java
- PersistenceTripApiTest.java
- AiAssistantBrowserTest
- .getListings
- UpdatePreferencesRequest
- .handleUnauthorizedException
- .onCreate
- .onUpdate

## God Nodes (most connected - your core abstractions)
1. `TravelListing` - 78 edges
2. `Destination` - 69 edges
3. `TravelListingResponse` - 67 edges
4. `Trip` - 51 edges
5. `TripResponse` - 45 edges
6. `DestinationSummaryResponse` - 44 edges
7. `ItineraryItem` - 43 edges
8. `ApiResponse` - 42 edges
9. `ItineraryItemResponse` - 40 edges
10. `ItineraryDay` - 37 edges

## Surprising Connections (you probably didn't know these)
- `AuthController` --references--> `AuthService`  [EXTRACTED]
  backend/src/main/java/com/voyageai/auth/AuthController.java → backend/src/main/java/com/voyageai/auth/AuthService.java
- `AuthService` --references--> `JwtTokenProvider`  [EXTRACTED]
  backend/src/main/java/com/voyageai/auth/AuthService.java → backend/src/main/java/com/voyageai/security/JwtTokenProvider.java
- `UserController` --references--> `AuthService`  [EXTRACTED]
  backend/src/main/java/com/voyageai/user/UserController.java → backend/src/main/java/com/voyageai/auth/AuthService.java
- `TripApiTest` --references--> `AuthService`  [EXTRACTED]
  backend/src/test/java/com/voyageai/trip/TripApiTest.java → backend/src/main/java/com/voyageai/auth/AuthService.java
- `DestinationSummaryResponse` --references--> `ProvenanceDto`  [EXTRACTED]
  backend/src/main/java/com/voyageai/destination/dto/DestinationSummaryResponse.java → backend/src/main/java/com/voyageai/common/dto/ProvenanceDto.java

## Import Cycles
- None detected.

## Communities (201 total, 45 thin omitted)

### Community 0 - "App.tsx"
Cohesion: 0.07
Nodes (38): App(), AiTravelAssistant(), AiTravelAssistantProps, GenerateResponse, HealthResponse, SAMPLE_PROMPTS, Badge(), BadgeProps (+30 more)

### Community 1 - "devDependencies"
Cohesion: 0.12
Nodes (17): autoprefixer, eslint, devDependencies, autoprefixer, eslint, globals, jsdom, @types/react (+9 more)

### Community 2 - "Voyage-AI — Software Requirements Specification (SRS)"
Cohesion: 0.05
Nodes (37): 10. SRS acceptance criteria, 1.1 Purpose, 1.2 Scope, 1.3 Definitions, 1.4 References, 1. Introduction, 2.1 Product perspective, 2.2 User classes (+29 more)

### Community 3 - "Voyage-AI — Pre-Implementation Environment Audit & Product Readiness Report"
Cohesion: 0.06
Nodes (35): 10. Prioritized Remediation Checklist, 11. Implementation-Readiness Verdict, 12.1 Verification Evidence Summary, 12.2 Limitations & Unverified Items, 12. Evidence, Limitations, and Items Unverified, 1. Executive Summary, 2.1 Audit Scope, 2.2 System & Host Environment (+27 more)

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

### Community 31 - "ApiResponse"
Cohesion: 0.15
Nodes (6): AuthController, PostMapping, RequestMapping, ResponseEntity, RestController, ApiResponse

### Community 32 - "Animation Performance"
Cohesion: 0.11
Nodes (17): Animation Performance, Hard Rules, Initial Response, Operating Posture, Phase 1: Find what animates, Phase 2: Diagnose, Phase 3: Report, React: Don't Animate Through State (+9 more)

### Community 33 - "2. Token Lifecycle & Transport"
Cohesion: 0.11
Nodes (17): 1. Overview & Strategy, 2.1 Token Issuance, 2.2 Client-Side Storage: In-Memory Only, 2.3 Token Transport, 2.4 Token Expiry & Expired Token Handling, 2.5 Logout & Client-Side Token Removal, 2.6 Server-Side Invalidation Expectations & Limitations, 2. Token Lifecycle & Transport (+9 more)

### Community 34 - "2. Token Lifecycle & Transport"
Cohesion: 0.11
Nodes (17): 1. Overview & Strategy, 2.1 Token Issuance, 2.2 Client-Side Storage: In-Memory Only, 2.3 Token Transport, 2.4 Token Expiry & Expired Token Handling, 2.5 Logout & Client-Side Token Removal, 2.6 Server-Side Invalidation Expectations & Limitations, 2. Token Lifecycle & Transport (+9 more)

### Community 35 - "Voyage-AI — Product Requirements Document (PRD)"
Cohesion: 0.11
Nodes (18): 10. Success measures, 11. Assumptions and constraints, 12. Risks, 13. Release acceptance, 14. Next documents, 1. Executive summary, 2. Problem, 3. Vision (+10 more)

### Community 36 - "Voyage-AI — UI/UX Specification"
Cohesion: 0.22
Nodes (9): 1. Design objective, 2. Experience principles, 3. Proposed sitemap, 5. Design system to define before page implementation, 6. Component inventory, 7. Interaction states, 8. Responsive behavior, 9. Design acceptance criteria (+1 more)

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
Cohesion: 0.09
Nodes (21): 1. Prerequisites & Required Tools, 2. Port Allocation & Host Collision Avoidance, 3. Quick Start & Local Setup, 4. Automated Testing Framework, 5. Development & Build Commands, 6. Project Layout & Directory Structure, 7. Data Authenticity & Architecture Principles, Backend (`backend/`) (+13 more)

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
Nodes (11): Agent execution rules, Guiding rule, Phase 0 — Repository and setup, Phase 1 — Design foundation, Phase 2 — Backend and persistence foundation, Phase 3 — First complete vertical slice, Phase 4 — Itinerary and budget, Phase 5 — AI integration (+3 more)

### Community 54 - "Voyage-AI — Non-Functional Requirements Document (NFRD)"
Cohesion: 0.18
Nodes (11): 10. Initial acceptance targets, 1. Purpose, 2. Performance, 3. Security and privacy, 4. Reliability and resilience, 5. Usability and accessibility, 6. Maintainability, 7. Testability (+3 more)

### Community 55 - "mvnw"
Cohesion: 0.33
Nodes (6): mvnw script, clean(), die(), exec_maven(), set_java_home(), verbose()

### Community 56 - "Design Context"
Cohesion: 0.20
Nodes (9): 1. Component Inventory, 2. Design Review Table (Emil Kowalski Philosophy Alignment), Aesthetic & Token Specifications, Brand Personality, Design Context, Design Principles (Impeccable & Emil Kowalski Alignment), Phase 1 Implementation & Design Decisions, Users & Purpose (+1 more)

### Community 57 - "Design Engineering"
Cohesion: 0.22
Nodes (8): Accessibility, Design Engineering, Initial Response, prefers-reduced-motion, Review Checklist, Review Format (Required), Stagger Animations, Touch device hover states

### Community 58 - "docs/README.md"
Cohesion: 0.15
Nodes (6): Baseline decisions, Documents, How to use this pack, Voyage-AI — Project Documentation, Course-outcome coverage notes, Voyage-AI — Requirements Traceability Matrix

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

### Community 74 - "Voyage-AI — Open Decisions and Assumptions"
Cohesion: 0.40
Nodes (4): Assumptions, Defaults for starting implementation, Provider selection checklist, Voyage-AI — Open Decisions and Assumptions

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
Nodes (3): Entity, Table, TravelListing

### Community 82 - "BasePlaywrightTest"
Cohesion: 0.15
Nodes (11): AfterAll, AfterEach, Browser, BrowserContext, Playwright, TestInfo, BasePlaywrightTest, BeforeAll (+3 more)

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
Nodes (11): 1. Overview & Test Architecture, 2. Test Suites Summary, 3. Database Safety Mandate, 4. Execution Commands, 5. Prerequisites for Local Testing, Backend Unit, Controller & Database Tests, Frontend Unit & Component Tests (Vitest), Full Local Test Suite (All Layers) (+3 more)

### Community 102 - ".getWeather"
Cohesion: 0.22
Nodes (7): Override, HttpStatus, WeatherServiceException, DisplayName, MockRestServiceServer, Test, OpenMeteoWeatherServiceTest

### Community 103 - "DestinationsDiscoveryTest"
Cohesion: 0.40
Nodes (4): DestinationsDiscoveryTest, BeforeEach, DisplayName, Test

### Community 104 - "Destination"
Cohesion: 0.03
Nodes (3): Destination, Entity, Table

### Community 105 - "scripts"
Cohesion: 0.25
Nodes (8): scripts, build, dev, lint, preview, test, test:unit, typecheck

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

### Community 130 - "ItineraryItem"
Cohesion: 0.10
Nodes (5): ItineraryItem, Entity, PrePersist, PreUpdate, Table

### Community 131 - "Trip"
Cohesion: 0.07
Nodes (5): Entity, PrePersist, PreUpdate, Table, Trip

### Community 132 - "ItineraryDay"
Cohesion: 0.06
Nodes (6): ItineraryDayResponse, ItineraryDay, Entity, PrePersist, PreUpdate, Table

### Community 133 - "Voyage-AI — System Architecture & Technical Design Specification"
Cohesion: 0.06
Nodes (30): 1.1 Project Purpose & System Boundaries, 1.2 Core Architectural Decisions & Trade-Offs, 1. Executive Architecture Overview, 2. Verified Technology Stack, 3.1 Backend Module Architecture (`com.voyageai`), 3.2 Frontend Component Architecture (`frontend/src`), 3. Component Architecture & Domain Boundaries, 4.1 System Context Diagram (+22 more)

### Community 134 - "User"
Cohesion: 0.11
Nodes (6): Transactional, Entity, PrePersist, PreUpdate, Table, User

### Community 137 - "GoogleGeminiService"
Cohesion: 0.14
Nodes (10): DotenvLoader, Logger, GoogleGeminiService, Autowired, Logger, RestClient, Service, BeforeAll (+2 more)

### Community 138 - "ListingType"
Cohesion: 0.13
Nodes (10): ListingType, ACCOMMODATION, ACTIVITY, ATTRACTION, RESTAURANT, Repository, TravelListingRepository, Service (+2 more)

### Community 139 - "GeminiServiceTest"
Cohesion: 0.28
Nodes (4): Override, GeminiServiceTest, DisplayName, Test

### Community 140 - "Voyage-AI — Comprehensive Project Report"
Cohesion: 0.10
Nodes (20): 1. Executive Summary, 2.1 The Crisis of Modern Travel Planning, 2.2 Core Project Objectives, 2. Problem Statement & Strategic Objectives, 3. Project Scope & Deliverables, 4.1 High-Level Architecture, 4.2 Verified Technology Summary, 4. System Overview & Technology Stack (+12 more)

### Community 141 - "TripApiTest.java"
Cohesion: 0.07
Nodes (11): AuthResponse, UserSummaryDto, ActiveProfiles, AutoConfigureMockMvc, BeforeEach, DisplayName, MockMvc, ObjectMapper (+3 more)

### Community 142 - "UserPrincipal"
Cohesion: 0.20
Nodes (4): Override, UserPrincipal, GrantedAuthority, UserDetails

### Community 143 - "Voyage-AI — Engineering Methodology & Operational Workflows"
Cohesion: 0.11
Nodes (18): 1.1 Evidenced Engineering Process, 1.2 Module Implementation Sequence, 1.3 Version Control & Branching Strategy, 1. Development Methodology & Engineering Lifecycle, 2.1 Workflow 1: Destination Discovery & Multi-Criteria Filtering, 2.2 Workflow 2: Weather Forecast Retrieval & Provenance Tracking, 2.3 Workflow 3: Live Backend Health Monitoring, 2. End-to-End Operational Workflows (+10 more)

### Community 145 - "AiGenerateRequest"
Cohesion: 0.17
Nodes (7): AiController, PostMapping, RequestMapping, ResponseEntity, RestController, AiGenerateRequest, GeminiService

### Community 146 - "AiHealthResponse"
Cohesion: 0.12
Nodes (9): GetMapping, AiHealthResponse, GeminiApiIntegrationTest, DisplayName, Logger, Tag, Test, BeforeEach (+1 more)

### Community 147 - "AiControllerTest.java"
Cohesion: 0.28
Nodes (7): AiControllerTest, ActiveProfiles, AutoConfigureMockMvc, DisplayName, MockMvc, SpringBootTest, Test

### Community 148 - "DestinationService"
Cohesion: 0.19
Nodes (10): DestinationController, GetMapping, RequestMapping, ResponseEntity, RestController, DestinationService, Page, Pageable (+2 more)

### Community 151 - "UserRepository"
Cohesion: 0.46
Nodes (5): AuthService, PasswordEncoder, Service, Repository, UserRepository

### Community 152 - "JwtTokenProvider"
Cohesion: 0.28
Nodes (4): Component, Logger, ObjectMapper, JwtTokenProvider

### Community 153 - "Voyage-AI — Testing Strategy & Software Quality Assessment"
Cohesion: 0.15
Nodes (12): 1. Testing Frameworks, Tools & Quality Architecture, 2. Test Directory Structure & Organization, 3.1 Backend Test Execution Audit (`mvn test` in `backend/`), 3.2 Frontend Test Execution Audit (`npm run test:unit` in `frontend/`), 3. Verified Test Execution Results, 4.1 Strict Test Database Isolation Guard, 4. Test Isolation & Database Safety Guarantees, 5.1 React `act(...)` Warning in `App.test.tsx` (+4 more)

### Community 154 - "GeminiApiResponse"
Cohesion: 0.30
Nodes (9): Content, GeminiApiRequest, Part, Candidate, Content, GeminiApiResponse, JsonIgnoreProperties, Part (+1 more)

### Community 155 - "3. Functional requirements"
Cohesion: 0.20
Nodes (10): 3. Functional requirements, A. Authentication and profile, B. Destination discovery, C. Travel listings, D. Trip setup, E. Recommendations and AI, F. Itinerary management, G. Budget (+2 more)

### Community 156 - "3. Actionable Implementation Plan"
Cohesion: 0.20
Nodes (9): 1. Technical Risk Register, 2. Prioritized Engineering Roadmap, 3.1 Task 1 (P0): Relational Schema & Persistence Foundation (Phase 2 Target), 3.2 Task 2 (P0): Stateless JWT Security & Ownership Enforcement, 3.3 Task 3 (P1): Dynamic Discovery Catalog Integration, 3.4 Task 4 (P2): Grounded AI Itinerary Proposal Generator (Phase 5 Target), 3. Actionable Implementation Plan, 4. Verification Milestones & Quality Gates (+1 more)

### Community 157 - "DestinationRepository"
Cohesion: 0.29
Nodes (5): DestinationRepository, Page, Pageable, Query, Repository

### Community 159 - "Voyage-AI — Documentation Suite Index & Navigation Guide"
Cohesion: 0.22
Nodes (8): 1. Overview & Purpose of the Documentation Suite, 2. Master Report Directory & Reading Order, 3.1 Implemented & Verified Baseline (Phase 1), 3.2 Status Classification Key, 3. Current Project State & Documentation Scope, 4. Canonical Repository Documentation References, Recommended Reading Sequences:, Voyage-AI — Documentation Suite Index & Navigation Guide

### Community 160 - "4. Key screen requirements"
Cohesion: 0.22
Nodes (9): 4. Key screen requirements, Budget, Destination details, Itinerary builder, Landing / Discover, Listing details, My Trips, Search results (+1 more)

### Community 161 - "generate_pdfs.py"
Cohesion: 0.36
Nodes (7): generate_pdf_via_edge(), main(), Converts Markdown text to full HTML5 with custom typography and CSS., Invokes Microsoft Edge Chromium in headless mode to render HTML to PDF., Validates the PDF using PyMuPDF (fitz). Returns dict with page count, size, and…, render_markdown_to_html(), validate_pdf()

### Community 162 - "TripRepository"
Cohesion: 0.16
Nodes (12): ItineraryController, RequestMapping, RestController, ItineraryDayRepository, Repository, ItineraryItemRepository, Repository, ItineraryService (+4 more)

### Community 163 - "Voyage-AI — Implementation Status & Feature Inventory"
Cohesion: 0.40
Nodes (4): 1. Status Classification Definitions, 2. Comprehensive Implementation Status Inventory, 3. Comprehensive Verification Summary, Voyage-AI — Implementation Status & Feature Inventory

### Community 164 - "Voyage-AI — Post-Implementation Verification & Final Acceptance Report"
Cohesion: 0.12
Nodes (15): 1. Executive Summary, 2. Requirement-to-Implementation Traceability Matrix, 3.1 Test Suite Summary Breakdown, 3.2 Individual Test Suite Results, 3. Comprehensive Test Execution Evidence, 4. Live Google Gemini AI Verification Details, 5. Persistence & Multi-Tenant Security Verification Details, 6. Zero Fabrication & Data Safety Policy Compliance (+7 more)

### Community 165 - ".updateItem"
Cohesion: 0.10
Nodes (3): UpdateItineraryItemRequest, PutMapping, Transactional

### Community 167 - "TripService"
Cohesion: 0.14
Nodes (9): DeleteMapping, GetMapping, RequestMapping, ResponseEntity, RestController, TripController, Service, Transactional (+1 more)

### Community 172 - "AuthApiTest"
Cohesion: 0.20
Nodes (9): AuthApiTest, ActiveProfiles, AutoConfigureMockMvc, BeforeEach, DisplayName, MockMvc, ObjectMapper, SpringBootTest (+1 more)

### Community 176 - "PersistenceIntegrationTest.java"
Cohesion: 0.24
Nodes (6): ActiveProfiles, DisplayName, SpringBootTest, Test, Transactional, PersistenceIntegrationTest

### Community 177 - ".getBackendBaseUrl"
Cohesion: 0.40
Nodes (6): DisplayName, HttpClient, ObjectMapper, Tag, Test, WeatherApiTest

### Community 178 - "CustomAccessDeniedHandler"
Cohesion: 0.30
Nodes (8): AccessDeniedException, AccessDeniedHandler, CustomAccessDeniedHandler, Component, HttpServletRequest, HttpServletResponse, ObjectMapper, Override

### Community 179 - "CustomAuthenticationEntryPoint"
Cohesion: 0.27
Nodes (8): AuthenticationEntryPoint, AuthenticationException, CustomAuthenticationEntryPoint, Component, HttpServletRequest, HttpServletResponse, ObjectMapper, Override

### Community 180 - "JwtAuthenticationFilter"
Cohesion: 0.31
Nodes (7): Component, HttpServletRequest, HttpServletResponse, Override, JwtAuthenticationFilter, FilterChain, OncePerRequestFilter

### Community 181 - "WeatherE2ETest.java"
Cohesion: 0.40
Nodes (5): DisplayName, ObjectMapper, Tag, Test, WeatherE2ETest

### Community 182 - "DatabaseMigrationConfig.java"
Cohesion: 0.36
Nodes (7): DatabaseMigrationConfig, Bean, Configuration, DataSource, Flyway, Logger, BeanFactoryPostProcessor

### Community 183 - "WeatherController.java"
Cohesion: 0.31
Nodes (6): GetMapping, RequestMapping, ResponseEntity, RestController, WeatherController, WeatherProvider

### Community 184 - "DestinationDetailModalTest"
Cohesion: 0.42
Nodes (4): DestinationDetailModalTest, BeforeEach, DisplayName, Test

### Community 186 - ".getId"
Cohesion: 0.16
Nodes (8): DeleteMapping, ResponseEntity, GetMapping, PutMapping, RequestMapping, ResponseEntity, RestController, UserController

### Community 187 - "TestConfig"
Cohesion: 0.30
Nodes (6): HealthApiTest, DisplayName, HttpClient, ObjectMapper, Test, TestConfig

### Community 188 - "HealthController.java"
Cohesion: 0.48
Nodes (5): HealthController, GetMapping, RequestMapping, ResponseEntity, RestController

### Community 189 - "ResourceNotFoundException"
Cohesion: 0.21
Nodes (3): ForbiddenException, ResourceNotFoundException, Service

### Community 190 - "NavigationTest"
Cohesion: 0.62
Nodes (3): DisplayName, Test, NavigationTest

### Community 191 - "AiApiTest.java"
Cohesion: 0.40
Nodes (7): AiApiTest, DisplayName, HttpClient, Logger, ObjectMapper, Tag, Test

### Community 192 - "ResponsiveTest"
Cohesion: 0.67
Nodes (3): DisplayName, Test, ResponsiveTest

### Community 194 - "PersistenceTripApiTest.java"
Cohesion: 0.44
Nodes (6): DisplayName, HttpClient, ObjectMapper, Tag, Test, PersistenceTripApiTest

### Community 195 - "AiAssistantBrowserTest"
Cohesion: 0.42
Nodes (6): AiAssistantBrowserTest, DisplayName, Logger, ObjectMapper, Tag, Test

### Community 196 - ".getListings"
Cohesion: 0.31
Nodes (5): GetMapping, RequestMapping, ResponseEntity, RestController, TravelListingController

## Knowledge Gaps
- **1118 isolated node(s):** `com.voyageai:voyage-ai-backend`, `ACCOMMODATION`, `RESTAURANT`, `ATTRACTION`, `ACTIVITY` (+1113 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **45 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ApiResponse` connect `ApiResponse` to `.getListings`, `.updateItem`, `TripService`, `.updateTrip`, `.addItemToDay`, `.createTrip`, `.addDayToTrip`, `AiGenerateRequest`, `AiHealthResponse`, `DestinationService`, `WeatherE2ETest.java`, `WeatherController.java`, `.getId`, `HealthController.java`?**
  _High betweenness centrality (0.063) - this node is a cross-community bridge._
- **Why does `WeatherResponse` connect `WeatherResponse` to `DailyForecastDTO`, `WeatherProvenanceDTO`, `WeatherUnitsDTO`, `.getWeather`, `WeatherController.java`, `CurrentData`, `CurrentWeatherDTO`, `.get`?**
  _High betweenness centrality (0.036) - this node is a cross-community bridge._
- **Why does `ResourceNotFoundException` connect `ResourceNotFoundException` to `.updateItem`, `User`, `TripService`, `ListingType`, `.addItemToDay`, `.updateTrip`, `.createTrip`, `.addDayToTrip`, `GlobalExceptionHandler`, `DestinationService`, `UserRepository`?**
  _High betweenness centrality (0.033) - this node is a cross-community bridge._
- **What connects `com.voyageai:voyage-ai-backend`, `ACCOMMODATION`, `RESTAURANT` to the rest of the system?**
  _1118 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `App.tsx` be split into smaller, more focused modules?**
  _Cohesion score 0.06948051948051948 - nodes in this community are weakly interconnected._
- **Should `devDependencies` be split into smaller, more focused modules?**
  _Cohesion score 0.11764705882352941 - nodes in this community are weakly interconnected._
- **Should `Voyage-AI — Software Requirements Specification (SRS)` be split into smaller, more focused modules?**
  _Cohesion score 0.05263157894736842 - nodes in this community are weakly interconnected._