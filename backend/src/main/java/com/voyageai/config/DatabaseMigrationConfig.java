package com.voyageai.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Ensures Flyway database migrations execute reliably before Hibernate JPA initializes,
 * both in production/development and during automated integration tests.
 */
@Configuration
public class DatabaseMigrationConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseMigrationConfig.class);

    @Value("${spring.flyway.enabled:true}")
    private boolean flywayEnabled;

    @Value("${spring.flyway.baseline-on-migrate:true}")
    private boolean baselineOnMigrate;

    @Value("${spring.flyway.locations:classpath:db/migration}")
    private String locations;

    @Bean(name = "flyway")
    public Flyway flyway(DataSource dataSource) {
        log.info("Configuring Flyway database migration bean (enabled={}, locations={})", flywayEnabled, locations);
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(locations)
                .baselineOnMigrate(baselineOnMigrate)
                .cleanDisabled(false)
                .load();

        if (flywayEnabled) {
            log.info("Executing Flyway migrations on DataSource...");
            flyway.migrate();
            log.info("Flyway database migrations completed successfully.");
        } else {
            log.info("Flyway database migrations are disabled by configuration.");
        }

        return flyway;
    }

    /**
     * Guarantees that LocalContainerEntityManagerFactoryBean ("entityManagerFactory")
     * depends on "flyway" so that migrations are applied before JPA validation/schema creation.
     */
    @Bean
    public static BeanFactoryPostProcessor flywayEntityManagerFactoryDependsOnPostProcessor() {
        return (ConfigurableListableBeanFactory beanFactory) -> {
            if (beanFactory.containsBeanDefinition("entityManagerFactory")) {
                BeanDefinition bd = beanFactory.getBeanDefinition("entityManagerFactory");
                String[] existing = bd.getDependsOn();
                if (existing == null || existing.length == 0) {
                    bd.setDependsOn("flyway");
                } else {
                    String[] updated = Arrays.copyOf(existing, existing.length + 1);
                    updated[existing.length] = "flyway";
                    bd.setDependsOn(updated);
                }
            }
        };
    }
}
