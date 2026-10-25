package com.voyageai.itinerary.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CreateItineraryDayRequest {

    @NotNull(message = "Day number is required")
    @Min(value = 1, message = "Day number must be at least 1")
    private Integer dayNumber;

    private LocalDate date;

    @Size(max = 128, message = "Title must not exceed 128 characters")
    private String title;

    private String notes;

    public CreateItineraryDayRequest() {
    }

    public CreateItineraryDayRequest(Integer dayNumber, LocalDate date, String title) {
        this.dayNumber = dayNumber;
        this.date = date;
        this.title = title;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
