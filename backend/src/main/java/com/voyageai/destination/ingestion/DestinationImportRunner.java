package com.voyageai.destination.ingestion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.destinations.import.enabled", havingValue = "true")
public class DestinationImportRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DestinationImportRunner.class);

    private final DestinationIngestionService ingestionService;

    public DestinationImportRunner(DestinationIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @Override
    public void run(String... args) {
        log.info("app.destinations.import.enabled=true -> Executing automatic destination import at startup...");
        try {
            DestinationImportResult result = ingestionService.importDestinations(null, false);
            log.info("Startup destination import complete: inserted={}, updated={}, skipped={}, errors={}",
                    result.getRecordsInserted(), result.getRecordsUpdated(),
                    result.getRecordsSkipped(), result.getErrors().size());
        } catch (Exception ex) {
            log.error("Error during startup destination import: {}", ex.getMessage(), ex);
        }
    }
}
