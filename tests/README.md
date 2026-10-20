# Voyage-AI — Centralized Automated Testing Workspace

This directory is the central entry point for Voyage-AI automated testing, housing Java Playwright browser tests, black-box API suites, test configuration, reusable fixtures, and runner scripts.

## Directory Structure

```text
tests/
  api/          -> Documented ownership of black-box REST API tests
  browser/      -> Documented ownership of browser UI automation suites
  e2e/          -> Documented ownership of end-to-end integration flows
  database/     -> Strategy and isolation documentation for persistence tests
  fixtures/     -> Reusable test payloads (mock-weather.json, etc.)
  config/       -> Environment and runner configuration (test.properties)
  scripts/      -> Specialized PowerShell test execution scripts
  pom.xml       -> Maven build configuration (Java 17, Playwright, JUnit 5)
  README.md     -> Testing workspace documentation
```

## Test Suites & Responsibilities

| Suite | Technology | Location | Primary Purpose |
| :--- | :--- | :--- | :--- |
| **Backend Unit & Controller** | JUnit 5 + MockMvc | `backend/src/test/` | Controller request mapping, exception handling, service logic (mocked boundaries, no live external calls). |
| **Database Integration** | Flyway + Isolated DB | `backend/src/test/` | Flyway schema migrations, history table verification, and connectivity isolation (isolated in-memory DB). |
| **Frontend Unit** | Vitest + RTL | `frontend/src/` | React component rendering, interactive state, and fallback components. |
| **Browser UI Automation** | Java Playwright + JUnit 5 | `tests/src/test/.../browser/` | Application header, discovery filtering, modal interactions, responsive layout (deterministic mock data). |
| **End-to-End Integration** | Java Playwright + JUnit 5 | `tests/src/test/.../e2e/` | Weather integration flow with route interception; `@smoke` live verification. |
| **Black-box API** | Java HttpClient + JUnit 5 | `tests/src/test/.../api/` | Direct HTTP verification of health and weather endpoints against running backend. |

## Running Tests

From the `tests/` directory:

```powershell
# Run all routine deterministic tests (offline, mocked)
mvn test

# Run browser tests only
mvn test -Dtest="*Browser*Test,NavigationTest,DestinationsDiscoveryTest,DestinationDetailModalTest,ResponsiveTest"

# Run black-box API tests only
mvn test -Dtest="*ApiTest"

# Run smoke tests against live local backend and Open-Meteo
mvn test -Psmoke
```

## Failure Artifacts

On test failure, Playwright automatically outputs:
- **Screenshots:** `tests/target/playwright-screenshots/<testName>.png`
- **Execution Traces:** `tests/target/playwright-traces/<testName>.zip`
