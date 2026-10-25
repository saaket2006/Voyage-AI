package com.voyageai.integration.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiApiResponse(
        List<Candidate> candidates,
        UsageMetadata usageMetadata,
        String modelVersion
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Candidate(
            Content content,
            String finishReason,
            Integer index
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Content(
            List<Part> parts,
            String role
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Part(
            String text,
            String thoughtSignature
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record UsageMetadata(
            Integer promptTokenCount,
            Integer candidatesTokenCount,
            Integer totalTokenCount
    ) {
    }
}
