package com.voyageai.itinerary.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItineraryDayResponse {

    private String id;
    private int dayNumber;
    private LocalDate date;
    private String title;
    private String notes;
    private List<ItineraryItemResponse> items = new ArrayList<>();

    public ItineraryDayResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
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

    public List<ItineraryItemResponse> getItems() {
        return items;
    }

    public void setItems(List<ItineraryItemResponse> items) {
        this.items = items;
    }
}
