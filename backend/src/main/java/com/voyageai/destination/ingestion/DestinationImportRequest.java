package com.voyageai.destination.ingestion;

import java.util.List;

public class DestinationImportRequest {

    private List<String> destinations;
    private boolean forceUpdate;

    public DestinationImportRequest() {
    }

    public DestinationImportRequest(List<String> destinations, boolean forceUpdate) {
        this.destinations = destinations;
        this.forceUpdate = forceUpdate;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
}
