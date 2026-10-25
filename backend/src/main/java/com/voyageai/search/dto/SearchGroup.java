package com.voyageai.search.dto;

import java.util.ArrayList;
import java.util.List;

public class SearchGroup {

    private String type;
    private String label;
    private List<SearchItem> items = new ArrayList<>();

    public SearchGroup() {
    }

    public SearchGroup(String type, String label, List<SearchItem> items) {
        this.type = type;
        this.label = label;
        this.items = items != null ? items : new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<SearchItem> getItems() {
        return items;
    }

    public void setItems(List<SearchItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }
}
