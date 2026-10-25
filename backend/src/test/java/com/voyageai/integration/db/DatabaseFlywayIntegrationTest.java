package com.voyageai.integration.db;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test verifying Flyway migration execution, schema initialization,
 * and database connectivity under a strictly isolated in-memory test database.
 *
 * SAFETY MANDATE:
 * - Uses an isolated H2 in-memory database in MySQL compatibility mode.
 * - NEVER points at the developer's normal 'voyage' database or 'pat' database.
 * - Verifies that V0__baseline.sql applies cleanly and creates flyway_schema_history.
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:voyage_flyway_test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=none"
})
class DatabaseFlywayIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Flyway flyway;

    @BeforeEach
    void setUp() {
        this.flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .cleanDisabled(false)
                .load();
        flyway.migrate();
    }

    @Test
    @DisplayName("Safety check: confirms datasource is isolated in-memory and not developer's voyage or pat database")
    void verifyDatabaseIsolationSafety() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String url = metaData.getURL();

            // Safety assertion: ensure test runs against memory database only
            assertThat(url)
                    .as("Safety check: Database URL must be an isolated in-memory database")
                    .contains("jdbc:h2:mem:voyage_flyway_test");

            assertThat(url.toLowerCase())
                    .as("Safety violation prevention: Must NEVER point to host voyage, pat, or legacy voyage_ai database")
                    .doesNotContain("localhost:3306/voyage")
                    .doesNotContain("localhost:3306/pat")
                    .doesNotContain(":3307/voyage")
                    .doesNotContain(":3307/pat")
                    .doesNotContain("voyage_ai");
        }
    }

    @Test
    @DisplayName("Flyway migrations apply successfully and establish migration history")
    void flywayMigrationsApplySuccessfully() {
        MigrationInfo[] appliedMigrations = flyway.info().applied();

        assertThat(appliedMigrations)
                .as("At least the V0 baseline migration must be applied")
                .isNotEmpty();

        MigrationInfo baseline = appliedMigrations[0];
        assertThat(baseline.getVersion().getVersion())
                .as("Baseline migration version must be 0")
                .isEqualTo("0");

        assertThat(baseline.getDescription())
                .as("Baseline description must match baseline")
                .isEqualTo("baseline");

        assertThat(baseline.getState().isApplied())
                .as("Baseline migration must be successfully applied")
                .isTrue();
    }

    @Test
    @DisplayName("Schema initialization: flyway_schema_history table exists and is queryable")
    void flywaySchemaHistoryTableExists() throws SQLException {
        // Verify table exists via metadata
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            boolean found = false;
            try (ResultSet rs = metaData.getTables(null, null, null, new String[]{"TABLE"})) {
                while (rs.next()) {
                    String name = rs.getString("TABLE_NAME");
                    if ("flyway_schema_history".equalsIgnoreCase(name)) {
                        found = true;
                        break;
                    }
                }
            }
            assertThat(found)
                    .as("flyway_schema_history table must exist in schema")
                    .isTrue();
        }

        // Verify that Flyway read the applied baseline migration from the history table
        MigrationInfo[] applied = flyway.info().applied();
        assertThat(applied)
                .as("Migration history must contain recorded migrations")
                .isNotEmpty();
        assertThat(applied[0].getState().isApplied())
                .as("Baseline migration recorded in flyway_schema_history must have succeeded")
                .isTrue();
    }

    @Test
    @DisplayName("Connectivity test: can execute query against isolated test database and verify seed data")
    void testDatabaseConnectivity() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertThat(result).isEqualTo(1);

        Integer destinationCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM destinations", Integer.class);
        assertThat(destinationCount).as("Curated authentic destinations must be seeded").isEqualTo(4);

        Integer listingsCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM travel_listings", Integer.class);
        assertThat(listingsCount).as("Authentic travel listings must be seeded").isGreaterThanOrEqualTo(14);
    }
}
