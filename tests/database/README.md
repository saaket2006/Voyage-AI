# Voyage-AI — Database Testing Strategy & Isolation

## Principles & Ownership

1. **Isolation Guarantee:**
   - Database integration tests run against an isolated in-memory or dedicated test database (e.g., H2 in MySQL compatibility mode).
   - Tests **NEVER** run destructive operations (such as `DROP DATABASE`, `TRUNCATE`, or `DELETE`) against the developer's local `voyage` database or the developer's existing `pat` database.
   - The test suite asserts that active connection URLs do not contain `voyage` or `pat` before executing any test statements.

2. **Flyway Migration Testing:**
   - Schema versioning is verified using Flyway baseline and versioned scripts (`db/migration/V*`).
   - The integration suite asserts that migrations apply successfully, create the `flyway_schema_history` metadata table, and establish the expected initial state.

3. **Ownership Boundary:**
   - Database integration tests are implemented under `backend/src/test/java/com/voyageai/integration/db/DatabaseFlywayIntegrationTest.java` where the Spring Boot context and JPA/Flyway lifecycle run natively.
   - Routine Maven backend builds (`mvn test` in `backend/`) verify this persistence layer without requiring external network access or mutating production stores.
