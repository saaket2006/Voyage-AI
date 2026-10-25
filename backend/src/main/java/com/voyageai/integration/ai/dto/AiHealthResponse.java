package com.voyageai.integration.ai.dto;

public class AiHealthResponse {

    private String status;
    private String model;
    private boolean configured;
    private boolean reachable;
    private String message;

    public AiHealthResponse() {
    }

    public AiHealthResponse(String status, String model, boolean configured, boolean reachable, String message) {
        this.status = status;
        this.model = model;
        this.configured = configured;
        this.reachable = reachable;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
