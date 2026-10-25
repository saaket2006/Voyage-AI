package com.voyageai.search.dto;

public class SearchMeta {

    private int limitPerGroup;
    private int totalResults;

    public SearchMeta() {
    }

    public SearchMeta(int limitPerGroup, int totalResults) {
        this.limitPerGroup = limitPerGroup;
        this.totalResults = totalResults;
    }

    public int getLimitPerGroup() {
        return limitPerGroup;
    }

    public void setLimitPerGroup(int limitPerGroup) {
        this.limitPerGroup = limitPerGroup;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
