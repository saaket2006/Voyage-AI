package com.voyageai.destination.ingestion;

import java.util.ArrayList;
import java.util.List;

public class DestinationImportResult {

    private int recordsFetched;
    private int recordsValidated;
    private int recordsInserted;
    private int recordsUpdated;
    private int recordsSkipped;
    private List<String> errors = new ArrayList<>();
    private List<String> importedDestinations = new ArrayList<>();

    public DestinationImportResult() {
    }

    public int getRecordsFetched() {
        return recordsFetched;
    }

    public void setRecordsFetched(int recordsFetched) {
        this.recordsFetched = recordsFetched;
    }

    public int getRecordsValidated() {
        return recordsValidated;
    }

    public void setRecordsValidated(int recordsValidated) {
        this.recordsValidated = recordsValidated;
    }

    public int getRecordsInserted() {
        return recordsInserted;
    }

    public void setRecordsInserted(int recordsInserted) {
        this.recordsInserted = recordsInserted;
    }

    public int getRecordsUpdated() {
        return recordsUpdated;
    }

    public void setRecordsUpdated(int recordsUpdated) {
        this.recordsUpdated = recordsUpdated;
    }

    public int getRecordsSkipped() {
        return recordsSkipped;
    }

    public void setRecordsSkipped(int recordsSkipped) {
        this.recordsSkipped = recordsSkipped;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getImportedDestinations() {
        return importedDestinations;
    }

    public void setImportedDestinations(List<String> importedDestinations) {
        this.importedDestinations = importedDestinations;
    }
}
