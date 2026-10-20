# Graph Report - Voyage-AI  (2026-10-20)

## Corpus Check
- 105 files · ~384,186 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1374 nodes · 1383 edges · 92 communities (85 shown, 7 thin omitted)
- Extraction: 100% EXTRACTED · 0% INFERRED · 0% AMBIGUOUS
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `f02f4578`
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
- ErrorResponse
- package.json
- 3. Functional requirements
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
- HealthControllerTest.java
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
- VoyageAiApplication
- Voyage-AI Database Migrations Directory
- Voyage-AI — Project Documentation
- vite-env.d.ts
- tsconfig.json
- rules/graphify.md
- workflows/graphify.md
- com.voyageai:voyage-ai-backend

## God Nodes (most connected - your core abstractions)
1. `Apple Design` - 21 edges
2. `Write Swift` - 19 edges
3. `Capabilities` - 18 edges
4. `compilerOptions` - 17 edges
5. `Design Engineering` - 16 edges
6. `Animation Standards Reference` - 16 edges
7. `Animation Recipes` - 15 edges
8. `Voyage-AI — Product Requirements Document (PRD)` - 15 edges
9. `Voyage-AI — Technical Design Document (TDD)` - 15 edges
10. `ErrorResponse` - 14 edges

## Surprising Connections (you probably didn't know these)
- `DestinationCardProps` --references--> `Destination`  [EXTRACTED]
  frontend/src/components/discovery/DestinationCard.tsx → frontend/src/types/destination.ts
- `DestinationDetailModalProps` --references--> `Destination`  [EXTRACTED]
  frontend/src/components/discovery/DestinationDetailModal.tsx → frontend/src/types/destination.ts
- `App()` --references--> `MOCK_DESTINATIONS`  [EXTRACTED]
  frontend/src/App.tsx → frontend/src/data/mockDestinations.ts
- `FilterBarProps` --references--> `BudgetTier`  [EXTRACTED]
  frontend/src/components/discovery/FilterBar.tsx → frontend/src/types/destination.ts
- `FilterBarProps` --references--> `Region`  [EXTRACTED]
  frontend/src/components/discovery/FilterBar.tsx → frontend/src/types/destination.ts

## Import Cycles
- None detected.

## Communities (92 total, 7 thin omitted)

### Community 0 - "App.tsx"
Cohesion: 0.08
Nodes (31): App(), Badge(), BadgeProps, Button(), ButtonProps, EmptyState(), EmptyStateProps, SkeletonCard() (+23 more)

### Community 1 - "devDependencies"
Cohesion: 0.05
Nodes (39): autoprefixer, eslint, @eslint/js, eslint-plugin-react-hooks, eslint-plugin-react-refresh, devDependencies, autoprefixer, eslint (+31 more)

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

### Community 17 - "ErrorResponse"
Cohesion: 0.15
Nodes (8): ErrorResponse, GlobalExceptionHandler, ResponseEntity, ExceptionHandler, Logger, MethodArgumentNotValidException, ResponseStatusException, RestControllerAdvice

### Community 18 - "package.json"
Cohesion: 0.09
Nodes (22): clsx, dependencies, clsx, lucide-react, react, react-dom, tailwind-merge, name (+14 more)

### Community 19 - "3. Functional requirements"
Cohesion: 0.09
Nodes (22): 1. Purpose, 2. Roles, 3. Functional requirements, 4. Business rules, 5. Main use cases, 6. Acceptance tests for key workflows, 7. Dependencies, A. Authentication and profile (+14 more)

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
Nodes (6): ApiResponse, HealthController, ResponseEntity, GetMapping, RequestMapping, RestController

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
Cohesion: 0.11
Nodes (18): 1. Design objective, 2. Experience principles, 3. Proposed sitemap, 4. Key screen requirements, 5. Design system to define before page implementation, 6. Component inventory, 7. Interaction states, 8. Responsive behavior (+10 more)

### Community 37 - "Finding Animation Opportunities"
Cohesion: 0.12
Nodes (16): 1. Frequency — how often will a user see this?, 2. Purpose — why does this animate?, 3. Speed — can it stay inside budget?, 4. Function — does motion help or hinder here?, Finding Animation Opportunities, Hard Rules, Initial Response, Operating Posture (+8 more)

### Community 38 - "Polish Systematically"
Cohesion: 0.12
Nodes (16): Code Quality, Color & Contrast, Content & Copy, Edge Cases & Error States, Final Verification, Forms & Inputs, Icons & Images, Interaction States (+8 more)

### Community 39 - "compilerOptions"
Cohesion: 0.12
Nodes (16): compilerOptions, isolatedModules, lib, module, moduleDetection, moduleResolution, noEmit, noFallthroughCasesInSwitch (+8 more)

### Community 40 - "Voyage-AI — All-in-One Travel Companion"
Cohesion: 0.12
Nodes (16): 1. Prerequisites & Required Tools, 2. Port Allocation & Host Collision Avoidance, 3. Quick Start & Local Setup, 4. Development & Build Commands, 5. Architecture & Project Layout, 6. Approved Architecture Decisions & Deferred Items, Approved Decisions, Backend (`backend/`) (+8 more)

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
Cohesion: 0.25
Nodes (7): 1. Overview & Major Components, 2. Communication and Data Flows, 3. Primary Data Stores & Persistence, 4. Authentication & Authorization Boundaries, 5. Major External Dependencies & Integrations, 6. Deployment Boundaries, Voyage-AI — High-Level System Architecture

### Community 63 - "HealthControllerTest.java"
Cohesion: 0.43
Nodes (6): AutoConfigureMockMvc, HealthControllerTest, ActiveProfiles, SpringBootTest, Test, MockMvc

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

### Community 79 - "Voyage-AI Database Migrations Directory"
Cohesion: 0.50
Nodes (3): Migration Naming Convention, Phase Boundaries, Voyage-AI Database Migrations Directory

### Community 80 - "Voyage-AI — Project Documentation"
Cohesion: 0.50
Nodes (4): Baseline decisions, Documents, How to use this pack, Voyage-AI — Project Documentation

## Knowledge Gaps
- **994 isolated node(s):** `com.voyageai:voyage-ai-backend`, `name`, `private`, `version`, `type` (+989 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **7 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Design Engineering` connect `Design Engineering` to `The Animation Decision Framework`, `clip-path for Animation`, `Performance Rules`, `Gesture and Drag Interactions`, `CSS Transform Mastery`, `The Sonner Principles (Building Loved Components)`, `Spring Animations`, `Core Philosophy`, `Debugging Animations`, `Component Building Principles`?**
  _High betweenness centrality (0.002) - this node is a cross-community bridge._
- **What connects `com.voyageai:voyage-ai-backend`, `name`, `private` to the rest of the system?**
  _994 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `App.tsx` be split into smaller, more focused modules?**
  _Cohesion score 0.08484848484848485 - nodes in this community are weakly interconnected._
- **Should `devDependencies` be split into smaller, more focused modules?**
  _Cohesion score 0.05128205128205128 - nodes in this community are weakly interconnected._
- **Should `Voyage-AI — Software Requirements Specification (SRS)` be split into smaller, more focused modules?**
  _Cohesion score 0.05263157894736842 - nodes in this community are weakly interconnected._
- **Should `Voyage-AI — Pre-Implementation Environment Audit & Product Readiness Report` be split into smaller, more focused modules?**
  _Cohesion score 0.05555555555555555 - nodes in this community are weakly interconnected._
- **Should `Expo Animation Recipes` be split into smaller, more focused modules?**
  _Cohesion score 0.05714285714285714 - nodes in this community are weakly interconnected._