# Voyage-AI — Functional Requirements Document (FRD)

**Version:** 0.1 · **Status:** Baseline

## 1. Purpose

Define user-visible functions and business rules for the Voyage-AI MVP. Requirements use IDs so they can be traced to implementation and tests.

## 2. Roles

- **Visitor:** can browse public destination/listing content and access sign-up/sign-in.
- **Registered traveler:** can manage their profile, preferences, shortlists, trips, and itineraries.
- **Administrator/maintainer:** manages curated seed content and operational configuration through controlled tooling or database seeding. A full admin portal is not required for MVP unless separately approved.

## 3. Functional requirements

### A. Authentication and profile

**FR-001 — Registration:** The system shall allow a visitor to create an account using the selected authentication method and valid required fields.

**FR-002 — Sign in/out:** Users shall be able to sign in and sign out. Protected API routes shall reject unauthenticated requests.

**FR-003 — Profile:** A registered user shall be able to view and update permitted profile fields.

**FR-004 — Preferences:** Users shall be able to maintain travel interests and default preferences such as budget style, preferred activities, and dietary or accessibility notes if the product elects to collect them.

**FR-005 — Ownership:** Users shall only view, modify, or delete trips and private saved items they own.

### B. Destination discovery

**FR-010 — Browse:** Users shall be able to browse destination cards with available image, name, region, summary, and relevant tags.

**FR-011 — Search:** Users shall be able to search destinations by supported text fields.

**FR-012 — Filter/sort:** Users shall be able to filter or sort by available attributes such as region, category, season, or estimated budget. Unsupported fields must not be presented as functional filters.

**FR-013 — Destination detail:** A destination detail page shall display available overview information and related stays, restaurants, attractions, and activities.

**FR-014 — Data provenance:** When practical, listings with externally sourced or time-sensitive details shall show source/freshness information.

### C. Travel listings

**FR-020 — Accommodation list:** Users shall be able to browse accommodation listings linked to a destination or geographic area.

**FR-021 — Accommodation detail:** Listing details shall show available name, description, location, amenities, image, price estimate/range, and external link fields.

**FR-022 — Restaurant discovery:** Users shall be able to browse restaurant listings and available cuisine, location, price range, opening hours, and rating fields.

**FR-023 — Attraction/activity discovery:** Users shall be able to browse attractions and activities and view available description, location, expected duration, timing, cost, and access details.

**FR-024 — Shortlist:** Users should be able to save and remove destinations or listings from a private shortlist. If excluded from the first build, it must not block the trip workflow.

**FR-025 — External links:** The system may open a provider's official or booking page when a verified URL is available. Voyage-AI must not imply that an external booking has been completed.

### D. Trip setup

**FR-030 — Create trip:** A registered user shall be able to create a trip with title, destination or destination preferences, start/end dates, traveler count, target budget, currency, and interests where applicable.

**FR-031 — Validate trip dates:** End date shall not precede start date. Invalid or missing required values shall receive actionable validation messages.

**FR-032 — Edit trip details:** The owner shall be able to update trip metadata.

**FR-033 — Trip dashboard:** The owner shall be able to list trips and identify their destination, dates, and planning status.

### E. Recommendations and AI

**FR-040 — Destination recommendations:** Given sufficient input, the system shall return destination suggestions relevant to user preferences. The UI shall state when suggestions are based on limited or seeded data.

**FR-041 — Itinerary generation:** The system shall accept a trip and selected context and request a structured itinerary proposal.

**FR-042 — Structured output:** An itinerary proposal shall contain days and ordered items with fields such as title, category, suggested time, duration, location/reference, estimated cost if available, and rationale where supported.

**FR-043 — Constraint handling:** The system shall validate generated output against trip dates and basic schema rules. It shall flag unsupported, conflicting, or incomplete details instead of silently treating them as confirmed.

**FR-044 — Regeneration/refinement:** Users shall be able to request a revised proposal using changed preferences or feedback, subject to configured AI limits.

**FR-045 — AI fallback:** If AI service is unavailable, the user shall receive a clear message and retain the ability to build/edit the itinerary manually.

**FR-046 — No fabricated certainty:** Generated output shall be labelled as suggested/estimated. It must not present unverified prices, opening hours, travel times, or reservations as facts.

### F. Itinerary management

**FR-050 — Create itinerary manually:** Users shall be able to create days and add items without using AI.

**FR-051 — Add item:** Users shall be able to add a discovered listing or custom activity to a trip day.

**FR-052 — Edit item:** Users shall be able to update title, notes, date/day, start time, duration, and other supported fields.

**FR-053 — Reorder items:** Users shall be able to reorder itinerary items within a day. Cross-day movement should be supported if practical.

**FR-054 — Remove item:** Users shall be able to remove an item after an appropriate confirmation or undo opportunity.

