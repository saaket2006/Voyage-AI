package com.voyageai.integration.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AiGenerateRequest {

    @NotBlank(message = "Prompt must not be blank")
    @Size(max = 4000, message = "Prompt must not exceed 4000 characters")
    private String prompt;

    public AiGenerateRequest() {
    }

    public AiGenerateRequest(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
