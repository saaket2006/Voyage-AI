package com.voyageai.destination.ingestion;

import com.voyageai.common.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/destinations")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDestinationController {

    private final DestinationIngestionService ingestionService;

    public AdminDestinationController(DestinationIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/import")
    public ResponseEntity<ApiResponse<DestinationImportResult>> importDestinations(
            @RequestBody(required = false) DestinationImportRequest request) {

        List<String> destinations = (request != null && request.getDestinations() != null)
                ? request.getDestinations()
                : Collections.emptyList();

        boolean forceUpdate = (request != null && request.isForceUpdate());

        DestinationImportResult result = ingestionService.importDestinations(destinations, forceUpdate);

        String message = String.format("Destination import completed: %d inserted, %d updated, %d skipped, %d errors.",
                result.getRecordsInserted(), result.getRecordsUpdated(),
                result.getRecordsSkipped(), result.getErrors().size());

        return ResponseEntity.ok(ApiResponse.success(result, message));
    }
}