**FR-055 — Save and reload:** Changes shall persist and be retrievable after sign-out/sign-in or a later session.

**FR-056 — Trip deletion:** The owner shall be able to delete a trip. The system shall apply documented relationship/deletion rules and avoid leaving orphaned itinerary records.

**FR-057 — Schedule warnings:** When sufficient data exists, the system should flag overlapping time slots, items outside trip dates, or known closed periods. Lack of data must not be represented as a confirmed conflict.

### G. Budget

**FR-060 — Budget target:** Users shall be able to set or update a target budget and currency.

**FR-061 — Cost estimate:** The trip view shall summarize available estimated costs by category and total.

**FR-062 — Missing costs:** Unknown costs shall be displayed as unavailable/not estimated, not as zero.

**FR-063 — Budget status:** The UI may show whether known estimated costs are below, near, or above the target, while clarifying that excluded expenses and unknown costs can change the total.

### H. Map and export (conditional MVP)

**FR-070 — Map view:** If a map provider is integrated, users shall be able to view destinations and itinerary stops geographically.

**FR-071 — Routing:** Travel-time/distance estimates shall be shown only when obtained from a suitable routing source.

**FR-072 — Export:** If included, users shall be able to export or print a saved itinerary without exposing private information to other users.

### I. Reliability and feedback

**FR-080 — Loading states:** Data-driven screens shall show loading feedback for asynchronous operations.

**FR-081 — Empty states:** Empty searches, empty saved-trip lists, and empty itinerary days shall provide useful next actions.

**FR-082 — Error states:** API, validation, authentication, and provider failures shall have clear, non-sensitive messages.

**FR-083 — Duplicate submission protection:** The UI should prevent accidental repeated submission of create/save/generation actions while a request is in progress.

**FR-084 — Persistence integrity:** A failed update shall not report success; persisted state should remain consistent.

## 4. Business rules

1. Every private trip belongs to exactly one owner in the MVP.
2. A trip's end date must be on or after its start date.
3. An itinerary item must belong to a trip and, where day entities are used, a valid itinerary day.
4. Estimated totals include only known costs and must disclose exclusions/unknowns.
5. AI-generated items are drafts until reviewed/saved by the user.
6. Booking status is not inferred from an external link click.
7. Deleting a trip follows a defined cascade or restricted-delete policy.
8. External source terms and licensing must be respected.
9. User-supplied text is treated as data, not executable instructions.
10. Private trip APIs must enforce authorization on the server, not just hide controls in the UI.

## 5. Main use cases

### UC-01: Discover a destination
**Actor:** Visitor or registered traveler  
**Precondition:** Destination data is available.  
**Flow:** Browse/search → apply supported filters → open destination detail → explore related listings.  
**Postcondition:** User has viewed destination information and may proceed to create a trip.

### UC-02: Create a trip
**Actor:** Registered traveler  
**Precondition:** User is authenticated.  
**Flow:** Select create trip → enter required trip details → validate → save → open trip workspace.  
**Alternate:** Invalid dates or fields are rejected with actionable errors.  
**Postcondition:** Trip is stored under the current user.

### UC-03: Generate an itinerary
**Actor:** Registered traveler  
**Precondition:** A valid trip exists.  
**Flow:** Select generate → backend prepares context → AI returns structured proposal → validate response → show draft → user edits and saves.  
**Alternate:** AI timeout/failure → show error and retain manual editing.  
**Postcondition:** A valid, user-editable itinerary draft or saved itinerary exists.

### UC-04: Edit a trip
**Actor:** Trip owner  
**Precondition:** Trip exists and is owned by user.  
**Flow:** Open trip → add/edit/reorder/remove items → save → reload to verify persistence.  
**Alternate:** Unauthorized access returns an appropriate forbidden/not-found response.  
**Postcondition:** Updated trip persists.

### UC-05: Review budget
**Actor:** Trip owner  
**Precondition:** Trip exists; cost data may be partial.  
**Flow:** Open budget summary → inspect known estimates and missing values → adjust target or trip items.  
**Postcondition:** User understands known costs and data gaps.

## 6. Acceptance tests for key workflows

- A visitor cannot call a protected trip mutation endpoint successfully.
- A user cannot read or modify another user's trip by guessing its ID.
- A valid trip can be created, retrieved, edited, and deleted by its owner.
- Invalid date ranges are rejected.
- A destination can be searched and its detail page shows associated data when available.
- An AI response that fails schema validation is not saved as a valid itinerary.
- AI provider failure leaves manual itinerary editing usable.
- Unknown item costs are not counted as zero without an explicit rule.
- Saved itinerary changes survive a new login/session.
- Core flows display loading, empty, success, and failure states.

## 7. Dependencies

- Chosen authentication configuration.
- Seeded or provider-sourced travel data.
- AI provider credentials/configuration.
- Optional mapping/routing provider.
- Database and deployment configuration.
