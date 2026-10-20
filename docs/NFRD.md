# Voyage-AI — Non-Functional Requirements Document (NFRD)

**Version:** 0.1 · **Status:** Baseline

## 1. Purpose

Define quality attributes and operational constraints for Voyage-AI. These requirements should be measurable where possible; targets below are initial engineering goals and should be validated against the team's environment.

## 2. Performance

- **NFR-PERF-01:** Under the agreed local/demo dataset and normal development workload, common non-AI API requests should target a p95 response time of under 700 ms, excluding network latency outside the application.
- **NFR-PERF-02:** AI/provider operations must have configured timeouts and show progress/loading feedback.
- **NFR-PERF-03:** List endpoints must support bounded result sizes; pagination should be used when datasets can grow.
- **NFR-PERF-04:** Frontend should avoid unnecessary full-page reloads and avoid loading large images at full resolution when not needed.
- **NFR-PERF-05:** Performance targets must be measured; they are not claims about current implementation.

## 3. Security and privacy

- **NFR-SEC-01:** All private operations must be authorized server-side.
- **NFR-SEC-02:** Passwords must never be stored as plaintext; use a recognized password hashing mechanism if password authentication is implemented.
- **NFR-SEC-03:** Secrets must be stored in environment variables or an approved secret store, not source control or frontend bundles.
- **NFR-SEC-04:** Validate and constrain input; use ORM parameterization and safe rendering to reduce injection risks.
- **NFR-SEC-05:** Configure CORS to allow only required origins.
- **NFR-SEC-06:** Use HTTPS in deployed environments.
- **NFR-SEC-07:** Session/cookie configuration, if used, must consider Secure, HttpOnly, SameSite, expiration, and CSRF protection as applicable.
- **NFR-SEC-08:** Never put credentials, session IDs, or private tokens in URLs.
- **NFR-SEC-09:** Logs must not include passwords, tokens, API keys, or unnecessary personal data.
- **NFR-SEC-10:** Collect only profile/trip data needed for product functionality and document deletion behavior.

## 4. Reliability and resilience

- **NFR-REL-01:** Failure of an AI or external travel-data provider must not make saved trip data inaccessible.
- **NFR-REL-02:** Provider calls must use timeouts and bounded retries only for appropriate transient failures.
- **NFR-REL-03:** Multi-record changes that must be consistent should run transactionally.
- **NFR-REL-04:** Failed writes must not be reported as successful.
- **NFR-REL-05:** The UI must preserve unsaved user input where practical after recoverable failures.
- **NFR-REL-06:** The development/demo profile must offer deterministic seed data so the main workflow can be demonstrated without relying on live API availability.

## 5. Usability and accessibility

- **NFR-UX-01:** Main workflows must be usable at common mobile, tablet, and desktop widths.
- **NFR-UX-02:** Forms must provide clear labels, validation feedback, and focus management.
- **NFR-UX-03:** Interactive elements must be keyboard accessible and show visible focus.
- **NFR-UX-04:** Text and controls should meet WCAG 2.2 AA contrast guidance where applicable.
- **NFR-UX-05:** Loading, empty, success, and error states must be intentionally designed.
- **NFR-UX-06:** Destructive actions require clear intent and suitable confirmation/undo.
- **NFR-UX-07:** The design system must remain consistent across pages and avoid decorative complexity that harms clarity.

## 6. Maintainability

- **NFR-MAINT-01:** Frontend components, API client, domain types, and page logic should have clear boundaries.
- **NFR-MAINT-02:** Backend layers should separate controllers, services, repositories, domain entities, DTOs, and integrations.
- **NFR-MAINT-03:** Business rules should not be duplicated across controllers.
- **NFR-MAINT-04:** Use consistent naming, formatting, linting, and documented project scripts.
- **NFR-MAINT-05:** Schema changes must be versioned through migrations.
- **NFR-MAINT-06:** Provider-specific code should be isolated behind adapters/services.

## 7. Testability

- **NFR-TEST-01:** Core domain logic must be testable without live AI or travel APIs.
- **NFR-TEST-02:** CI/local scripts should run backend tests and frontend lint/test/build checks.
- **NFR-TEST-03:** Core user workflows and authorization rules require automated coverage.
- **NFR-TEST-04:** API examples and Postman collections must remain synchronized with API behavior.

## 8. Portability and deployment

- **NFR-DEP-01:** The backend must build as a runnable JAR.
- **NFR-DEP-02:** Docker configuration must build from documented source and configuration.
- **NFR-DEP-03:** Docker Compose should start the application and required local supporting services.
- **NFR-DEP-04:** Configuration must distinguish local/test/demo/deployment environments.
- **NFR-DEP-05:** No machine-specific absolute paths should be required for normal startup.

## 9. Data quality and transparency

- **NFR-DATA-01:** Estimated prices must be distinguishable from confirmed prices.
- **NFR-DATA-02:** Unknown cost values must not silently become zero.
- **NFR-DATA-03:** External data should retain source and retrieval time where feasible.
- **NFR-DATA-04:** AI-generated content must be identified as a proposal.
- **NFR-DATA-05:** The system must not claim confirmed reservations, guaranteed availability, or verified opening hours without supporting data.

## 10. Initial acceptance targets

Targets are preliminary and should be revisited after an end-to-end prototype:
- No critical authorization bypass in tested trip endpoints.
- Core backend tests and frontend build pass in a clean checkout.
- Main discovery-to-saved-itinerary flow can be completed using seed data.
- The application remains usable when AI provider configuration is absent or unavailable.
- Responsive layouts work at representative mobile and desktop widths.
- A new team member can start the application by following the README.
