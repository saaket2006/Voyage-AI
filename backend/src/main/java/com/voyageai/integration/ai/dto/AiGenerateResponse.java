package com.voyageai.integration.ai.dto;

public class AiGenerateResponse {

    private String model;
    private String response;
    private boolean success;
    private Integer promptTokens;
    private Integer candidateTokens;
    private Integer totalTokens;

    public AiGenerateResponse() {
    }

    public AiGenerateResponse(String model, String response, boolean success) {
        this.model = model;
        this.response = response;
        this.success = success;
    }

    public AiGenerateResponse(String model, String response, boolean success, Integer promptTokens, Integer candidateTokens, Integer totalTokens) {
        this.model = model;
        this.response = response;
        this.success = success;
        this.promptTokens = promptTokens;
        this.candidateTokens = candidateTokens;
        this.totalTokens = totalTokens;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(Integer promptTokens) {
        this.promptTokens = promptTokens;
    }

    public Integer getCandidateTokens() {
        return candidateTokens;
    }

    public void setCandidateTokens(Integer candidateTokens) {
        this.candidateTokens = candidateTokens;
    }

    public Integer getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(Integer totalTokens) {
        this.totalTokens = totalTokens;
    }
}
