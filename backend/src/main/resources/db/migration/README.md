# Voyage-AI Database Migrations Directory

This directory contains versioned Flyway SQL migration scripts for the Voyage-AI relational database.

## Migration Naming Convention
Flyway scripts follow strict semantic versioning prefixes:
- `V<Version>__<Description>.sql` (Two underscores separate version from description)
  - Example: `V1__init_schema.sql` (Phase 2: Users, Destinations, Listings, Trips, Days, Items)
  - Example: `V2__seed_travel_data.sql` (Phase 2: Curated Indian destinations and listings)
- Repeatable migrations:
  - `R__<Description>.sql`

## Phase Boundaries
- **Phase 0:** `V0__baseline.sql` acts as an initial baseline script verifying Flyway execution against an empty database.
- **Phase 2:** Core domain tables, constraints, foreign keys, and initial seed data.
