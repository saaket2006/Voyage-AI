package com.voyageai.search.dto;

public class SearchItem {

    private String id;
    private String name;
    private String subtitle;
    private String description;
    private SearchItemLocation location;
    private String imageUrl;
    private String sourceUrl;
    private String route;

    public SearchItem() {
    }

    public SearchItem(String id, String name, String subtitle, String description,
                      SearchItemLocation location, String imageUrl, String sourceUrl, String route) {
        this.id = id;
        this.name = name;
        this.subtitle = subtitle;
        this.description = description;
        this.location = location;
        this.imageUrl = imageUrl;
        this.sourceUrl = sourceUrl;
        this.route = route;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SearchItemLocation getLocation() {
        return location;
    }

    public void setLocation(SearchItemLocation location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
