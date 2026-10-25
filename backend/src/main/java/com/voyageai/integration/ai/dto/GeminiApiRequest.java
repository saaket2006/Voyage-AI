package com.voyageai.integration.ai.dto;

import java.util.List;

public record GeminiApiRequest(List<Content> contents) {

    public GeminiApiRequest(String text) {
        this(List.of(new Content(List.of(new Part(text)))));
    }

    public record Content(List<Part> parts) {
    }

    public record Part(String text) {
    }
}
