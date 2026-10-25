package com.voyageai.destination.ingestion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WikimediaSummaryResponse {

    private String title;
    private String displaytitle;

    @JsonProperty("wikibase_item")
    private String wikibaseItem;

    private String description;
    private String extract;

    private Coordinates coordinates;
    private Image thumbnail;
    private Image originalimage;

    @JsonProperty("content_urls")
    private ContentUrls contentUrls;

    public WikimediaSummaryResponse() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplaytitle() {
        return displaytitle;
    }

    public void setDisplaytitle(String displaytitle) {
        this.displaytitle = displaytitle;
    }

    public String getWikibaseItem() {
        return wikibaseItem;
    }

    public void setWikibaseItem(String wikibaseItem) {
        this.wikibaseItem = wikibaseItem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Image getOriginalimage() {
        return originalimage;
    }

    public void setOriginalimage(Image originalimage) {
        this.originalimage = originalimage;
    }

    public ContentUrls getContentUrls() {
        return contentUrls;
    }

    public void setContentUrls(ContentUrls contentUrls) {
        this.contentUrls = contentUrls;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coordinates {
        private Double lat;
        private Double lon;

        public Coordinates() {}

        public Coordinates(Double lat, Double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public Double getLat() { return lat; }
        public void setLat(Double lat) { this.lat = lat; }

        public Double getLon() { return lon; }
        public void setLon(Double lon) { this.lon = lon; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        private String source;
        private Integer width;
        private Integer height;

        public Image() {}

        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }

        public Integer getWidth() { return width; }
        public void setWidth(Integer width) { this.width = width; }

        public Integer getHeight() { return height; }
        public void setHeight(Integer height) { this.height = height; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentUrls {
        private PageUrl desktop;

        public ContentUrls() {}

        public PageUrl getDesktop() { return desktop; }
        public void setDesktop(PageUrl desktop) { this.desktop = desktop; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageUrl {
        private String page;

        public PageUrl() {}

        public String getPage() { return page; }
        public void setPage(String page) { this.page = page; }
    }
}
