package com.voyageai.search.dto;

import java.util.ArrayList;
import java.util.List;

public class SearchSuggestionResponse {

    private String query;
    private List<SearchGroup> groups = new ArrayList<>();
    private SearchMeta meta;

    public SearchSuggestionResponse() {
    }

    public SearchSuggestionResponse(String query, List<SearchGroup> groups, SearchMeta meta) {
        this.query = query;
        this.groups = groups != null ? groups : new ArrayList<>();
        this.meta = meta;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<SearchGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<SearchGroup> groups) {
        this.groups = groups != null ? groups : new ArrayList<>();
    }

    public SearchMeta getMeta() {
        return meta;
    }

    public void setMeta(SearchMeta meta) {
        this.meta = meta;
    }
}
